package com.earthman.app.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.RankingModelTopinfo.RankingModelTopinfoEntity;
import com.earthman.app.enums.RankingDataType;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.widget.BaseViewHolder;

public class RankingDetailAdapter extends BaseAdapter {

	private ArrayList<RankingModelTopinfoEntity> mItemList;
	private YwbImageLoader ywbImageLoader;
	private RankingDataType mDataType;

	public RankingDetailAdapter(RankingDataType dataType, ArrayList<RankingModelTopinfoEntity> itemList) {
		super();
		this.mDataType = dataType;
		this.mItemList = itemList;
		ywbImageLoader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mItemList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, null);
		}
		RankingModelTopinfoEntity infoEntity = mItemList.get(position);
		TextView tvTop = BaseViewHolder.get(convertView, R.id.tv_top);
		ImageView ivHead = BaseViewHolder.get(convertView, R.id.iv_head);
		TextView tvNickname = BaseViewHolder.get(convertView, R.id.tv_nickname);
		TextView tvCount = BaseViewHolder.get(convertView, R.id.tv_count);
		TextView tvEarthCode = BaseViewHolder.get(convertView, R.id.tv_earthmanid);
		TextView tvTypeHint = BaseViewHolder.get(convertView, R.id.tv_type_hint);
		// --------------------------------------
		tvTop.setText(String.valueOf(position + 1));
		switch (position) {
		case 0:
			tvTop.setBackgroundResource(R.color.number1);
			break;
		case 1:
			tvTop.setBackgroundResource(R.color.number1);
			break;
		case 2:
			tvTop.setBackgroundResource(R.color.number1);
			break;
		case 3:
			tvTop.setBackgroundResource(R.color.number1);
			break;
		case 4:
			tvTop.setBackgroundResource(R.color.number1);
			break;
		case 5:
			tvTop.setBackgroundResource(R.color.number2);
			break;
		case 6:
			tvTop.setBackgroundResource(R.color.number2);
			break;
		case 7:
			tvTop.setBackgroundResource(R.color.number2);
			break;
		case 8:
			tvTop.setBackgroundResource(R.color.number2);
			break;
		case 9:
			tvTop.setBackgroundResource(R.color.number2);
			break;
		case 10:
			tvTop.setBackgroundResource(R.color.number3);
			break;
		case 11:
			tvTop.setBackgroundResource(R.color.number3);
			break;
		case 12:
			tvTop.setBackgroundResource(R.color.number3);
			break;
		case 13:
			tvTop.setBackgroundResource(R.color.number3);
			break;
		case 14:
			tvTop.setBackgroundResource(R.color.number3);
			break;
		case 15:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		case 16:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		case 17:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		case 18:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		case 19:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		default:
			tvTop.setBackgroundResource(R.color.number4);
			break;
		}
		ywbImageLoader.loadImage(infoEntity.getAvatar(), ivHead, R.drawable.ic_unhead);
		tvNickname.setText(String.format(parent.getContext().getString(R.string.neichen), infoEntity.getNice()));
		tvCount.setText(infoEntity.getRecommendNum());
		tvEarthCode.setText(String.format(parent.getContext().getString(R.string.diqiuren_id), infoEntity.getCardId()));
		switch (mDataType) {
		case RECOMMEND:
			tvTypeHint.setText("推广次数");
			break;
		case POPULARITY:
			tvTypeHint.setText("人气指数");
			break;
		case VIDEO:
			tvTypeHint.setText("关注人气");
			break;
		case CONSUME:
			tvTypeHint.setText("消费金额");
			break;
		}

		return convertView;
	}

}
