package com.earthman.app.nim.uikit.session.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.earthman.app.R;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.nim.uikit.SoftKeyboardUtils;
import com.earthman.app.nim.uikit.cache.FriendDataCache;
import com.earthman.app.nim.uikit.common.activity.TActionBarActivity;
import com.earthman.app.nim.uikit.session.SessionCustomization;
import com.earthman.app.nim.uikit.session.constant.Extras;
import com.earthman.app.nim.uikit.session.fragment.MessageFragment;
import com.earthman.app.nim.uikit.session.helper.MessageListPanelHelper;
import com.earthman.app.nim.uikit.uinfo.UserInfoHelper;
import com.earthman.app.nim.uikit.uinfo.UserInfoObservable;
import com.earthman.app.utils.SystemBarTintManager;
import com.earthman.app.widget.DialogCommon;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends FragmentActivity implements OnClickListener {

	private boolean isResume = false;
	protected String sessionId;
	private SessionCustomization customization;
	private MessageFragment messageFragment;
	private TextView mTvLeftTitle;

	public static void start(Context context, String contactId, SessionCustomization customization) {
		BaseMessageActivity activity;
		Intent intent = new Intent();
		intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
		intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
		intent.setClass(context, P2PMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nim_message_activity);
		initSystemBar(this);
		parseIntent();
		initTitlebar();
		addMessageFragment();
		// 单聊特例话数据，包括个人信息，
		// requestBuddyInfo();
		registerObservers(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		registerObservers(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isResume = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isResume = false;
		SoftKeyboardUtils.hideSoftKeyboard(this);
	}

	private void requestBuddyInfo() {
		mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
	}

	private void registerObservers(boolean register) {
		if (register) {
			registerUserInfoObserver();
		} else {
			unregisterUserInfoObserver();
		}
		NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
		FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
	}

	FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> accounts) {
			mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
		}

		@Override
		public void onDeletedFriends(List<String> accounts) {
			mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
		}

		@Override
		public void onAddUserToBlackList(List<String> account) {
			mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> account) {
			mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
		}
	};

	private UserInfoObservable.UserInfoObserver uinfoObserver;

	private void registerUserInfoObserver() {
		if (uinfoObserver == null) {
			uinfoObserver = new UserInfoObservable.UserInfoObserver() {
				@Override
				public void onUserInfoChanged(List<String> accounts) {
					if (accounts.contains(sessionId)) {
						requestBuddyInfo();
					}
				}
			};
		}

		UserInfoHelper.registerObserver(uinfoObserver);
	}

	private void unregisterUserInfoObserver() {
		if (uinfoObserver != null) {
			UserInfoHelper.unregisterObserver(uinfoObserver);
		}
	}

	/**
	 * 命令消息接收观察者
	 */
	Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
		@Override
		public void onEvent(CustomNotification message) {
			if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
				return;
			}
			showCommandMessage(message);
		}
	};

	protected void showCommandMessage(CustomNotification message) {
		if (!isResume) {
			return;
		}

		String content = message.getContent();
		try {
			JSONObject json = JSON.parseObject(content);
			int id = json.getIntValue("id");
			if (id == 1) {
				// 正在输入
				Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {

		}
	}

	protected void addMessageFragment() {
		Bundle arguments = getIntent().getExtras();
		arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
		messageFragment = new MessageFragment();
		messageFragment.setArguments(arguments);
		messageFragment.setContainerId(R.id.message_fragment_container);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.message_fragment_container, messageFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onBackPressed() {
		if (messageFragment == null || !messageFragment.onBackPressed()) {
			super.onBackPressed();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (messageFragment != null) {
			messageFragment.onActivityResult(requestCode, resultCode, data);
		}

		if (customization != null) {
			customization.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	private void initTitlebar() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		mTvLeftTitle = (TextView) findViewById(R.id.tv_left);
		mTvLeftTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));

		Button btnRight = (Button) findViewById(R.id.btn_right);
		btnRight.setText(R.string.clear_record);
		btnRight.setOnClickListener(this);
	}

	private void parseIntent() {
		sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
		customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);

		// if (customization != null) {
		// addRightCustomViewOnActionBar(this, customization.buttons);
		// }
	}

	// 添加action bar的右侧按钮及响应事件
	private void addRightCustomViewOnActionBar(TActionBarActivity activity, List<SessionCustomization.OptionsButton> buttons) {
		if (buttons == null || buttons.size() == 0) {
			return;
		}

		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		LinearLayout view = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.nim_action_bar_custom_view, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		for (final SessionCustomization.OptionsButton button : buttons) {
			ImageButton imageView = new ImageButton(activity);
			imageView.setImageResource(button.iconId);
			imageView.setBackgroundResource(R.drawable.nim_nim_action_bar_button_selector);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// button.onClick(BaseMessageActivity.this, v, sessionId);
				}
			});
			view.addView(imageView, params);
		}

		actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT | Gravity.CENTER));
	}

	public void initSystemBar(Activity activity) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			setTranslucentStatus(activity, true);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);

		tintManager.setStatusBarTintEnabled(true);

		// 使用颜色资源

		tintManager.setStatusBarTintResource(R.color.tab_text_hover);

	}

	private void setTranslucentStatus(Activity activity, boolean on) {

		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			DialogCommon dialog=new DialogCommon(this, getString(R.string.hint), getString(R.string.clear_record_hint), getString(R.string.ok), new CommonDialogListener() {
				@Override
				public void onRightButtonClick() {
					NIMClient.getService(MsgService.class).clearChattingHistory(sessionId,SessionTypeEnum.P2P);
					MessageListPanelHelper.getInstance().notifyClearMessages(sessionId);
					Toast.makeText(P2PMessageActivity.this, R.string.clear_all_success, Toast.LENGTH_SHORT).show();
				}
			});
			dialog.show();
			break;
		}
	}
}
