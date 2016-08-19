package com.earthman.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.earthman.app.R;

/**
 * 敬献记录
 * 
 * @author xiexianyong
 * 
 */
public class RecordAdapter extends BaseAdapter {

	private Context context;

	public RecordAdapter(Context context) {
		this.context = context;
	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return 5;
	}

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return position;
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.rank_item, null);
		}
		return convertView;
	}

}
