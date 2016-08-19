package com.earthman.app.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.adapter.MainTabAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.CircleUpdateBean;
import com.earthman.app.bean.MainTabInfo;
import com.earthman.app.enums.MainTabEnum;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.listener.EditTextDialogListener;
import com.earthman.app.nim.avchat.AVChatProfile;
import com.earthman.app.nim.avchat.activity.AVChatActivity;
import com.earthman.app.nim.contact.activity.AddFriendActivity;
import com.earthman.app.nim.login.LogoutHelper;
import com.earthman.app.nim.main.helper.SystemMessageUnreadManager;
import com.earthman.app.nim.main.model.Extras;
import com.earthman.app.nim.main.reminder.ReminderItem;
import com.earthman.app.nim.main.reminder.ReminderManager;
import com.earthman.app.nim.main.reminder.ReminderManager.UnreadNumChangedCallback;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.team.TeamCreateHelper;
import com.earthman.app.nim.team.activity.AdvancedTeamSearchActivity;
import com.earthman.app.nim.uikit.LoginSyncDataStatusObserver;
import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.common.ui.dialog.DialogMaker;
import com.earthman.app.nim.uikit.contact.ContactsCustomization;
import com.earthman.app.nim.uikit.contact.ContactsFragment;
import com.earthman.app.nim.uikit.contact.core.item.AbsContactItem;
import com.earthman.app.nim.uikit.contact.core.viewholder.AbsContactViewHolder;
import com.earthman.app.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.earthman.app.nim.uikit.recent.RecentContactsFragment;
import com.earthman.app.nim.uikit.team.helper.TeamHelper;
import com.earthman.app.ui.activity.circle.CircleCreateActivity;
import com.earthman.app.ui.activity.login.LoginActivity;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.ui.activity.mine.ActivateAccountActivity;
import com.earthman.app.ui.fragment.main.CircleFragment;
import com.earthman.app.ui.fragment.main.ContactListFragment.FuncItem;
import com.earthman.app.ui.fragment.main.DynamicFragment;
import com.earthman.app.ui.fragment.main.HomeFragment;
import com.earthman.app.ui.fragment.main.MineFragment;
import com.earthman.app.utils.CheckUpdatUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.UserUtils;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.DialogEditText;
import com.earthman.app.widget.DynamicFabuDialog;
import com.earthman.app.widget.InviteFriendDialog;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.popujar.PopuItem;
import com.earthman.app.widget.popujar.PopuJar;
import com.earthman.app.widget.popujar.PopuJar.OnPopuItemClickListener;
import com.earthman.app.zxing.activity.CaptureActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import de.greenrobot.event.EventBus;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-1 下午3:48:14
 * @Decription 主活动类：包含六大模块
 */
@ContentView(R.layout.main_activity)
public class MainActivity extends BaseActivity implements OnItemClickListener, UnreadNumChangedCallback {

	private static final String EXTRA_APP_QUIT = "APP_QUIT";
	private static final int REQUEST_CODE_NORMAL = 1;
	private static final int REQUEST_CODE_ADVANCED = 2;
	private static final String TAG = MainActivity.class.getSimpleName();

	@ViewInject(R.id.btn_edit_circle)
	private Button mBtnEditCircle;
	@ViewInject(R.id.btn_release_dynamic)
	public Button mBtnReleaseDynamic;
	@ViewInject(R.id.btn_add)
	private Button mBtnAdd;
	// ------------------------------------------------
	private List<MainTabInfo> mTabList;
	@ViewInject(R.id.ll_container)
	private LinearLayout mLlContainer;
	@ViewInject(R.id.gv_tab)
	public GridView mGvTab;
	public MainTabAdapter mTabAdfapter;
	// ------------------------------------------------
	private FragmentManager mFragmentManager;
	private Fragment mCurrentFragment;
	// ----------
	private HomeFragment mHomeFragment;
	public ContactsFragment mContactFragment;
	private RecentContactsFragment mRecentContactsFragment;
	private CircleFragment mCircleFragment;
	private DynamicFragment mDynamicFragment;
	private MineFragment mMineFragment;
	// ------------------------------------------------
	private InviteFriendDialog inviteFriendDialog;
	private DynamicFabuDialog dynamicFabuDialog;

	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	private int FRIENDDYNAMIC = 1;

	public static void start(Context context) {
		start(context, null);
	}

