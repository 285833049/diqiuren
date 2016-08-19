package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MineItemInfo;

/**
 * description: 我的：选项适配器 company: 地球人 author: Vinci date: 2016-3-15下午6:21:11
 * version: 1.0 remark: 创建页面（Vinci）
 */
public class MineItemAdapter extends BaseAdapter {

	private List<MineItemInfo> mItemList;
	private ViewHolder mHolder;

	public MineItemAdapter(List<MineItemInfo> itemList) {
		super();
		this.mItemList = itemList;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_item, null);
			mHolder.tvTitleBg = (TextView) convertView.findViewById(R.id.tv_title_bg);
			mHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		MineItemInfo info = mItemList.get(position);
		mHolder.tvTitleBg.setVisibility(info.isTitle() ? View.VISIBLE : View.GONE);
		mHolder.ivIcon.setBackgroundResource(info.getIconID());
		mHolder.tvName.setText(info.getItemName());

		return convertView;
	}

	private class ViewHolder {
		private TextView tvTitleBg;
		private ImageView ivIcon;
		private TextView tvName;
	}

}
