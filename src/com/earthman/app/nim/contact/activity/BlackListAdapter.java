package com.earthman.app.nim.contact.activity;

import java.util.List;

import android.content.Context;

import com.earthman.app.nim.uikit.common.adapter.TAdapter;
import com.earthman.app.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by huangjun on 2015/8/12.
 */
public class BlackListAdapter extends TAdapter<UserInfoProvider.UserInfo> {

    public BlackListAdapter(Context context, List<UserInfoProvider.UserInfo> items, TAdapterDelegate delegate, ViewHolderEventListener
            viewHolderEventListener) {
        super(context, items, delegate);

        this.viewHolderEventListener = viewHolderEventListener;
    }

    public interface ViewHolderEventListener {
        void onItemClick(UserInfoProvider.UserInfo user);

        void onRemove(UserInfoProvider.UserInfo user);
    }

    private ViewHolderEventListener viewHolderEventListener;

    public ViewHolderEventListener getEventListener() {
        return viewHolderEventListener;
    }
}
