package com.earthman.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.earthman.app.R;
import com.earthman.app.utils.AndroidUtils;

/**
 * 作者：Zhou
 * 日期：2016-3-16 下午4:43:48
 * 描述：（滚动快捷导航）
 */
public class ScrollNavigateLayout extends LinearLayout{

	private Context context;
	private MyGridView gridView;
	private BaseAdapter adapter;	 
	private int numColumn;
	private TypedArray array;
	private int horizontalSpacing;
	private int verticalSpacing;
	private int itemWidth;
	
	/**
	 * 创建一个新的实例 ScrollNavigateLayout.
	 *
	 * @param context
	 * @param attrs
	 */
	public ScrollNavigateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;		
		//获取属性
		array = context.obtainStyledAttributes(attrs, R.styleable.scrollnavigatelayout);
		horizontalSpacing = array.getInt(R.styleable.scrollnavigatelayout_horizontalspacing, 0);
		verticalSpacing = array.getInt(R.styleable.scrollnavigatelayout_verticalspacing, 0);
		itemWidth = array.getInt(R.styleable.scrollnavigatelayout_itemWidth, 0);
		array.recycle();
		setOrientation(HORIZONTAL);		
		View view = LayoutInflater.from(context).inflate(R.layout.scrollnavigatelayout, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		initView(view);
		addView(view, params);						
	}
	
	/**
	 * 
	 * initView(初始化控件)
	 * @param view
	 * void
	 * @exception
	 */
	private void initView(View view){
		gridView = (MyGridView) view.findViewById(R.id.gridview);
		gridView.setHorizontalSpacing(AndroidUtils.dip2px(context, horizontalSpacing));		
		gridView.setVerticalSpacing(AndroidUtils.dip2px(context, verticalSpacing));
		gridView.setColumnWidth(AndroidUtils.dip2px(context, itemWidth));
	}
	
	/**
	 * 
	 * setAdapter(为gridview设置适配器)
	 * @param adapter
	 * void
	 * @exception
	 */
	public void setAdapter(BaseAdapter adapter){
		if(adapter == null){
			return;
		}
		this.adapter = adapter;				
		gridView.setAdapter(this.adapter);
		setParams();
	}
	
	/**
	 * 
	 * notifyDataChanged(gridview数据改变时通知)
	 * void
	 * @exception
	 */
	public void notifyDataChanged(){
		if(adapter == null){
			return;
		}
		adapter.notifyDataSetChanged();
		setParams();		
	}
	
	private void setParams(){
		this.numColumn = adapter.getCount();
		int width = numColumn * (itemWidth + AndroidUtils.dip2px(context, horizontalSpacing));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		gridView.setNumColumns(numColumn);
		gridView.setLayoutParams(layoutParams);
	}
	
	
	public void setOnItemClickListener(OnItemClickListener listener){
		gridView.setOnItemClickListener(listener);
	}
	
	
}
