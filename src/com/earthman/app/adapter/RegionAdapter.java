package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.AddrItem;

/**
 * 作者：Zhou
 * 日期：2015-10-21 下午5:47:41
 * 版权：地球人
 * 描述：（）
 */
public class RegionAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<AddrItem> addrItems;
	public RegionAdapter(Context context, ArrayList<AddrItem> addrItems){
		this.context = context;
		this.addrItems = addrItems;

	}
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if(addrItems == null){
			return 0;
		}
		return addrItems.size();
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
		return position;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.region_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_address_name.setText(addrItems.get(position).getAddrName());
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_address_name;
		private ViewHolder(View convertView){
			tv_address_name = (TextView) convertView.findViewById(R.id.tv_address_name);
		}
	}

}
