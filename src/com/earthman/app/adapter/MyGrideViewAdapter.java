package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.GetEarthManFcResponse.EarthManFcInfo;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.widget.RoundCornerImageView;

public class MyGrideViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<EarthManFcInfo> earthManFcInfos;
	private ViewHolder viewHolder;
	private YwbImageLoader ywbImageLoader;

	public MyGrideViewAdapter(Context context, ArrayList<EarthManFcInfo> earthManFcInfos) {
		this.context = context;
		this.earthManFcInfos = earthManFcInfos;
		ywbImageLoader  = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return earthManFcInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.earthman_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		EarthManFcInfo earthManFcInfo = earthManFcInfos.get(position);
		ywbImageLoader.loadImage(earthManFcInfo.getAvatar(), viewHolder.iv_avatar, R.drawable.user_avatar);
		if(!TextUtils.isEmpty(earthManFcInfo.getRemarks())){
			viewHolder.tv_nickname.setText(earthManFcInfo.getRemarks());
		}else{
			viewHolder.tv_nickname.setText(earthManFcInfo.getNice());
		}		
		return convertView;
	}
	
	private class ViewHolder{
		private RoundCornerImageView iv_avatar;
		private TextView tv_nickname;
		private ViewHolder(View convertView){
			iv_avatar = (RoundCornerImageView) convertView.findViewById(R.id.iv_avatar);
			tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
		}
	}

}
