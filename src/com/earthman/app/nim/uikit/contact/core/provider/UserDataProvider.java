package com.earthman.app.nim.uikit.contact.core.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.UIKitLogTag;
import com.earthman.app.nim.uikit.common.util.log.LogUtil;
import com.earthman.app.nim.uikit.contact.core.item.AbsContactItem;
import com.earthman.app.nim.uikit.contact.core.item.ContactItem;
import com.earthman.app.nim.uikit.contact.core.item.ItemTypes;
import com.earthman.app.nim.uikit.contact.core.query.TextQuery;
import com.earthman.app.nim.uikit.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

public final class UserDataProvider {
    public static final List<AbsContactItem> provide(TextQuery query) {
        List<UserInfoProvider.UserInfo> sources = query(query);
        List<AbsContactItem> items = new ArrayList<AbsContactItem>(sources.size());
        for (UserInfoProvider.UserInfo u : sources) {
            items.add(new ContactItem(ContactHelper.makeContactFromUserInfo(u), ItemTypes.FRIEND));
        }

        LogUtil.i(UIKitLogTag.CONTACT, "contact provide data size =" + items.size());
        return items;
    }

    private static final List<UserInfoProvider.UserInfo> query(TextQuery query) {
        if (query != null) {
            List<UserInfoProvider.UserInfo> users = NimUIKit.getContactProvider().getUserInfoOfMyFriends();
            for (Iterator<UserInfoProvider.UserInfo> iter = users.iterator(); iter.hasNext(); ) {
                if (!ContactSearch.hitUser(iter.next(), query)) {
                    iter.remove();
                }
            }
            return users;
        } else {
            return NimUIKit.getContactProvider().getUserInfoOfMyFriends();
        }
    }
}