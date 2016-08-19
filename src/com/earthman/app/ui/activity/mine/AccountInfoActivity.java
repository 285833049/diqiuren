package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.AccountInfo;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Title: MyAccountInfoActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月2日
 */
@ContentView(R.layout.activity_account_info)
public class AccountInfoActivity extends BaseActivity {
	// ---------------------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------------用户信息---------------
	@ViewInject(R.id.myaccount_earthid)
	TextView mTvAccountInfo;
	@ViewInject(R.id.myaccount_youxiaoqi)
	TextView mTvAccountUsefulLife;
	@ViewInject(R.id.tv_activate_currency_balance)
	TextView mTvActivateCurrencyBalance;
	@ViewInject(R.id.tv_earth_currency_balance)
	TextView mTvEarthCurrencyBalance;
	@ViewInject(R.id.myaccount_creattime)
	TextView mTvAccountRegistData;
	private UserInfoPreferences mPreferences;

	@Override
	protected void handclick(View v) {
	}

	@Override
	protected void initData() {
		mPreferences = new UserInfoPreferences(this);
		getDataFromNet();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleRight.setVisibility(View.GONE);
		mBtnTitleLeft.setText(R.string.myaccount);
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void setAttribute() {

	}

	/**
	 * 获取数据
	 */
	private void getDataFromNet() {
		// TODO 获取个人数据
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(mPreferences.getUserID());
		list.add(mPreferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MY_ACCOUNT_INFO, list);
		executeRequest(new FastJsonRequest<AccountInfo>(Method.GET, getUrl, AccountInfo.class, new Response.Listener<AccountInfo>() {
			@Override
			public void onResponse(AccountInfo response) {
				if ("000000".equals(response.getCode())) {
					LogUtil.i("个人数据", response.toString());
					mTvAccountInfo.setText(response.getResult().getCardId());
					int year = response.getResult().getYear();
					mTvAccountUsefulLife.setText(year > Constants.MAX_AGE ? getString(R.string.forever) : year + getString(R.string.year));
					mTvActivateCurrencyBalance.setText(response.getResult().getCurrencyatv());
					mTvEarthCurrencyBalance.setText(response.getResult().getCurrencyem());
					mTvAccountRegistData.setText(response.getResult().getDuedate());
				} else {
					NetStatusHandUtil.getInstance().handStatus(AccountInfoActivity.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}
}
