package com.earthman.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.utils.AndroidUtils;

/**
 * 作者：Zhou
 * 日期：2016-2-24 下午7:34:20
 * 描述：（）
 */
public class NavigateLayout extends LinearLayout{

	private String[] txtArrays;
	private TypedArray imgResIds;
	private Context context;
	
	/**
	 * 创建一个新的实例 NavigateLayout.
	 *
	 * @param context
	 * @param attrs
	 */
	public NavigateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.navigate);
		int txtResId = typedArray.getResourceId(R.styleable.navigate_textRes, 0);		
		int imgResId = typedArray.getResourceId(R.styleable.navigate_iconRes, 0);
		txtArrays = getResources().getStringArray(txtResId);
		imgResIds = getResources().obtainTypedArray(imgResId);
		typedArray.recycle();
		initView();
	}
		
	private void initView(){
		for(int i = 0; i < txtArrays.length; i++){
			TextView tView  = new TextView(context);
			tView.setText(txtArrays[i]);
			Drawable drawable = getResources().getDrawable(imgResIds.getResourceId(i, -1));
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tView.setCompoundDrawables(null, drawable, null, null);
			tView.setTextColor(context.getResources().getColor(R.color.black2));
			tView.setTextSize(12);
			tView.setCompoundDrawablePadding(AndroidUtils.dip2px(context, 10));
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, AndroidUtils.dip2px(context, 15), AndroidUtils.dip2px(context, 25), AndroidUtils.dip2px(context, 10));
			addView(tView, layoutParams);
		}
	    
		
	}
	
	

}
