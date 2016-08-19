package com.earthman.app.ui.fragment.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.GetEarthmanTopResponse;
import com.earthman.app.adapter.HomeNavigateAdapter;
import com.earthman.app.adapter.MyGrideViewAdapter;
import com.earthman.app.adapter.RankingAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BannerInfo;
import com.earthman.app.bean.GetEarthManFcResponse;
import com.earthman.app.bean.GetEarthManFcResponse.EarthManFcInfo;
import com.earthman.app.bean.GetHomeVideoResponse;
import com.earthman.app.bean.GetHomeVideoResponse.GetHomeVideoResult.HomeVideo;
import com.earthman.app.bean.HomeDataResult;
import com.earthman.app.bean.HomeDataResult.HomeData.AboutUs;
import com.earthman.app.bean.HomeDataResult.HomeData.Banner;
import com.earthman.app.bean.HomeNaviItem;
import com.earthman.app.bean.RankingInfo;
import com.earthman.app.enums.CarouselImage;
import com.earthman.app.enums.HomeNaviType;
import com.earthman.app.enums.RankingDataType;
import com.earthman.app.eventbusbean.ModifyMark;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.pulltoreflesh.XListView.IXListViewListener;
import com.earthman.app.ui.activity.home.CompanyIntroActivity;
import com.earthman.app.ui.activity.home.VideoRegionActivity;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.ui.activity.news.NewsActivity;
import com.earthman.app.ui.activity.ranking.RankingActivity;
import com.earthman.app.ui.fragment.news.CompanyNewsFragment;
import com.earthman.app.ui.fragment.news.EarthNewsFragment;
import com.earthman.app.ui.fragment.news.WorldNewsFragment;
import com.earthman.app.ui.fragment.publics.PublicBannerFragment;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.HorizontalListView;
import com.earthman.app.widget.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou 日期：2016-2-24 上午10:39:15 描述：（）
 */
public class HomeFragment extends BaseFragment implements IXListViewListener, OnClickListener {

	@ViewInject(R.id.lv_ranking)
	private XListView mLvRanking;
	private RankingAdapter rankAdapter;// 排行榜适配器
	private FragmentManager fragmentManager;
	private FragmentTabHost mTabHost;
	private String[] tabNames;// Tab文字
	private Class<?>[] tabContents;// Tab内容
	private MyGridView main_gv_earthman;
	private MyGrideViewAdapter mGvAdapter;
	private View headView;
	private TextView homeintruduce;
	private HorizontalListView mNaviLayout;
	private List<HomeNaviItem> mNaviList;
	private HomeNavigateAdapter homeNavigateAdapter;
	private ArrayList<Banner> banners;
	private ImageView iv_introduce;
	private ArrayList<EarthManFcInfo> earthManFcInfos;
	private ArrayList<RankingInfo> earthManTops;

	private LinearLayout mLltVideoGetMore;// 视频专区 --查看更多

	// ---------------------------------------------------------------
	@ViewInject(R.id.ll_banner_container)
	private LinearLayout mLlBannerContainer;
	@ViewInject(R.id.ll_video_top_container)
	private LinearLayout mLlVideoTopContainer;
	@ViewInject(R.id.ll_video_left_container)
	private LinearLayout mLlVideoLeftContainer;
	@ViewInject(R.id.ll_video_right_container)
	private LinearLayout mLlVideoRightContainer;
	// ---------------------------------------------------------------
	PublicBannerFragment mFragmentBanner;
	PublicBannerFragment mFragmentVideoTop;
	PublicBannerFragment mFragmentVideoLeft;
	PublicBannerFragment mFragmentVideoRight;
	// -----------------地球人文学8月2号修改------------------------
	private LinearLayout mTvLoveCall;
	private LinearLayout mTvSongFu;
	private LinearLayout mTvPeriodical;
	private LinearLayout mTvStory;
	// -----------------------------------------------------

	private TextView mTvRankingMore;
	private RelativeLayout mHomeIntroduceLayout;

