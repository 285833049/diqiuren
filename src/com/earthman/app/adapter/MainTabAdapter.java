package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MainTabInfo;

/**
 * @Description：网格视图标签适配器
 * @author：Vinci
 * @date：2014年12月30日 上午11:04:26
 */
public class MainTabAdapter extends BaseAdapter {

	private List<MainTabInfo> mTabList;
	private MainTabInfo mTab;
	private int selectedTabIndex;
	private ViewHolder holder;

	public MainTabAdapter(List<MainTabInfo> itemlist) {
		super();
		this.mTabList = itemlist;
	}

	public void setSelectedTabIndex(int selectedTabIndex) {
		this.selectedTabIndex = selectedTabIndex;
	}

	@Override
	public int getCount() {
		return mTabList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTabList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_tab_item, null);
			holder.ivTabIcon = (ImageView) convertView.findViewById(R.id.iv_tab_icon);
			holder.tvTabName = (TextView) convertView.findViewById(R.id.tv_tab_name);
			holder.tvUnreadNum = (TextView) convertView.findViewById(R.id.tv_unread_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mTab = mTabList.get(position);
		if (selectedTabIndex == position) {
			holder.ivTabIcon.setBackgroundResource(mTab.getSelectedImg());
			holder.tvTabName.setTextColor(parent.getResources().getColor(R.color.blue));
		} else {
			holder.ivTabIcon.setBackgroundResource(mTab.getNormalImg());
			holder.tvTabName.setTextColor(parent.getResources().getColor(R.color.black_3));
		}
		holder.tvTabName.setText(mTab.getTabName());
		holder.tvUnreadNum.setText("" + (mTab.getMsgNum() > 99 ? "···" : mTab.getMsgNum()));
		holder.tvUnreadNum.setVisibility(mTab.getMsgNum() > 0 ? View.VISIBLE : View.GONE);
		return convertView;
	}

	private class ViewHolder {
		ImageView ivTabIcon;
		TextView tvTabName;
		TextView tvUnreadNum;
	}

}
