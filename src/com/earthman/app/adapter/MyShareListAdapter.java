package com.earthman.app.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.ShareListInfo.ShareListresult.ShareListEntity;
import com.earthman.app.imageloader.YwbImageLoader;

/**
 * 我分享者
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * 				@Date：2016-3-19 下午7:03:01
 * @Decription
 */
public class MyShareListAdapter extends BaseAdapter {
	private YwbImageLoader imageLoader;
	private Context context;
	private ArrayList<ShareListEntity> data;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public MyShareListAdapter(Context context, ArrayList<ShareListEntity> myOffline) {
		this.context = context;
		this.data = myOffline;
		imageLoader = new YwbImageLoader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.share_mine_item, null);
			viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.nickname_share);
			viewHolder.tv_user_cardid = (TextView) convertView.findViewById(R.id.mine_share_cardid);
			viewHolder.tv_user_icon = (ImageView) convertView.findViewById(R.id.head_share);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		ShareListEntity shareentity = data.get(position);
		if (shareentity != null) {
			imageLoader.loadImage(data.get(position).getAvatar(), viewHolder.tv_user_icon, R.drawable.user_avatar);
			if(!TextUtils.isEmpty(shareentity.getRemarks())){
				viewHolder.tv_user_name.setText(shareentity.getRemarks());
			}else{
				viewHolder.tv_user_name.setText(shareentity.getNice());
			}			
			viewHolder.tv_user_cardid.setText(shareentity.getCardId());
		}

		return convertView;
	}
	private class ViewHolder {
		TextView tv_user_name;
		TextView tv_user_cardid;
		ImageView tv_user_icon;
	}
}
