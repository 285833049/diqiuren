package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MapInfo;
import com.earthman.app.widget.BaseViewHolder;

public class DialogSelectMapAdapter extends BaseAdapter {

	private List<MapInfo> mMapList;

	public DialogSelectMapAdapter(List<MapInfo> mapList) {
		super();
		this.mMapList = mapList;
	}

	@Override
	public int getCount() {
		return mMapList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_select_map_item, null);
		}
		ImageView ivMapIcon = BaseViewHolder.get(convertView, R.id.iv_icon);
		TextView tvMapName = BaseViewHolder.get(convertView, R.id.tv_name);
		MapInfo info = mMapList.get(position);
		ivMapIcon.setBackgroundResource(info.getMapIcon());
		tvMapName.setText(info.getMapName());
		return convertView;
	}

}
