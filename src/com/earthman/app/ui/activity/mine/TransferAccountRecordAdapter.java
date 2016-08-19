package com.earthman.app.ui.activity.mine;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.DateUtils;
import com.earthman.app.utils.StringUtils;
import com.earthman.app.widget.BaseViewHolder;
import com.earthman.app.widget.RoundCornerImageView;

public class TransferAccountRecordAdapter extends BaseAdapter {

	private List<TransferAccountRecordItem> mItemList;
	private TransferAccountRecordItem mItem;
	private YwbImageLoader mImageLoader;

	public TransferAccountRecordAdapter(List<TransferAccountRecordItem> itemList) {
		super();
		this.mItemList = itemList;
		mImageLoader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_account_record_item, null);
		}
		mItem = mItemList.get(position);

		TextView tvDate = BaseViewHolder.get(convertView, R.id.tv_date);
		RoundCornerImageView ivAvatar = BaseViewHolder.get(convertView, R.id.iv_avatar);
		TextView tvAmount = BaseViewHolder.get(convertView, R.id.tv_amount);
		TextView tvCurrency = BaseViewHolder.get(convertView, R.id.tv_currency);
		TextView tvSource = BaseViewHolder.get(convertView, R.id.tv_source);

		mItem = mItemList.get(position);
		tvDate.setText(DateUtils.getFormatDate(mItem.getCreatedat()) + "\n" + DateUtils.getMonthAndDay(mItem.getCreatedat()));
		mImageLoader.loadImage(mItem.getAvatar(), ivAvatar, R.drawable.user_avatar);
		tvAmount.setText(mItem.getMoney()>0?"+"+mItem.getMoney():mItem.getMoney()+"");
		tvCurrency.setText(mItem.getPaytype().equals("0")?"激活币":"地球币");
		tvSource.setText(StringUtils.getFormatName(mItem.getNice(),mItem.getRealname(), mItem.getAliase(),mItem.getCardid()) + " - " + mItem.getRemarks());
		// ivCurrencyIcon.setBackgroundResource(mItem.getPaymentName().equals("地球币")?R.drawable.ic_currency_earth:R.drawable.ic_currency_activate);
		// tvDate.setText(mItem.getPaymentDate());
		// tvRecord.setText(String.format(parent.getContext().getResources().getString(R.string.mine_transfer_account_record),
		// mItem.getNickname(),mItem.getCardId(),mItem.getAmount(),mItem.getPaymentName()));
		return convertView;
	}

}
