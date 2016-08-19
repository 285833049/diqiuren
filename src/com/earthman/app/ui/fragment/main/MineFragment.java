package com.earthman.app.ui.fragment.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.ImgViewPagerAdapter;
import com.earthman.app.adapter.MineItemAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.MineItemInfo;
import com.earthman.app.bean.MineUpdateInfo;
import com.earthman.app.bean.PersonalInfo;
import com.earthman.app.bean.PersonalInfo.ProfessionalStatus;
import com.earthman.app.constant.Constent;
import com.earthman.app.contactsave.ContactSaveActivity;
import com.earthman.app.enums.MineItemType;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.nim.login.LogoutHelper;
import com.earthman.app.ui.activity.MainActivity;
import com.earthman.app.ui.activity.album.AlbumListActivity;
import com.earthman.app.ui.activity.dynamic.MineMyDynamic;
import com.earthman.app.ui.activity.login.LoginActivity;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.ui.activity.mine.AccountActivity;
import com.earthman.app.ui.activity.mine.AccountFamilyPhone;
import com.earthman.app.ui.activity.mine.ActivateAccountActivity;
import com.earthman.app.ui.activity.mine.MineActivationFriend;
import com.earthman.app.ui.activity.mine.MinePhoneRecharge;
import com.earthman.app.ui.activity.mine.MineSetting;
import com.earthman.app.ui.activity.mine.MineShareActivity;
import com.earthman.app.ui.activity.mine.MineTransfer;
import com.earthman.app.ui.activity.mine.ModifyPersonalInfo;
import com.earthman.app.ui.activity.mine.TransactionRecordListActivity;
import com.earthman.app.ui.activity.mine.WhoISeeActivity;
import com.earthman.app.ui.activity.video.VideoListActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.SharePreferenceUtil;
import com.earthman.app.widget.InviteFriendDialog;
import com.earthman.app.widget.QrCodeDialog;
import com.earthman.app.widget.RoundCornerImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou 日期：2016-2-24 上午11:09:58 描述：（）
 */
public class MineFragment extends BaseFragment implements OnItemClickListener {
	// ------------------------------head
	private ImgViewPagerAdapter pagerAdapter;
	private int currentPageIndex;
	private int PERSONAL_BANAER_TYPE = 1;// 有透明的改变的bannaer
	ViewPager viewpager_banner;
	TextView mine_youxiaoqi;
	TextView my_firend_text;
	TextView who_i_see_text;
	TextView who_see_me;
	TextView friend_list_item_name;
	RoundCornerImageView mine_img_user_icon;
	LinearLayout who_see_me_layout;
	// ---------------------------------
	RelativeLayout mice_personal_data_layout;
	LinearLayout who_i_see;
	// ------------------------------------
	private List<MineItemInfo> mItemList;
	@ViewInject(R.id.lv_content)
	private ListView mLvContent;
	// ------------------------------------
	@ViewInject(R.id.mine_login_out)
	Button login_out;
	private RadioButton her_albums;
	private RadioButton her_videos;
	TextView mine_cardid;
	private InviteFriendDialog inviteFriendDialog;
	LinearLayout my_friend_layout;
	private YwbImageLoader loader;

	private QrCodeDialog dialog;
	private RadioButton her_talks;
	private int MYDYNAMIC = 2;
	private int MINEDYNAMIC = 2;
	ProfessionalStatus result;
	private ArrayList<String> mBanerPics;// 轮播图的图片
	private View verticalline;
	private RadioButton her_email;

