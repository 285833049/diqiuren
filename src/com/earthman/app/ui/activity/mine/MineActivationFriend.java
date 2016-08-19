package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.ReplaceFriendActivationAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.MineReplaceJihuoResponse;
import com.earthman.app.bean.MineReplaceJihuoResponse.ReplaceJihuoResult;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 代替好友激活
 * 
 * @author xiexianyong
 *         修改 Eric
 */
@ContentView(R.layout.replace_friend_ctivation)
public class MineActivationFriend extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------
	@ViewInject(R.id.tjhlist)
	XListView mXLvActivation;
	private ReplaceFriendActivationAdapter mActivationAdapter;
	private ArrayList<ReplaceJihuoResult> activationResultList = new ArrayList<MineReplaceJihuoResponse.ReplaceJihuoResult>();

	@Override
	protected void handclick(View v) {

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@OnClick(R.id.btn_right)
	private void clearData(View view) {
		mActivationAdapter.clearData();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		doGetJihuoList(preferences);
	}

	/**
	 * 代替好友激活列表数据
	 */
	private void doGetJihuoList(UserInfoPreferences userInfoPreferences) {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.REPLACE_JIHUO_LIST, list);
		executeRequest(new FastJsonRequest<MineReplaceJihuoResponse>(Method.GET, loginUrl, MineReplaceJihuoResponse.class,
				new Response.Listener<MineReplaceJihuoResponse>() {
					@Override
					public void onResponse(MineReplaceJihuoResponse response) {
						dismissLoading();
						if ("000000".equals(response.getCode())) {
							activationResultList.clear();
							activationResultList.addAll(response.getResult());
							mActivationAdapter.notifyDataSetChanged();
						} else {
							NetStatusHandUtil.getInstance().handStatus(MineActivationFriend.this, response.getCode(),response.getMessage());						
						}
					}
				}, getErrorListener()));
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleRight.setVisibility(View.GONE);
		mXLvActivation.setAutoLoadEnable(false);
		mXLvActivation.setPullLoadEnable(false);
		mXLvActivation.setPullRefreshEnable(false);
		mActivationAdapter = new ReplaceFriendActivationAdapter(MineActivationFriend.this, activationResultList);
		mXLvActivation.setAdapter(mActivationAdapter);
	}

	@Override
	protected void setAttribute() {
		mBtnTitleRight.setText(R.string.clear);
		mTvTitleLeft.setText(R.string.instead_of_friend_activation);
	}

}
