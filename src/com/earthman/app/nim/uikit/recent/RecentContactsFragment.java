package com.earthman.app.nim.uikit.recent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.config.preference.Preferences;
import com.earthman.app.nim.login.LogoutHelper;
import com.earthman.app.nim.main.activity.MultiportActivity;
import com.earthman.app.nim.main.reminder.ReminderManager;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.session.extension.GuessAttachment;
import com.earthman.app.nim.session.extension.RTSAttachment;
import com.earthman.app.nim.session.extension.SnapChatAttachment;
import com.earthman.app.nim.session.extension.StickerAttachment;
import com.earthman.app.nim.uikit.cache.FriendDataCache;
import com.earthman.app.nim.uikit.cache.TeamDataCache;
import com.earthman.app.nim.uikit.common.adapter.TAdapterDelegate;
import com.earthman.app.nim.uikit.common.adapter.TViewHolder;
import com.earthman.app.nim.uikit.common.fragment.TFragment;
import com.earthman.app.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.earthman.app.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;
import com.earthman.app.nim.uikit.common.ui.listview.ListViewUtil;
import com.earthman.app.nim.uikit.common.util.log.LogUtil;
import com.earthman.app.nim.uikit.recent.viewholder.CommonRecentViewHolder;
import com.earthman.app.nim.uikit.recent.viewholder.RecentContactAdapter;
import com.earthman.app.nim.uikit.recent.viewholder.RecentViewHolder;
import com.earthman.app.nim.uikit.recent.viewholder.TeamRecentViewHolder;
import com.earthman.app.nim.uikit.uinfo.UserInfoHelper;
import com.earthman.app.nim.uikit.uinfo.UserInfoObservable;
import com.earthman.app.ui.activity.login.LoginActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment implements TAdapterDelegate, OnClickListener {

	private View notifyBar;
	private TextView notifyBarText;
	// 同时在线的其他端的信息
	private List<OnlineClient> onlineClients;
	private View multiportBar;

	// 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
	public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag
	// view
	private ListView listView;
	private View emptyBg;
	private TextView emptyHint;
	// data
	private List<RecentContact> items;
	private RecentContactAdapter adapter;
	private boolean msgLoaded = false;
	private RecentContactsCallback callback;
	private UserInfoObservable.UserInfoObserver userInfoObserver;
	private LinearLayout ll_clear_unread;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		addRecentContactsCallBack();
		findViews();
		initMessageList();
		requestMessages(true);
		registerObservers(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.nim_recent_contacts, container, false);
	}

	@Override
	public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
		SessionTypeEnum type = items.get(position).getSessionType();
		if (type == SessionTypeEnum.Team) {
			return TeamRecentViewHolder.class;
		} else {
			return CommonRecentViewHolder.class;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean enabled(int position) {
		return true;
	}

	private void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
		boolean empty = items.isEmpty() && msgLoaded;
		emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
		emptyHint.setHint("还没有会话，在好友中找个人聊聊吧！");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		registerObservers(false);
	}

	/**
	 * 查找页面控件
	 */
	private void findViews() {
		notifyBar = getView().findViewById(R.id.status_notify_bar);
		notifyBarText = (TextView) getView().findViewById(R.id.status_desc_label);
		notifyBar.setVisibility(View.GONE);

		multiportBar = getView().findViewById(R.id.multiport_notify_bar);
		multiportBar.setVisibility(View.GONE);
		multiportBar.setOnClickListener(this);
		listView = findView(R.id.lvMessages);
		emptyBg = findView(R.id.emptyBg);
		emptyHint = findView(R.id.message_list_empty_hint);

		ll_clear_unread = findView(R.id.ll_clear_unread);
		ll_clear_unread.setOnClickListener(this);
		TextView tvClearIcon = findView(R.id.tv_clear_icon);
		tvClearIcon.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/iconfont.ttf"));
		tvClearIcon.setOnClickListener(this);
	}

	/**
	 * 初始化消息列表
	 */
	private void initMessageList() {
		items = new ArrayList<RecentContact>();

		adapter = new RecentContactAdapter(getActivity(), items, this);
		adapter.setCallback(callback);

		listView.setAdapter(adapter);
		listView.setItemsCanFocus(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (callback != null) {
					RecentContact recent = (RecentContact) parent.getAdapter().getItem(position);
					callback.onItemClick(recent);
				}
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < listView.getHeaderViewsCount()) {
					return false;
				}
				showLongClickMenu((RecentContact) parent.getAdapter().getItem(position));

				return true;
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				adapter.onMutable(scrollState == SCROLL_STATE_FLING);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});

	}

	private void addRecentContactsCallBack() {
		this.setCallback(new RecentContactsCallback() {
			@Override
			public void onRecentContactsLoaded() {
				// 最近联系人列表加载完毕
			}

			@Override
			public void onUnreadCountChange(int unreadCount) {
				if (unreadCount > 0) {
					ll_clear_unread.setVisibility(View.VISIBLE);
				} else {
					ll_clear_unread.setVisibility(View.GONE);
				}
				ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
			}

			@Override
			public void onItemClick(RecentContact recent) {
				// 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
				switch (recent.getSessionType()) {
				case P2P:
					SessionHelper.startP2PSession(getActivity(), recent.getContactId());
					break;
				case Team:
					SessionHelper.startTeamSession(getActivity(), recent.getContactId());
					break;
				default:
					break;
				}
			}

			@Override
			public String getDigestOfAttachment(MsgAttachment attachment) {
				// 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
				// 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
				if (attachment instanceof GuessAttachment) {
					GuessAttachment guess = (GuessAttachment) attachment;
					return guess.getValue().getDesc();
				} else if (attachment instanceof RTSAttachment) {
					return "[白板]";
				} else if (attachment instanceof StickerAttachment) {
					return "[贴图]";
				} else if (attachment instanceof SnapChatAttachment) {
					return "[阅后即焚]";
				}

				return null;
			}

			@Override
			public String getDigestOfTipMsg(RecentContact recent) {
				String msgId = recent.getRecentMessageId();
				List<String> uuids = new ArrayList<String>(1);
				uuids.add(msgId);
				List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
				if (msgs != null && !msgs.isEmpty()) {
					IMMessage msg = msgs.get(0);
					Map<String, Object> content = msg.getRemoteExtension();
					if (content != null && !content.isEmpty()) {
						return (String) content.get("content");
					}
				}

				return null;
			}
		});
	}

	private void showLongClickMenu(final RecentContact recent) {
		CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
		alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
		String title = getString(R.string.main_msg_list_delete_chatting);
		alertDialog.addItem(title, new onSeparateItemClickListener() {
			@Override
			public void onClick() {
				// 删除会话，删除后，消息历史被一起删除
				NIMClient.getService(MsgService.class).deleteRecentContact(recent);
				NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
				items.remove(recent);

				if (recent.getUnreadCount() > 0) {
					refreshMessages(true);
				} else {
					notifyDataSetChanged();
				}
			}
		});

		title = (isTagSet(recent, RECENT_TAG_STICKY) ? getString(R.string.main_msg_list_clear_sticky_on_top) : getString(R.string.main_msg_list_sticky_on_top));
		alertDialog.addItem(title, new onSeparateItemClickListener() {
			@Override
			public void onClick() {
				if (isTagSet(recent, RECENT_TAG_STICKY)) {
					removeTag(recent, RECENT_TAG_STICKY);
				} else {
					addTag(recent, RECENT_TAG_STICKY);
				}
				NIMClient.getService(MsgService.class).updateRecent(recent);

				refreshMessages(false);
			}
		});
		alertDialog.show();
	}

	private void addTag(RecentContact recent, long tag) {
		tag = recent.getTag() | tag;
		recent.setTag(tag);
	}

	private void removeTag(RecentContact recent, long tag) {
		tag = recent.getTag() & ~tag;
		recent.setTag(tag);
	}

	private boolean isTagSet(RecentContact recent, long tag) {
		return (recent.getTag() & tag) == tag;
	}

	private List<RecentContact> loadedRecents;

	private void requestMessages(boolean delay) {
		if (msgLoaded) {
			return;
		}
		getHandler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (msgLoaded) {
					return;
				}
				// 查询最近联系人列表数据
				NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

					@Override
					public void onResult(int code, List<RecentContact> recents, Throwable exception) {
						if (code != ResponseCode.RES_SUCCESS || recents == null) {
							return;
						}
						loadedRecents = recents;

						// 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
						//
						msgLoaded = true;
						if (isAdded()) {
							onRecentContactsLoaded();
						}
					}
				});
			}
		}, delay ? 250 : 0);
	}

	private void onRecentContactsLoaded() {
		items.clear();
		if (loadedRecents != null) {
			items.addAll(loadedRecents);
			loadedRecents = null;
		}
		refreshMessages(true);

		if (callback != null) {
			callback.onRecentContactsLoaded();
		}
	}

	private void refreshMessages(boolean unreadChanged) {
		sortRecentContacts(items);
		notifyDataSetChanged();
		if (unreadChanged) {

			// 方式一：累加每个最近联系人的未读（快）
			/*
			 * int unreadNum = 0;
			 * for (RecentContact r : items) {
			 * unreadNum += r.getUnreadCount();
			 * }
			 */

			// 方式二：直接从SDK读取（相对慢）
			if (callback != null) {

				callback.onUnreadCountChange(NIMClient.getService(MsgService.class).getTotalUnreadCount());
			}
		}
	}

	/**
	 * **************************** 排序 ***********************************
	 */
	private void sortRecentContacts(List<RecentContact> list) {
		if (list.size() == 0) {
			return;
		}
		Collections.sort(list, comp);
	}

	private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

		@Override
		public int compare(RecentContact o1, RecentContact o2) {
			// 先比较置顶tag
			long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
			if (sticky != 0) {
				return sticky > 0 ? -1 : 1;
			} else {
				long time = o1.getTime() - o2.getTime();
				return time == 0 ? 0 : (time > 0 ? -1 : 1);
			}
		}
	};

	/**
	 * ********************** 收消息，处理状态变化 ************************
	 */
	private void registerObservers(boolean register) {
		NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
		NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
		// ---------------------------------------------------
		MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
		service.observeRecentContact(messageObserver, register);
		service.observeMsgStatus(statusObserver, register);
		service.observeRecentContactDeleted(deleteObserver, register);
		registerTeamUpdateObserver(register);
		registerTeamMemberUpdateObserver(register);
		FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
		if (register) {
			registerUserInfoObserver();
		} else {
			unregisterUserInfoObserver();
		}
	}

	/**
	 * 用户状态变化
	 */
	Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

		@Override
		public void onEvent(StatusCode code) {
			if (code.wontAutoLogin()) {
				kickOut(code);
			} else {
				if (code == StatusCode.NET_BROKEN) {
					notifyBar.setVisibility(View.VISIBLE);
					notifyBarText.setText(R.string.net_broken);
				} else if (code == StatusCode.UNLOGIN) {
					notifyBar.setVisibility(View.VISIBLE);
					notifyBarText.setText(R.string.nim_status_unlogin);
				} else {
					notifyBar.setVisibility(View.GONE);
				}
			}
		}
	};

	private void kickOut(StatusCode code) {
		Preferences.saveUserToken("");

		if (code == StatusCode.PWD_ERROR) {
			LogUtil.e("Auth", "user password error");
			Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
		} else {
			LogUtil.i("Auth", "Kicked!");
		}
		onLogout();
	}

	// 注销
	private void onLogout() {
		Toast.makeText(getActivity(), R.string.relogin_inform, Toast.LENGTH_SHORT).show();
		// 清理缓存&注销监听&清除状态
		LogoutHelper.logout();
		LoginActivity.start(getActivity(), true);
		getActivity().finish();
	}

	Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
		@Override
		public void onEvent(List<OnlineClient> onlineClients) {
			if (onlineClients == null || onlineClients.size() == 0) {
				multiportBar.setVisibility(View.GONE);
			} else {
				multiportBar.setVisibility(View.VISIBLE);
				TextView status = (TextView) multiportBar.findViewById(R.id.multiport_desc_label);
				OnlineClient client = onlineClients.get(0);
				switch (client.getClientType()) {
				case ClientType.Windows:
					status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
					break;
				case ClientType.Web:
					status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
					break;
				case ClientType.iOS:
				case ClientType.Android:
					status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
					break;
				default:
					multiportBar.setVisibility(View.GONE);
					break;
				}
			}
		}
	};

	/**
	 * 注册群信息&群成员更新监听
	 */
	private void registerTeamUpdateObserver(boolean register) {
		if (register) {
			TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
		} else {
			TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
		}
	}

	private void registerTeamMemberUpdateObserver(boolean register) {
		if (register) {
			TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
		} else {
			TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
		}
	}

	Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
		@Override
		public void onEvent(List<RecentContact> messages) {
			int index;
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

			refreshMessages(true);
		}
	};

	Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
		@Override
		public void onEvent(IMMessage message) {
			int index = getItemIndex(message.getUuid());
			if (index >= 0 && index < items.size()) {
				RecentContact item = items.get(index);
				item.setMsgStatus(message.getStatus());
				refreshViewHolderByIndex(index);
			}
		}
	};

	Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
		@Override
		public void onEvent(RecentContact recentContact) {
			if (recentContact != null) {
				for (RecentContact item : items) {
					if (TextUtils.equals(item.getContactId(), recentContact.getContactId()) && item.getSessionType() == recentContact.getSessionType()) {
						items.remove(item);
						refreshMessages(true);
						break;
					}
				}
			} else {
				items.clear();
				refreshMessages(true);
			}
		}
	};

	TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

		@Override
		public void onUpdateTeams(List<Team> teams) {
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onRemoveTeam(Team team) {

		}
	};

	TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
		@Override
		public void onUpdateTeamMember(List<TeamMember> members) {
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onRemoveTeamMember(TeamMember member) {

		}
	};

	private int getItemIndex(String uuid) {
		for (int i = 0; i < items.size(); i++) {
			RecentContact item = items.get(i);
			if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
				return i;
			}
		}

		return -1;
	}

	protected void refreshViewHolderByIndex(final int index) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Object tag = ListViewUtil.getViewHolderByIndex(listView, index);
				if (tag instanceof RecentViewHolder) {
					RecentViewHolder viewHolder = (RecentViewHolder) tag;
					viewHolder.refreshCurrentItem();
				}
			}
		});
	}

	public void setCallback(RecentContactsCallback callback) {
		this.callback = callback;
	}

	private void registerUserInfoObserver() {
		if (userInfoObserver == null) {
			userInfoObserver = new UserInfoObservable.UserInfoObserver() {
				@Override
				public void onUserInfoChanged(List<String> accounts) {
					refreshMessages(false);
				}
			};
		}

		UserInfoHelper.registerObserver(userInfoObserver);
	}

	private void unregisterUserInfoObserver() {
		if (userInfoObserver != null) {
			UserInfoHelper.unregisterObserver(userInfoObserver);
		}
	}

	FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> accounts) {
			refreshMessages(false);
		}

		@Override
		public void onDeletedFriends(List<String> accounts) {
			refreshMessages(false);
		}

		@Override
		public void onAddUserToBlackList(List<String> account) {
			refreshMessages(false);
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> account) {
			refreshMessages(false);
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.multiport_notify_bar:
			MultiportActivity.startActivity(getActivity(), onlineClients);
			break;
		case R.id.ll_clear_unread:
			ll_clear_unread.setVisibility(View.GONE);
			NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
				@Override
				public void onResult(int code, List<RecentContact> recents, Throwable e) {
					for (RecentContact recent : recents) {
						NIMClient.getService(MsgService.class).clearUnreadCount(recent.getContactId(),
								recent.getSessionType() == SessionTypeEnum.Team ? SessionTypeEnum.Team : SessionTypeEnum.P2P);
					}
				}
			});

			break;
		}
	}
}