	public static void start(Context context, Intent extras) {
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_NORMAL) {
				final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
				if (selected != null && !selected.isEmpty()) {
					final DialogEditText dialogEditText = new DialogEditText(MainActivity.this, "请输入讨论组名称", "", "", new EditTextDialogListener() {

						@Override
						public void onRightButtonClick(EditText etContent) {
							TeamCreateHelper.createNormalTeam(MainActivity.this, etContent.getText().toString(), selected, false, null);
						}

						@Override
						public void onLeftButtonClick(EditText etContent) {
						}
					});
					dialogEditText.show();
				} else {
					Toast.makeText(MainActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == REQUEST_CODE_ADVANCED) {
				final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
				if (selected != null && !selected.isEmpty()) {
					final DialogEditText dialogEditText = new DialogEditText(MainActivity.this, "请输入高级群名称", "", "", new EditTextDialogListener() {

						@Override
						public void onRightButtonClick(EditText etContent) {
							TeamCreateHelper.createAdvancedTeam(MainActivity.this, etContent.getText().toString(), selected);
						}

						@Override
						public void onLeftButtonClick(EditText etContent) {
						}
					});
					dialogEditText.show();
				} else {
					Toast.makeText(MainActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private void registerObservers(boolean register) {
		MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
		service.observeRecentContact(messageObserver, register);
	}

	// 注销
	public static void logout(Context context, boolean quit) {
		Intent extra = new Intent();
		extra.putExtra(EXTRA_APP_QUIT, quit);
		start(context, extra);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onParseIntent();
		// 等待同步数据完成
		boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
			@Override
			public void onEvent(Void v) {
				DialogMaker.dismissProgressDialog();
			}
		});
		Log.i(TAG, "sync completed = " + syncCompleted);
		if (!syncCompleted) {
			DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
		}
		// ------------------------------
		registerMsgUnreadInfoObserver(true);
		registerSystemMessageObservers(true);
		requestSystemMessageUnreadCount();
	}

	/**
	 * 注册未读消息数量观察者
	 */
	private void registerMsgUnreadInfoObserver(boolean register) {
		if (register) {
			ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
		} else {
			ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
		}
	}

	/**
	 * 注册/注销系统消息未读数变化
	 * 
	 * @param register
	 */
	private void registerSystemMessageObservers(boolean register) {
		NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);
	}

	private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
		@Override
		public void onEvent(Integer unreadCount) {
			SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
			ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
		}
	};

	/**
	 * 查询系统消息未读数
	 */
	private void requestSystemMessageUnreadCount() {
		int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
		SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
		ReminderManager.getInstance().updateContactUnreadNum(unread);
	}

	private void onParseIntent() {
		Intent intent = getIntent();
		if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
			IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
			switch (message.getSessionType()) {
			case P2P:
				SessionHelper.startP2PSession(this, message.getSessionId());
				break;
			case Team:
				SessionHelper.startTeamSession(this, message.getSessionId());
				break;
			default:
				break;
			}
		} else if (intent.hasExtra(EXTRA_APP_QUIT)) {
			onLogout();
			return;
		} else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
			if (AVChatProfile.getInstance().isAVChatting()) {
				Intent localIntent = new Intent();
				localIntent.setClass(this, AVChatActivity.class);
				startActivity(localIntent);
			}
		} else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
			Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
			String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
			if (!TextUtils.isEmpty(account)) {
				SessionHelper.startP2PSession(this, account);
			}
		}
	}

	protected void initData() {
		mFragmentManager = getSupportFragmentManager();
		mCurrentFragment = mHomeFragment = new HomeFragment();
		mContactFragment = new ContactsFragment();
		mContactFragment.setContactsCustomization(new ContactsCustomization() {
			@Override
			public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
				return FuncItem.FuncViewHolder.class;
			}

			@Override
			public List<AbsContactItem> onGetFuncItems() {
				return FuncItem.provide();
			}

			@Override
			public void onFuncItemClick(AbsContactItem item) {
				FuncItem.handle(MainActivity.this, item);
			}
		});
		// mContactFragment.attachTabData(MainTab.CONTACT);
		mRecentContactsFragment = new RecentContactsFragment();
		mCircleFragment = new CircleFragment();
		mDynamicFragment = new DynamicFragment();
		mMineFragment = new MineFragment();

		mTabList = new ArrayList<MainTabInfo>();
		mTabList.add(new MainTabInfo(R.drawable.home_default, R.drawable.home_selected, getString(R.string.tab_home), 0, MainTabEnum.HOME));
		mTabList.add(new MainTabInfo(R.drawable.friend_default, R.drawable.friend_selected, getString(R.string.tab_friends), 0, MainTabEnum.FRIEND));
		mTabList.add(new MainTabInfo(R.drawable.chat_default, R.drawable.chat_selected, getString(R.string.tab_chat), 0, MainTabEnum.SESSION));
		mTabList.add(new MainTabInfo(R.drawable.circle_default, R.drawable.circle_selected, getString(R.string.tab_circle), 0, MainTabEnum.CIRCLE));
		mTabList.add(new MainTabInfo(R.drawable.dynamic_default, R.drawable.dynamic_selected, getString(R.string.tab_dynamic), 0, MainTabEnum.DYNAMIC));
		mTabList.add(new MainTabInfo(R.drawable.mine_default, R.drawable.mine_selected, getString(R.string.tab_mine), 0, MainTabEnum.MINE));
		mGvTab.setAdapter(mTabAdfapter = new MainTabAdapter(mTabList));
		mGvTab.setOnItemClickListener(this);
		mFragmentManager.beginTransaction().replace(R.id.ll_container, mHomeFragment).commit();// 默认初始化第一个
		// ------------------------------------初始化聊天未读消息数
		MainTabInfo friendTab = mTabList.get(2);
		friendTab.setMsgNum(NIMClient.getService(MsgService.class).getTotalUnreadCount());
		mTabAdfapter.notifyDataSetChanged();
	}

	// data
	private List<RecentContact> items;
	Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
		@Override
		public void onEvent(List<RecentContact> messages) {
			int index;
			items = new ArrayList<RecentContact>();
			for (RecentContact msg : messages) {
				index = -1;
				for (int i = 0; i < items.size(); i++) {
					if (msg.getContactId().equals(items.get(i).getContactId()) && msg.getSessionType() == (items.get(i).getSessionType())) {
						index = i;
						break;
					}
				}

				if (index >= 0) {
					items.remove(index);
				}

				items.add(msg);
			}

			MainTabInfo friendTab = mTabList.get(2);
			friendTab.setMsgNum(NIMClient.getService(MsgService.class).getTotalUnreadCount());
			mTabAdfapter.notifyDataSetChanged();

		}
	};

	protected void initView() {
		dynamicFabuDialog = new DynamicFabuDialog(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MainTabInfo tabInfo = mTabList.get(position);
		mTabAdfapter.setSelectedTabIndex(position);
		mTabAdfapter.notifyDataSetChanged();
		// --------------------
		mBtnReleaseDynamic.setVisibility(View.GONE);
		mBtnEditCircle.setVisibility(View.GONE);
		// ------------
		switch (tabInfo.getTabEnum()) {
		case HOME:
			gotoFragment(mHomeFragment);
			break;
		case FRIEND:
			if (UserUtils.checkState(this))
				gotoFragment(mContactFragment);
			break;
		case SESSION:
			if (UserUtils.checkState(this))
				gotoFragment(mRecentContactsFragment);
			break;
		case CIRCLE:
			mBtnEditCircle.setVisibility(View.VISIBLE);
			gotoFragment(mCircleFragment);
			break;
		case DYNAMIC:
			mBtnReleaseDynamic.setVisibility(View.VISIBLE);
			gotoFragment(mDynamicFragment);
			Intent intent = new Intent("myDynamic");
			intent.putExtra("type", FRIENDDYNAMIC);
			sendBroadcast(intent);
			break;
		case MINE:
			gotoFragment(mMineFragment);
			break;
		}
	}

	public void gotoFragment(Fragment toFragment) {
		if (mCurrentFragment != toFragment) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			if (!toFragment.isAdded()) { // 先判断是否被add过
				transaction.hide(mCurrentFragment).add(R.id.ll_container, toFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mCurrentFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
			}
			mCurrentFragment = toFragment;
		} else if (mCurrentFragment == toFragment && mCurrentFragment instanceof CircleFragment) {
			EventBus.getDefault().post(new CircleUpdateBean());
		}
	}

	protected void setAttribute() {
		new CheckUpdatUtils(this).doCheckNewVersionTask(false);
	}

	private long touchTime = 0l;// 按第一次返回键的时间
	private long waitTime = 2000l;// 两次返回键的有效时间

	// 退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 实现按两次“返回键”按退出程序
			long currentTime = System.currentTimeMillis();
			if (currentTime - touchTime > waitTime) {
				// Toast提示再次点击
				MyToast.makeText(MainActivity.this, getResources().getString(R.string.click_again_exit), Toast.LENGTH_LONG).show();
				touchTime = currentTime;// 记录第一次点击的时间
			} else {
				this.finish();
			}
			return true;
		}
		return false;
	}

	private void showPopwindow(View view) {
		PopuJar popuJar = new PopuJar(this);
		popuJar.addPopuItem(new PopuItem(0, getString(R.string.pop_scan), R.drawable.ic_qrscan, false));
		popuJar.addPopuItem(new PopuItem(1, getString(R.string.pop_add_friend), R.drawable.ic_add_friend, false));
		popuJar.addPopuItem(new PopuItem(2, getString(R.string.pop_discussion_groups), R.drawable.ic_group_chat, false));
		popuJar.addPopuItem(new PopuItem(3, getString(R.string.pop_group), R.drawable.ic_advanced_group, false));
		popuJar.addPopuItem(new PopuItem(4, getString(R.string.pop_group_search), R.drawable.ic_search, false));
		popuJar.addPopuItem(new PopuItem(5, getString(R.string.pop_share), R.drawable.ic_share, false));
		popuJar.show(view);
		popuJar.setOnPopuItemClickListener(new OnPopuItemClickListener() {
			@Override
			public void onItemClick(PopuJar source, int pos, int actionId) {
				switch (pos) {
				case 0:// 扫一扫
					startActivity(new Intent(MainActivity.this, CaptureActivity.class));
					break;
				case 1:// 添加好友
					AddFriendActivity.start(MainActivity.this);
					break;
				case 2:// 创建讨论组(讨论组)
					ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
					NimUIKit.startContactSelect(MainActivity.this, option, REQUEST_CODE_NORMAL);
					break;
				case 3:// 创建高级群
					ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
					NimUIKit.startContactSelect(MainActivity.this, advancedOption, REQUEST_CODE_ADVANCED);
					break;
				case 4:// 搜索高级群
					AdvancedTeamSearchActivity.start(MainActivity.this);
					break;
				case 5:// 分享
						// 添加分享界面
					if (inviteFriendDialog == null) {
						inviteFriendDialog = new InviteFriendDialog(MainActivity.this, Constants.APP_SHARE);
					}
					inviteFriendDialog.showDialog();
					break;
				}
			}
		});
	}

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@OnClick(value = { R.id.btn_edit_circle, R.id.btn_release_dynamic, R.id.btn_add })
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_circle:
			if (!checkUserStatus()) {
				return;
			}
			mCircleFragment.startActivityForResult(new Intent(this, CircleCreateActivity.class), 1);
			break;
		case R.id.btn_release_dynamic:
			if (!checkUserStatus()) {
				return;
			}
			dynamicFabuDialog.show();
			break;
		case R.id.btn_add:
			if (!checkUserStatus()) {
				return;
			}
			showPopwindow(mBtnAdd);
			break;
		}
	}

	/**
	 * 
	 * checkUserStatus(检查用户状态)
	 * void
	 * 
	 * @exception
	 */
	protected boolean checkUserStatus() {
		UserInfoPreferences preferences = new UserInfoPreferences(this);
		if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活
			DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.active_inform), getString(R.string.go_active),
					new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							startActivity(new Intent(MainActivity.this, ActivateAccountActivity.class));
						}
					});
			commonDialog.show();
			return false;
		} else if (Constants.YOUKE.equals(preferences.getUserStatus())) {// 游客身份
			DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.register_inform), getString(R.string.go_register),
					new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							startActivity(new Intent(MainActivity.this, RegistActivity.class));
						}
					});
			commonDialog.show();
			return false;
		}
		return true;
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private LocalBroadcastManager broadcastManager;
	private BroadcastReceiver internalDebugReceiver;
	private BroadcastReceiver broadcastReceiver;

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// outState.putBoolean("isConflict", isConflict);
		// outState.putBoolean(Constant.ACCOUNT_REMOVED,
		// isCurrentAccountRemoved);
		// super.onSaveInstanceState(outState);//这行代码打开 一旦Fragment闪退真个应用切换将失效
	}

	// 注销
	private void onLogout() {
		// 清理缓存&注销监听
		LogoutHelper.logout();
		// 启动登录
		LoginActivity.start(this);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		enableMsgNotification(false);
	}

	@Override
	public void onPause() {
		super.onPause();
		enableMsgNotification(true);
	}

	private void enableMsgNotification(boolean enable) {
		// boolean msg = (pager.getCurrentItem() !=
		// MainTab.RECENT_CONTACTS.tabIndex);
		// if (enable | msg) {
		if (enable) {
			/**
			 * 设置最近联系人的消息为已读
			 * 
			 * @param account
			 *            , 聊天对象帐号，或者以下两个值： {@link #MSG_CHATTING_ACCOUNT_ALL}
			 *            目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
			 *            {@link #MSG_CHATTING_ACCOUNT_NONE}
			 *            目前没有与任何人对话，需要状态栏消息通知
			 */
			NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
		} else {
			NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
		}
	}

	/**
	 * 未读消息数量观察者实现
	 */
	@Override
	public void onUnreadNumChanged(ReminderItem item) {
		int unreadNum = item.getUnread();
		switch (item.getId()) {
		case 0:// 聊天
			mTabList.get(2).setMsgNum(NIMClient.getService(MsgService.class).getTotalUnreadCount());
			// mTabList.get(2).setMsgNum(unreadNum);
			break;
		case 1:// 好友
			registerObservers(true);
			mTabList.get(1).setMsgNum(unreadNum);
			break;
		}
		mTabAdfapter.notifyDataSetChanged();
	}
}
