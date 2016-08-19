package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.fragment.main.DynamicFragment;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.widget.BaseViewHolder;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月30日 下午6:37:06
 * @Decription
 */
public class MyPicGrideAdapter extends BaseAdapter {

	private ArrayList<String> picsList;
	private YwbImageLoader imageLoader;
	private int screenWidth;

	public MyPicGrideAdapter(Context context, ArrayList<String> picsList) {
		this.picsList = picsList;
		imageLoader = new YwbImageLoader();
		screenWidth=(int)AndroidUtils.getDeviceWidth(context);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return picsList.size()>9?9:picsList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_published_grida, null);
		}
		ImageView imageView = BaseViewHolder.get(convertView, R.id.item_grida_image);
		TextView tvNoDisplayNum=BaseViewHolder.get(convertView, R.id.tv_no_display_num);
		FrameLayout.LayoutParams llLayout=(FrameLayout.LayoutParams) imageView.getLayoutParams();
		
		/*switch (picsList.size()) {
		case 1:
			llLayout.setMargins(AndroidUtils.dip2px(parent.getContext(), 10), 0, 0, 0);
			break;
		case 2:
		case 4:
			llLayout.height=(screenWidth-AndroidUtils.dip2px(parent.getContext(), 2))/2;
		default:
			llLayout.height=(screenWidth-AndroidUtils.dip2px(parent.getContext(), 4))/3;
			break;
		}
		
		imageView.setLayoutParams(llLayout);
		*/
		DynamicFragment.setImageWH(imageView, picsList.size(), screenWidth-AndroidUtils.dip2px(parent.getContext(), 2), llLayout);
		imageLoader.loadImage(picsList.get(position), imageView, R.drawable.pictures_no);
		if(picsList.size()>9&&position==8)
		{
			tvNoDisplayNum.setVisibility(View.VISIBLE);
			tvNoDisplayNum.setText("+"+(picsList.size()-9));
			tvNoDisplayNum.setLayoutParams(llLayout);
		}else{
			tvNoDisplayNum.setVisibility(View.GONE);
		}
		return convertView;
	}

}
