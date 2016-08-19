package com.earthman.app.ui.fragment.publics;

import java.util.ArrayList;
import java.util.List;

import com.earthman.app.R;
import com.earthman.app.bean.BannerInfo;
import com.earthman.app.enums.CarouselImage;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.home.VideoRegionDetailActivity;
import com.earthman.app.utils.HttpUrlConstants;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PublicBannerAdapter extends PagerAdapter {

	private Context mContext;
	private List<BannerInfo> mBanners;
	private List<ImageView> mImgList;
	private YwbImageLoader mImageLoader;
	private CarouselImage mCarouseType;

	public PublicBannerAdapter(Context context, CarouselImage carouseType, List<BannerInfo> banners) {
		super();
		this.mContext = context;
		this.mCarouseType = carouseType;
		this.mBanners = banners;
		mImageLoader = new YwbImageLoader();
		mImgList = new ArrayList<ImageView>();
		for (int i = 0; i < mBanners.size(); i++) {
			mImgList.add((ImageView) LayoutInflater.from(mContext).inflate(R.layout.public_img, null));
		}
	}

	@Override
	public int getCount() {
		return mBanners.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 销毁当前page的相隔2个及2个以上的item时调用
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
	}

	/**
	 * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = mImgList.get(position);
		BannerInfo bannerInfo = mBanners.get(position);
		switch (mCarouseType) {
		case BANNER:
			break;
		case VIDEO:
			imageView.setOnClickListener(new ImgClick(bannerInfo.getActionId()));
			break;
		}
		String imgURL = bannerInfo.getImgUrl().indexOf("http") == -1 ? HttpUrlConstants.getImgURL(bannerInfo.getImgUrl()) : bannerInfo.getImgUrl();
		mImageLoader.loadImage(imgURL, imageView);
		container.addView(imageView);
		return imageView; // 返回该view对象，作为key
	}

	private class ImgClick implements OnClickListener {

		private int id;

		public ImgClick(int id) {
			super();
			this.id = id;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, VideoRegionDetailActivity.class);
			intent.putExtra("id", id);
			mContext.startActivity(intent);
		}

	}

}
