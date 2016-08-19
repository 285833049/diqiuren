package com.earthman.app.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.BillListResponse.BillResult;
import com.earthman.app.ui.activity.mine.TransactionDetailActivity;
import com.earthman.app.utils.MyStringUtils;

/**
 * 交易记录Adapter
 * 
 * @author xiexianyong
 * @Date 2016-03-06
 * 
 */
public class TransactionListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<BillResult> billResults;

	public TransactionListAdapter(Context context) {
		this.context = context;
	}

	public void setData(ArrayList<BillResult>  billResults) {
		this.billResults = billResults;
	}

	@Override
	public int getCount() {
		return billResults.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if (convertView == null) {
			viewHolder=new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.transaction_record_list_item, null);
			viewHolder.transaction_date=(TextView) convertView.findViewById(R.id.transaction_date);
			viewHolder.transaction_money=(TextView) convertView.findViewById(R.id.transaction_money);
			viewHolder.transaction_name=(TextView) convertView.findViewById(R.id.transaction_name);
			viewHolder.transaction_type=(TextView) convertView.findViewById(R.id.transaction_type);
			convertView.setTag(viewHolder);
		}
		// 跳转到会话界面
		viewHolder=(ViewHolder) convertView.getTag();
		 final BillResult billResult = billResults.get(position);
		viewHolder.transaction_date.setText(MyStringUtils.mTimeFormatData(Long.parseLong(billResult.getCreatedAt())));
		String indextr = billResult.getMoney().substring(0);
		if (indextr.equals("+")) {
			viewHolder.transaction_money.setTextColor(Color.parseColor("#FF6C00"));
		}else if (indextr.equals("-")){
			viewHolder.transaction_money.setTextColor(context.getResources().getColor(R.color.title_background));
		}
		viewHolder.transaction_money.setText(billResult.getMoney());
		viewHolder.transaction_name.setText(billResult.getType());
		viewHolder.transaction_type.setText(billResult.getPayWay());
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), TransactionDetailActivity.class);
				Bundle bundle=new Bundle();

				bundle.putString("createdAt", billResult.getCreatedAt());
				bundle.putString("id", billResult.getId());
				bundle.putString("money", billResult.getMoney());
				bundle.putString("otherAccount", billResult.getOtherAccount());
				bundle.putString("payWay", billResult.getPayWay());
				bundle.putString("type", billResult.getType());
				intent.putExtra("billResult", bundle);
				start((Activity) v.getContext(), intent);

			}

			public void start(Activity srcActivity, Intent intent) {
				srcActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder{
		TextView transaction_name;
		TextView transaction_money;
		TextView transaction_date;
		TextView transaction_type;
}
}
