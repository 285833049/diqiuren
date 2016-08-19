package com.earthman.app.nim.login;

import com.earthman.app.nim.NimCache;
import com.earthman.app.nim.uikit.LoginSyncDataStatusObserver;
import com.earthman.app.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
//        ChatRoomHelper.logout();//暂不集成聊天室
        NimCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
        NIMClient.getService(AuthService.class).logout();
    }
}
