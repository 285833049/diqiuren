package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.DeadCategoryInfo;
import com.earthman.app.widget.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 上午10:20:33
 * @Decription 祭祀商品列表
 */
public class DeadFlowerAdapter extends BaseAdapter {

	private List<DeadCategoryInfo> mCategoryList;

	public DeadFlowerAdapter(List<DeadCategoryInfo> categoryList) {
		super();
		this.mCategoryList = categoryList;
	}

	@Override
	public int getCount() {
		return mCategoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dead_flower_item, null);
		}
		DeadCategoryInfo info = mCategoryList.get(position);
		ImageView ivFlower = BaseViewHolder.get(convertView, R.id.iv_flower);
		TextView tvName = BaseViewHolder.get(convertView, R.id.tv_name);
		TextView tvIndate = BaseViewHolder.get(convertView, R.id.tv_indate);

		ImageLoader.getInstance().displayImage(info.getPicture(), ivFlower);
		tvName.setText(info.getName());
		tvIndate.setText(info.getDescription());
		
		return convertView;
	}
}
