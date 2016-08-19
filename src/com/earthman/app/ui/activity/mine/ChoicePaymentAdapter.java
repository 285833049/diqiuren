package com.earthman.app.ui.activity.mine;

import java.util.List;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.widget.BaseViewHolder;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-16 上午8:51:24
 * @Decription 选择支付方式适配器
 */
public class ChoicePaymentAdapter extends BaseAdapter {

	private List<PaymentBalanceItem> mItemList;
	private PaymentBalanceItem mItem;
	private int mSelection;
	private String mCurrencyName;
	
	public ChoicePaymentAdapter(String currencyName,List<PaymentBalanceItem> mItemList) {
		super();
		this.mCurrencyName=currencyName;
		this.mItemList = mItemList;
	}
	
	public void setSelection(int selection) {
		this.mSelection = selection;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {	
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_payment_item, null);
		}

		ImageView ivIcon = BaseViewHolder.get(convertView, R.id.iv_icon);
		TextView tvName = BaseViewHolder.get(convertView, R.id.tv_name);
		TextView tvBalance = BaseViewHolder.get(convertView, R.id.tv_balance);
		TextView tvSelectedFlag=BaseViewHolder.get(convertView, R.id.tv_selected_flag);
		tvSelectedFlag.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/iconfont.ttf"));

		mItem = mItemList.get(position);

		ivIcon.setBackgroundResource(mItem.getPaymentIcon());
		tvName.setText(mItem.getPaymentName());
		tvBalance.setText(String.format(parent.getContext().getString(R.string.mine_payment_balance), mItem.getPaymentName(),mItem.getPaymentBalance()));
		
		tvSelectedFlag.setVisibility(mCurrencyName.equals(mItem.getPaymentName())?View.VISIBLE:View.GONE);

		return convertView;
	}

}
