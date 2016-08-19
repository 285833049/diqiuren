package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.HomeNaviItem;
import com.earthman.app.widget.BaseViewHolder;

/**
 * 作者：Zhou
 * 日期：2016-3-16 下午8:18:31
 * 描述：（）
 */
public class HomeNavigateAdapter extends BaseAdapter {

	private List<HomeNaviItem> mNaviList;

	public HomeNavigateAdapter(List<HomeNaviItem> naviList) {
		mNaviList = naviList;
	}

	@Override
	public int getCount() {
		return mNaviList.size();
	}

	@Override
	public Object getItem(int position) {
		return mNaviList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigate_item, null);
		}
		HomeNaviItem item = (HomeNaviItem) getItem(position);
		
		ImageView ivNavIcon=BaseViewHolder.get(convertView, R.id.iv_nav_icon);
		TextView tvNavName=BaseViewHolder.get(convertView, R.id.tv_nav_name);
		
		ivNavIcon.setBackgroundResource(item.getNaviIcon());
		tvNavName.setText(item.getNaviName());
		return convertView;
	}
	
}
