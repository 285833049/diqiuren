package com.earthman.app.nim.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.earthman.app.nim.main.viewholder.SystemMessageViewHolder;
import com.earthman.app.nim.uikit.common.adapter.TAdapter;
import com.earthman.app.nim.uikit.common.adapter.TAdapterDelegate;

public class SystemMessageAdapter extends TAdapter {

	private SystemMessageViewHolder.SystemMessageListener systemMessageListener;

	public SystemMessageAdapter(Context context, List<?> items, TAdapterDelegate delegate, SystemMessageViewHolder.SystemMessageListener listener) {
		super(context, items, delegate);
		this.systemMessageListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (systemMessageListener != null) {
			((SystemMessageViewHolder) view.getTag()).setListener(systemMessageListener);
		}

		return view;
	}
}
