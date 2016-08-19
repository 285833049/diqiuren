package com.earthman.app.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.earthman.app.R;
import com.earthman.app.bean.CoordinatesInfo;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-18 下午3:19:36
 * @Decription 绘制线条
 */
public class DrawLine extends SurfaceView implements SurfaceHolder.Callback {

	private final byte SLEEP_TIME = 30;// 睡眠时间
	private int mDurection = 600;// 总持续时间
	private SurfaceHolder mSurfaceHolder;

	private Bitmap mBgBitmap;

	private LoopThread mThread;
	private List<CoordinatesInfo> mCoordinatesList;

	// ---------------------------------Java代码初始化时调用
	public DrawLine(Context context, List<CoordinatesInfo> coordinatesList) {
		super(context);
		this.mCoordinatesList = coordinatesList;
		// this.setZOrderOnTop(true);
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		getHolder().addCallback(this); // 设置Surface生命周期回调
		mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_circle);
		mThread = new LoopThread(mSurfaceHolder, getContext());
		mThread.start();
	}

	// ---------------------------------布局添加时候调用
	public DrawLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.setZOrderOnTop(true);
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		getHolder().addCallback(this); // 设置Surface生命周期回调
		mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_circle);
	}

	public void setCoordinatesList(List<CoordinatesInfo> coordinatesList) {
		this.mDurection = 550;// 重置
		this.mCoordinatesList = coordinatesList;
		mThread = new LoopThread(mSurfaceHolder, getContext());
		mThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {// 在创建时激发，一般在这里调用画图的线程。
		mSurfaceHolder = holder;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {// 在surface的大小发生改变时激发

	}

	public void surfaceDestroyed(SurfaceHolder holder) {// 销毁时激发，一般在这里将画图的线程停止、释放。
		try {
			mThread.isRunning = false;
			mThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 执行绘制的绘制线程
	class LoopThread extends Thread {

		SurfaceHolder surfaceHolder;
		Context context;
		boolean isRunning;
		Paint paint;

		public LoopThread(SurfaceHolder surfaceHolder, Context context) {
			this.surfaceHolder = surfaceHolder;
			this.context = context;
			isRunning = true;
			paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(1);
			paint.setAntiAlias(true);
		}

		@Override
		public void run() {
			Canvas canvas = null;
			while (isRunning) {
				try {
					synchronized (surfaceHolder) {
						canvas = surfaceHolder.lockCanvas();
						doDraw(canvas);
						Thread.sleep(SLEEP_TIME);
					}
					if ((mDurection -= SLEEP_TIME) <= 0)
						isRunning = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (surfaceHolder != null && canvas != null)
						surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}

		public void doDraw(Canvas canvas) {
			if(canvas==null)return;
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			canvas.drawBitmap(mBgBitmap, 0, 0, null);
			for (CoordinatesInfo info : mCoordinatesList) {
				canvas.drawLine(info.getStartX(), info.getStartY(), info.getCurrentX(), info.getCurrentY(), paint);
			}
		}
	}

}
