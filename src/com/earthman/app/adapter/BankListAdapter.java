package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.MyBankListReponse.MyBankList;
import com.earthman.app.utils.BankConstants;
import com.earthman.app.utils.MyStringUtils;

/**
 * 作者：Zhou
 * 日期：2016-3-2 下午6:57:43
 * 描述：（）
 */
public class BankListAdapter extends BaseAdapter {

	private Context context;
	private BankConstants constants;
	private ArrayList<MyBankList> list;

	public BankListAdapter(Context context, ArrayList<MyBankList> list) {
		this.context = context;
		this.list = list;
		constants = new BankConstants(context);
	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.card_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MyBankList myBankList = list.get(position);
		String bankNnmEnd4 = MyStringUtils.getBankNnmEnd4(String.valueOf(myBankList.getDestaccnumber()));
		viewHolder.weihao.setText(bankNnmEnd4);
		int bankType = myBankList.getBanktype();
		int cardType = myBankList.getCardtype();
		viewHolder.rl_item.setBackgroundResource(constants.getBankDrawable(bankType));
		Drawable drawable = constants.getBankImgDrawable(bankType);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		viewHolder.card.setCompoundDrawables(drawable, null, null, null);
		viewHolder.card.setText(constants.getBankName(bankType) + "-" + constants.getCardName(cardType));
		return convertView;
	}

	private class ViewHolder {
		private TextView weihao;
		private TextView card;
		private RelativeLayout rl_item;

		private ViewHolder(View convertView) {
			weihao = (TextView) convertView.findViewById(R.id.weihao);
			card = (TextView) convertView.findViewById(R.id.card);
			rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
		}
	}

}
