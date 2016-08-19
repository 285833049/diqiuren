package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.FamilyListResponse.FamilyResult.MyFamily;
import com.earthman.app.imageloader.YwbImageLoader;

/**
 * @Title: MyFamilyAdapter
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月16日
 */

public class MyFamilyAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<MyFamily> result;
	private YwbImageLoader imageLoader;

	public MyFamilyAdapter(Context context,ArrayList<MyFamily> result) {
		this.context = context;
		this.result=result;
		imageLoader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return result.size();
	}


	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_family_phones, null);
			holder.usericon=(ImageView) convertView.findViewById(R.id.user_avatar);
			holder.usermatter=(TextView) convertView.findViewById(R.id.user_name);
			holder.usercardid=(TextView) convertView.findViewById(R.id.user_cardid);
			holder.userPhone=(TextView) convertView.findViewById(R.id.user_phone);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.usercardid.setText("地球人代码："+result.get(position).getCardId());
		holder.usermatter.setText(result.get(position).getMatter());
		holder.userPhone.setText("绑定手机号："+result.get(position).getPhone());
		imageLoader.loadImage(result.get(position).getAvatar(), holder.usericon, R.drawable.user_avatar);
		return convertView;
	}
public  class ViewHolder{
	private TextView usermatter;
	private TextView userPhone;
	private TextView usercardid;
	private ImageView usericon;
}
}