	@Override
	protected View createView() {
		View view = LayoutInflater.from(activity).inflate(R.layout.home_fragment, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void initView(View convertView) {
		EventBus.getDefault().register(this);
		headView = LayoutInflater.from(activity).inflate(R.layout.head_layout, null);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.ll_banner_container, mFragmentBanner = new PublicBannerFragment());
		transaction.commit();

		main_gv_earthman = (MyGridView) headView.findViewById(R.id.main_gv_earthman);
		homeintruduce = (TextView) headView.findViewById(R.id.home_introduce);// 公司介绍
		iv_introduce = (ImageView) headView.findViewById(R.id.iv_introduce);
		mLltVideoGetMore = (LinearLayout) headView.findViewById(R.id.video_get_more);
		mHomeIntroduceLayout = (RelativeLayout) headView.findViewById(R.id.home_introduce_layout);
		mTvRankingMore = (TextView) headView.findViewById(R.id.tv_ranking_more);

		initHomeVideoView();

		mTvLoveCall = (LinearLayout) headView.findViewById(R.id.tv_wx_fh);
		mTvSongFu = (LinearLayout) headView.findViewById(R.id.tv_wx_gc);
		mTvPeriodical = (LinearLayout) headView.findViewById(R.id.tv_wx_qk);
		mTvStory = (LinearLayout) headView.findViewById(R.id.tv_wx_gs);

		mTvLoveCall.setOnClickListener(this);
		mTvSongFu.setOnClickListener(this);
		mTvPeriodical.setOnClickListener(this);
		mTvStory.setOnClickListener(this);

		mTvRankingMore.setOnClickListener(this);
		initNavigate();
		mLvRanking.addExtraHead(headView);
		mLvRanking.setPullLoadEnable(false);
		mLvRanking.setAutoLoadEnable(false);
		mLvRanking.setPullRefreshEnable(true);
		mLvRanking.setXListViewListener(this);

		main_gv_earthman.setSelector(new ColorDrawable(Color.TRANSPARENT));// 取消GridView中Item选中时默认的背景色
		mLvRanking.setAdapter(rankAdapter);
		mTabHost = (FragmentTabHost) headView.findViewById(android.R.id.tabhost);
		mTabHost.setup(activity, fragmentManager, R.id.news_content);
		// 初始化标签
		for (int i = 0; i < tabNames.length; i++) {
			TabSpec mTabSpect = mTabHost.newTabSpec(tabNames[i]).setIndicator(getTabView(tabNames[i]));
			mTabHost.addTab(mTabSpect, tabContents[i], null);
		}

		main_gv_earthman.setAdapter(mGvAdapter);
		main_gv_earthman.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent personalDetialIntent = new Intent(activity, PeronalDetialActivity.class);
				personalDetialIntent.putExtra("friendsuserid", String.valueOf(earthManFcInfos.get(position).getUniqueid()));
				activity.startActivity(personalDetialIntent);
			}
		});
		mLvRanking.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent personalDetialIntent = new Intent(activity, PeronalDetialActivity.class);
				personalDetialIntent.putExtra("friendsuserid", String.valueOf(earthManTops.get(position - 1).getUniqueid()));
				startActivity(personalDetialIntent);
			}
		});
	}

	private void initNavigate() {
		mNaviList = new ArrayList<HomeNaviItem>();
		String[] naviNameArray = getResources().getStringArray(R.array.navigate_names);
		mNaviList.add(new HomeNaviItem(HomeNaviType.CONPANY_INTRO, R.drawable.ic_home_navi_company_intro, naviNameArray[0]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.COMPANY_NEWS, R.drawable.ic_home_navi_company_news, naviNameArray[1]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.EARTHMAN_NEWS, R.drawable.ic_home_navi_earthman_news, naviNameArray[2]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.WORLD_NEWS, R.drawable.ic_home_navi_world_news, naviNameArray[3]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.VEDIO_AREA, R.drawable.iconfont_yingpian, naviNameArray[4]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.EARTHMAN_LITERATURE, R.drawable.iconfont_bookmarks, naviNameArray[5]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.EARTHMAN_PUBLIC_WELFARE, R.drawable.iconfont_kongxin, naviNameArray[6]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.EARTHMAN_MIEN, R.drawable.iconfont_gerenzhongxin, naviNameArray[7]));
		mNaviList.add(new HomeNaviItem(HomeNaviType.RANKING, R.drawable.iconfont_dingdan, naviNameArray[8]));

		mNaviLayout = (HorizontalListView) headView.findViewById(R.id.navigate);
		homeNavigateAdapter = new HomeNavigateAdapter(mNaviList);
		mNaviLayout.setAdapter(homeNavigateAdapter);
		mNaviLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (mNaviList.get(position).getNaviType()) {
				case CONPANY_INTRO:
					Intent intent = new Intent(activity, CompanyIntroActivity.class);
					intent.putExtra("introduceStr", jieshao);
					startActivity(intent);
					break;
				case COMPANY_NEWS:
					startActivity(new Intent(activity, NewsActivity.class).putExtra("index", 0));
					break;
				case EARTHMAN_NEWS:
					startActivity(new Intent(activity, NewsActivity.class).putExtra("index", 1));
					break;
				case WORLD_NEWS:
					startActivity(new Intent(activity, NewsActivity.class).putExtra("index", 2));
					break;
				case VEDIO_AREA:
					startActivity(new Intent(activity, VideoRegionActivity.class));
					break;
				case EARTHMAN_LITERATURE:
				case EARTHMAN_PUBLIC_WELFARE:
				case EARTHMAN_MIEN:
					Toast.makeText(activity, R.string.develop_inform, Toast.LENGTH_SHORT).show();
					break;
				case RANKING:
					startActivity(new Intent(activity, RankingActivity.class));
					break;
				}
			}
		});
	}

	/**
	 * 获取标签
	 * 
	 * @param tabName
	 * @param resId
	 * @return
	 */
	private View getTabView(String tabName) {
		View view = LayoutInflater.from(activity).inflate(R.layout.news_tab_item, null, false);
		TextView tvTabName = (TextView) view.findViewById(R.id.tv_news_tab);
		tvTabName.setText(tabName);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// mFragmentBanner.start();
		// mFragmentVideo1.start();
		// mFragmentVideo2.start();
		// mFragmentVideo3.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		// mFragmentBanner.stop();
		// mFragmentVideo1.stop();
		// mFragmentVideo2.stop();
		// mFragmentVideo3.stop();
	}

	@Override
	protected void initData() {
		earthManTops = new ArrayList<RankingInfo>();
		rankAdapter = new RankingAdapter(false, RankingDataType.RECOMMEND, earthManTops);
		fragmentManager = getChildFragmentManager();
		tabNames = getResources().getStringArray(R.array.news_tabnames);
		tabContents = new Class[] { CompanyNewsFragment.class, EarthNewsFragment.class, WorldNewsFragment.class };
		earthManFcInfos = new ArrayList<GetEarthManFcResponse.EarthManFcInfo>();
		mGvAdapter = new MyGrideViewAdapter(activity, earthManFcInfos);
		infoPreferences = new UserInfoPreferences(activity);
		banners = new ArrayList<Banner>();
		getHomeData();

	}

	private void getHomeData() {
		doGetHomeData();
		doGetHomeVideos();
		doGetEarthManFC();
		doGetEarthmanTop();
	}

	/**
	 * 获取首页数据
	 */
	private void doGetHomeData() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		list.add(HttpUrlConstants.BANNER_POSITION);
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.HOME_DATA, list);
		executeRequest(new FastJsonRequest<HomeDataResult>(Method.GET, loginUrl, HomeDataResult.class, new Response.Listener<HomeDataResult>() {

			@Override
			public void onResponse(HomeDataResult response) {
				if ("000000".equals(response.getCode())) {
					// 首页数据,保存数据
					setDataToUI(response);
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 
	 * doGetHomeVideos(获取首页视频专区数据) void
	 * 
	 * @exception
	 */
	private void doGetHomeVideos() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		list.add(0);
		list.add(0);
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_HOME_VIDEO, list);
		executeRequest(new FastJsonRequest<GetHomeVideoResponse>(Method.GET, loginUrl, GetHomeVideoResponse.class, new Response.Listener<GetHomeVideoResponse>() {

			@Override
			public void onResponse(GetHomeVideoResponse response) {
				if ("000000".equals(response.getCode())) {
					// 首页数据,保存数据
					List<BannerInfo> banners = new ArrayList<BannerInfo>();
					for (HomeVideo video : response.getResult().getVideos()) {
						banners.add(new BannerInfo(video.getImg(), video.getId(), video.getName()));
					}
					try {
						mFragmentVideoTop.setBanners(CarouselImage.VIDEO, banners.subList(0, 3));
						mFragmentVideoLeft.setBanners(CarouselImage.VIDEO, banners.subList(3, 6));
						mFragmentVideoRight.setBanners(CarouselImage.VIDEO, banners.subList(6, banners.size()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					// updateHomeVideo(response.getResult());//#
					// mFragmentVideo1.setBanners(banners);
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	private void initHomeVideoView() {
		FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
		transaction1.replace(R.id.ll_video_top_container, mFragmentVideoTop = new PublicBannerFragment());
		transaction1.commit();

		FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
		transaction2.replace(R.id.ll_video_left_container, mFragmentVideoLeft = new PublicBannerFragment());
		transaction2.commit();

		FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
		transaction3.replace(R.id.ll_video_right_container, mFragmentVideoRight = new PublicBannerFragment());
		transaction3.commit();
	}

	/**
	 * 
	 * doGetEarthManFC(获取地球人风采) void
	 * 
	 * @exception
	 */
	private void doGetEarthManFC() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_EARTHMANFC, list);
		executeRequest(new FastJsonRequest<GetEarthManFcResponse>(Method.GET, loginUrl, GetEarthManFcResponse.class, new Response.Listener<GetEarthManFcResponse>() {

			@Override
			public void onResponse(GetEarthManFcResponse response) {
				if ("000000".equals(response.getCode())) {
					// 更新地球人风采数据
					earthManFcInfos.clear();
					earthManFcInfos.addAll(response.getResult());
					mGvAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 
	 * doGetPaiHang(获取排行) void
	 * 
	 * @exception
	 */
	private void doGetEarthmanTop() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_EARTHMANTOP, list);
		executeRequest(new FastJsonRequest<GetEarthmanTopResponse>(Method.GET, loginUrl, GetEarthmanTopResponse.class, new Response.Listener<GetEarthmanTopResponse>() {

			@Override
			public void onResponse(GetEarthmanTopResponse response) {
				if ("000000".equals(response.getCode())) {
					// 更新地球人风采数据
					earthManTops.clear();
					earthManTops.addAll(response.getResult());
					rankAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 设置数据到界面
	 * 
	 * @param response
	 */
	private void setDataToUI(HomeDataResult response) {
		AboutUs aboutUs = response.getResult().getAboutus();
		jieshao = aboutUs.getContent();
		banners.clear();
		banners.addAll(response.getResult().getBanners());
		List<BannerInfo> banners = new ArrayList<BannerInfo>();
		for (Banner banner : response.getResult().getBanners()) {
			banners.add(new BannerInfo(banner.getImg(), Integer.valueOf(banner.getId()), banner.getTitle()));
		}
		mFragmentBanner.setBanners(CarouselImage.BANNER, banners);
		// pagerAdapter = new HomeBannerAdapter(activity, banners);#
		homeintruduce.setText(Html.fromHtml(jieshao));
		new YwbImageLoader().loadImage(aboutUs.getImg(), iv_introduce, R.drawable.user_avatar);
	}

	private UserInfoPreferences infoPreferences;
	private String jieshao;

	@Override
	protected void setAttribute() {
		mLltVideoGetMore.setOnClickListener(this);// 视频专区 查看更多点击跳转
		mHomeIntroduceLayout.setOnClickListener(this);
	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.video_get_more:
			/*
			 * Intent intent2 = new Intent(Intent.ACTION_VIEW);
			 * intent2.setData(Uri.parse(HttpUrlConstants.VIDEO_REGION_URL));
			 * startActivity(intent2);
			 */
			startActivity(new Intent(activity, VideoRegionActivity.class));
			break;
		case R.id.tv_ranking_more:
			startActivity(new Intent(activity, RankingActivity.class));
			break;

		case R.id.home_introduce_layout:
			Intent intent = new Intent(activity, CompanyIntroActivity.class);
			intent.putExtra("introduceStr", jieshao);
			startActivity(intent);
			break;
		case R.id.tv_wx_fh:
			Toast.makeText(activity, "即将开通，尽请期待", Toast.LENGTH_LONG).show();
			break;
		case R.id.tv_wx_gc:
			Toast.makeText(activity, "即将开通，尽请期待", Toast.LENGTH_LONG).show();
			break;
		case R.id.tv_wx_qk:
			Toast.makeText(activity, "即将开通，尽请期待", Toast.LENGTH_LONG).show();
			break;
		case R.id.tv_wx_gs:
			Toast.makeText(activity, "即将开通，尽请期待", Toast.LENGTH_LONG).show();
			break;
		}

	}

	@Override
	public void onRefresh() {
		getHomeData();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mLvRanking.stopRefresh();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		mLvRanking.stopLoadMore();
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void onEventMainThread(ModifyMark event) {
		doGetEarthManFC();
	}

}
