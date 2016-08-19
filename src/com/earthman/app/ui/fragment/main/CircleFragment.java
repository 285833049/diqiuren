package com.earthman.app.ui.fragment.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.CircleAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.CircleFriendInfo;
import com.earthman.app.bean.CircleListInfo;
import com.earthman.app.bean.CircleListModel;
import com.earthman.app.bean.CircleUpdateBean;
import com.earthman.app.bean.CoordinatesInfo;
import com.earthman.app.eventbusbean.ModifyMark;
import com.earthman.app.ui.activity.circle.CircleDetailActivity;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.ScreenUtils;
import com.earthman.app.widget.CircleImageView;
import com.earthman.app.widget.DrawLine;
import com.earthman.app.widget.HorizontalListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-17 上午10:29:07
 * @Decription 圈子
 */
public class CircleFragment extends BaseFragment implements OnItemClickListener, OnLongClickListener {

	@ViewInject(R.id.lv_circle_type)
	private HorizontalListView mLvCircleType;
	private CircleAdapter mAdapter;
	private List<CircleListInfo> mCircleList;
	private List<CircleFriendInfo> mCurrentFriendList;
	private int friendsGroupPosition;
	@ViewInject(R.id.drawLine)
	private DrawLine mDrawLine;

	@ViewInject(R.id.iv_shade_bg)
	private ImageView mIvShadeBg;

	private View mView;
	@ViewInject(R.id.tv_circle_name)
	private TextView mTvCircleName;
	@ViewInject(R.id.tv_see_all)
	private TextView mTvSeeAll;
	// ---------------------------------------------------
	private final byte MAX_SIZE = 15;
	@ViewInject(R.id.iv_head)
	private CircleImageView mIvHead;// 自己头像。
	@ViewInject(R.id.iv_logo)
	private CircleImageView mEathLogo;// 加载过程显示系统App图标
	private int mScreenWidth;// 屏幕宽度。
	private int mScreenHeight;// 屏幕高度。
	private int mImageWidth;// 头像宽度。

	private UserInfoPreferences mUserInfo;
	@ViewInject(R.id.tv_head_name)
	private TextView mTvHeadName;
	private String headURL;
	private String headName;
	private String friendID;
	// ---------------------------------------------------对象组
	@ViewInject(R.id.rl_circle_detail)
	private RelativeLayout mRlCircleDetail;// 对象容器
	private List<LinearLayout> mObjectList;
	private List<CoordinatesInfo> mCoordinatesList;// 坐标对象
	private boolean isReleaseLock = true;// 防止SurfaceView绘制中途中断

	@Override
	protected View createView() {
		EventBus.getDefault().register(this);
		mView = LayoutInflater.from(activity).inflate(R.layout.circle_fragment, null);
		ViewUtils.inject(this, mView);
		return mView;
	}

	@Override
	protected void initData() {
		mUserInfo = new UserInfoPreferences(activity);
		friendsGroupPosition = 0;
		initPrepare();
		doGetCirleList(friendID = mUserInfo.getUserID());
	}

	@Override
	protected void initView(View convertView) {
		mLvCircleType.setOnItemClickListener(this);
	}

	@Override
	protected void setAttribute() {

	}

