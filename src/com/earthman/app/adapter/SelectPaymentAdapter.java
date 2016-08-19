package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.PaymentInfo;
import com.earthman.app.widget.BaseViewHolder;

/**
 * @Title: MyBankListAdapter
 * @Description: 选择支付方式适配器
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月4日
 */

public class SelectPaymentAdapter extends BaseAdapter {

	private List<PaymentInfo> mPaymentList;
	private int mSelection = -1;

	public void setSelection(int selection) {
		this.mSelection = selection;
	}

	public SelectPaymentAdapter(List<PaymentInfo> paymentList) {
		this.mPaymentList = paymentList;
	}

	@Override
	public int getCount() {
		return mPaymentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPaymentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_payment_item, null);
		}
		ImageView ivBankIcon = BaseViewHolder.get(convertView, R.id.iv_bank_icon);
		TextView tvBankName = BaseViewHolder.get(convertView, R.id.tv_bank_name);
		ImageView ivSelectedFlag = BaseViewHolder.get(convertView, R.id.iv_selected_flag);

		PaymentInfo info = mPaymentList.get(position);
		ivBankIcon.setBackgroundResource(info.getBankIcon());
		tvBankName.setText(info.getBankName());
		ivSelectedFlag.setBackgroundResource(mSelection == position ? R.drawable.ic_selected : R.drawable.ic_unselected);
		return convertView;
	}

}
