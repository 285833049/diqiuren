package com.earthman.app.adapter;

import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.CircleFriendInfo;
import com.earthman.app.widget.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午5:12:01
 * @Decription 亲友圈：查看所有界面适配器
 */
public class CircleDetailAdapter extends BaseAdapter {

	private List<CircleFriendInfo> mFriendList;

	public CircleDetailAdapter(List<CircleFriendInfo> friendList) {
		super();
		this.mFriendList = friendList;
	}

	@Override
	public int getCount() {
		return mFriendList.size();
	}

	@Override
	public Object getItem(int position) {
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_list_item, null);
		}
		//----------------------------
		CircleFriendInfo info = mFriendList.get(position);
		//----------------------------
		ImageView ivHead=BaseViewHolder.get(convertView, R.id.iv_head);
		TextView tvName = BaseViewHolder.get(convertView, R.id.tv_name);
		TextView tvEarthCode = BaseViewHolder.get(convertView, R.id.tv_earth_code);
		//-----------------------------
//		ImageLoader.getInstance().displayImage(info.getAvatar(), ivHead);
//		ImageLoader.getImageListener(ivHead, R.drawable.bg_img_error, R.drawable.bg_img_error);
		String nikeName="<font color=#333333>" + info.getNice() + "</font>";
		String relationship=info.getMatter().length()==0?"":"<font color=#0070d9>("+ info.getMatter() + ")</font>";
		ImageLoader.getInstance().displayImage(info.getAvatar(),ivHead);
		if(!TextUtils.isEmpty(info.getRemarks())){
			tvName.setText(Html.fromHtml(info.getRemarks()+relationship));
		}else{
			tvName.setText(Html.fromHtml(nikeName+relationship));
		}		
		tvEarthCode.setText(parent.getContext().getString(R.string.circle_earth_code) + info.getCardId());
		return convertView;
	}
}
