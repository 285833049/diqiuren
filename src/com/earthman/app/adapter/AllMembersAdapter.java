package com.earthman.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.cache.TeamDataCache;
import com.earthman.app.nim.uikit.common.ui.imageview.HeadImageView;
import com.earthman.app.nim.uikit.team.adapter.TeamMemberAdapter;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * 全部成员Adapter
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-6-27 下午5:59:27
 * @Decription
 */
public class AllMembersAdapter extends BaseAdapter {

	private Context context;
	private List<TeamMember> list;
	private TeamMemberAdapter.TeamMemberItem memberItem;

	public AllMembersAdapter(Context context, List<TeamMember> groupUserInfo) {
		this.context = context;
		this.list = groupUserInfo;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.group_all_menbers_list_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final TeamMember AllMemberEntity = list.get(position);
		viewHolder.all_menbers_user_name.setText(TeamDataCache.getInstance().getTeamMemberDisplayName(AllMemberEntity.getTid(), AllMemberEntity.getAccount()));
		viewHolder.all_menbers_user_ico.loadBuddyAvatar(AllMemberEntity.getAccount());
		viewHolder.all_menbers_user_cardid.setText(AllMemberEntity.getAccount());

		// 跳转到他的主页
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PeronalDetialActivity.class);
				intent.putExtra("friendsuserid", AllMemberEntity.getAccount());
				start((Activity) v.getContext(), intent);

			}

			public void start(Activity srcActivity, Intent intent) {
				srcActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private HeadImageView all_menbers_user_ico;
		private TextView all_menbers_user_name;
		private TextView all_menbers_user_cardid;

		private ViewHolder(View convertView) {
			all_menbers_user_ico = (HeadImageView) convertView.findViewById(R.id.all_menbers_user_ico);
			all_menbers_user_name = (TextView) convertView.findViewById(R.id.all_menbers_user_name);
			all_menbers_user_cardid = (TextView) convertView.findViewById(R.id.all_menbers_user_cardid);
		}
	}

}
