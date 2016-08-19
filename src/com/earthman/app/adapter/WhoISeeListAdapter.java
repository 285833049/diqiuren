package com.earthman.app.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.VisitHistoryResponse.VisitHistoryResult.VisitorsListEntity;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.ui.activity.mine.WhoISeeActivity;
import com.earthman.app.widget.RoundCornerImageView;

/**
 * 我看过谁adapter
 * 
 * @author xiexianyong
 * 
 */

public class WhoISeeListAdapter extends BaseAdapter {
	private YwbImageLoader imageLoader;
	private Context context;
	private ArrayList<VisitorsListEntity> list;

	public WhoISeeListAdapter(Context context, ArrayList<VisitorsListEntity> result) {
		this.context = context;
		this.list = result;
		imageLoader = new YwbImageLoader();
	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	public void clearData() {
		list.clear();
		notifyDataSetChanged();
	};

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.who_i_see_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		VisitorsListEntity visitorsentity = list.get(position);
		if(!TextUtils.isEmpty(visitorsentity.getRemarks())){
			viewHolder.visitors_user_name.setText(visitorsentity.getRemarks());
		}else{
			viewHolder.visitors_user_name.setText(visitorsentity.getNice());
		}		
		viewHolder.visitors_user_cardid.setText(visitorsentity.getCardId());
		imageLoader.loadImage(visitorsentity.getAvatar(), viewHolder.visitors_user_ico, R.drawable.user_avatar);
		int isFrined = visitorsentity.getIsFrined();
		if (isFrined == 1) {// 好友
			viewHolder.add_friend_btn.setVisibility(View.GONE);
		} else if (isFrined == 0) {// 不是好友
			viewHolder.add_friend_btn.setVisibility(View.VISIBLE);
		}
		final String friend = visitorsentity.getId();
		viewHolder.add_friend_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (context instanceof WhoISeeActivity) {
					WhoISeeActivity whoisee = (WhoISeeActivity) context;
					whoisee.getaddfriend(friend);
				}
			}
		});

		final String friendsuserid = visitorsentity.getId();
		// 跳转到他的主页
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PeronalDetialActivity.class);
				intent.putExtra("friendsuserid", friendsuserid);
				start((Activity) v.getContext(), intent);

			}

			public void start(Activity srcActivity, Intent intent) {
				srcActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private RoundCornerImageView visitors_user_ico;
		private TextView visitors_user_name;
		private TextView visitors_user_cardid;
		private Button add_friend_btn;

		private ViewHolder(View convertView) {
			visitors_user_ico = (RoundCornerImageView) convertView.findViewById(R.id.visitors_user_ico);
			visitors_user_name = (TextView) convertView.findViewById(R.id.visitors_user_name);
			visitors_user_cardid = (TextView) convertView.findViewById(R.id.visitors_user_cardid);
			add_friend_btn = (Button) convertView.findViewById(R.id.add_friend_btn);
		}
	}
}