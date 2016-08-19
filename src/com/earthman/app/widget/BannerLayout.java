package com.earthman.app.widget;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.earthman.app.R;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-16 下午3:26:20
 * @Decription
 */
public class BannerLayout extends LinearLayout{

	private final int CHANGE_IMAGE = 0x01;// 切换图片信息标识
	private LinearLayout mLlDot;// dot布局
	private ViewPager mVpBanner;// 焦点图控件
	private Timer timer;// 记时器
	private ChangeImageTask task;// 图片轮询任务
	private int pageCount;// 焦点图的总个数
	private int currentPageIndex;// 焦点图片的索引
	private ImageView[] dotViews;// dot
	private PagerAdapter adapter;// 焦点图适配器
	private LayoutInflater inflater;
	private View contentView;
	private int screenWidth;
	private Context mContext;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CHANGE_IMAGE:
				
				if(currentPageIndex==0){
					mVpBanner.setCurrentItem(currentPageIndex,false);
				}else{
					mVpBanner.setCurrentItem(currentPageIndex);
				}
				for (int i = 0; i < dotViews.length; i++) {
					dotViews[i].setImageResource(i == currentPageIndex % dotViews.length ? R.drawable.banner_point_hov : R.drawable.banner_point_default);
				}
				break;
			}
		}

	};

	/**
	 * 创建一个新的实例 BannerLayout.
	 *
	 * @param context
	 * @param attrs
	 */
	public BannerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflater = LayoutInflater.from(context);
		initData();
		initView();
	}


	private void initView(){
		setOrientation(VERTICAL);
		contentView = inflater.inflate(R.layout.banner_layout, null);
		mLlDot = (LinearLayout) contentView.findViewById(R.id.linearlayout_dot);
		mVpBanner = (ViewPager) contentView.findViewById(R.id.viewpager_banner);
		addView(contentView);
		initViewPager();
		initDotView();
	}


	private void initData(){
		screenWidth = (int) AndroidUtils.getDeviceWidth(mContext);
		timer = new Timer();// 初始化计时器
	}
	
	
	public void setAdapter(PagerAdapter adapter, int pageCount){
		this.adapter = adapter;
		this.pageCount = pageCount;
		mVpBanner.setAdapter(adapter);
		initDotView();
	}
	
	
	public void notifyDataSetChanged(int pageCount){
		if(adapter == null){
			return;
		}
		this.pageCount = pageCount;
		adapter.notifyDataSetChanged();
		initDotView();
	}
	
	
	/**
	 * 
	 * startTask(启动task)
	 * void
	 * @exception
	 */
	public void startTask(){
		if (task == null) {
			task = new ChangeImageTask();
			timer.schedule(task, 10*1000, 10*1000);
		}
	}
	
	/**
	 * 
	 * stopTask(停掉task)
	 * void
	 * @exception
	 */
	public void stopTask(){
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
	
	/**
	 * 
	 * stopTimer(停掉计时器)
	 * void
	 * @exception
	 */
	public void stopTimer(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}


	/**
	 * 初始化viewpager_banner
	 */
	private void initViewPager() {
		RelativeLayout.LayoutParams layoutparames = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 256 / 480);
		mVpBanner.setLayoutParams(layoutparames);
		mVpBanner.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentPageIndex = position;
				if (pageCount > 1) {
					for (int i = 0; i < dotViews.length; i++) {
						dotViews[i].setImageResource(i == currentPageIndex % dotViews.length ? R.drawable.banner_point_hov : R.drawable.banner_point_default);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mVpBanner.setPageTransformer(true, new DepthPageTransformer());
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, new AccelerateInterpolator());
			field.set(mVpBanner, scroller);
			scroller.setmDuration(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化小圆点
	 */
	private void initDotView() {
		if (pageCount < 2) {
			return;
		}
		mLlDot.removeAllViews();
		dotViews = new ImageView[pageCount];
		for (int i = 0; i < pageCount; i++) {
			dotViews[i] = new ImageView(mContext);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (7 * AndroidUtils.getDeviceDisplayDensity(mContext)),
					(int) (7 * AndroidUtils.getDeviceDisplayDensity(mContext)));
			int margin = AndroidUtils.dip2px(mContext, 3);
			layoutParams.setMargins(0, 0, margin, 0);
			dotViews[i].setLayoutParams(layoutParams);
			dotViews[i].setImageResource(i == 0 ? R.drawable.banner_point_hov : R.drawable.banner_point_default);
			mLlDot.addView(dotViews[i]);
		}

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
	
	/**
	 * 控制viewpage的切换时间
	 */
	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 1000;

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


	/**
	 * 轮换图片
	 * 
	 * @author tianliang.zhou
	 * 
	 */
	private class ChangeImageTask extends TimerTask {
		@Override
		public void run() {
			if (pageCount > 1) {
				int currentItem = mVpBanner.getCurrentItem();
				currentPageIndex = currentPageIndex==pageCount-1?0:currentItem+1;
				mHandler.obtainMessage(CHANGE_IMAGE).sendToTarget();
			}
		}
	};

}
