package com.earthman.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月25日 下午6:17:05
 * @Decription
 */
public class MyCommentListView extends MyListView {

	/**
	 * 创建一个新的实例 MyCommentListView.
	 *
	 * @param context
	 * @param attrs
	 */
	public MyCommentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 下面这句话是关键
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);// 也有所不同哦
	}
}
