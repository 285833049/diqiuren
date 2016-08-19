package com.earthman.app.ui.activity.mine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.ImgViewPagerAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.PersonalInfo;
import com.earthman.app.bean.PersonalInfo.ProfessionalStatus;
import com.earthman.app.eventbusbean.ModifyMark;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.uikit.session.constant.Extras;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.ui.activity.album.AlbumListActivity;
import com.earthman.app.ui.activity.circle.DeadActivity;
import com.earthman.app.ui.activity.dynamic.MineMyDynamic;
import com.earthman.app.ui.activity.friend.SetFriendGroupsActivity;
import com.earthman.app.ui.activity.video.VideoListActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.MathUtils;
import com.earthman.app.utils.MyStringUtils;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.utils.SharePreferenceUtil;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyScrollView;
import com.earthman.app.widget.MyScrollView.ScrollViewListener;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 她的主页
 * 
 * @Title: PeronalDetialActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月1日
 */
@ContentView(R.layout.activity_personal_information)
public class PeronalDetialActivity extends BaseActivity implements ScrollViewListener {
	@ViewInject(R.id.rl_title_lll)
	RelativeLayout title;
	@ViewInject(R.id.rl_death)
	RelativeLayout rl_death;
	@ViewInject(R.id.scrollview)
	MyScrollView scview;
	@ViewInject(R.id.personal_detial_head)
	LinearLayout head;
	@ViewInject(R.id.title_dot_ll)
	LinearLayout title_dot_ll;
	@ViewInject(R.id.title_back_iv)
	ImageView back;
	@ViewInject(R.id.personal_viewpager_banner)
	ViewPager viewpager_banner;
	private int head_height;
	private double scal;
	private ImgViewPagerAdapter pagerAdapter;
	private int currentPageIndex;
	private int PERSONAL_BANAER_TYPE = 1;// 有透明的改变的bannaer
	String friendsuserid;
	@ViewInject(R.id.her_name)
	TextView her_name;
	@ViewInject(R.id.her_nice)
	TextView her_nice;
	@ViewInject(R.id.her_cardid)
	TextView her_cardid;
	@ViewInject(R.id.her_gender)
	TextView her_gender;
	@ViewInject(R.id.her_birthday)
	TextView her_birthday;
	@ViewInject(R.id.her_blood)
	TextView her_blood;
	@ViewInject(R.id.her_height)
	TextView her_height;
	@ViewInject(R.id.her_weight)
	TextView her_weight;
	@ViewInject(R.id.her_contact)
	TextView her_contact;
	@ViewInject(R.id.her_feelings)
	TextView her_feelings;
	@ViewInject(R.id.her_degrees)
	TextView her_degrees;
	@ViewInject(R.id.her_professional)
	TextView her_professional;
	@ViewInject(R.id.her_emergencyContact)
	TextView her_emergencyContact;
	@ViewInject(R.id.her_companyAddress)
	TextView her_companyAddress;
	@ViewInject(R.id.her_homeAddress)
	TextView her_homeAddress;
	@ViewInject(R.id.her_life)
	TextView her_life;
	@ViewInject(R.id.her_talks)
	RadioButton her_talks;
	@ViewInject(R.id.her_albums)
	RadioButton her_albums;
	@ViewInject(R.id.her_videos)
	RadioButton her_videos;
	@ViewInject(R.id.title_name_tv)
	TextView title_name_tv;

	@ViewInject(R.id.her_name_btn)
	TextView her_name_btn;
	@ViewInject(R.id.her_nice_btn)
	TextView her_nice_btn;
	@ViewInject(R.id.her_cardid_btn)
	TextView her_cardid_btn;
	@ViewInject(R.id.her_gender_btn)
	TextView her_gender_btn;
	@ViewInject(R.id.her_birthday_btn)
	TextView her_birthday_btn;
	@ViewInject(R.id.her_blood_btn)
	TextView her_blood_btn;
	@ViewInject(R.id.her_height_btn)
	TextView her_height_btn;
	@ViewInject(R.id.her_weight_btn)
	TextView her_weight_btn;
	@ViewInject(R.id.her_contact_btn)
	TextView her_contact_btn;
	@ViewInject(R.id.her_feelings_btn)
	TextView her_feelings_btn;
	@ViewInject(R.id.her_degrees_btn)
	TextView her_degrees_btn;
	@ViewInject(R.id.her_professional_btn)
	TextView her_professional_btn;
	@ViewInject(R.id.her_emergencyContact_btn)
	TextView her_emergencyContact_btn;
	@ViewInject(R.id.her_companyAddress_btn)
	TextView her_companyAddress_btn;
	@ViewInject(R.id.her_homeAddress_btn)
	TextView her_homeAddress_btn;
	@ViewInject(R.id.her_life_btn)
	TextView her_life_btn;
	@ViewInject(R.id.her_email)
	RadioButton mHerEmail;
	@ViewInject(R.id.verticalline)
	View mVerticalLine;

