package com.earthman.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-3-17 下午5:08:38
 * @Decription
 */
public class MyGridViewView extends GridView {
	public MyGridViewView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridViewView(Context context) {
		super(context);
	}

	public MyGridViewView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
