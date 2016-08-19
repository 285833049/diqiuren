package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.RecentlyResponse.RecentlyResult;
import com.earthman.app.imageloader.YwbImageLoader;

/**
 * @Title: MyZZListAdapter
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */

public class MineTransferAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<RecentlyResult> response;
	private YwbImageLoader imageLoader;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return response.size();
	}

	public MineTransferAdapter(Context context, ArrayList<RecentlyResult> result) {
		super();
		this.context = context;
		this.response = result;
		imageLoader = new YwbImageLoader();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.account_items, null);
			viewHolder.usericon = (ImageView) convertView.findViewById(R.id.mine_img_user_icon);
			viewHolder.username = (TextView) convertView.findViewById(R.id.friend_list_item_name);
			viewHolder.usercardid = (TextView) convertView.findViewById(R.id.friend_item_cardid);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.usercardid.setText(response.get(position).getCardId());
		if(!TextUtils.isEmpty(response.get(position).getRemarks())){
			viewHolder.username.setText(response.get(position).getRemarks());
		}else{
			viewHolder.username.setText(response.get(position).getNice());
		}		
		imageLoader.loadImage(response.get(position).getAvatar(), viewHolder.usericon, R.drawable.user_avatar);
		return convertView;
	}

	public class ViewHolder {
		ImageView usericon;
		TextView username;
		TextView usercardid;
	}
}