	String money;
	private ActivityResultListener listener;
	private String name;
	private int FRIENDDYNAMIC = 1;
	private PaySuccessReceiver receiver;
	private ArrayList<String> personalDetialBanerPics = new ArrayList<String>();
	private String isFriend;
	private String remarks;
	private ProfessionalStatus professionalStatus;
	private String account;
	private int userId;

	public static void start(Context context, String account) {
		Intent intent = new Intent();
		intent.setClass(context, PeronalDetialActivity.class);
		intent.putExtra(Extras.EXTRA_ACCOUNT, account);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}

	public static void startSee(Context context, String friendsuserid) {
		Intent peronalIntent = new Intent(context, PeronalDetialActivity.class);
		peronalIntent.putExtra("friendsuserid", friendsuserid);
		context.startActivity(peronalIntent);
	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {

		case R.id.her_email:
			if (!checkUserStatus()) {
				return;
			}
			Intent emailIntent = new Intent(Intent.ACTION_VIEW);
			emailIntent.setData(Uri.parse(HttpUrlConstants.EMAIL_URL));
			startActivity(emailIntent);
			break;
		case R.id.title_back_iv:
			finish();
			break;
		case R.id.title_dot_ll:
			if (checkUserStatus()) {
				Showpop(title_dot_ll);
			}
			break;
		case R.id.her_name_btn:
			if (checkUserStatus()) {
				determinePay("realName");
			}
			break;
		case R.id.her_nice_btn:
			if (checkUserStatus()) {
				determinePay("nice");
			}
			break;
		case R.id.her_cardid_btn:
			// determinePay();
			break;
		case R.id.her_gender_btn:
			if (checkUserStatus()) {
				determinePay("gender");
			}
			break;
		case R.id.her_birthday_btn:
			if (checkUserStatus()) {
				determinePay("birthday");
			}
			break;
		case R.id.her_blood_btn:
			if (checkUserStatus()) {
				determinePay("blood");
			}
			break;
		case R.id.her_height_btn:
			if (checkUserStatus()) {
				determinePay("height");
			}
			break;
		case R.id.her_weight_btn:
			if (checkUserStatus()) {
				determinePay("weight");
			}
			break;
		case R.id.her_contact_btn:
			if (checkUserStatus()) {
				determinePay("contact");
			}
			break;

		case R.id.her_feelings_btn:
			if (checkUserStatus()) {
				determinePay("feelings");
			}
			break;
		case R.id.her_degrees_btn:
			if (checkUserStatus()) {
				determinePay("degrees");
			}
			break;
		case R.id.her_professional_btn:
			if (checkUserStatus()) {
				determinePay("professional");
			}
			break;

		case R.id.her_emergencyContact_btn:
			if (checkUserStatus()) {
				determinePay("emergencyContact");
			}
			break;

		case R.id.her_companyAddress_btn:
			if (checkUserStatus()) {
				determinePay("companyAddress");
			}
			break;

		case R.id.her_homeAddress_btn:
			if (checkUserStatus()) {
				determinePay("homeAddress");
			}
			break;
		case R.id.her_life_btn:
			if (checkUserStatus()) {
				determinePay("life");
			}
			break;
		case R.id.rl_death:// 去敬献
			if (checkUserStatus()) {
				Intent intent = getIntent();
				intent.setClass(PeronalDetialActivity.this, DeadActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.her_talks:// 好友的动态
			if (checkUserStatus()) {
				Intent dynamicIntent = new Intent(PeronalDetialActivity.this, MineMyDynamic.class);
				dynamicIntent.putExtra("pagetype", FRIENDDYNAMIC);
				dynamicIntent.putExtra("friendId", userId);
				dynamicIntent.putExtra("friendName", nice);
				startActivity(dynamicIntent);
			}
			break;
		case R.id.her_albums:// 好友相册
			if (checkUserStatus()) {
				AlbumListActivity.intoActivity(PeronalDetialActivity.this, AlbumListActivity.SEE_FRIEND_ALBUM, Integer.parseInt("" + userId));
			}
			break;
		case R.id.her_videos:// 好友视频
			if (checkUserStatus()) {
				VideoListActivity.intoActivity(PeronalDetialActivity.this, VideoListActivity.SEE_FRIEND_VIDEO, Integer.parseInt("" + userId));
			}
		default:
			break;
		}
	}

	/**
	 * 
	 * determinePay(判断是否设置支付密码)
	 * void
	 * 0未设置过 1设置过
	 * 
	 * @exception
	 */
	private void determinePay(String columnname) {
		int setpwd = preferences.getIsSetPwd();
		if (setpwd == 0) {
			showSetPayPwdAlert();
		} else if (setpwd == 1) {
			money = "" + 2;
			showMyDialog(money, columnname);
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
		}
	}

	private void showMyDialog(final String money, final String columnname) {

		PayPwdDialog payPwdDialog = new PayPwdDialog(this, money, new OnSurePayClickListener() {

			@Override
			public void onSurePayClick(int payType, String pwd) {
				if (pwd == null || pwd.equals("")) {
					pwd = preferences.getUserPayPsw();
				}
				if (payType == 0) {
					payType = 2;
				}
				new PayFactory(PeronalDetialActivity.this, PayFactory.CASE_PAY_FOR_LOOK, payType, money).genUserInfoPayRequest(pwd, userId, columnname);
			}

		});
		listener = payPwdDialog;
		payPwdDialog.setCanceledOnTouchOutside(false);
		payPwdDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PayPwdDialog.CHANGE_PAYTYPE:
				if (listener != null) {
					listener.onActivityResult(requestCode, resultCode, data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 弹出删除好友选项
	 */
	@SuppressLint("InflateParams")
	private void Showpop(View view) {
		View contentView = LayoutInflater.from(this).inflate(R.layout.delete_friend_pop, null);
		final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
			}
		});

		popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

		popupWindow.showAsDropDown(view, 0, 20);
		View deleteview = contentView.findViewById(R.id.delete_friend);
		View beizhu = contentView.findViewById(R.id.add_beizhu_set);
		View add_friend = contentView.findViewById(R.id.add_friend);
		View chat = contentView.findViewById(R.id.chat);
		View line = contentView.findViewById(R.id.line);
		View line1 = contentView.findViewById(R.id.line1);
		if ("0".equals(isFriend)) {// 0 不是好友关系
			beizhu.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			deleteview.setVisibility(View.GONE);
			chat.setVisibility(View.GONE);
			line1.setVisibility(View.GONE);
			add_friend.setVisibility(View.VISIBLE);
		}
		add_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doGetAddFriend();
				popupWindow.dismiss();

			}
		});
		beizhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PeronalDetialActivity.this, SetFriendGroupsActivity.class);
				intent.putExtra("friendid", friendsuserid);
				intent.putExtra("remarks", remarks);
				intent.putExtra("nice", nice);
				startActivity(intent);
				popupWindow.dismiss();
			}
		});
		deleteview.setOnClickListener(new OnClickListener() {

			DialogCommon deleteDialog;

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				deleteDialog = new DialogCommon(PeronalDetialActivity.this, "", getString(R.string.friend_delete_confirm), "", new CommonDialogListener() {
					@Override
					public void onRightButtonClick() {
						// 删除好友
						doDeleteFriend(deleteDialog);
					}

				});
				deleteDialog.show();
			}

		});
		chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SessionHelper.startP2PSession(PeronalDetialActivity.this, getIntent().getStringExtra("friendsuserid"));
			}
		});
	}

	/**
	 * 删除好友操作
	 */
	private void doDeleteFriend(final DialogCommon myDialogStyle1) {
		// 删除好友操作
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(friendsuserid);
		list.add(userInfoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.DELETE_FRIEND, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					myDialogStyle1.dismiss();
					myLoadingDialog.dismiss();
					setResult(0x005);
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(PeronalDetialActivity.this, response.getCode(), response.getMessage());

				}
			}
		}, getErrorListener()));
	}

	/**
	 * 添加好友请求
	 */
	private void doGetAddFriend() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(friendsuserid);
		list.add(userInfoPreferences.getUserToken());
		list.add(Constants.ADD_FRIEND_TO_SOMEONE);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.ADD_FRIEND, list);

		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class,
				new Response.Listener<BaseResponse>() {
					@Override
					public void onResponse(BaseResponse response) {
						if ("000000".equals(response.getCode())) {
							MyToast.makeText(PeronalDetialActivity.this, R.string.add_friend2, Toast.LENGTH_LONG).show();
						} else {
							NetStatusHandUtil.getInstance().handStatus(PeronalDetialActivity.this, response.getCode(), response.getMessage());
						}
					}
				}, getErrorListener());
		executeRequest(fastJsonRequest);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			LogUtil.d(TAG, extras.toString());
			friendsuserid = getIntent().getStringExtra("friendsuserid");
			professionalStatus = (ProfessionalStatus) getIntent().getSerializableExtra("professionalStatus");
		} else {
			LogUtil.i(TAG, "暂无好友信息");
			MyToast.makeText(PeronalDetialActivity.this, R.string.add_friend3, Toast.LENGTH_LONG).show();
		}
		userInfoPreferences = new UserInfoPreferences(this);
		if (!Constants.YOUKE.equals(preferences.getUserStatus())) {// 用户如果是游客，则不加入统计
			friendsVisitStatistical();// 朋友被查看统计接口
		}

		if (professionalStatus == null) {
			doGetFriend();
		} else {
			userId = professionalStatus.getUserId();
		}

	}

	boolean die = false;

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		EventBus.getDefault().register(this);
		mVerticalLine.setVisibility(View.VISIBLE);
		mHerEmail.setVisibility(View.VISIBLE);
		title.getBackground().setAlpha(0);
		mHerEmail.setOnClickListener(this);
		back.setOnClickListener(this);
		scview.setScrollViewListener(this);// 设置滑动监听，若为死者界面则不设置，且设置title背景为黑色
		title_dot_ll.setVisibility(viewpager_banner.VISIBLE);
		title_dot_ll.setOnClickListener(this);
		her_name_btn.setOnClickListener(this);
		her_nice_btn.setOnClickListener(this);
		her_cardid_btn.setOnClickListener(this);
		her_gender_btn.setOnClickListener(this);
		her_birthday_btn.setOnClickListener(this);
		her_blood_btn.setOnClickListener(this);
		her_height_btn.setOnClickListener(this);
		her_weight_btn.setOnClickListener(this);
		her_contact_btn.setOnClickListener(this);
		her_feelings_btn.setOnClickListener(this);
		her_degrees_btn.setOnClickListener(this);
		her_professional_btn.setOnClickListener(this);
		her_emergencyContact_btn.setOnClickListener(this);
		her_companyAddress_btn.setOnClickListener(this);
		her_homeAddress_btn.setOnClickListener(this);
		her_life_btn.setOnClickListener(this);
		rl_death.setOnClickListener(this);
		her_talks.setOnClickListener(this);
		her_albums.setOnClickListener(this);
		her_videos.setOnClickListener(this);
		if (professionalStatus != null)
			setUserValue(professionalStatus);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		head.measure(w, h);
		head_height = head.getMeasuredHeight();
		scal = MathUtils.div(255, head_height);
		timer = new Timer();// 初始化计时器

		viewpager_banner.setPageTransformer(true, new DepthPageTransformer());
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager_banner.getContext(), new AccelerateInterpolator());
			field.set(viewpager_banner, scroller);
			scroller.setmDuration(500);
		} catch (Exception e) {
		}
	}

	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 500;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}
	}

	// viewpage切换动画
	private class DepthPageTransformer implements ViewPager.PageTransformer {

		private static final float MIN_SCALE = 0.70f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);

				// Counteract the default slide transition
				view.setTranslationX(pageWidth * -position);
				// Scale the page down (between MIN_SCALE and 1)
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		receiver = new PaySuccessReceiver();
		IntentFilter filter = new IntentFilter(Constants.PAY_SUCCESS_ACTION);
		receiver.setHandler(mHandler);
		registerReceiver(receiver, filter);

	}

	private Timer timer;// 记时器
	private ChangeImageTask task;// 图片轮询任务

	@Override
	public void onResume() {
		if (task == null) {
			task = new ChangeImageTask();
			timer.schedule(task, 3000, 3000);
		}
		super.onResume();
	}

	private boolean toRight;

	/**
	 * 轮换图片
	 * 
	 * @author tianliang.zhou
	 * 
	 */
	// private int pageCount = 3;// 焦点图的总个数

	private class ChangeImageTask extends TimerTask {
		@Override
		public void run() {
			if (personalDetialBanerPics.size() > 1) {
				int currentItem = viewpager_banner.getCurrentItem();
				if (currentItem == 0) {
					toRight = true;
				} else if (currentItem == personalDetialBanerPics.size() - 1) {
					toRight = false;
				}
				currentPageIndex = toRight ? currentItem + 1 : currentItem - 1;
				// currentPageIndex = (currentPageIndex + 1) %
				// personalDetialBanerPics.size();
				mHandler.obtainMessage(CHANGE_IMAGE).sendToTarget();
			}
		}

	};

	private final int CHANGE_IMAGE = 0x01;// 切换图片信息标识
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CHANGE_IMAGE:
				LogUtil.i(TAG, "currentPageIndex个人中心" + currentPageIndex);
				viewpager_banner.setCurrentItem(currentPageIndex);
				break;
			case PayFactory.CASE_PAY_FOR_LOOK:
				doGetFriend();
				break;
			default:
				break;
			}
		}

	};
	private UserInfoPreferences userInfoPreferences;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.widget.MyScrollView.ScrollViewListener#
	 * onScrollChanged(com.ywb.earthman.app.widget.MyScrollView, int, int, int,
	 * int)
	 */
	@Override
	public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
		if (y <= head_height) {
			title.getBackground().setAlpha((int) MathUtils.mul(scal, y));
		} else {
			title.getBackground().setAlpha(255);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		unregisterReceiver(receiver);
		EventBus.getDefault().unregister(this);
		super.onDestroy();

	}

	private String nice;
	private String DIE_STR = "死亡";

	/**
	 * 朋友被查看统计接口
	 */
	private void friendsVisitStatistical() {
		if (friendsuserid == null || friendsuserid.equals("null")) {
			friendsuserid = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
		}
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(friendsuserid);
		list.add(userInfoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.FRIENDS_VISIT, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
			}
		}, getErrorListener()));
	}

	private void doGetFriend() {
		myLoadingDialog.show();
		if (friendsuserid == null || friendsuserid.equals("null")) {
			friendsuserid = getIntent().getStringExtra(Extras.EXTRA_UNIQUEID);
		}
		ArrayList<Object> list = new ArrayList<Object>();
		SharePreferenceUtil.init(PeronalDetialActivity.this, Constants.USER_INFO_TABLE);
		list.add(userInfoPreferences.getUserID());
		list.add(friendsuserid);
		list.add(userInfoPreferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(PeronalDetialActivity.this, HttpUrlConstants.FRIEND_INFO, list);
		LogUtil.i(TAG, getUrl);
		executeRequest(new FastJsonRequest<PersonalInfo>(Method.GET, getUrl, PersonalInfo.class, new Response.Listener<PersonalInfo>() {
			@Override
			public void onResponse(PersonalInfo response) {
				myLoadingDialog.dismiss();
				if ("000000".equals(response.getCode())) {
					professionalStatus = response.getResult();
					userId = professionalStatus.getUserId();
					if (professionalStatus == null) {
						return;
					}
					if (personalDetialBanerPics != null) {
						personalDetialBanerPics.clear();
					}
					setUserValue(professionalStatus);
					isFriend = professionalStatus.getIsFriend();
					// personalDetialBanerPics.addAll(new
					// ArrayList<String>(Arrays.asList(response.getResult().getBanners().split(","))));
					// pagerAdapter = new
					// ImgViewPagerAdapter(PeronalDetialActivity.this,
					// PERSONAL_BANAER_TYPE, personalDetialBanerPics);
					// viewpager_banner.setAdapter(pagerAdapter);
					// // pagerAdapter.notifyDataSetChanged();
					// her_talks.setText(String.format(getString(R.string.dynamic_num),
					// professionalStatus.getTalks()));
					// her_albums.setText(String.format(getString(R.string.album_num),
					// professionalStatus.getAlbums()));
					// her_videos.setText(String.format(getString(R.string.video_num),
					// professionalStatus.getVideos()));
					// remarks = professionalStatus.getRemarks();
					// if (!TextUtils.isEmpty(professionalStatus.getRemarks()))
					// {
					// title_name_tv.setText(professionalStatus.getRemarks());
					// } else {
					// title_name_tv.setText(professionalStatus.getNice());
					// }
					// // 用户状态判定 status：账户状态[未激活，已激活，死亡]
					// if (DIE_STR.equals(professionalStatus.getStatus())) {
					// rl_death.setVisibility(View.VISIBLE);
					// }
					//
					// // 真实姓名
					// name = professionalStatus.getRealName();
					// her_name_btn.setVisibility(isShowPayBtn(professionalStatus.getRealNameStatus(),
					// name) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(name)) {
					// her_name.setText("****");
					// } else {
					// her_name.setText(name);
					// }
					// nice = professionalStatus.getNice();
					// her_nice_btn.setVisibility(isShowPayBtn(professionalStatus.getNiceStatus(),
					// nice) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(nice)) {
					// her_nice.setText("****");
					// } else {
					// her_nice.setText(nice);
					// }
					// // 地球人id
					// String cardid = professionalStatus.getCardId();
					// her_cardid.setText(cardid);
					// her_cardid_btn.setVisibility(View.INVISIBLE);
					// // 性别
					// String gender = professionalStatus.getGender();
					// her_gender_btn.setVisibility(isShowPayBtn(professionalStatus.getGenderStatus(),
					// gender) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(gender)) {
					// her_gender.setText("****");
					// } else {
					// her_gender.setText(gender);
					// }
					// // 生日
					// String birthday = professionalStatus.getBirthday();
					// her_birthday_btn.setVisibility(isShowPayBtn(professionalStatus.getBirthdayStatus(),
					// birthday) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(birthday)) {
					// her_birthday.setText("****");
					// } else {
					// if (birthday.contains("-")) {
					// her_birthday.setText(birthday);
					// } else {
					// long birthdayL = Long.parseLong(birthday);
					// her_birthday.setText(MyStringUtils.mTimeFormatData(birthdayL));
					// }
					// }
					// // 血型
					// String blood = professionalStatus.getBlood();
					// her_blood_btn.setVisibility(isShowPayBtn(professionalStatus.getBloodStatus(),
					// blood) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(blood)) {
					// her_blood.setText("****");
					// } else {
					// her_blood.setText(blood);
					// }
					// // 身高
					// String height = professionalStatus.getHeight();
					// her_height_btn.setVisibility(isShowPayBtn(professionalStatus.getHeightStatus(),
					// height) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(height)) {
					// her_height.setText("****");
					// } else {
					// her_height.setText(height + "cm");
					// }
					// // 体重
					// String weight = professionalStatus.getWeight();
					// her_weight_btn.setVisibility(isShowPayBtn(professionalStatus.getWeightStatus(),
					// weight) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(weight)) {
					// her_weight.setText("****");
					// } else {
					// her_weight.setText(weight + "kg");
					// }
					// // 联系方式
					// String contact = professionalStatus.getContact();
					// her_contact_btn.setVisibility(isShowPayBtn(professionalStatus.getContactStatus(),
					// contact) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(contact)) {
					// her_contact.setText("****");
					// } else {
					// her_contact.setText(contact);
					// }
					// // 情感状态
					// String feelings = professionalStatus.getFeelings();
					// her_feelings_btn.setVisibility(isShowPayBtn(professionalStatus.getFeelingsStatus(),
					// feelings) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(feelings)) {
					// her_feelings.setText("****");
					// } else {
					// her_feelings.setText(feelings);
					// }
					// // 学历
					// String degrees = professionalStatus.getDegrees();
					// her_degrees_btn.setVisibility(isShowPayBtn(professionalStatus.getDegreesStatus(),
					// degrees) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(degrees)) {
					// her_degrees.setText("****");
					// } else {
					// her_degrees.setText(degrees);
					// }
					// // 职业
					// String professional =
					// professionalStatus.getProfessional();
					// her_professional_btn.setVisibility(isShowPayBtn(professionalStatus.getProfessionalStatus(),
					// professional) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(professional)) {
					// her_professional.setText("****");
					// } else {
					// her_professional.setText(professional);
					// }
					// // 紧急联系人
					// String emergencyContact =
					// professionalStatus.getEmergencyContact();
					// her_emergencyContact_btn.setVisibility(isShowPayBtn(professionalStatus.getEmergencyContactStatus(),
					// emergencyContact) ? View.VISIBLE
					// : View.INVISIBLE);
					// if (isHideText(emergencyContact)) {
					// her_emergencyContact.setText("****");
					// } else {
					// her_emergencyContact.setText(emergencyContact);
					// }
					// // 工作地点
					// String companyAddress =
					// professionalStatus.getCompanyAddress();
					// her_companyAddress_btn.setVisibility(isShowPayBtn(professionalStatus.getCompanyAddressStatus(),
					// companyAddress) ? View.VISIBLE
					// : View.INVISIBLE);
					// if (isHideText(companyAddress)) {
					// her_companyAddress.setText("****");
					// } else {
					// her_companyAddress.setText(companyAddress);
					// }
					// // 籍贯
					// String homeAddress = professionalStatus.getHomeAddress();
					// her_homeAddress_btn.setVisibility(isShowPayBtn(professionalStatus.getHomeAddressStatus(),
					// homeAddress) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(homeAddress)) {
					// her_homeAddress.setText("****");
					// } else {
					// her_homeAddress.setText(homeAddress);
					// }
					// // 个性签名
					// String life = professionalStatus.getLife();
					// her_life_btn.setVisibility(isShowPayBtn(professionalStatus.getLifeStatus(),
					// life) ? View.VISIBLE : View.INVISIBLE);
					// if (isHideText(life)) {
					// her_life.setText("****");
					// } else {
					// her_life.setText(life);
					// }
				} else {
					NetStatusHandUtil.getInstance().handStatus(PeronalDetialActivity.this, response.getCode(), response.getMessage());

				}
			}
		}, getErrorListener()));
	}

	public void onEventMainThread(ModifyMark event) {
		title_name_tv.setText(event.getRemark());
	}

	/**
	 * 
	 * showPrivacy(是否显示支付按钮)
	 * 
	 * @param status
	 *            void
	 * @exception
	 */
	private boolean isShowPayBtn(String status, String text) {
		if (text != null) {
			return false;
		}
		if (!TextUtils.isEmpty(status) && Constants.PERSONINFO_PAY == Integer.parseInt(status)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * isHideText(是否隐藏文本，以"****"代替)
	 * 
	 * @param status
	 * @return
	 *         boolean
	 * @exception
	 */
	private boolean isHideText(String text) {
		if (text == null) {
			return true;
		}
		return false;
	}

	private void setUserValue(ProfessionalStatus professionalStatus) {
		personalDetialBanerPics.addAll(new ArrayList<String>(Arrays.asList(professionalStatus.getBanners().split(","))));
		pagerAdapter = new ImgViewPagerAdapter(PeronalDetialActivity.this, PERSONAL_BANAER_TYPE, personalDetialBanerPics);
		viewpager_banner.setAdapter(pagerAdapter);
		// pagerAdapter.notifyDataSetChanged();
		her_talks.setText(String.format(getString(R.string.dynamic_num), professionalStatus.getTalks()));
		her_albums.setText(String.format(getString(R.string.album_num), professionalStatus.getAlbums()));
		her_videos.setText(String.format(getString(R.string.video_num), professionalStatus.getVideos()));
		remarks = professionalStatus.getRemarks();
		if (!TextUtils.isEmpty(professionalStatus.getRemarks())) {
			title_name_tv.setText(professionalStatus.getRemarks());
		} else {
			title_name_tv.setText(professionalStatus.getNice());
		}
		// 用户状态判定 status：账户状态[未激活，已激活，死亡]
		if (DIE_STR.equals(professionalStatus.getStatus())) {
			rl_death.setVisibility(View.VISIBLE);
		}

		// 真实姓名
		name = professionalStatus.getRealName();
		her_name_btn.setVisibility(isShowPayBtn(professionalStatus.getRealNameStatus(), name) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(name)) {
			her_name.setText("****");
		} else {
			her_name.setText(name);
		}
		nice = professionalStatus.getNice();
		her_nice_btn.setVisibility(isShowPayBtn(professionalStatus.getNiceStatus(), nice) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(nice)) {
			her_nice.setText("****");
		} else {
			her_nice.setText(nice);
		}
		// 地球人id
		String cardid = professionalStatus.getCardId();
		her_cardid.setText(cardid);
		her_cardid_btn.setVisibility(View.INVISIBLE);
		// 性别
		String gender = professionalStatus.getGender();
		her_gender_btn.setVisibility(isShowPayBtn(professionalStatus.getGenderStatus(), gender) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(gender)) {
			her_gender.setText("****");
		} else {
			her_gender.setText(gender);
		}
		// 生日
		String birthday = professionalStatus.getBirthday();
		her_birthday_btn.setVisibility(isShowPayBtn(professionalStatus.getBirthdayStatus(), birthday) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(birthday)) {
			her_birthday.setText("****");
		} else {
			if (birthday.contains("-")) {
				her_birthday.setText(birthday);
			} else {
				long birthdayL = Long.parseLong(birthday);
				her_birthday.setText(MyStringUtils.mTimeFormatData(birthdayL));
			}
		}
		// 血型
		String blood = professionalStatus.getBlood();
		her_blood_btn.setVisibility(isShowPayBtn(professionalStatus.getBloodStatus(), blood) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(blood)) {
			her_blood.setText("****");
		} else {
			her_blood.setText(blood);
		}
		// 身高
		String height = professionalStatus.getHeight();
		her_height_btn.setVisibility(isShowPayBtn(professionalStatus.getHeightStatus(), height) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(height)) {
			her_height.setText("****");
		} else {
			her_height.setText(height + "cm");
		}
		// 体重
		String weight = professionalStatus.getWeight();
		her_weight_btn.setVisibility(isShowPayBtn(professionalStatus.getWeightStatus(), weight) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(weight)) {
			her_weight.setText("****");
		} else {
			her_weight.setText(weight + "kg");
		}
		// 联系方式
		String contact = professionalStatus.getContact();
		her_contact_btn.setVisibility(isShowPayBtn(professionalStatus.getContactStatus(), contact) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(contact)) {
			her_contact.setText("****");
		} else {
			her_contact.setText(contact);
		}
		// 情感状态
		String feelings = professionalStatus.getFeelings();
		her_feelings_btn.setVisibility(isShowPayBtn(professionalStatus.getFeelingsStatus(), feelings) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(feelings)) {
			her_feelings.setText("****");
		} else {
			her_feelings.setText(feelings);
		}
		// 学历
		String degrees = professionalStatus.getDegrees();
		her_degrees_btn.setVisibility(isShowPayBtn(professionalStatus.getDegreesStatus(), degrees) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(degrees)) {
			her_degrees.setText("****");
		} else {
			her_degrees.setText(degrees);
		}
		// 职业
		String professional = professionalStatus.getProfessional();
		her_professional_btn.setVisibility(isShowPayBtn(professionalStatus.getProfessionalStatus(), professional) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(professional)) {
			her_professional.setText("****");
		} else {
			her_professional.setText(professional);
		}
		// 紧急联系人
		String emergencyContact = professionalStatus.getEmergencyContact();
		her_emergencyContact_btn.setVisibility(isShowPayBtn(professionalStatus.getEmergencyContactStatus(), emergencyContact) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(emergencyContact)) {
			her_emergencyContact.setText("****");
		} else {
			her_emergencyContact.setText(emergencyContact);
		}
		// 工作地点
		String companyAddress = professionalStatus.getCompanyAddress();
		her_companyAddress_btn.setVisibility(isShowPayBtn(professionalStatus.getCompanyAddressStatus(), companyAddress) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(companyAddress)) {
			her_companyAddress.setText("****");
		} else {
			her_companyAddress.setText(companyAddress);
		}
		// 籍贯
		String homeAddress = professionalStatus.getHomeAddress();
		her_homeAddress_btn.setVisibility(isShowPayBtn(professionalStatus.getHomeAddressStatus(), homeAddress) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(homeAddress)) {
			her_homeAddress.setText("****");
		} else {
			her_homeAddress.setText(homeAddress);
		}
		// 个性签名
		String life = professionalStatus.getLife();
		her_life_btn.setVisibility(isShowPayBtn(professionalStatus.getLifeStatus(), life) ? View.VISIBLE : View.INVISIBLE);
		if (isHideText(life)) {
			her_life.setText("****");
		} else {
			her_life.setText(life);
		}
	}

}
