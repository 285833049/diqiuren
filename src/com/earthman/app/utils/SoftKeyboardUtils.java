package com.earthman.app.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-27 下午3:27:03
 * @Decription 软键盘管理工具
 */
public class SoftKeyboardUtils {

	public static void hideFragmentSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		try {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	// 软键盘是否已经显示
	public static boolean isShowedKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive())
			return true;
		else
			return false;
	}

	// 显示软键盘
	public static void showKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}

	// 隐藏软键盘
	public static void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive())
			imm.hideSoftInputFromInputMethod(v.getApplicationWindowToken(), 0);
	}

	// 操作软键盘
	public static void controlKeyboard(final View v, final boolean isShow) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (isShow) {
					imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
				} else {
					// imm.hideSoftInputFromInputMethod(v.getApplicationWindowToken(),
					// 0);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}, 300);
	}
}
