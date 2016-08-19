package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.CircleListInfo;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-16下午3:33:41
 * @Decription 圈子亲友组适配器
 */
public class CircleAdapter extends BaseAdapter {

	private List<CircleListInfo> mCircleList;
	private ViewHolder mHolder;
	private int selectedPosition;

	public CircleAdapter(List<CircleListInfo> circleList) {
		super();
		this.mCircleList = circleList;
	}
	

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	private class ViewHolder {
		LinearLayout llLayout;
		TextView tvCircleName;
		TextView tvNum;
	}

	@Override
	public int getCount() {
		return mCircleList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCircleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_item, null);
			mHolder.llLayout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
			mHolder.tvCircleName = (TextView) convertView.findViewById(R.id.tv_circle_name);
			mHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		CircleListInfo info = mCircleList.get(position);
		if (this.selectedPosition == position) {
			mHolder.llLayout.setBackgroundResource(R.drawable.circle_item_selected);
		} else {
			mHolder.llLayout.setBackgroundResource(R.drawable.circle_item_normal);
		}
		mHolder.tvCircleName.setText(info.getName());
		mHolder.tvNum.setText("(" + info.getTotal() + ")");
		return convertView;
	}
}