	@OnClick(value = { R.id.tv_see_all })
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.tv_see_all:
			try {
				Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
				intent.putExtra("friendList", (Serializable) mCircleList.get(mAdapter.getSelectedPosition()).getFriends());
				intent.putExtra("title", mCircleList.get(mAdapter.getSelectedPosition()).getName());
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_head:

			break;
		case R.id.iv_logo:
			break;
		default:
			CircleFriendInfo info = mCurrentFriendList.get(v.getId());
			headURL = info.getAvatar();
			headName = info.getNice();
			doGetCirleList(friendID = String.valueOf(info.getId()));
			break;
		}
	}

	private void doGetCirleList(String friendid) {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(mUserInfo.getUserID());
		list.add(mUserInfo.getUserToken());
		list.add(friendid);
		String url = HttpUrlConstants.getUrl(activity, HttpUrlConstants.CIRCLE_ALL_FRIENDS_GROUPS, list);
		executeRequest(new FastJsonRequest<CircleListModel>(Method.GET, url, CircleListModel.class, new Response.Listener<CircleListModel>() {

			@Override
			public void onResponse(CircleListModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					mLvCircleType.setAdapter(mAdapter = new CircleAdapter(mCircleList = response.getResult()));
					if (response.getResult()!=null&&response.getResult().size()!=0) {
						mTvCircleName.setText(mCircleList.get(0).getName());
						showFriendsDetail();
					}
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	private void showFriendsDetail() {
		if (mObjectList != null) {
			for (LinearLayout ll : mObjectList) {
				ll.removeAllViews();
			}
		}
		try {
			initArguments();
			initSubviews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPrepare() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		mUserInfo = new UserInfoPreferences(activity);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = ScreenUtils.getScreenHeight(getActivity()) - AndroidUtils.dip2px(getActivity(), 240);
		mImageWidth = dm.widthPixels >> 3;
		headName = "我";
		headURL = mUserInfo.getUserIco();
	}

	private void initArguments() {
		mObjectList = new ArrayList<LinearLayout>();
		mCoordinatesList = new ArrayList<CoordinatesInfo>();
		mCurrentFriendList = mCircleList.get(friendsGroupPosition).getFriends();
		for (int i = 0; i < mCurrentFriendList.size() && i < MAX_SIZE; i++) {
			CircleFriendInfo friendInfo = mCurrentFriendList.get(i);
			LinearLayout llObject = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.circle_object_view, null);
			CircleImageView ivObjectHead = (CircleImageView) llObject.findViewById(R.id.iv_object_head);
			if (friendInfo.getAvatar() != null)
				ImageLoader.getInstance().displayImage(friendInfo.getAvatar(), ivObjectHead);
			// ivObjectHead.setOnClickListener(this);
			TextView tvObjectName = (TextView) llObject.findViewById(R.id.tv_object_name);
			if(!TextUtils.isEmpty(friendInfo.getRemarks())){//有备注显示备注名，无备注名显示昵称
				tvObjectName.setText(friendInfo.getRemarks());
			}else{
				tvObjectName.setText(friendInfo.getNice());
			}			
			// ------------------------------
			mCoordinatesList.add(new CoordinatesInfo(mScreenWidth >> 1, mScreenHeight >> 1));
			// ------------------------------
			isReleaseLock = false;
			RelativeLayout.LayoutParams paras = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);// 初始化位置为中心点
			paras.leftMargin = mScreenWidth / 2 - mImageWidth / 2;// 中心点x
			paras.topMargin = mScreenHeight / 2 - mImageWidth / 2;// 中心点y
			llObject.setLayoutParams(paras);
			llObject.setId(i);
			llObject.setOnClickListener(this);
			llObject.setOnLongClickListener(this);
			// ------------------------------保存
			mObjectList.add(llObject);
			mRlCircleDetail.addView(llObject);
		}
	}

	/**
	 * 初始化按钮。
	 */
	private void initSubviews() {
		RelativeLayout.LayoutParams paras = new RelativeLayout.LayoutParams(mImageWidth + 20, mImageWidth + 20);
		paras.leftMargin = mScreenWidth / 2 - mImageWidth / 2 - AndroidUtils.dip2px(getActivity(), 3);
		paras.topMargin = mScreenHeight / 2 - mImageWidth / 2 - AndroidUtils.dip2px(getActivity(), 3);
		mTvHeadName.setText(headName);
		mIvHead.setLayoutParams(paras);
		ImageLoader.getInstance().displayImage(headURL, mIvHead);
		mEathLogo.setLayoutParams(paras);
		mEathLogo.setOnClickListener(this);

		if (mCircleList != null && mCurrentFriendList.size() != 0)
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					// int startAngle = getRandom(360);
					int startAngle = 0;
					int friendSize = mCurrentFriendList.size() >= MAX_SIZE ? MAX_SIZE : mCurrentFriendList.size();// 最多只显示15个
					int nextAngle = 360 / friendSize;
					int radius = mScreenWidth / 2 - mImageWidth;
					ValueAnimator[] animList = new ValueAnimator[friendSize + 2];
					animList[0] = ObjectAnimator.ofFloat(mIvHead, "alpha", 0f, 1f);
					animList[1] = ObjectAnimator.ofFloat(mEathLogo, "alpha", 1f, 0f);
					for (int i = 0; i < friendSize; i++) {
						animList[i + 2] = drawTrajectory(mObjectList.get(i), startAngle + nextAngle * (i + 1), radius, i);
					}
					AnimatorSet animSet = new AnimatorSet();
					animSet.playTogether(animList);
					animSet.setDuration(1000);
					animSet.start();

					try {
						// mDrawLine.setAnimation(hideShade());
						mIvShadeBg.setVisibility(View.GONE);
						mIvHead.setVisibility(View.VISIBLE);
						mDrawLine.setVisibility(View.VISIBLE);
						mDrawLine.setCoordinatesList(mCoordinatesList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	}

	/**
	 * 绘制轨迹路线
	 * 
	 * @param view
	 *            移动的对象。
	 * @param angle
	 *            偏移的角度
	 * @param length
	 *            偏移的距离
	 * @return 移动动画
	 */
	public ValueAnimator drawTrajectory(final View view, final int angle, final int radius, int position) {
		CoordinatesInfo info = mCoordinatesList.get(position);
		Point point = centerRadiusPoint(new Point(info.getStartX(), info.getStartY()), angle, radius);// 根据中心点+弧度+半径获取终点距离
		info.setEndXY(point.x, point.y);
		// ------------------------------------------------------------
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setObjectValues(new PointF(0, 0));
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
			@Override
			public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
				PointF point = new PointF();
				point.x = (float) (fraction * radius * Math.cos(angle * Math.PI / 180));
				point.y = (float) (fraction * radius * Math.sin(angle * Math.PI / 180));
				return point;
			}
		});
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF point = (PointF) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
				params.leftMargin = (int) point.x + (mScreenWidth / 2 - mImageWidth / 2);
				params.topMargin = (int) point.y + (mScreenHeight / 2 - mImageWidth / 2);
				view.setLayoutParams(params);
			}
		});
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				isReleaseLock = true;// 释放绘制锁
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		return valueAnimator;
	}

	public Point centerRadiusPoint(Point center, double angle, double radius) {
		Point point = new Point();
		double angleHude = angle * Math.PI / 180;/* 角度变成弧度 */
		point.x = (int) (radius * Math.cos(angleHude)) + center.x;
		point.y = (int) (radius * Math.sin(angleHude)) + center.y;
		return point;
	}

	// // 获取区间 0-range 随机数
	// private int getRandom(int range) {
	// return (int) (Math.random() * range);
	// }
	//
	// // 获取min-max随机数
	// private int getRandom(int min, int max) {
	// return min + (int) (Math.random() * ((max - min) + 1));
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		try {
			if (isReleaseLock && friendsGroupPosition != position) {// 动画绘制完成，并且点击的不是当前下标
				// mDrawLine.setVisibility(View.GONE);
				mTvCircleName.setText(mCircleList.get(position).getName());
				mAdapter.setSelectedPosition(friendsGroupPosition = position);
				mAdapter.notifyDataSetChanged();
				showFriendsDetail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					showFriendsDetail();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, 500);
	}

	@Override
	public void onStop() {
		super.onStop();
		// ----------------------离开当前页面Remove掉所有头像以及隐藏线条
		try {
			mDrawLine.setVisibility(View.GONE);
			mIvHead.setVisibility(View.GONE);
			if (mObjectList != null) {
				for (LinearLayout ll : mObjectList) {
					ll.removeAllViews();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			doGetCirleList(friendID);// 添加、删除、编辑返回后需要更新当前页面数据
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		Intent intent = new Intent(getActivity(), PeronalDetialActivity.class);
		intent.putExtra("friendsuserid", String.valueOf(mCurrentFriendList.get(arg0.getId()).getUniqueid()));
		startActivity(intent);
		return false;
	}

	public void onEvent(CircleUpdateBean event) {
		friendsGroupPosition=0;
		headName = "我";
		headURL = mUserInfo.getUserIco();
		doGetCirleList(friendID = mUserInfo.getUserID());
	}
	
	public void onEventMainThread(ModifyMark event) {
		doGetCirleList(friendID);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
