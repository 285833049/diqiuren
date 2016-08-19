package com.earthman.app.ui.fragment.publics;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Scroller;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.bean.BannerInfo;
import com.earthman.app.enums.CarouselImage;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.widget.HorizontalListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-8-10 下午3:21:42
 * @Decription 公共Banner
 */
public class PublicBannerFragment extends BaseFragment {

	private final byte MOVE_NEXT = 0x01;// 移动标记
	private View mView;
	@ViewInject(R.id.vp_banner)
	private ViewPager mVpBanner;
	@ViewInject(R.id.lv_dot)
	private HorizontalListView mLvDot;
	private BannerDotAdapter mDotAdapter;
	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;
	private List<BannerInfo> mBanners;

	private int mCurrPosition;
	private ChangeImageTask mMoveTask;// 图片轮询任务
	private Timer mTimer;// 记时器

	private int mDuration = 1000;

	@Override
	protected View createView() {
		mView = LayoutInflater.from(activity).inflate(R.layout.public_banner_fragment, null);
		ViewUtils.inject(this, mView);
		mVpBanner.setOffscreenPageLimit(1);
		mVpBanner.setPageTransformer(true, new DepthPageTransformer());
		mVpBanner.setOnPageChangeListener(new PageListener());
		return mView;
	}
	
	private class PageListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			mDotAdapter.setSelection(arg0);
			mDotAdapter.notifyDataSetChanged();
			mTvTitle.setText(mBanners.get(arg0).getTitle());
		}
		
	}

	public void setBanners(CarouselImage carouseType, List<BannerInfo> banners) {
		this.mBanners = banners;
		mVpBanner.setAdapter(new PublicBannerAdapter(getActivity(), carouseType, banners));
		RelativeLayout.LayoutParams rlParams = (LayoutParams) mLvDot.getLayoutParams();
		rlParams.width = AndroidUtils.dip2px(getActivity(), 10 * mBanners.size());
		mLvDot.setLayoutParams(rlParams);

		RelativeLayout.LayoutParams rlParams2 = (LayoutParams) mTvTitle.getLayoutParams();
		rlParams2.rightMargin = rlParams.width + AndroidUtils.dip2px(getActivity(), 10);
		mTvTitle.setLayoutParams(rlParams2);

		mLvDot.setAdapter(mDotAdapter = new BannerDotAdapter(mBanners.size()));
		mTvTitle.setText(banners.get(0).getTitle());
		start();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView(View convertView) {
	}

	@Override
	protected void setAttribute() {

	}

	@Override
	protected void handclick(View v) {

	}

	public void start() {
		if (mMoveTask == null) {
			mMoveTask = new ChangeImageTask();
			mTimer = new Timer();// 初始化计时器
			mTimer.schedule(mMoveTask, 10 * mDuration, 10 * mDuration);
		}
	}

	// stopTask(停掉task)
	public void stop() {
		if (mMoveTask != null) {
			mMoveTask.cancel();
			mMoveTask = null;
		}
	}

	// 轮换图片
	private class ChangeImageTask extends TimerTask {
		@Override
		public void run() {
			if (mBanners.size() > 1) {
				mCurrPosition = mVpBanner.getCurrentItem();
				mCurrPosition = mCurrPosition == mBanners.size() - 1 ? 0 : mCurrPosition + 1;
				mHandler.obtainMessage(MOVE_NEXT).sendToTarget();
			}
		}
	};

	/**
	 * 控制viewpage的切换时间
	 */
	public class FixedSpeedScroller extends Scroller {

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		start();
	}

	@Override
	public void onPause() {
		super.onPause();
		stop();
	}
	
	// viewpage切换动画
		private class DepthPageTransformer implements ViewPager.PageTransformer {

			private static final float MIN_SCALE = 0.70f;

			public void transformPage(View view, float position) {
				int pageWidth = view.getWidth();

				if (position < -1) { // [-Infinity,-1)
					view.setAlpha(0);

				} else if (position <= 0) { // [-1,0]
					view.setAlpha(1);
					view.setTranslationX(0);
					view.setScaleX(1);
					view.setScaleY(1);

				} else if (position <= 1) { // (0,1]
					view.setAlpha(1 - position);
					view.setTranslationX(pageWidth * -position);
					float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
					view.setScaleX(scaleFactor);
					view.setScaleY(scaleFactor);

				} else { // (1,+Infinity]
					view.setAlpha(0);
				}
			}

		}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MOVE_NEXT:
				// -----------------------切换下一个页面
				if (mCurrPosition == 0) {
					mVpBanner.setCurrentItem(mCurrPosition, false);
				} else {
					mVpBanner.setCurrentItem(mCurrPosition);
				}
				// -----------------------设置新标题
				mTvTitle.setText(mBanners.get(mCurrPosition).getTitle());
				// -----------------------更新Dot
				mDotAdapter.setSelection(mCurrPosition);
				mDotAdapter.notifyDataSetChanged();
				break;
			}
		}

	};

}
