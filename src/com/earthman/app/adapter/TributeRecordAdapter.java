package com.earthman.app.adapter;

import java.util.List;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.DeadFewPresentedInfo;
import com.earthman.app.widget.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 上午10:33:52
 * @Decription 敬献记录适配器
 */
public class TributeRecordAdapter extends BaseAdapter {

	private List<DeadFewPresentedInfo> mPressentedList;

	public TributeRecordAdapter(List<DeadFewPresentedInfo> pressentedList) {
		super();
		this.mPressentedList = pressentedList;
	}

	@Override
	public int getCount() {
		return this.mPressentedList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mPressentedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flowers_record_item, null);
		}
		DeadFewPresentedInfo info = mPressentedList.get(position);

		ImageView ivHead = BaseViewHolder.get(convertView, R.id.iv_head);
		TextView tvTitle = BaseViewHolder.get(convertView, R.id.tv_title);
		TextView tvTime = BaseViewHolder.get(convertView, R.id.tv_time);
		TextView tvAlias = BaseViewHolder.get(convertView, R.id.tv_alias);

		ImageLoader.getInstance().displayImage(info.getAvatar(), ivHead);
		tvTitle.setText(Html.fromHtml("<font color=#2EBCE7>" + info.getCardId() + "</font> 敬献 " + "<font color=#2EBCE7>" + info.getName() + "</font> " + info.getAmount() + " 朵"));
		tvTime.setText(info.getCreatetime());
		tvAlias.setText(info.getNice());

		return convertView;
	}

}
