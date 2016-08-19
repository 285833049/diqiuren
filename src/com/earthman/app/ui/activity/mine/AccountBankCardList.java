package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.BankListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.MyBankListReponse;
import com.earthman.app.bean.MyBankListReponse.MyBankList;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.ui.activity.bankcard.PayPwdVerifyActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 银行卡列表
 * 
 * @author xiexianyong
 *         修改 Eric
 * 
 */
@ContentView(R.layout.bankcard_list)
public class AccountBankCardList extends BaseActivity implements OnClickListener {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//--------------listview------------------
	@ViewInject(R.id.lv_card)
	private XListView mXlvBankCard;
	private BankListAdapter mBankCardAdapter;

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
		doGetBankCard();//获取银行卡列表
	}

	/**
	 * 获取银行卡list
	 */
	private void doGetBankCard() {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MY_BANK_LIST, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<MyBankListReponse>(Method.GET, loginUrl, MyBankListReponse.class, new Response.Listener<MyBankListReponse>() {
			@Override
			public void onResponse(MyBankListReponse response) {
				dismissLoading();
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					LogUtil.i(TAG, response.toString());
					ArrayList<MyBankList> result = response.getResult();
					mBankCardAdapter = new BankListAdapter(AccountBankCardList.this, result);
					mXlvBankCard.setAdapter(mBankCardAdapter);
					mXlvBankCard.setAutoLoadEnable(false);
					mXlvBankCard.setPullLoadEnable(false);
					mXlvBankCard.setPullRefreshEnable(false);

				} else {
					NetStatusHandUtil.getInstance().handStatus(AccountBankCardList.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));

	}

	@Override
	protected void initView() {

		ViewUtils.inject(this);

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@OnClick(R.id.btn_right)
	private void onFindClick(View v) {
		if(preferences.getIsSetPwd() == 0){//未设置过支付密码，需要先设置支付密码
			startActivity(new Intent(AccountBankCardList.this, SetPayPwdActivity.class));
		}else{//设置过支付密码，跳转到支付密码校验页面
			startActivity(new Intent(AccountBankCardList.this, PayPwdVerifyActivity.class));
		}
		//startActivity(new Intent(AccountBankCardList.this, AddCardStepOneActivity.class));
		
	}

	@Override
	protected void setAttribute() {
		mBtnTitleLeft.setText(R.string.myaccount_bank_list);
		mBtnTitleRight.setText(R.string.add_bankcard);
	}

}
