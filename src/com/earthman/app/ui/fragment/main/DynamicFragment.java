package com.earthman.app.ui.fragment.main;

import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyDynamicAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.bean.DynamicResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent;
import com.earthman.app.eventbusbean.ModifyMark;
import com.earthman.app.eventbusbean.OKDynamicSend;
import com.earthman.app.ui.activity.dynamic.DynamicDetialActivity;
import com.earthman.app.ui.activity.dynamic.MineMyDynamic;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.ScreenUtils;
import com.earthman.app.utils.SharePreferenceUtil;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.pullview.PullToRefreshBase;
import com.earthman.app.widget.pullview.PullToRefreshBase.OnRefreshListener;
import com.earthman.app.widget.pullview.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou 日期：2016-2-24 上午10:42:43 描述：（）
 */
public class DynamicFragment extends BaseFragment implements OnItemClickListener {

	private ListView mLvContent;
	@ViewInject(R.id.lv_content)
	private PullToRefreshListView mPullListView;
	private ArrayList<DynamicContent> mDynamicList;
	private MyDynamicAdapter mAdapter;
	private ImageView usericon;
	private TextView usernick;
	private int index;

	@Override
	protected View createView() {
		View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_fragment, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void initData() {
		EventBus.getDefault().register(this);
	}

	@Override
	protected void initView(View convertView) {
		View headView = LayoutInflater.from(activity).inflate(R.layout.dynamic_list_head, null);
		usernick = (TextView) headView.findViewById(R.id.user_nick);
		usericon = (ImageView) headView.findViewById(R.id.user_icon);
		// ----------------设置用户头像姓名------------------
		usernick.setText(preferences.getUsernice());
		ywbImageLoader.loadImage(preferences.getUserIco(), usericon, R.drawable.user_avatar);
		// -----------------PullListView---------------
		LinearLayout.LayoutParams flParams = (LayoutParams) mPullListView.getLayoutParams();
		flParams.height = ScreenUtils.getScreenHeight(activity) - AndroidUtils.dip2px(activity, 100);// 100==title50+tab50
		mPullListView.setLayoutParams(flParams);
		mPullListView.setPullLoadEnabled(true);
		mPullListView.setScrollLoadEnabled(false);
		mLvContent = mPullListView.getRefreshableView();
		mLvContent.setOnItemClickListener(this);
		mLvContent.addHeaderView(headView);
		mLvContent.setAdapter(mAdapter = new MyDynamicAdapter(activity, mLvContent, mDynamicList = new ArrayList<DynamicResponse.DynamicContent>()));
		SharePreferenceUtil.init(activity, "dynamic");
		
		mLvContent.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));//快速滑动时不加载图片
		if (!TextUtils.isEmpty(SharePreferenceUtil.getString("dynamic"))) {
			mDynamicList.addAll(JSON.parseObject(SharePreferenceUtil.getString("dynamic"), DynamicResponse.class).getResult());
			mAdapter.notifyDataSetChanged();
		}
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				doGetMyDynamic(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				doGetMyDynamic(false);
			}
		});
		mPullListView.doPullRefreshing(true, 200);
	}

	// 发表动态后的刷新数据
	public void onEvent(OKDynamicSend event) {
		if (event.getMessage() == 1) {
			doGetMyDynamic(true);
			ywbImageLoader.loadImage(preferences.getUserIco(), usericon, R.drawable.user_avatar);
			usernick.setText(preferences.getUsernice());
		}
	}

	public void onEventMainThread(ModifyMark event) {
		for (DynamicContent content : mDynamicList) {
			if (content.getUserId().equals(event.getId())) {
				content.setRemarks(event.getRemark());
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * doGetMyDynamic(获取动态)
	 * void
	 * 
	 * @exception
	 */
	private void doGetMyDynamic(final boolean isRefresh) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(index = isRefresh ? 0 : ++index);
		String loginUrl = null;
		loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_FRIEND_DYNAMIC, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<DynamicResponse>(Method.GET, loginUrl, DynamicResponse.class, new Response.Listener<DynamicResponse>() {
			@Override
			public void onResponse(DynamicResponse response) {
				SharePreferenceUtil.init(activity, "dynamic");
				mPullListView.onPullDownRefreshComplete();
				mPullListView.onPullUpRefreshComplete();
				mPullListView.setHasMoreData(response.getResult().size() > 0 ? true : false);
				if ("000000".equals(response.getCode())) {
					if (isRefresh) {
						mDynamicList.clear();
						SharePreferenceUtil.putString("dynamic", JSON.toJSONString(response));
					}
					mDynamicList.addAll(mDynamicList.size(), response.getResult());
					mAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissLoading();
				// SharePreferenceUtil.init(activity, "dynamic");
				// if (isRefresh) {
				// mDynamicList.clear();
				// mDynamicList.addAll(JSON.parseObject(SharePreferenceUtil.getString("dynamic"),
				// DynamicResponse.class).getResult());
				// mAdapter.notifyDataSetChanged();
				// }
				MyToast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();

			}
		}));
	}

	@Override
	protected void setAttribute() {

	}

	@Override
	protected void handclick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		if (!checkUserStatus()) {
			return;
		}
		if (position == 0) {
			activity.startActivity(new Intent(activity, MineMyDynamic.class));// 跳转到自己的动态
		} else {
			Intent intent = new Intent(activity, DynamicDetialActivity.class);
			intent.putExtra("dynamicdetial", mDynamicList.get(position - 1));
			activity.startActivity(intent);
		}
	};
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();

	}
	
	/**
	 * 动态显示 设置 列
	 * @param gView
	 * @param columns 图片数量
	 */
	public static void setNumColumns(GridView gView,int size){
		switch (size) {
		case 1:
			gView.setNumColumns(1);
			break;
		case 2:
		case 4:
			gView.setNumColumns(2);
			break;
		default:
			gView.setNumColumns(3);
			break;
		}
	}
	
	/**
	 * 设置ImageView 宽高
	 * @param imageview 
	 * @param size 动态图片数量
	 * @param screenWidth 图片显示区域宽度
	 * @param layoutParams 
	 */
	public static void setImageWH(ImageView imageview,int size,int screenWidth,android.view.ViewGroup.LayoutParams layoutParams){
		int width = 0;
		int height = 0;
		if (size == 1) {
			width = screenWidth;
			height = screenWidth * 4 / 5;
		}else if (size == 2||size == 4) {
			width = (screenWidth) / 2;
			height = (screenWidth) / 2;
		}else {
			width = (screenWidth) / 3;
			height = (screenWidth) / 3;
		}
		layoutParams.height=height;
		layoutParams.width=width;
		imageview.setLayoutParams(layoutParams);
	}

}
