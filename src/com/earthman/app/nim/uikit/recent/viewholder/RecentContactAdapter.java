package com.earthman.app.nim.uikit.recent.viewholder;

import java.util.List;

import android.content.Context;

import com.earthman.app.nim.uikit.common.adapter.TAdapter;
import com.earthman.app.nim.uikit.common.adapter.TAdapterDelegate;
import com.earthman.app.nim.uikit.recent.RecentContactsCallback;
import com.netease.nimlib.sdk.msg.model.RecentContact;

/**
 * 最近联系人列表的adapter，管理了一个callback
 */
public class RecentContactAdapter extends TAdapter<RecentContact> {

    private RecentContactsCallback callback;

    public RecentContactAdapter(Context context, List<RecentContact> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
    }

    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }
}
