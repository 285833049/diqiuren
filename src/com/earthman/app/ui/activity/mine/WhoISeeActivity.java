package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.WhoISeeListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.VisitHistoryResponse;
import com.earthman.app.bean.VisitHistoryResponse.VisitHistoryResult.VisitorsListEntity;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我看过谁
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.who_i_see_list)
public class WhoISeeActivity extends BaseActivity {
	@ViewInject(R.id.who_i_see_list)
	XListView mLvISee;
	private WhoISeeListAdapter adapter;
	@ViewInject(R.id.btn_back)
	private Button mBtnLeft;
	@ViewInject(R.id.tv_left)
	private TextView mTVTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnRight;
	@ViewInject(R.id.paying_visitors)
	TextView mPayingVisitorNum;
	@ViewInject(R.id.total_browse_volume)
	TextView mTvTotalNum;
	private int type;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			adapter.clearData();
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		type = getIntent().getIntExtra("type", type);
		doGetVisiterList();
	}

	private void doGetVisiterList() {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(type);
		Log.d("xianyong", "user_token:" + preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_VISITH_HISTORY, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<VisitHistoryResponse>(Method.GET, loginUrl, VisitHistoryResponse.class,
				new Response.Listener<VisitHistoryResponse>() {

					@Override
					public void onResponse(VisitHistoryResponse response) {
						dismissLoading();
						if ("000000".equals(response.getCode())) {
							mTvTotalNum.setText("" + response.getResult().getVisitNumbers());
							mPayingVisitorNum.setText("" + response.getResult().getPayMembers());
							ArrayList<VisitorsListEntity> result = response.getResult().getVisitors();
							adapter = new WhoISeeListAdapter(WhoISeeActivity.this, result);
							mLvISee.setAdapter(adapter);
							mLvISee.setAutoLoadEnable(false);
							mLvISee.setPullLoadEnable(false);
							mLvISee.setPullRefreshEnable(false);
						} else {
							NetStatusHandUtil.getInstance().handStatus(WhoISeeActivity.this, response.getCode(), response.getMessage());

						}
					}
				}, getErrorListener()));
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnRight.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		if (type == 1) {
			mTVTitleLeft.setText(R.string.who_i_see);
		} else {
			mTVTitleLeft.setText(R.string.mine_see_me);
		}
		mBtnRight.setText(R.string.clear);
	}

	public void getaddfriend(String friend) {
		ArrayList<Object> lists = new ArrayList<Object>();
		lists.add(preferences.getUserID());
		lists.add(friend);
		lists.add(preferences.getUserToken());
		lists.add(0);// 0：用户A单方申请添加B1：用户B确认添加A
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.ADD_FRIEND, lists);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<VisitHistoryResponse>(Method.GET, loginUrl, VisitHistoryResponse.class,
				new Response.Listener<VisitHistoryResponse>() {
					@Override
					public void onResponse(VisitHistoryResponse response) {
						if ("000000".equals(response.getCode())) {
							MyToast.makeText(WhoISeeActivity.this, getString(R.string.addFriendTip), Toast.LENGTH_LONG).show();
						} else {
							NetStatusHandUtil.getInstance().handStatus(WhoISeeActivity.this, response.getCode(), response.getMessage());
						}
					}
				}, getErrorListener()));
	}
}
