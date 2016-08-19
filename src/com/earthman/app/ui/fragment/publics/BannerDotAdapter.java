package com.earthman.app.ui.fragment.publics;

import com.earthman.app.R;
import com.earthman.app.widget.BaseViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-8-11 下午3:29:05
 * @Decription Banner标记点适配器
 */
public class BannerDotAdapter extends BaseAdapter {

	private int size;
	private int selection;

	public BannerDotAdapter(int size) {
		super();
		this.size = size;
	}

	public void setSelection(int selection) {
		this.selection = selection;

	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		if (converView == null) {
			converView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_dot_item, null);
		}
		ImageView imgDot = BaseViewHolder.get(converView, R.id.iv_dot);
		imgDot.setBackgroundResource(selection == position ? R.drawable.banner_point_hov : R.drawable.banner_point_default);
		return converView;
	}

}
