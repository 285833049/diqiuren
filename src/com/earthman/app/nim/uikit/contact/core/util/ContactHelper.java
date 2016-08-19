package com.earthman.app.nim.uikit.contact.core.util;

import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.contact.core.model.IContact;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by huangjun on 2015/9/8.
 */
public class ContactHelper {
    public static IContact makeContactFromUserInfo(final UserInfoProvider.UserInfo userInfo) {
        return new IContact() {
            @Override
            public String getContactId() {
                return userInfo.getAccount();
            }

            @Override
            public int getContactType() {
                return Type.Friend;
            }

            @Override
            public String getDisplayName() {
//				NimUserInfo nimUserInfo=NIMClient.getService(UserService.class).getUserInfo(userInfo.getAccount());
//				Map<String,Object> nimUserMap=nimUserInfo.getExtensionMap();
//				String a=(String) nimUserMap.get("userid");
                return NimUIKit.getContactProvider().getUserDisplayName(userInfo.getAccount());
            }

//			@Override
//			public int getUserId() {
//				NimUserInfo nimUserInfo=NIMClient.getService(UserService.class).getUserInfo(userInfo.getAccount());
//				Map<String,Object> nimUserMap=nimUserInfo.getExtensionMap();
//				return (Integer) nimUserMap.get("userid");
//			}
//
			@Override
			public String getGroupName() {
				Friend friend = NIMClient.getService(FriendService.class).getFriendByAccount(userInfo.getAccount());
				return friend.getAlias();
			}
            
        };
    }
}
