package com.earthman.app.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.earthman.app.R;
import com.earthman.app.eventbusbean.ChooseResult;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-20 下午3:14:17
 * @Decription
 */
public class LocationAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<PoiItem> poiItems;
	private LayoutInflater inflater;
	
	public LocationAdapter(Context context, ArrayList<PoiItem> poiItems){
		this.context = context;
		this.poiItems = poiItems;
		inflater = LayoutInflater.from(context);
	}
	
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return poiItems.size();
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return poiItems.get(position);
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.location_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		final PoiItem poiItem = poiItems.get(position);
		viewHolder.tv_title.setText(poiItem.getTitle());
		viewHolder.tv_address.setText(poiItem.getSnippet());
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new ChooseResult(3, null, poiItem.getTitle(), poiItem.getSnippet()).setlatlog(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
				if(context instanceof Activity){
					Activity activity = (Activity) context;
					activity.finish();
				}				
			}
		});
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv_title;
		private TextView tv_address;
		private ViewHolder(View convertView){
			tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			tv_address = (TextView) convertView.findViewById(R.id.tv_address);
		}
	}

}
