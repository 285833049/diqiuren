package com.earthman.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.constant.Constent;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.ui.activity.mine.AccountFamilyRelativeChoise;
import com.earthman.app.widget.BaseViewHolder;

public class MyGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> relativeList;

	public MyGridAdapter(Context mContext,List<String>relativeList ) {
		super();
		this.mContext = mContext;
		this.relativeList = relativeList;
	}

	@Override
	public int getCount() {
		return relativeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_1, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		int num = relativeList.size();
		tv.setText(relativeList.get(position));
		tv.setBackgroundColor(getLine(position) == 1 ? 0xfff5f6f8 : 0xFFFFFFFF);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (relativeList.get(position).equals("自定义+")) {
					if (mContext instanceof AccountFamilyRelativeChoise) {
						AccountFamilyRelativeChoise alert = (AccountFamilyRelativeChoise) mContext;
						alert.showPopupWindow();
					}
				} else {
					Log.d("xianyong", "aa:" + relativeList.get(position));
					Intent intent = new Intent(mContext, RegistActivity.class);
					intent.putExtra("type", Constent.REGIST_TO_FAMILY);
					intent.putExtra("relative", relativeList.get(position));
					mContext.startActivity(intent);
					AccountFamilyRelativeChoise alert = (AccountFamilyRelativeChoise) mContext;
					alert.finish();
				}
			}
		});
		return convertView;
	}

	private int getLine(int position) {
		int merchant = position / 4;
		int remainder = merchant % 2;
		return remainder;
	}
}