package com.earthman.app.ui.activity.dynamic;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyDynamicAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.DynamicResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.pulltoreflesh.XListView.IXListViewListener;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月6日 上午10:58:33
 * @Decription
 */
@ContentView(R.layout.mine_my_dynamic)
public class MineMyDynamic extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	@ViewInject(R.id.dynamic_listview)
	XListView mLvdynamic;
	private MyDynamicAdapter myDynamicAdapter;
	private ArrayList<DynamicContent> dynamicList = new ArrayList<DynamicResponse.DynamicContent>();
	private int index = 0;
	private int friendId;
	private int pageType;
	private String friendName;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			pageType = getIntent().getExtras().getInt("pagetype");
			friendId = getIntent().getIntExtra("friendId", friendId);
			friendName = getIntent().getExtras().getString("friendName");
		}
		doGetMyDynamic(true);
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		if (pageType == 1) {
			mTvTitleLeft.setText(String.format(getString(R.string.frienddynamictitle), friendName));
		} else {
			mTvTitleLeft.setText(R.string.mydynamictitle);
		}
		mBtnTitleLeft.setOnClickListener(this);
		mLvdynamic.setXListViewListener(listener);
		myDynamicAdapter = new MyDynamicAdapter(MineMyDynamic.this,mLvdynamic, dynamicList);
		mLvdynamic.setAdapter(myDynamicAdapter);
		mLvdynamic.setPullLoadEnable(false);
		mLvdynamic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == dynamicList.size() + 1) {
					// 加载更多
				} else {
					Intent dynamicDetialIntent = new Intent(MineMyDynamic.this, DynamicDetialActivity.class);
					dynamicDetialIntent.putExtra("dynamicdetial", dynamicList.get(position - 1));
					startActivity(dynamicDetialIntent);
				}
			}
		});
	}

	@Override
	protected void setAttribute() {

	}

	// xlistview 监听器
	private IXListViewListener listener = new IXListViewListener() {
		@Override
		public void onRefresh() {
			doGetMyDynamic(true);
		}

		@Override
		public void onLoadMore() {
			doGetMyDynamic(false);
		}
	};

	private void doGetMyDynamic(final boolean isRefresh) {
		showLoading();
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(isRefresh?0:++index);
		if (pageType == 1) {
			list.add(friendId);
		}
		String loginUrl=HttpUrlConstants.getUrl(this, 1==pageType?HttpUrlConstants.VISITFRFRIENDDYNAMIC:HttpUrlConstants.GET_MY_DYNAMIC, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<DynamicResponse>(Method.GET, loginUrl, DynamicResponse.class, new Response.Listener<DynamicResponse>() {
			@Override
			public void onResponse(DynamicResponse response) {
				dismissLoading();
				mLvdynamic.setPullLoadEnable(true);
				if ("000000".equals(response.getCode())) {
					mLvdynamic.stopLoadMore();
					mLvdynamic.stopRefresh();
					if (0 == response.getResult().size()) {
						MyToast.makeText(MineMyDynamic.this, R.string.footer_tips, Toast.LENGTH_SHORT).show();
						mLvdynamic.setPullLoadEnable(false);
						return;
					}
					if (isRefresh) {
						dynamicList.clear();
					}
					dynamicList.addAll(dynamicList.size(), response.getResult());
					myDynamicAdapter.notifyDataSetChanged();
//					if (0 == response.getResult().size()) {
//						MyToast.makeText(MineMyDynamic.this, R.string.footer_tips, Toast.LENGTH_SHORT).show();
//						mLvdynamic.setPullLoadEnable(false);
//						return;
//					}
//					if (response.getResult().size()<10) {
//						mLvdynamic.setPullLoadEnable(false);
//					}
//					if (0 == index) {
//						dynamicList.clear();
//						dynamicList.addAll(response.getResult());
//					} else {
//						dynamicList.addAll(dynamicList.size(), response.getResult());
//					}
//					myDynamicAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(MineMyDynamic.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}
}
