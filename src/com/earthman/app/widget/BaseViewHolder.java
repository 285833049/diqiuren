package com.earthman.app.widget;

import android.util.SparseArray;
import android.view.View;

/**
 * viewHolder
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-3-17 下午8:43:16
 * @Decription
 */

public class BaseViewHolder {
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
