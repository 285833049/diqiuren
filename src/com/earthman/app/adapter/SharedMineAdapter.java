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
import com.earthman.app.bean.ShareListInfo.ShareListresult.ShareListEntity;
import com.earthman.app.imageloader.YwbImageLoader;

/**
 * @Title: SharedMineAdapter
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月15日
 */

public class SharedMineAdapter extends BaseAdapter {
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

	public SharedMineAdapter(Context context, ArrayList<ShareListEntity> myUpline) {
		this.context = context;
		this.data = myUpline;
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

		imageLoader.loadImage(shareentity.getAvatar(), viewHolder.tv_user_icon, R.drawable.user_avatar);
		if(!TextUtils.isEmpty(shareentity.getRemarks())){
			viewHolder.tv_user_name.setText(shareentity.getRemarks());
		}else{
			viewHolder.tv_user_name.setText(shareentity.getNice());
		}		
		viewHolder.tv_user_cardid.setText(shareentity.getCardId());

		return convertView;
	}

	private class ViewHolder {
		TextView tv_user_name;
		TextView tv_user_cardid;
		ImageView tv_user_icon;
	}
}
