package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.RankingInfo;
import com.earthman.app.enums.RankingDataType;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.widget.RoundCornerImageView;

/**
 * 作者：Zhou
 * 日期：2016-2-24 下午5:05:06
 * 描述：（排行榜适配器）
 */
public class RankingAdapter extends BaseAdapter {

	private List<RankingInfo> mRankingList;
	private ViewHolder mHolder;
	private YwbImageLoader ywbImageLoader;
	private boolean isShowTopFive;// 是否显示前五名
	private RankingDataType mDataType;

	public RankingAdapter(boolean isShowTopFive, RankingDataType dataType, List<RankingInfo> rankingList) {
		this.isShowTopFive = isShowTopFive;
		this.mDataType = dataType;
		this.mRankingList = rankingList;
		ywbImageLoader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		if (isShowTopFive) {
			return mRankingList.size() >= 5 ? 5 : mRankingList.size();
		} else {
			return mRankingList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return mRankingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, null);
			mHolder = new ViewHolder(convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.tvTop.setText(String.valueOf(position + 1));
		switch (position) {
		case 0:
			mHolder.tvTop.setBackgroundResource(R.color.number1);
			break;
		case 1:
			mHolder.tvTop.setBackgroundResource(R.color.number2);
		case 2:
			mHolder.tvTop.setBackgroundResource(R.color.number3);
			break;
		case 3:
			mHolder.tvTop.setBackgroundResource(R.color.number4);
			break;
		case 4:
			mHolder.tvTop.setBackgroundResource(R.color.number5);
			break;
		default:
			mHolder.tvTop.setBackgroundResource(R.color.number6);
			break;
		}

		RankingInfo top = mRankingList.get(position);
		ywbImageLoader.loadImage(top.getAvatar(), mHolder.ivHead, R.drawable.ic_unhead);
		mHolder.tvNickname.setText(String.format(parent.getContext().getString(R.string.neichen), top.getNice()));
		mHolder.tvCount.setText(top.getRecommendNum());
		mHolder.tvEarthCode.setText(String.format(parent.getContext().getString(R.string.diqiuren_id), top.getCardId()));
		switch (mDataType) {
		case RECOMMEND:
			mHolder.tvTypeHint.setText("推广次数");
			break;
		case POPULARITY:
			mHolder.tvTypeHint.setText("人气指数");
			break;
		case VIDEO:
			mHolder.tvTypeHint.setText("关注人气");
			break;
		case CONSUME:
			mHolder.tvTypeHint.setText("消费金额");
			break;
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvTop;
		RoundCornerImageView ivHead;
		TextView tvNickname;
		TextView tvEarthCode;
		TextView tvCount;
		TextView tvTypeHint;

		private ViewHolder(View convertView) {
			tvTop = (TextView) convertView.findViewById(R.id.tv_top);
			ivHead = (RoundCornerImageView) convertView.findViewById(R.id.iv_head);
			tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
			tvEarthCode = (TextView) convertView.findViewById(R.id.tv_earthmanid);
			tvCount = (TextView) convertView.findViewById(R.id.tv_count);
			tvTypeHint = (TextView) convertView.findViewById(R.id.tv_type_hint);
		}
	}

}
