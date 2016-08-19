package com.earthman.app.nim.uikit.session.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.SoftKeyboardUtils;
import com.earthman.app.nim.uikit.cache.FriendDataCache;
import com.earthman.app.nim.uikit.cache.SimpleCallback;
import com.earthman.app.nim.uikit.cache.TeamDataCache;
import com.earthman.app.nim.uikit.session.SessionCustomization;
import com.earthman.app.nim.uikit.session.constant.Extras;
import com.earthman.app.nim.uikit.session.fragment.TeamMessageFragment;
import com.earthman.app.utils.SystemBarTintManager;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * 讨论组界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
public class TeamMessageActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = "TMA";

	protected String sessionId;
	private SessionCustomization customization;
	// private MessageFragment messageFragment;
	private TextView mTvLeftTitle;

	// model
	private Team team;

	private View invalidTeamTipView;

	private TeamMessageFragment fragment;

	private Class<? extends Activity> backToClass;
	private int GroupMembership;

	public static void start(Context context, String tid, SessionCustomization customization, Class<? extends Activity> backToClass) {
		Intent intent = new Intent();
		intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
		intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
		intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
		intent.setClass(context, TeamMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nim_team_message_activity);
		backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
		initSystemBar(this);
		parseIntent();
		initTitlebar();
		invalidTeamTipView = findViewById(R.id.invalid_team_tip);
		addMessageFragment();
		registerTeamUpdateObserver(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}

		if (customization != null) {
			customization.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	private void parseIntent() {
		sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
		customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);

		// if (customization != null) {
		// addRightCustomViewOnActionBar(this, customization.buttons);
		// }
	}

	private void initTitlebar() {
		mTvLeftTitle = (TextView) findViewById(R.id.tv_left);
		Button btnRight = (Button) findViewById(R.id.btn_right);
		btnRight.setText(R.string.settings);
		btnRight.setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
	}

	protected void addMessageFragment() {
		// 添加fragment
		Bundle arguments = getIntent().getExtras();
		arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
		fragment = new TeamMessageFragment();
		fragment.setArguments(arguments);
		fragment.setContainerId(R.id.message_fragment_container);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.message_fragment_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		registerTeamUpdateObserver(false);
		SoftKeyboardUtils.hideSoftKeyboard(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestTeamInfo();
	}

	/**
	 * 请求群基本信息
	 */
	private void requestTeamInfo() {
		// 请求群基本信息
		Team t = TeamDataCache.getInstance().getTeamById(sessionId);
		if (t != null) {
			updateTeamInfo(t);
		} else {
			TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
				@Override
				public void onResult(boolean success, Team result) {
					if (success && result != null) {
						updateTeamInfo(result);
					} else {
						onRequestTeamInfoFailed();
					}
				}
			});
		}
	}

	private void onRequestTeamInfoFailed() {
		Toast.makeText(TeamMessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT);
		finish();
	}

	/**
	 * 更新群信息
	 * 
	 * @param d
	 */
	private void updateTeamInfo(final Team d) {
		if (d == null) {
			return;
		}

		team = d;
		fragment.setTeam(team);
		mTvLeftTitle.setText(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");
		invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
		GroupMembership = team.getMemberCount();
	}

	/**
	 * 注册群信息更新监听
	 * 
	 * @param register
	 */
	private void registerTeamUpdateObserver(boolean register) {
		if (register) {
			TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
			TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
		} else {
			TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
			TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
		}
		FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
	}

	/**
	 * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
	 */
	TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
		@Override
		public void onUpdateTeams(List<Team> teams) {
			if (team == null) {
				return;
			}
			for (Team t : teams) {
				if (t.getId().equals(team.getId())) {
					updateTeamInfo(t);
					break;
				}
			}
		}

		@Override
		public void onRemoveTeam(Team team) {

		}
	};

	/**
	 * 群成员资料变动通知和移除群成员通知
	 */
	TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

		@Override
		public void onUpdateTeamMember(List<TeamMember> members) {
			fragment.refreshMessageList();
		}

		@Override
		public void onRemoveTeamMember(TeamMember member) {

		}
	};

	FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> accounts) {
			fragment.refreshMessageList();
		}

		@Override
		public void onDeletedFriends(List<String> accounts) {
			fragment.refreshMessageList();
		}

		@Override
		public void onAddUserToBlackList(List<String> account) {
			fragment.refreshMessageList();
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> account) {
			fragment.refreshMessageList();
		}
	};

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// if (backToClass != null) {
	// Intent intent = new Intent();
	// intent.setClass(this, backToClass);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	// Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// startActivity(intent);
	// finish();
	// }
	// }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_back) {
			HideKeyboard(v);
			finish();
		} else if (v.getId() == R.id.btn_right) {
//			NimUIKit.startTeamInfo(this, sessionId);
			Team team = TeamDataCache.getInstance().getTeamById(sessionId);
			if (team != null && team.isMyTeam()) {
				NimUIKit.startTeamInfo(this, sessionId);
			} else {
				Toast.makeText(this, R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
			}
		}
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

	// 隐藏虚拟键盘
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		}
	}
}
