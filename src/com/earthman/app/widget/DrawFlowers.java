package com.earthman.app.widget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.earthman.app.R;
import com.earthman.app.bean.DeadSurroundInfo;
import com.earthman.app.bean.FlowerInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-22 下午7:06:42
 * @Decription 绘制鲜花
 */
public class DrawFlowers extends RelativeLayout {

	private Context mContext;
	private ImageView mIvFlower;
	private List<FlowerInfo> mFlowerList;

	private int ANIM_DURECTION = 500;

	public DrawFlowers(Context context) {
		super(context);
	}

	public DrawFlowers(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// DisplayImageOptions options = new
		// DisplayImageOptions.Builder().cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY).build();
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options).build();
		// ImageLoader.getInstance().init(config);
	}

	public DrawFlowers(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 设置鲜花参数
	 * 
	 * @param benchmarkPoint
	 *            参照的基准点
	 * @param flowersList
	 *            鲜花列表
	 */
	public void setFlowersList(Point benchmarkPoint, List<DeadSurroundInfo> surroundList) {
		// AnimationSet animSet = getAnimationSet();
		removeAllViews();
		mFlowerList = new ArrayList<FlowerInfo>();
		// ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

		List<Point> pointList = getFlowerPoint(benchmarkPoint, surroundList.size());

		for (int i = 0; i < surroundList.size(); i++) {
			ImageView ivFlower = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.flower_item, null);
			ivFlower.setBackgroundResource(R.drawable.ic_flower_2);
			ivFlower.setScaleType(ScaleType.FIT_CENTER);
			ImageLoader.getInstance().displayImage(surroundList.get(i).getValue(), ivFlower);
			// ivFlower.setBackgroundColor(Color.RED);
			// bitmapUtils.display(ivFlower, surroundList.get(i).getValue());
			addView(ivFlower);
			RelativeLayout.LayoutParams params = (LayoutParams) ivFlower.getLayoutParams();
			Point point = pointList.get(i);
			params.leftMargin = point.x;
			params.topMargin = point.y;
			ivFlower.setLayoutParams(params);
			ivFlower.setAnimation(getAnimationSet(i));
			point = pointList.get(i);
			mFlowerList.add(new FlowerInfo(BitmapFactory.decodeResource(getResources(), R.drawable.ic_flower_1), point.x, point.y));
		}
	}

	public Drawable returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BitmapDrawable(bitmap);
	}

	private AnimationSet getAnimationSet(int position) {
		AnimationSet animSet = new AnimationSet(false);
		AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
		alphaAnim.setDuration(ANIM_DURECTION);
		alphaAnim.setFillAfter(true);
		alphaAnim.setStartOffset(position * 200);

		ScaleAnimation scaleAnim = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(ANIM_DURECTION);
		scaleAnim.setFillAfter(true);
		alphaAnim.setStartOffset(position * 200);

		animSet.addAnimation(alphaAnim);
		animSet.addAnimation(scaleAnim);

		return animSet;
	}

	private List<Point> getFlowerPoint(Point benchmarkPoint, int flowersNum) {
		List<Point> pointList = new ArrayList<Point>();
		Point innerPoint = new Point(benchmarkPoint.x - (flowersNum % 5 - 1) * 20, benchmarkPoint.y); // 内部花朵坐标
		int middleStartAngle=13;
		int withoutStartAngle = 10;
		int innerInterval = dip2px(getContext(), 14);
		int middleRadius = dip2px(getContext(), 50);// 中间半径距离
		int middleNextAngle = 36;// 180/5
		int withoutRadius = dip2px(getContext(), 100);// 外部半径距离
		int withoutNextAngle = 20;// 180/9

		int index = 0;

		if (flowersNum < 5) {
			for (int i = 0; i < flowersNum; i++) {
				pointList.add(new Point(innerPoint.x + i * innerInterval, (int)(middleRadius*1.5f)));
			}
		} else if (flowersNum == 5) {
			for (int i = 0; i < flowersNum; i++) {
				pointList.add(centerRadiusPoint(benchmarkPoint, middleStartAngle + i * middleNextAngle, middleRadius));
			}
		} else if (flowersNum <= 9) {
			Point middlePoint=new Point(benchmarkPoint);
			middlePoint.y=middlePoint.y+middleRadius;
			for (int i = 0; i < flowersNum; i++) {
				if (i < 5) {
					pointList.add(centerRadiusPoint(middlePoint, middleStartAngle + i * middleNextAngle, middleRadius));
				} else {
					pointList.add(new Point(innerPoint.x + index++ * innerInterval,(int)(middleRadius*1.5f)));
				}
			}
		} else if (flowersNum < 14) {
			for (int i = 0; i < flowersNum; i++) {
				if (i < 9) {
					pointList.add(centerRadiusPoint(benchmarkPoint, withoutStartAngle + i * withoutNextAngle, withoutRadius));
				} else {
					pointList.add(new Point(innerPoint.x + index++ * innerInterval,(int)(middleRadius*1.5f)));
				}
			}
		} else {// 14
			for (int i = 0; i < flowersNum; i++) {
				if (i < 5) {
					pointList.add(centerRadiusPoint(benchmarkPoint, middleStartAngle + i * middleNextAngle, middleRadius));
				} else {
					pointList.add(centerRadiusPoint(benchmarkPoint, withoutStartAngle + index++ * withoutNextAngle, withoutRadius));
				}
			}
		}
		return pointList;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public Point centerRadiusPoint(Point center, double angle, double radius) {
		Point point = new Point();
		double angleHude = angle * Math.PI / 180;/* 角度变成弧度 */
		point.x = (int) (radius * Math.cos(angleHude)) + center.x;
		point.y = (int) (radius * Math.sin(angleHude)) + center.y;
		return point;
	}

}
