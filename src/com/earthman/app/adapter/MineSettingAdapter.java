package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MineSettingInfo;

public class MineSettingAdapter extends BaseAdapter {

	private List<MineSettingInfo> mSettingList;

	public MineSettingAdapter(List<MineSettingInfo> settingList) {
		super();
		this.mSettingList = settingList;
	}

	@Override
	public int getCount() {
		return mSettingList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSettingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_setting_item, null);
		}
		MineSettingInfo info = mSettingList.get(position);
		TextView tvTitleBg = (TextView) convertView.findViewById(R.id.tv_title_bg);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);

		tvTitleBg.setVisibility(info.isTitle() ? View.VISIBLE : View.GONE);
		tvName.setText(info.getName());
		return convertView;
	}

}
