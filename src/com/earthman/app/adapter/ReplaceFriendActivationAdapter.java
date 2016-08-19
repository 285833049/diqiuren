package com.earthman.app.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MineReplaceJihuoResponse.ReplaceJihuoResult;
import com.earthman.app.ui.activity.mine.ActivationDetailsActivity;

/**
 * 代替好友激活
 * 
 * @author xiexianyong
 * 
 */
public class ReplaceFriendActivationAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ReplaceJihuoResult> result;

	public ReplaceFriendActivationAdapter(Context context, ArrayList<ReplaceJihuoResult> result) {
		this.context = context;
		this.result = result;
	}

	public void clearData() {
		result.clear();
		notifyDataSetChanged();
	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return result.size();
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
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.replace_friend_activation_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		final ReplaceJihuoResult replaceJihuoResult = result.get(position);

		holder.userid.setText(replaceJihuoResult.getCardId());
		holder.user_message.setText(replaceJihuoResult.getMessage());
		// 跳转到会话界面
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ActivationDetailsActivity.class);
				intent.putExtra("fee", replaceJihuoResult.getFee());
				intent.putExtra("cardId", replaceJihuoResult.getCardId());
				intent.putExtra("message", replaceJihuoResult.getMessage());
				intent.putExtra("id", replaceJihuoResult.getId());
				intent.putExtra("createdAt", replaceJihuoResult.getCreatedAt());
				start((Activity) v.getContext(), intent);

			}

			public void start(Activity srcActivity, Intent intent) {
				srcActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		TextView userid;
		TextView user_message;
		ImageView usericon;

		public ViewHolder(View convertView) {
			userid = (TextView) convertView.findViewById(R.id.friend_id);
			user_message = (TextView) convertView.findViewById(R.id.friend_message);
			usericon = (ImageView) convertView.findViewById(R.id.img_user_icon);
		}
	}
}