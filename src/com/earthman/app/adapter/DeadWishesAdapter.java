package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.DeadFewWishesInfo;
import com.earthman.app.widget.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 上午10:54:01
 * @Decription 寄语适配器
 */
public class DeadWishesAdapter extends BaseAdapter {

	private List<DeadFewWishesInfo> mWishesList;

	public DeadWishesAdapter(List<DeadFewWishesInfo> wishesList) {
		super();
		this.mWishesList = wishesList;
	}

	@Override
	public int getCount() {
		return mWishesList.size();
	}

	@Override
	public Object getItem(int position) {
		return mWishesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dead_wishes_item, null);
		}
		DeadFewWishesInfo info = mWishesList.get(position);

		ImageView ivHead = BaseViewHolder.get(convertView, R.id.iv_head);
		TextView tvCardID = BaseViewHolder.get(convertView, R.id.tv_card_id);
		TextView tvCreateTime = BaseViewHolder.get(convertView, R.id.tv_create_time);
		TextView tvNickName = BaseViewHolder.get(convertView, R.id.tv_nickname);
		TextView tvWishes = BaseViewHolder.get(convertView, R.id.tv_wishes);

		ImageLoader.getInstance().displayImage(info.getAvatar(), ivHead);
		tvCardID.setText(info.getCardId());
		tvCreateTime.setText(info.getCreatetime());
		tvNickName.setText(info.getNice());
		tvWishes.setText(info.getWishes());
		return convertView;
	}

}
