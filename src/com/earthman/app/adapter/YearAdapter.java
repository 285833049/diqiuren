package com.earthman.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.listener.ItemClickListener;

/**
 * 作者：Zhou
 * 日期：2016-2-28 上午11:35:19
 * 描述：（）
 */
public class YearAdapter extends BaseAdapter{

	private Context context;
	private String[] years;
	private ItemClickListener listener;
	public YearAdapter(Context context, ItemClickListener listener, String[] years){
		this.context = context;
		this.listener = listener;
		this.years = years;
	}
	
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return years.length;
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return years[position];
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.year_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_year.setText(years[position]);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onItemClick(position, years[position]);
				}
				
			}
		});
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_year;
		private ViewHolder(View convertView){
			tv_year = (TextView) convertView.findViewById(R.id.tv_year);
		}
	}

}
