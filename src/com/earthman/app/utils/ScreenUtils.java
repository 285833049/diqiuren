package com.earthman.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-24 上午9:15:16
 * @Decription 屏幕工具类
 */
@SuppressLint("InlinedApi")
public class ScreenUtils {
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int getStateBarHeight(Activity activity) {
		Rect rectangle = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
		return rectangle.top;
	}

	/**
	 * 获取屏幕的高度：避免带有导航栏手机获取高度时减去了导航栏高度适配器问题
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		switch (dm.widthPixels) {
		case 480:
			return 854;
		case 540:
			return 960;
		case 600:
			return 800;
			// -----------------------------720 p
		case 720:
			return 1280;
			// -----------------------------1080 p
		case 1080:// 常规
		case 1152:// 魅族
			return 1920;
			// -----------------------------4k
		case 1440:// NOTE 5
		case 1536:// 魅族5PRO
		case 1600:// 三星
			return 2560;
		default:
			return dm.heightPixels;
		}
	}

	/**
	 * 隐藏导航栏
	 * 
	 * @param activiy
	 * @exception
	 */
	@SuppressLint("InlinedApi")
	public static void hideNavigationBar(Activity activiy) {
		try {
			activiy.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
