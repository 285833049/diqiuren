package com.earthman.app.pulltoreflesh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.earthman.app.R;

/**
 * XListView, it's based on <a
 * href="https://github.com/Maxwin-z/XListView-Android">XListView(Maxwin)</a>
 * 
 * @author markmjw
 * @date 2013-10-08
 */
public class XExpandableListView extends ExpandableListView
		implements
			OnScrollListener {
	// private static final String TAG = "XListView";

	private final static int SCROLL_BACK_HEADER = 0;
	private final static int SCROLL_BACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400;

	// when pull up >= 150px
	private final static int PULL_LOAD_MORE_DELTA = 50;

	// support iOS like pull
	private final static float OFFSET_RADIO = 1.8f;

	private float mLastY = -1;

	// used for scroll back
	private Scroller mScroller;
	// user's scroll listener
	private OnScrollListener mScrollListener;
	// for mScroller, scroll back from header or footer.
	private int mScrollBack;

	// the interface to trigger refresh and load more.
	private IXListViewListener mListener;

	private XHeaderView mHeader;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderContent;
	private TextView mHeaderTime;
	private int mHeaderHeight;

	private LinearLayout mFooterLayout;
	private XFooterView mFooterView;
	private boolean mIsFooterReady = false;

	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false;

	private boolean mEnablePullLoad = true;
	private boolean mEnableAutoLoad = false;
	private boolean mPullLoading = false;

	// total list items, used to detect is at the bottom of ListView
	private int mTotalItemCount;

	/* 左滑动显示“删除按钮” */
	private float minDis = 10;
	private float mLastMotionX;// 记住上次X触摸屏的位置
	private float mLastMotionY;// 记住上次Y触摸屏的位置
	private boolean isLock = false;
	/* 左滑动显示“删除按钮” */

	public XExpandableListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		// init header view
		mHeader = new XHeaderView(context);
		mHeaderContent = (RelativeLayout) mHeader
				.findViewById(R.id.header_content);
		mHeaderTime = (TextView) mHeader.findViewById(R.id.header_hint_time);
		addHeaderView(mHeader);

		// init footer view
		mFooterView = new XFooterView(context);
		mFooterLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		mFooterLayout.addView(mFooterView, params);

		// init header height
		ViewTreeObserver observer = mHeader.getViewTreeObserver();
		if (null != observer) {
			observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onGlobalLayout() {
					mHeaderHeight = mHeaderContent.getHeight();
					ViewTreeObserver observer = getViewTreeObserver();

					if (null != observer) {
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							observer.removeGlobalOnLayoutListener(this);
						} else {
							observer.removeOnGlobalLayoutListener(this);
						}
					}
				}
			});
		}
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		// make sure XFooterView is the last footer view, and only add once.
		if (!mIsFooterReady) {
			mIsFooterReady = true;
			addFooterView(mFooterLayout);
		}

		super.setAdapter(adapter);
	}

	/**
	 * Enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;

		// disable, hide the content
		mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * Enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;

		if (!mEnablePullLoad) {
			mFooterView.setBottomMargin(0);
			mFooterView.hide();
			mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
			mFooterView.setOnClickListener(null);

		} else {
			mPullLoading = false;
			mFooterView.setPadding(0, 0, 0, 0);
			mFooterView.show();
			mFooterView.setState(XFooterView.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * Enable or disable auto load more feature when scroll to bottom.
	 * 
	 * @param enable
	 */
	public void setAutoLoadEnable(boolean enable) {
		mEnableAutoLoad = enable;
	}

	/**
	 * Stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * Stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading) {
			mPullLoading = false;
			mFooterView.setState(XFooterView.STATE_NORMAL);
		}
	}

	/**
	 * Set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTime.setText(time);
	}

	/**
	 * Set listener.
	 * 
	 * @param listener
	 */
	public void setXListViewListener(IXListViewListener listener) {
		mListener = listener;
	}

	/**
	 * Auto call back refresh.
	 */
	public void autoRefresh() {
		mHeader.setVisibleHeight(mHeaderHeight);

		if (mEnablePullRefresh && !mPullRefreshing) {
			// update the arrow image not refreshing
			if (mHeader.getVisibleHeight() > mHeaderHeight) {
				mHeader.setState(XHeaderView.STATE_READY);
			} else {
				mHeader.setState(XHeaderView.STATE_NORMAL);
			}
		}

		mPullRefreshing = true;
		mHeader.setState(XHeaderView.STATE_REFRESHING);
		refresh();
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener listener = (OnXScrollListener) mScrollListener;
			listener.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());

		if (mEnablePullRefresh && !mPullRefreshing) {
			// update the arrow image unrefreshing
			if (mHeader.getVisibleHeight() > mHeaderHeight) {
				mHeader.setState(XHeaderView.STATE_READY);
			} else {
				mHeader.setState(XHeaderView.STATE_NORMAL);
			}
		}

		// scroll to top each time
		setSelection(0);
	}

	private void resetHeaderHeight() {
		int height = mHeader.getVisibleHeight();
		if (height == 0)
			return;

		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderHeight)
			return;

		// default: scroll back to dismiss header.
		int finalHeight = 0;
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderHeight) {
			finalHeight = mHeaderHeight;
		}

		mScrollBack = SCROLL_BACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);

		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;

		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) {
				// height enough to invoke load more.
				mFooterView.setState(XFooterView.STATE_READY);
			} else {
				mFooterView.setState(XFooterView.STATE_NORMAL);
			}
		}

		mFooterView.setBottomMargin(height);

		// scroll to bottom
		// setSelection(mTotalItemCount - 1);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();

		if (bottomMargin > 0) {
			mScrollBack = SCROLL_BACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XFooterView.STATE_LOADING);
		loadMore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN :
				mLastY = ev.getRawY();
				break;

			case MotionEvent.ACTION_MOVE :
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();

				if (getFirstVisiblePosition() == 0
						&& (mHeader.getVisibleHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();

				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;

			default :
				// reset
				mLastY = -1;
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh
							&& mHeader.getVisibleHeight() > mHeaderHeight) {
						mPullRefreshing = true;
						mHeader.setState(XHeaderView.STATE_REFRESHING);
						refresh();
					}

					resetHeaderHeight();

				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLL_BACK_HEADER) {
				mHeader.setVisibleHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}

			postInvalidate();
			invokeOnScrolling();
		}

		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (mEnableAutoLoad && getLastVisiblePosition() == getCount() - 1) {
				startLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	private void refresh() {
		if (mEnablePullRefresh && null != mListener) {
			mListener.onRefresh();
		}
	}

	private int i = 0;
	private void loadMore() {
		if (mEnablePullLoad && null != mListener) {
			mListener.onLoadMore();
		}
	}

	/**
	 * You can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * Implements this interface to get refresh/load more event.
	 * 
	 * @author markmjw
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	/* 左滑动显示“删除按钮” */
	/**
	 * 如果一个ViewGroup的onInterceptTouchEvent()方法返回true，说明Touch事件被截获，
	 * 子View不再接收到Touch事件，而是转向本ViewGroup的
	 * onTouchEvent()方法处理。从Down开始，之后的Move，Up都会直接在onTouchEvent()方法中处理。
	 * 先前还在处理touch event的child view将会接收到一个 ACTION_CANCEL。
	 * 如果onInterceptTouchEvent()返回false，则事件会交给child view处理。
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isIntercept(ev)) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean dte = super.dispatchTouchEvent(event);
		if (MotionEvent.ACTION_UP == event.getAction() && !dte) {// onItemClick
			int position = pointToPosition((int) event.getX(),
					(int) event.getY());
			View view = getChildAt(position);
			if (view == null) {
				return dte;
			}
			super.performItemClick(view, position, view.getId());
		}
		return dte;
	}

	@Override
	// 处理点击事件，如果是手势的事件则不作点击事件 普通View
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	// 处理点击事件，如果是手势的事件则不作点击事件 ListView
	public boolean performItemClick(View view, int position, long id) {
		return super.performItemClick(view, position, id);
	}

	/**
	 * 检测是ListView滑动还是item滑动 isLock 一旦判读是item滑动，则在up之前都是返回false
	 */
	private boolean isIntercept(MotionEvent ev) {
		float x = ev.getX();
		float y = ev.getY();
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN :
				mLastMotionX = x;
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE :
				if (!isLock) {
					float deltaX = Math.abs(mLastMotionX - x);
					float deltay = Math.abs(mLastMotionY - y);
					mLastMotionX = x;
					mLastMotionY = y;
					if (deltaX > deltay && deltaX > minDis) {
						isLock = true;
						return false;
					}
				} else {
					return false;
				}
				break;
			case MotionEvent.ACTION_UP :
				isLock = false;
				break;
			case MotionEvent.ACTION_CANCEL :
				isLock = false;
				break;
		}
		return true;
	}
	/* 左滑动显示“删除按钮” */
}
