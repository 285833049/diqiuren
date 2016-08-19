package com.earthman.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.earthman.app.bean.HomeDataResult.HomeData.Banner;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.LogUtil;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-17 下午8:42:37
 * @Decription
 */
public class HomeBannerAdapter extends PagerAdapter {

	private YwbImageLoader ywbImageLoader;
	
	private ArrayList<View> viewList=new ArrayList<View>();

	public HomeBannerAdapter(Context context, List<Banner> mBanners) {
		ywbImageLoader = new YwbImageLoader();
		initImageView(mBanners,context);
	}

	/**
	 * initImageView(这里用一句话描述这个方法的作用)
	 * @param banners2
	 * void
	 * @exception
	*/
	private void initImageView(List<Banner> bannersList,Context context) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (bannersList!=null&&bannersList.size()!=0) {
			try {
				for (int l = 0; l < bannersList.size(); l++) {
					ImageView v = new ImageView(context);
					ywbImageLoader.loadImage(bannersList.get(l).getImg(), v);
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
		LogUtil.i("position轮播图", position+"首页");
		View view = viewList.get(position);
		if (viewList.size()!=0) {
//			ViewPager viewPager = (ViewPager) container;
			container.addView(view);
		}
		return viewList.get(position);
	}
	
}