	@Override
	protected View createView() {
		EventBus.getDefault().register(this);
		View view = LayoutInflater.from(activity).inflate(R.layout.mine_fragment, null);
		ViewUtils.inject(this, view);
		return view;

	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#initView(android.view.View)
	 */
	@Override
	protected void initView(View convertView) {

		mice_personal_data_layout.setOnClickListener(this);
		who_i_see.setOnClickListener(this);
		login_out.setOnClickListener(this);
		who_see_me_layout.setOnClickListener(this);
		my_friend_layout.setOnClickListener(this);
		timer = new Timer();// 初始化计时器
		mBanerPics = new ArrayList<String>();

		viewpager_banner.setPageTransformer(true, new DepthPageTransformer());
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager_banner.getContext(), new AccelerateInterpolator());
			field.set(viewpager_banner, scroller);
			scroller.setmDuration(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		verticalline.setVisibility(View.VISIBLE);
		her_email.setVisibility(View.VISIBLE);

		mLvContent.setOnItemClickListener(this);
		her_albums.setOnClickListener(this);
		her_videos.setOnClickListener(this);
		her_talks.setOnClickListener(this);
		her_email.setOnClickListener(this);
	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#initData()
	 */
	@Override
	protected void initData() {
		userInfoPreferences = new UserInfoPreferences(activity);
		loader = new YwbImageLoader();
		getuserinfo();
		// InitDataFromNet();
		// -----------------------------------------------------------initHead
		View mineHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.mine_head, null);
		mine_cardid = (TextView) mineHeadView.findViewById(R.id.mine_cardid);
		mice_personal_data_layout = (RelativeLayout) mineHeadView.findViewById(R.id.mice_personal_data_layout);
		who_i_see = (LinearLayout) mineHeadView.findViewById(R.id.who_i_see);
		viewpager_banner = (ViewPager) mineHeadView.findViewById(R.id.personal_viewpager_banner);
		mine_youxiaoqi = (TextView) mineHeadView.findViewById(R.id.mine_youxiaoqi);
		my_firend_text = (TextView) mineHeadView.findViewById(R.id.my_firend_text);
		who_i_see_text = (TextView) mineHeadView.findViewById(R.id.who_i_see_text);
		who_see_me_layout = (LinearLayout) mineHeadView.findViewById(R.id.who_see_me_layout);
		who_see_me = (TextView) mineHeadView.findViewById(R.id.who_see_me);
		friend_list_item_name = (TextView) mineHeadView.findViewById(R.id.friend_list_item_name);
		mine_img_user_icon = (RoundCornerImageView) mineHeadView.findViewById(R.id.mine_img_user_icon);
		my_friend_layout = (LinearLayout) mineHeadView.findViewById(R.id.my_friend_layout);
		her_albums = (RadioButton) mineHeadView.findViewById(R.id.her_albums);
		her_talks = (RadioButton) mineHeadView.findViewById(R.id.her_talks);
		her_videos = (RadioButton) mineHeadView.findViewById(R.id.her_videos);
		verticalline = mineHeadView.findViewById(R.id.verticalline);
		her_email = (RadioButton) mineHeadView.findViewById(R.id.her_email);

		// -----------------------------------------------------------initBody
		mItemList = new ArrayList<MineItemInfo>();
		mItemList.add(new MineItemInfo(MineItemType.MINE_ACCOUNT, R.drawable.zhanghu, getString(R.string.myaccount), true));
		mItemList.add(new MineItemInfo(MineItemType.MIME_QRCODE, R.drawable.erweima, getString(R.string.myqrcode), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_TRANSACTION_RECORDS, R.drawable.jiaoyixiangqing, getString(R.string.mine_chage), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_TRANSFER, R.drawable.zhuanzhang, getString(R.string.mine_zhuanzhang), false));//#
		mItemList.add(new MineItemInfo(MineItemType.MINE_RECHARGE, R.drawable.chongzhi, getString(R.string.mine_huafei), false));

		mItemList.add(new MineItemInfo(MineItemType.MINE_INSTEAD_FRIEND_REGISTER, R.drawable.zhuce, getString(R.string.mine_daitizhuce), true));
		mItemList.add(new MineItemInfo(MineItemType.MINE_INSTEAD_FRIEND_ACTIVATE, R.drawable.wojihuo, getString(R.string.mine_daitijihuo), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_ACTIVATE_ACCOUNT, R.drawable.jihuo, getString(R.string.mine_jihuo), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_INVITE_REGISTER, R.drawable.yaoqing, getString(R.string.mine_yaoqing), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_FAMILY_PHONE, R.drawable.qinqinghao_h, getString(R.string.mine_family_phone), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_SHAREER, R.drawable.fenxiangzhe, getString(R.string.mine_share), false));

		mItemList.add(new MineItemInfo(MineItemType.MINE_EVERYDAY_RECORD, R.drawable.jishiben, getString(R.string.mine_jishi), true));
		mItemList.add(new MineItemInfo(MineItemType.MINE_COLLECT_ARTICLE, R.drawable.wenzi, getString(R.string.mine_zhengwen), false));
		mItemList.add(new MineItemInfo(MineItemType.MINE_HEALTH_MANAGEMENT_CENTER, R.drawable.jiankang, getString(R.string.mine_jiankang), false));

		mItemList.add(new MineItemInfo(MineItemType.MINE_SETTING, R.drawable.shezhi, getString(R.string.mine_setting), true));
		mItemList.add(new MineItemInfo(MineItemType.MINE_CONTACTS_BACKUP, R.drawable.tongbu, getString(R.string.mine_tongxunlubackup), false));
		// -----------------------------------------------------------user
		mLvContent.addHeaderView(mineHeadView);
		mLvContent.setAdapter(new MineItemAdapter(mItemList));

	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		if (!userInfoPreferences.getIsThirdLogin()) {
			friend_list_item_name.setText(userInfoPreferences.getUsernice());
			loader.loadImage(userInfoPreferences.getUserIco(), mine_img_user_icon);
		} else {
			getuserinfo();
		}
	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.mice_personal_data_layout:// 个人资料
			if (!checkUserStatus()) {
				return;
			}
			Intent intent = new Intent(activity, ModifyPersonalInfo.class);
			// intent.putExtra("result", result);
			startActivityForResult(intent, Constants.REQUEST_SETPERSONDETAIL);
			break;
		case R.id.who_i_see:// 我看过谁
			if (!checkUserStatus()) {
				return;
			}
			Intent intentwks = new Intent(activity, WhoISeeActivity.class);
			intentwks.putExtra("type", 1);
			startActivity(intentwks);
			break;
		case R.id.who_see_me_layout:// 谁看过我
			if (!checkUserStatus()) {
				return;
			}
			Intent intentwsk = new Intent(activity, WhoISeeActivity.class);
			intentwsk.putExtra("type", 0);
			startActivity(intentwsk);
			break;
		case R.id.mine_login_out:// 退出登录清除相关数据
			doLogout();
			break;
		case R.id.my_friend_layout:// 跳转到好友界面
			if (!checkUserStatus()) {
				return;
			}
			MainActivity mianActivity = (MainActivity) getActivity();
			mianActivity.gotoFragment(mianActivity.mContactFragment);
			mianActivity.mTabAdfapter.setSelectedTabIndex(1);
			mianActivity.mTabAdfapter.notifyDataSetChanged();
			break;
		case R.id.her_albums:// 相册
			if (!checkUserStatus()) {
				return;
			}
			Intent albumIntent = new Intent(this.activity, AlbumListActivity.class);
			albumIntent.putExtra("pageType", AlbumListActivity.SEE_MINE_ALBUM);
			albumIntent.putExtra("friendid", 0);
			startActivityForResult(albumIntent, Constants.REQUEST_SETPERSONDETAIL);
			break;
		case R.id.her_videos:// 视频
			if (!checkUserStatus()) {
				return;
			}
			Intent videoIntent = new Intent(this.activity, VideoListActivity.class);
			videoIntent.putExtra("pageType", VideoListActivity.SEE_MINE_VIDEO);
			videoIntent.putExtra("friendid", 0);
			startActivityForResult(videoIntent, Constants.REQUEST_SETPERSONDETAIL);
			break;
		case R.id.her_talks:// 我的动态
			if (!checkUserStatus()) {
				return;
			}
			Intent dynamicintent = new Intent(this.activity, MineMyDynamic.class);
			dynamicintent.putExtra("pagetype", MINEDYNAMIC);
			startActivity(dynamicintent);
			break;
		case R.id.her_email:// 邮箱入口
			if (!checkUserStatus()) {
				return;
			}
			Intent emailIntent = new Intent(Intent.ACTION_VIEW);
			emailIntent.setData(Uri.parse(HttpUrlConstants.EMAIL_URL));
			startActivity(emailIntent);
		default:
			break;
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

	private Timer timer;// 记时器
	private ChangeImageTask task;// 图片轮询任务

	// private boolean isBannerChange;

	@Override
	public void onResume() {
		// isBannerChange=true;
		if (task == null) {
			task = new ChangeImageTask();
			timer.schedule(task, 3000, 3000);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		// LogUtil.i(TAG, "isBannerChange"+isBannerChange);
		// isBannerChange=false;
		if (task != null) {
			task.cancel();
			task = null;
		}
		super.onPause();
	}

	/**
	 * 获取个人信息
	 */
	private void getuserinfo() {
		ArrayList<Object> list = new ArrayList<Object>();
		SharePreferenceUtil.init(activity, Constants.USER_INFO_TABLE);
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		showLoading();
		String getUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_FRIEND_INFO, list);
		executeRequest(new FastJsonRequest<PersonalInfo>(Method.GET, getUrl, PersonalInfo.class, new Response.Listener<PersonalInfo>() {

			@Override
			public void onResponse(PersonalInfo response) {
				Log.d("xianyong", "setuser:" + response);
				if ("000000".equals(response.getCode())) {
					result = response.getResult();
					if (result == null) {
						return;
					}
					// 轮播图片
					if (mBanerPics != null) {
						mBanerPics.clear();
					}
					mBanerPics.addAll(new ArrayList<String>(Arrays.asList(result.getBanners().split(","))));
					pagerAdapter = new ImgViewPagerAdapter(activity, PERSONAL_BANAER_TYPE, mBanerPics);
					viewpager_banner.setAdapter(pagerAdapter);
					pagerAdapter = new ImgViewPagerAdapter(activity, PERSONAL_BANAER_TYPE, mBanerPics);
					viewpager_banner.setAdapter(pagerAdapter);
					// pagerAdapter.notifyDataSetChanged();
					loader.loadImage(result.getAvatar(), mine_img_user_icon, R.drawable.user_avatar);
					my_firend_text.setText("" + result.getFriends());
					who_i_see_text.setText("" +result.getVisitme() );
					who_see_me.setText("" +result.getMyvisit() );
					friend_list_item_name.setText(result.getNice());
					if (Constants.SHARE_ACTIVED.equals(userInfoPreferences.getUserStatus())) {
						mine_youxiaoqi.setText(Html.fromHtml(String.format(getString(R.string.mine_youxiaoqi), result.getDuedate())));
					}
					// 显示动态、相册、视频个数
					her_talks.setText(String.format(getString(R.string.dynamic_num), response.getResult().getTalks()));
					her_albums.setText(String.format(getString(R.string.album_num), response.getResult().getAlbums()));
					her_videos.setText(String.format(getString(R.string.video_num), response.getResult().getVideos()));
					mine_cardid.setText(result.getCardId());
					// userInfoPreferences.setUserToken(result.getToken());
					userInfoPreferences.setUserDueDate(result.getDuedate());
					userInfoPreferences.setUserIco(result.getAvatar());// 用户头像
					userInfoPreferences.setUsercardId(result.getCardId());// 用户地球人id
					userInfoPreferences.setUserrealName(result.getRealName());// /姓名
					userInfoPreferences.setUsernice(result.getNice());// 昵称
					userInfoPreferences.setUserGender(result.getGender());// 性别
					userInfoPreferences.setUserBirthday(result.getBirthday());// 生日
					userInfoPreferences.setUserBlood(result.getBlood());// 血型
					userInfoPreferences.setUserHeight(result.getHeight());// 身高
					userInfoPreferences.setUserWeight(result.getWeight());// 体重
					userInfoPreferences.setUserFeelings(result.getFeelings());// 情感状态
					userInfoPreferences.setUserDegrees(result.getDegrees());// 学历
					userInfoPreferences.setUserProfessional(result.getProfessional());// 职业
					userInfoPreferences.setUseremergencyContact(result.getEmergencyContact());// 紧急联系人
					userInfoPreferences.setUserCompanyAddress(result.getCompanyAddress());// 工作地点
					userInfoPreferences.setUserHomeAddress(result.getHomeAddress());// 籍贯
					userInfoPreferences.setUserLife(result.getLife());// 个性签名

				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}

				dismissLoading();
			}
		}, getErrorListener()));
	}

	/**
	 * 
	 * doLogout(这里用一句话描述这个方法的作用)
	 * void
	 * 
	 * @exception
	 */
	private void doLogout() {
		LogoutHelper.logout();
		userInfoPreferences.clearData();
		userInfoPreferences.setIsLogin(true);
		userInfoPreferences.setIsfirstLogin(false);
		startActivity(new Intent(getActivity(), LoginActivity.class));
		getActivity().finish();
	}

	/**
	 * 轮换图片
	 * 
	 * @author tianliang.zhou
	 * 
	 */
	boolean toRight = false;

	private class ChangeImageTask extends TimerTask {

		@Override
		public void run() {
			int currentItem = viewpager_banner.getCurrentItem();
			if (currentItem == 0) {
				toRight = true;
			} else if (currentItem == mBanerPics.size() - 1) {
				toRight = false;
			}
			currentPageIndex = toRight ? currentItem + 1 : currentItem - 1;
			// currentPageIndex = ++currentPageIndex % mBanerPics.size();
			// if (isBannerChange) {
			LogUtil.i(TAG, "Mine轮播图" + currentPageIndex);
			mHandler.obtainMessage(CHANGE_IMAGE).sendToTarget();
			// }
			// if (pageCount > 1) {
			// LogUtil.i("currentPageIndex", "currentPageIndex" + "----->" +
			// currentPageIndex);
			// currentPageIndex = (currentPageIndex + 1) % pageCount;
			//
			// LogUtil.i("currentPageIndex", "currentPageIndex" + "----->" +
			// currentPageIndex);
			// mHandler.obtainMessage(CHANGE_IMAGE).sendToTarget();
			// }
		}

	};

	private final int CHANGE_IMAGE = 0x02;// 切换图片信息标识
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CHANGE_IMAGE:
				viewpager_banner.setCurrentItem(currentPageIndex);

				break;
			default:
				break;
			}
		}

	};
	private UserInfoPreferences userInfoPreferences;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if (position == 0) {
			return;
		}
		MineItemInfo info = mItemList.get(position - 1);
		switch (info.getItemType()) {
		case MINE_ACTIVATE_ACCOUNT: // 激活账户
			if (Constants.YOUKE.equals(userInfoPreferences.getUserStatus())) {
				checkUserStatus();
				return;
			}
			break;
		default:
			if (!checkUserStatus()) {
				return;
			}
		}
		Intent intent = new Intent();
		switch (info.getItemType()) {
		case MINE_ACCOUNT: // 我的账户
			intent.setClass(getActivity(), AccountActivity.class);
			break;
		case MIME_QRCODE:// 二维码
			if (dialog == null) {
				dialog = new QrCodeDialog(activity);
			}
			dialog.showDialog();
			return;
		case MINE_TRANSACTION_RECORDS: // 交易记录
			intent.setClass(getActivity(), TransactionRecordListActivity.class);
			break;
		case MINE_TRANSFER: // 转账
			intent.setClass(getActivity(), MineTransfer.class);
			break;
		case MINE_RECHARGE: // 话费充值
			intent.setClass(getActivity(), MinePhoneRecharge.class);
			break;
		case MINE_INSTEAD_FRIEND_REGISTER:
			intent.putExtra("type", Constent.REGIST_TO_FRIEND);// 代替好友注册
			intent.setClass(getActivity(), RegistActivity.class);
			break;
		case MINE_INSTEAD_FRIEND_ACTIVATE: // 代替好友激活
			intent.setClass(getActivity(), MineActivationFriend.class);
			break;
		case MINE_ACTIVATE_ACCOUNT: // 激活账户
			intent.setClass(getActivity(), ActivateAccountActivity.class);
			break;
		case MINE_INVITE_REGISTER: // 邀请注册
			if (inviteFriendDialog == null) {
				inviteFriendDialog = new InviteFriendDialog(activity, Constants.INVITE_FRIEND_SHARE);
			}
			inviteFriendDialog.showDialog();
			return;

		case MINE_FAMILY_PHONE:// 亲情号
			intent.setClass(getActivity(), AccountFamilyPhone.class);// 亲情号注册
			break;
		case MINE_SHAREER: // 分享者
			intent.setClass(getActivity(), MineShareActivity.class);
			break;
		case MINE_EVERYDAY_RECORD: // 每天记事

			return;
		case MINE_COLLECT_ARTICLE: // 有奖征文

			return;
		case MINE_HEALTH_MANAGEMENT_CENTER: // 健康管理中心

			return;
		case MINE_SETTING: // 设置
			intent.setClass(getActivity(), MineSetting.class);
			break;
		case MINE_CONTACTS_BACKUP: // 通讯录备份
			intent.setClass(getActivity(), ContactSaveActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);

	}

	public void onEvent(MineUpdateInfo event) {
		getuserinfo();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case Constants.REQUEST_SETPERSONDETAIL:
				getuserinfo();
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
