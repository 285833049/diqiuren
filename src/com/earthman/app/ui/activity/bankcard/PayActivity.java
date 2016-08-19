package com.earthman.app.ui.activity.bankcard;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.PayStyleAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.TotalFreeResponse;
import com.earthman.app.constant.Constent;
import com.earthman.app.ui.fragment.pay.LifePayFragment;
import com.earthman.app.ui.fragment.pay.YearPayFragment;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者：Zhou 日期：2016-2-26 下午2:21:51 描述：（）
 */
@ContentView(R.layout.register_pay)
public class PayActivity extends BaseActivity {

	private FragmentManager fragmentManager;
	@ViewInject(android.R.id.tabhost)
	private FragmentTabHost mTabHost;
	@ViewInject(R.id.lv_pay)
	private MyListView lv_pay;
	private String[] tabNames;// Tab文字
	private Class<?>[] tabContents;// Tab内容
	private int[] bgRes;
	private PayStyleAdapter adapter;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_should_pay)
	public TextView tv_should_pay;
	@ViewInject(R.id.btn_year_pay)
	public Button mBtnYearPay;
	@ViewInject(R.id.btn_life_pay)
	public Button mBtnLifePay;
	@ViewInject(R.id.btn_pay_later)
	private Button btn_pay_later;
	private Object offLine;
	private Object offLineId;

	/*
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay_later:
			setResult(0x1212);
			finish();
			break;
		default:
			break;
		}
	}
	

	public PayStyleAdapter getAdapter() {
		return adapter;
	}


	/**
	 * 
	 */
	public int getPayType() {
		Map<Integer, Boolean> map = adapter.map;
		for (int i = 0; i < map.size(); i++) {
			if (map.get(i) == true) {
				Pay_method = i + 1;
			}
		}
		
		
		
		return Pay_method;
	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == 0x1212) {
			setResult(0x12123);
			finish();
		}
	}

	/*
	 * @see com.ywb.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		getType();// 获取支付类型
		fragmentManager = getSupportFragmentManager();
		tabNames = getResources().getStringArray(R.array.pay_names);
		bgRes = new int[] { R.drawable.lifepay_bg_selector, R.drawable.yearpay_bg_selector };
		tabContents = new Class[] { LifePayFragment.class, YearPayFragment.class };
		adapter = new PayStyleAdapter(this, type);
		UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
		doGetLifePay(userInfoPreferences);
	}

	/**
	 * 获取终身服务费多少
	 */
	private void doGetLifePay(UserInfoPreferences userInfoPreferences) {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
			list.add(userInfoPreferences.getUserID());
		} else {
			list.add(userInfoPreferences.getUserpayID());
		}
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MY_LIFE_TOTAL_MONEY, list);
		executeRequest(new FastJsonRequest<TotalFreeResponse>(Method.GET, loginUrl, TotalFreeResponse.class, new Response.Listener<TotalFreeResponse>() {
			@Override
			public void onResponse(TotalFreeResponse response) {
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					LogUtil.i("终生服务费", response.toString());
					totalFee = response.getResult().getTotalFee();
					tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), totalFee)));
				} else {
					NetStatusHandUtil.getInstance().handStatus(PayActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	private String totalFee;

	/*
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnLifePay.setVisibility(View.VISIBLE);
		mTabHost.setup(this, fragmentManager, R.id.news_content);
		// 初始化标签
		for (int i = 0; i < tabNames.length; i++) {
			TabSpec mTabSpect = mTabHost.newTabSpec(tabNames[i]).setIndicator(getTabView(tabNames[i], bgRes[i]));
			mTabHost.addTab(mTabSpect, tabContents[i], null);
			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

				@Override
				public void onTabChanged(String tabId) {
					if (tabNames[0] == tabId) {
						index = 0;
						mBtnYearPay.setVisibility(View.GONE);
						mBtnLifePay.setVisibility(View.VISIBLE);
						tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), totalFee)));
					} else if (tabNames[1] == tabId) {
						index = 1;
						mBtnYearPay.setVisibility(View.VISIBLE);
						mBtnLifePay.setVisibility(View.GONE);
					}
				}
			});
		}
		lv_pay.setAdapter(adapter);
		btn_pay_later.setOnClickListener(this);
	}

	/**
	 * getType(这里用一句话描述这个方法的作用)
	 * void
	 * 
	 * @exception
	 **/
	private void getType() {
		if (getIntent().getExtras() != null) {
			type = (Integer) getIntent().getExtras().get("type");
			LogUtil.i(TAG, "type注册类型" + type);
		}
	}

	int index;
	private int Pay_method = 0;
	public int type;

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	/**
	 * 获取标签
	 * 
	 * @param tabName
	 * @param resId
	 * @return
	 */
	private View getTabView(String tabName, int bgResId) {
		View view = LayoutInflater.from(this).inflate(R.layout.news_tab_item, null, false);
		TextView tvTabName = (TextView) view.findViewById(R.id.tv_news_tab);
		tvTabName.setText(tabName);
		tvTabName.setBackgroundResource(bgResId);
		return view;
	}

	@Override
	protected void setAttribute() {
		if (type == Constent.RENEW) {
			tv_title.setText(R.string.continue_pay);
		} else {
			tv_title.setText(R.string.mine_jihuo);
		}

	}
}
