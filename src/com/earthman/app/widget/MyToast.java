package com.earthman.app.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.Toast;
/**
 * 作者：Zhou
 * 日期：2015-12-23 下午1:52:38
 * 版权：地球人
 * 描述：（）
 */
public class MyToast{

	public static Toast makeText(Context context, CharSequence text, int duration) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		return toast;
	}

	public static Toast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId), duration);
	}

}
