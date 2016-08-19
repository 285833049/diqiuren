package com.earthman.app.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.RelativeItem;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月18日 上午10:39:58
 * @Decription
 */
public class RelativeGrideAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<RelativeItem> data;

	public RelativeGrideAdapter(Context context, ArrayList<RelativeItem> data) {
		this.context = context;
		this.data = data;

	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.relative_items, null);
		LinearLayout ll_relative = (LinearLayout) convertView.findViewById(R.id.relative_ll);
		TextView relative_tv = (TextView) convertView.findViewById(R.id.relative_tv);
		ImageView relative_iv = (ImageView) convertView.findViewById(R.id.relative_iv);
		RelativeItem relativeItem = data.get(position);
		ll_relative.setBackground(context.getResources().getDrawable(relativeItem.getLine()));
		relative_iv.setBackground(context.getResources().getDrawable(relativeItem.getAdd()));
		relative_tv.setText(relativeItem.getName());
		relative_tv.setTextColor(context.getResources().getColor(relativeItem.getColor()));
		return convertView;
	}
}
