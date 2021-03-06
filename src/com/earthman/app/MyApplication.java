package com.earthman.app;

import java.util.ArrayList;
import java.util.List;

import org.litepal.LitePalApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.charlie.lee.androidcommon.http.RequestManager;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.nim.NimCache;
import com.earthman.app.nim.avchat.AVChatProfile;
import com.earthman.app.nim.avchat.activity.AVChatActivity;
import com.earthman.app.nim.common.util.sys.SystemUtil;
import com.earthman.app.nim.config.ExtraOptions;
import com.earthman.app.nim.config.preference.Preferences;
import com.earthman.app.nim.config.preference.UserPreferences;
import com.earthman.app.nim.contact.ContactHelper;
import com.earthman.app.nim.rts.activity.RTSActivity;
import com.earthman.app.nim.session.NimDemoLocationProvider;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.uikit.ImageLoaderKit;
import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.cache.FriendDataCache;
import com.earthman.app.nim.uikit.cache.NimUserInfoCache;
import com.earthman.app.nim.uikit.cache.TeamDataCache;
import com.earthman.app.nim.uikit.contact.ContactProvider;
import com.earthman.app.nim.uikit.contact.core.query.PinYin;
import com.earthman.app.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.earthman.app.ui.activity.WelcomeActivity;
import com.earthman.app.utils.CrashHandler;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatRingerConfig;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class MyApplication extends LitePalApplication {

	public static Context applicationContext;
	private static MyApplication instance;
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		applicationContext = this;
		LogUtil.isDebug = true;
		instance = this;
		// ---------------------Bugly、UMeng统计
		if (HttpUrlConstants.isDevelopmentMode == false) {// 非开发模式才会执行统计信息
			CrashReport.initCrashReport(getApplicationContext(), "900024997", false);
			// AnalyticsConfig.setAppkey(this, "570b5d88e0f55ad47300262e");
			AnalyticsConfig.setChannel("SelfPlatform");
			MobclickAgent.setDebugMode(true);
			MobclickAgent.openActivityDurationTrack(false);// 禁止使用友盟默认统计，否则Fragmetn统计失效
			try {
				UserInfoPreferences userinfo = new UserInfoPreferences(this);
				CrashReport.setUserId(userinfo.getUseruniqueId());// 设置地球人代码为用户的唯一标识
				MobclickAgent.onProfileSignIn(userinfo.getUseruniqueId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 初始化ImageLoader
		//YwbImageLoader.initImageLoader(this);

		initNetwork();
		initNIM();//初始化云信
		// 添加全局异常捕获
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		// Connector.getDatabase();// 运用litepal创建本地数据库
	}

	public static MyApplication getInstance() {
		return instance;
	}

	private void initNetwork() {
		RequestManager rm = RequestManager.getInstance();
		rm.setTipTimeout(getString(R.string.net_error));
		rm.setTipNoHostAddress(getString(R.string.net_error));
		rm.setTipNoConnector(getString(R.string.no_connector));
		rm.setTipNetworkError(getString(R.string.net_error));
		rm.setTipConnectServerException(getString(R.string.net_error));
		rm.init(this);
	}

	/**
	 * initNIM初始化云信
	 * void
	 */
	private void initNIM() {
		NimCache.setContext(this);
		NIMClient.init(this, getLoginInfo(), getOptions());
		ExtraOptions.provide();
		
        if (inMainProcess()) {
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 注册网络通话来电
            enableAVChat();
            // 注册白板会话
            enableRTS();
            // 注册语言变化监听
            registerLocaleReceiver(true);
        }
	}

	private LoginInfo getLoginInfo() {
		String account = Preferences.getUserAccount();
		String token = Preferences.getUserToken();

		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
			NimCache.setAccount(account.toLowerCase());
			return new LoginInfo(account, token);
		} else {
			return null;
		}
	}

	private SDKOptions getOptions() {
		SDKOptions options = new SDKOptions();

		// 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
		StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
		if (config == null) {
			config = new StatusBarNotificationConfig();
		}
		// 点击通知需要跳转到的界面
		config.notificationEntrance = WelcomeActivity.class;
		config.notificationSmallIconId = R.drawable.ic_launcher;

		// 通知铃声的uri字符串
		config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
		options.statusBarNotificationConfig = config;
		NimCache.setNotificationConfig(config);
		UserPreferences.setStatusConfig(config);

		// 配置保存图片，文件，log等数据的目录
		String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
		options.sdkStorageRootPath = sdkPath;

		// 配置数据库加密秘钥
		options.databaseEncryptKey = "NETEASE";

		// 配置是否需要预下载附件缩略图	
		options.preloadAttach = true;
		
		// 配置附件缩略图的尺寸大小，
		options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

		// 用户信息提供者
		options.userInfoProvider = infoProvider;

		// 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
		options.messageNotifierCustomization = messageNotifierCustomization;

		return options;
	}
	
    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }
    
    /**
     * 音视频通话配置与监听
     */
    private void enableAVChat() {
        setupAVChat();
        registerAVChatIncomingCallObserver(true);
    }
    
    private void setupAVChat() {
        AVChatRingerConfig config = new AVChatRingerConfig();
        config.res_connecting = R.raw.avchat_connecting;
        config.res_no_response = R.raw.avchat_no_response;
        config.res_peer_busy = R.raw.avchat_peer_busy;
        config.res_peer_reject = R.raw.avchat_peer_reject;
        config.res_ring = R.raw.avchat_ring;
        AVChatManager.getInstance().setRingerConfig(config); // 设置铃声配置
    }
    
    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                Log.e("Extra", "Extra Message->" + extra);
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(NimCache.getContext(), data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }
    
    /**
     * 白板实时时会话配置与监听
     */
    private void enableRTS() {
        //setupRTS();
        registerRTSIncomingObserver(true);
    }
    
    private void registerRTSIncomingObserver(boolean register) {
        RTSManager.getInstance().observeIncomingSession(new Observer<RTSData>() {
            @Override
            public void onEvent(RTSData rtsData) {
                RTSActivity.incomingSession(NimCache.getContext(), rtsData, RTSActivity.FROM_BROADCAST_RECEIVER);
            }
        }, register);
    }
    
    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }
    
    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private void initUIKit() {
        // 初始化，需要传入用户信息提供者
        NimUIKit.init(this, infoProvider, contactProvider);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();
    }

    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }

            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.drawable.avatar_def;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            Drawable drawable = getResources().getDrawable(R.drawable.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

    private ContactProvider contactProvider = new ContactProvider() {
        @Override
        public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
            List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
            List<UserInfoProvider.UserInfo> users = new ArrayList<UserInfoProvider.UserInfo>(nimUsers.size());
            if (!nimUsers.isEmpty()) {
                users.addAll(nimUsers);
            }

            return users;
        }

        @Override
        public int getMyFriendsCount() {
            return FriendDataCache.getInstance().getMyFriendCounts();
        }

        @Override
        public String getUserDisplayName(String account) {
            return NimUserInfoCache.getInstance().getUserDisplayName(account);
        }
    };

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

}
