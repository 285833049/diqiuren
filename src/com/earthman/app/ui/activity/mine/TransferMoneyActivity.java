package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.User_Info_Response;
import com.earthman.app.listener.FragmentResultListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.utils.BankConstants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 转账
 * 
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-14 下午2:56:01
 * @Decription
 */
@ContentView(R.layout.transfer_money_activity)
public class TransferMoneyActivity extends BaseActivity implements OnClickListener, FragmentResultListener {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnBack;
	@ViewInject(R.id.btn_right)
	Button mBtnRight;
	@ViewInject(R.id.iv_icon)
	ImageView mIvIcon;
	@ViewInject(R.id.tv_nick)
	TextView mTvNick;
	@ViewInject(R.id.tv_card_id)
	TextView mTvCardID;

	@ViewInject(R.id.et_transfer_money)
	EditText mEtTransferMoney;
	@ViewInject(R.id.tv_currency_name)
	TextView nTvCurrencyName;
	@ViewInject(R.id.et_transfer_remark)
	EditText mEtTransferRemark;
	@ViewInject(R.id.btn_confirm_transfer)
	Button mBtnConfirmTransfer;
	@ViewInject(R.id.btn_change_payment)
	Button mBtnChangePayment;

	ChoicePaymentFragment mChoicePaymentFragment;

	private String mCardId;
	private String mCurrencyName;
	private String usericon;
	private String username;

	@Override
	protected void initData() {
		mBtnRight.setText(R.string.mine_transfer_money_record);
		mBtnChangePayment.setText(Html.fromHtml(String.format(getString(R.string.mine_transfer_money_change_payment), mCurrencyName = "激活币")
				+ "，<font color=#16B7E5>更改</font"));
	}

	@Override
	protected void initView() {
		mTvTitleLeft.setText(getString(R.string.mine_transfer_money_to_friends));
		mBtnBack.setOnClickListener(this);
		mBtnRight.setOnClickListener(this);

		mBtnConfirmTransfer.setEnabled(false);

//		if (getIntent().getExtras() != null) {
//			if ("0".equals(getIntent().getStringExtra("pageType"))) {
//				usericon = getIntent().getStringExtra("icon");
//				username = getIntent().getStringExtra("nick");
//				mTvCardID.setText(mCardId = getIntent().getStringExtra("cardid"));
//				mTvNick.setText(username);
//				ImageLoader.getInstance().displayImage(usericon, mIvIcon);
//				mBtnConfirmTransfer.setEnabled(true);
//			} else {
				String otherAccount = getIntent().getStringExtra("otherAccount");
				doGetOtherInfo(otherAccount);
//			}
//		}
		mBtnBack.setOnClickListener(this);
		mBtnConfirmTransfer.setOnClickListener(this);
		mBtnChangePayment.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {

	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:// 转账记录
			startActivity(new Intent(this, TransferAccountRecordActivity.class).putExtra("cardid", getIntent().getStringExtra("cardid")));
			break;
		case R.id.btn_confirm_transfer://
			String message = mEtTransferRemark.getText().toString().trim();
			String money = mEtTransferMoney.getText().toString().trim();
			if (TextUtils.isEmpty(money)) {
				MyToast.makeText(this, R.string.zhuanzhang2, Toast.LENGTH_SHORT).show();
				return;
			}
			if (1 == preferences.getIsSetPwd()) {
				showPayDialog(money, TextUtils.isEmpty(message) ? getString(R.string.mine_zhuanzhang) : message);
			} else {
				showSetPayPwdAlert();
			}
			break;
		case R.id.btn_change_payment:
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(R.anim.short_menu_pop_in, R.anim.short_menu_pop_out);
			mChoicePaymentFragment = new ChoicePaymentFragment();
			mChoicePaymentFragment.setArguments(mCurrencyName, this);
			transaction.replace(R.id.ll_container, mChoicePaymentFragment);
			transaction.commit();
			break;
		}
	}

	private void showPayDialog(final String money, final String message) {
		PayPwdDialog payPwdDialog = new PayPwdDialog(this, money, new OnSurePayClickListener() {
			@Override
			public void onSurePayClick(int payType, String pwd) {
				// 默认地球币支付
				doGetPay(money, message, pwd);
			}
		});
		payPwdDialog.setPayWay(mCurrencyName.equals("地球币") ? BankConstants.PAY_WAY_DIQIUBI : BankConstants.PAY_WAY_ATY);
		payPwdDialog.dismissChange();
		payPwdDialog.setCanceledOnTouchOutside(false);
		payPwdDialog.show();
	}

	/**
	 * 
	 * doGetPay(转账地球人账户)
	 * void{userid}/{token}/{paypasswd}/{otheruserid}/{amount}
	 * 
	 * @exception
	 */
	private void doGetPay(String money, String mesage, String pwd) {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(com.earthman.app.utils.MD5.getMessageDigest(pwd.getBytes()));
		list.add(mCardId);
		list.add(Float.parseFloat(money));
		// list.add(TextUtils.isEmpty(remarks)?"转账":remarks);

		String getUrl = HttpUrlConstants.getPostUrl(this, mCurrencyName.equals("激活币") ? HttpUrlConstants.ACTIVATE_CURRENCY_DONATION
				: HttpUrlConstants.ACCOUNT_TRANSFER, list);
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("keyword", mesage);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				dismissLoading();
				if ("000000".equals(response2.getCode())) {
					MyToast.makeText(TransferMoneyActivity.this, R.string.zhuanzhang3, Toast.LENGTH_LONG).show();
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(TransferMoneyActivity.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, getUrl, jsonRequest, listener, getErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}
		};
		executeRequest(jsonObjectRequest);

	}

	/**
	 * doGetOtherInfo(获取他人的用户信息)
	 * void/appuser/selectUsersByEarthId
	 * 
	 * @exception
	 */
	private void doGetOtherInfo(String otherAccount) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(otherAccount);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_USER_INFO, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<User_Info_Response>(Method.GET, loginUrl, User_Info_Response.class, new Response.Listener<User_Info_Response>() {
			@Override
			public void onResponse(User_Info_Response response) {
				if ("000000".equals(response.getCode())) {
					mCardId = response.getResult().getCardId();
					mTvCardID.setText(response.getResult().getCardId());
					mTvNick.setText(response.getResult().getNice());
					ImageLoader.getInstance().displayImage(response.getResult().getAvatar(), mIvIcon);
					ImageLoader.getInstance().displayImage(usericon, mIvIcon);
					mBtnConfirmTransfer.setEnabled(true);
				} else {
					NetStatusHandUtil.getInstance().handStatus(TransferMoneyActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	public void onFragmentResult(Fragment fragment, Bundle bundle) {
		if (bundle == null)
			return;
		mCurrencyName = bundle.getString("currencyName");
		nTvCurrencyName.setText(mCurrencyName);
		mBtnChangePayment.setText(Html.fromHtml(String.format(getString(R.string.mine_transfer_money_change_payment), mCurrencyName)
				+ "，<font color=#16B7E5>更改</font"));
	}
}
