package com.earthman.app.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.constant.Constent;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.LogUtil;

/**
 * 作者：Zhou 日期：2016-2-26 下午4:25:58 描述：（）
 */
public class PayStyleAdapter extends BaseAdapter {

	private Context context;
	private String[] payNames;
	private TypedArray iconRes;
	public Map<Integer, Boolean> map;
	
	public String[] getPayNames() {
		return payNames;
	}

	public PayStyleAdapter(Context context, int type) {
		this.context = context;
		UserInfoPreferences preferences = new UserInfoPreferences(context);

		if (Constants.SHARE_ACTIVED.equals(preferences.getUserStatus())) {// 已激活

			if (type == Constent.REGIST_TO_SELF) {
				payNames = context.getResources().getStringArray(
						R.array.paystyle_names);
			} else if (type == Constent.REGIST_TO_FRIEND
					|| type == Constent.REGIST_TO_FAMILY
					|| type == Constent.RENEW) {
				payNames = context.getResources().getStringArray(
						R.array.payment_names);
			}
			iconRes = context.getResources().obtainTypedArray(
					R.array.payment_icons);

		} else {// 未激活
			if (type == Constent.REGIST_TO_SELF) {
				payNames = context.getResources().getStringArray(
						R.array.not_paystyle_names);
			} else if (type == Constent.REGIST_TO_FRIEND
					|| type == Constent.REGIST_TO_FAMILY
					|| type == Constent.RENEW) {
				payNames = context.getResources().getStringArray(
						R.array.not_payment_names);
			}
			iconRes = context.getResources().obtainTypedArray(
					R.array.not_payment_icons);
		}

		// if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活
		// if (type == Constent.REGIST_TO_SELF) {
		// payNames =
		// context.getResources().getStringArray(R.array.not_paystyle_names);
		// } else if (type == Constent.REGIST_TO_FRIEND || type ==
		// Constent.REGIST_TO_FAMILY || type == Constent.RENEW) {
		// payNames =
		// context.getResources().getStringArray(R.array.not_payment_names);
		// }
		// iconRes =
		// context.getResources().obtainTypedArray(R.array.not_payment_icons);
		// } else{// 激活
		// if (type == Constent.REGIST_TO_SELF) {
		// payNames =
		// context.getResources().getStringArray(R.array.paystyle_names);
		// } else if (type == Constent.REGIST_TO_FRIEND || type ==
		// Constent.REGIST_TO_FAMILY || type == Constent.RENEW) {
		// payNames =
		// context.getResources().getStringArray(R.array.payment_names);
		// }
		// iconRes =
		// context.getResources().obtainTypedArray(R.array.payment_icons);
		// }

		map = new HashMap<Integer, Boolean>();
		for (int i = 0; i < payNames.length; i++)

		{
			map.put(i, false);
			LogUtil.i("EarthMan", payNames[i]);
		}

	}

	/*
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return payNames.length;
	}

	/*
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return payNames[position];
	}

	/*
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.pay_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.pay_style_name.setText(payNames[position]);
		Drawable drawable = context.getResources().getDrawable(
				iconRes.getResourceId(position, 0));
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		viewHolder.pay_style_name.setCompoundDrawables(drawable, null, null,
				null);
		if (position == payNames.length - 1) {
			// viewHolder.money.setText("(美元结算)");
			viewHolder.money.setText("");
		} else {
			viewHolder.money.setText("");
		}

		viewHolder.checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							map.put(position, true);
							for (int i = 0; i < payNames.length; i++) {
								if (i != position) {
									map.put(i, false);
								}
							}
						} else {
							map.put(position, false);
						}
						notifyDataSetChanged();
					}
				});
		viewHolder.checkbox.setChecked(map.get(position));
		return convertView;
	}

	private class ViewHolder {
		private TextView pay_style_name;
		private TextView money;
		private CheckBox checkbox;

		private ViewHolder(View convertView) {
			pay_style_name = (TextView) convertView
					.findViewById(R.id.pay_style_name);
			money = (TextView) convertView.findViewById(R.id.money);
			checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		}

	}

}
