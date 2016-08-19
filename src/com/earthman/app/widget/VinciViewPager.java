package com.earthman.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
/**
 * <p>description: 自定义可过滤HorizontalListView水平滚动ViewPager</p>
 * <p>company:     地球人</p>
 * <p>author:      Vinci</p>
 * <p>date:        2016-3-16下午6:35:45</p>
 */
public class VinciViewPager extends ViewPager {

	public VinciViewPager(Context context) {
		super(context);
	}

	public VinciViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof HorizontalListView) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
