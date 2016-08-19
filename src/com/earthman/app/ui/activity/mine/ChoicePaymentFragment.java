package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.bean.UserBalanceInfo;
import com.earthman.app.listener.FragmentResultListener;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-14 下午5:14:21
 * @Decription 选择支付方式
 */
public class ChoicePaymentFragment extends BaseFragment implements OnItemClickListener {

	@ViewInject(R.id.btn_outside)
	private Button mBtnOutside;
	@ViewInject(R.id.btn_close)
	private Button mBtnClose;
	@ViewInject(R.id.lv_content)
	private ListView mLvContent;
	private ChoicePaymentAdapter mAdapter;
	private List<PaymentBalanceItem> mItemList;

	private String mCurrencyName;
	private FragmentResultListener mListener;
	
	public void setArguments(String currencyName,FragmentResultListener mListener) {
		this.mCurrencyName=currencyName;
		this.mListener = mListener;
	}

	@Override
	protected View createView() {
		View convertView = LayoutInflater.from(activity).inflate(R.layout.choice_payment_fragment, null);
		ViewUtils.inject(this, convertView);
		return convertView;
	}

	@Override
	protected void initData() {
		getQueryBalance();// 获取余额
	}

	private void getQueryBalance() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.QUERY_BALANCE, list);
		executeRequest(new FastJsonRequest<UserBalanceInfo>(Method.GET, loginUrl, UserBalanceInfo.class, new Response.Listener<UserBalanceInfo>() {
			@Override
			public void onResponse(UserBalanceInfo response) {
				if ("000000".equals(response.getCode())) {
					mItemList = new ArrayList<PaymentBalanceItem>();
					mItemList.add(new PaymentBalanceItem(R.drawable.ic_currency_activate, "激活币", response.getResult().getCurrencyatv()));
					mItemList.add(new PaymentBalanceItem(R.drawable.ic_currency_earth, "地球币", response.getResult().getCurrencyem()));
					mLvContent.setAdapter(mAdapter = new ChoicePaymentAdapter(mCurrencyName,mItemList));

				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));

	}

	@Override
	protected void initView(View convertView) {
		mBtnClose.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/iconfont.ttf"));
		mBtnOutside.setOnClickListener(this);
		mBtnClose.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		mLvContent.setOnItemClickListener(this);
	}

	private void removeFragment(String currencyName) {
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.short_menu_pop_in, R.anim.short_menu_pop_out);
		transaction.remove(this);
		transaction.commit();
		
		Bundle bundle=new Bundle();
		bundle.putString("currencyName", currencyName);
		mListener.onFragmentResult(this, bundle);
	}

	@Override
	protected void handclick(View v) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_outside:
		case R.id.btn_close:
			removeFragment(mCurrencyName);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mAdapter.setSelection(arg2);
		mAdapter.notifyDataSetChanged();
		removeFragment(mItemList.get(arg2).getPaymentName());
	}

}
