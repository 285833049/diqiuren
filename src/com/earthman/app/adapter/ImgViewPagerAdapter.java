package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.LogUtil;

public class ImgViewPagerAdapter extends PagerAdapter {
	private LayoutInflater inflater;
	private int type;
	private Context context;

	private ArrayList<String> banerPics;//轮播图的数据
	private YwbImageLoader ywbImageLoader;
	private ArrayList<View> viewList=new ArrayList<View>();

	public ImgViewPagerAdapter(Context context, int type,ArrayList<String> banerPics) {
		this.context = context;
		this.banerPics = banerPics;
		this.type = type;
		inflater = LayoutInflater.from(context);
		ywbImageLoader=new YwbImageLoader();
		initImageView(banerPics,context);
	}
	private void initImageView(ArrayList<String> bannersList,Context context) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (bannersList!=null&&bannersList.size()!=0) {
			try {
				for (int l = 0; l < bannersList.size(); l++) {
					ImageView v = new ImageView(context);
					ywbImageLoader.loadImage(banerPics.get(l),v);
					v.setScaleType(ScaleType.FIT_XY);
					v.setLayoutParams(params);
					viewList.add(v);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getCount() {
		 if (viewList!= null) {
             return viewList.size();
         }      
         return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));// 删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		LogUtil.i("position轮播图", position+"mine");
//		View view = inflater.inflate(R.layout.health_img, null);
//			ImageView imageView = (ImageView) view.findViewById(R.id.iv_pic);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//		imageView.setAdjustViewBounds(true);
//		imageView.setScaleType(ScaleType.FIT_XY);
//		imageView.setLayoutParams(params);
//		ImageLoader.getInstance().displayImage(banerPics.get(position), imageView);
////		if (view.getParent() == null) {
////			container.addView(view);
////		} else {
////			((ViewGroup) view.getParent()).removeView(view);
////			container.addView(view);
////		}
//		return view;
		View view = viewList.get(position);
		if (viewList.size()!=0) {
//			ViewPager viewPager = (ViewPager) container;
			container.addView(view);
		}
		return viewList.get(position);
	}
}
