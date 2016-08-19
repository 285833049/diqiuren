package com.earthman.app.ui.fragment.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.YearAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.MineUpdateInfo;
import com.earthman.app.constant.Constent;
import com.earthman.app.listener.ItemClickListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.mine.RegistReplacePay;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.BankConstants;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.MD5;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou 日期：2016-2-26 下午2:29:37 描述：（）
 */
public class YearPayFragment extends BaseFragment {

	@ViewInject(R.id.rl_year)
	private RelativeLayout rl_year;
	@ViewInject(R.id.year)
	private TextView year;
	@ViewInject(R.id.et_year)
	private EditText et_year;
	@ViewInject(R.id.underline)
	private View underline;
	private UserInfoPreferences userInfoPreferences;

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#createView()
	 */
	@Override
	protected View createView() {
		View view = LayoutInflater.from(activity).inflate(R.layout.yearpay_fragment, null);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		payactivity.tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), money)));
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what > 0) {
				PayActivity activity2 = (PayActivity) activity;
				activity2.setResult(0x1212);// 关闭支付界面
				activity2.finish();

			}
		};
	};

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#initView(android.view.View)
	 */
	@Override
	protected void initView(View convertView) {
		ViewUtils.inject(this, convertView);
		payactivity = (PayActivity) getActivity();
		Button pay = payactivity.mBtnYearPay;
		type = payactivity.type;
		paySuccessReceiver = new PaySuccessReceiver();
		paySuccessReceiver.setHandler(handler);
		activity.registerReceiver(paySuccessReceiver, new IntentFilter(Constants.PAY_SUCCESS_ACTION));

		pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int Pay_method;
				if(payactivity.getAdapter().getPayNames().length<4){//地球币、微信支付、支付宝
					Pay_method = payactivity.getPayType()+1;
				}else{//激活币、地球币、微信支付...
					Pay_method = payactivity.getPayType();
				}

				if (0 == Pay_method) {
					MyToast.makeText(activity, R.string.toast2, Toast.LENGTH_LONG).show();
					return;
				}
				if (1 == Pay_method) {// 激活币
					if (type == Constent.REGIST_TO_SELF) {
						LogUtil.i(TAG, "money-->" + money + "month-->" + money / 10);
						Intent payintent = new Intent(activity, RegistReplacePay.class);
						payintent.putExtra("money", String.valueOf(money));
						payintent.putExtra("paymentMethod", Constants.PAYSTYLE_EARTH);
						payintent.putExtra("paymentType", Constants.YEARPAY);
						payintent.putExtra("month", money / 10);
						startActivityForResult(payintent, 12);
					} else if (type == Constent.REGIST_TO_FAMILY || type == Constent.REGIST_TO_FRIEND || type == Constent.RENEW) {// 给好友激活或亲情号激活或续费用地球币
						if (0 == userInfoPreferences.getIsSetPwd()) {
							BaseActivity BaseActivity = (BaseActivity) activity;
							BaseActivity.showSetPayPwdAlert();
						} else {
							PayPwdDialog payPwdDialog = new PayPwdDialog(activity, String.valueOf(money), new OnSurePayClickListener() {
								@Override
								public void onSurePayClick(int payType, String pwd) {
									// TODO 激活币支付
									doPostActivation(pwd);
								}
							});
							payPwdDialog.setPayWay(BankConstants.PAY_WAY_ATY);
							payPwdDialog.setCancelable(false);
							payPwdDialog.dismissChange();
							payPwdDialog.show();
						}
					}
					return;

				} else if (2 == Pay_method) {//地球币代付
					if (type == Constent.REGIST_TO_SELF) {// 激活自己的账户采用地球币代付

						LogUtil.i(TAG, "money-->" + money + "month-->" + money / 10);
						Intent payintent = new Intent(activity, RegistReplacePay.class);
						payintent.putExtra("money", String.valueOf(money));
						payintent.putExtra("paymentMethod", Constants.PAYSTYLE_EARTH);
						payintent.putExtra("paymentType", Constants.YEARPAY);
						payintent.putExtra("month", money / 10);
						startActivityForResult(payintent, 12);
					} else if (type == Constent.REGIST_TO_FAMILY || type == Constent.REGIST_TO_FRIEND || type == Constent.RENEW) {// 给好友激活或亲情号激活或续费用地球币
						if (0 == userInfoPreferences.getIsSetPwd()) {
							BaseActivity BaseActivity = (BaseActivity) activity;
							BaseActivity.showSetPayPwdAlert();
						} else {
							PayPwdDialog payPwdDialog = new PayPwdDialog(activity, String.valueOf(money), new OnSurePayClickListener() {
								@Override
								public void onSurePayClick(int payType, String pwd) {
									// TODO 地球人支付
									doPostPayCoin(pwd);
								}
							});
							payPwdDialog.setPayWay(BankConstants.PAY_WAY_DIQIUBI);
							payPwdDialog.setCancelable(false);
							payPwdDialog.dismissChange();
							payPwdDialog.show();
						}
					}
				} else if (3 == Pay_method) {// 微信支付
					if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
						new PayFactory(activity, PayFactory.CASE_ACTIVE_SELF, PayFactory.PAYTYPE_WEIXIN, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					} else if (type == Constent.REGIST_TO_FAMILY) {
						new PayFactory(activity, PayFactory.CASE_ADDQINQING, PayFactory.PAYTYPE_WEIXIN, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					} else if (type == Constent.REGIST_TO_FRIEND) {
						new PayFactory(activity, PayFactory.CASE_REGISTER_FOR_FRIEND, PayFactory.PAYTYPE_WEIXIN, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					}
				} else if (4 == Pay_method) {// 支付宝支付
					if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
						new PayFactory(activity, PayFactory.CASE_ACTIVE_SELF, PayFactory.PAYTYPE_ALIPAY, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					} else if (type == Constent.REGIST_TO_FAMILY) {
						new PayFactory(activity, PayFactory.CASE_ADDQINQING, PayFactory.PAYTYPE_ALIPAY, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					} else if (type == Constent.REGIST_TO_FRIEND) {
						new PayFactory(activity, PayFactory.CASE_REGISTER_FOR_FRIEND, PayFactory.PAYTYPE_ALIPAY, String.valueOf(money)).genActiveRequest(
								String.valueOf(money / 10), "");
					}
				} else if (5 == Pay_method) {// 银联
					Toast.makeText(activity, "银联", Toast.LENGTH_LONG).show();
				} else if (6 == Pay_method) {
					Toast.makeText(activity, "信用", Toast.LENGTH_LONG).show();
				} else {
					MyToast.makeText(activity, "其他支付方式未完成", Toast.LENGTH_LONG).show();
				}
			}

		});

	}

	/**
	 * 
	 * doPostPayCoin(支付)
	 * void
	 * 
	 * @exception
	 */
	private void doPostPayCoin(String psw) {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		list.add(MD5.getMessageDigest(psw.getBytes()));
		JSONObject jsonRequest = new JSONObject();
		try {
			// post方式访问，post提交参数：dataJson包含值：
			// userid；激活用户id(可以是自己id，也可以是他人id)
			// year：缴费年份（数字类型，如果是趸交，传值1000）
			// money：缴费金额（浮点数字类型）"
			if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
				jsonRequest.put("userId", userInfoPreferences.getUserID());
			} else {
				jsonRequest.put("userId", userInfoPreferences.getUserpayID());
			}
			jsonRequest.put("year", money / 10);
			jsonRequest.put("money", money);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(activity, HttpUrlConstants.ACTIVE_PAYBY_EARTHCOIN, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					if (type == Constent.RENEW) {
						MyToast.makeText(activity, R.string.renew_success, Toast.LENGTH_LONG).show();
					} else {
						MyToast.makeText(activity, R.string.zhifu1, Toast.LENGTH_LONG).show();
					}
					new UserInfoPreferences(activity).setUserStatus(Constants.SHARE_ACTIVED);
					EventBus.getDefault().post(new MineUpdateInfo());
					activity.setResult(0x12123);
					activity.finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response2.getCode(), response2.getMessage());
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

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#initData()
	 */
	@Override
	protected void initData() {
		userInfoPreferences = new UserInfoPreferences(activity);
		PayActivity payactivity = (PayActivity) getActivity();
		type = payactivity.type;
	}

	@OnClick(R.id.rl_year)
	private void onYearClick(View v) {
		View contentView = LayoutInflater.from(activity).inflate(R.layout.year_pop, null);
		final PopupWindow popupWindow = new PopupWindow(contentView, (int) (AndroidUtils.getDeviceWidth(activity) - AndroidUtils.dip2px(activity, 30)),
				LayoutParams.WRAP_CONTENT);
		ListView listView = (ListView) contentView.findViewById(R.id.lv_year);
		final String[] years = getResources().getStringArray(R.array.year_names);
		YearAdapter yearAdapter = new YearAdapter(activity, new ItemClickListener() {

			@Override
			public void onItemClick(int position, Object object) {
				year.setText(years[position]);
				if (position == years.length - 1) {
					et_year.setVisibility(View.VISIBLE);
					underline.setVisibility(View.VISIBLE);
					payactivity.tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), money)));
				} else {
					et_year.setVisibility(View.GONE);
					underline.setVisibility(View.GONE);
					money = moneytype[position];
					payactivity.tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), money)));
				}
				popupWindow.dismiss();
			}
		}, years);

		listView.setAdapter(yearAdapter);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(v);
	}

	int money = 10;
	int moneytype[] = { 10, 20, 30, 40, 50 };
	int moneytypesss[] = { 1, 2, 3, 4, 5 };
	private PayActivity payactivity;
	private int type;
	private PaySuccessReceiver paySuccessReceiver;

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#setAttribute()
	 */
	@Override
	protected void setAttribute() {

		et_year.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (et_year.isFocused()) {

					String str = s.toString().trim();
					if (str.length() == 0) {
						money = 0;
					} else {
						money = Integer.parseInt(str) * 10;
					}
					payactivity.tv_should_pay.setText(Html.fromHtml(String.format(getString(R.string.should_pay), money)));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		activity.unregisterReceiver(paySuccessReceiver);
		super.onDestroy();
	}

	/**
	 * @param pwd
	 *            激活币激活
	 *            doPostActivation(这里用一句话描述这个方法的作用)
	 *            void
	 * 
	 * @exception
	 */
	private void doPostActivation(String pwd) {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		list.add(MD5.getMessageDigest(pwd.getBytes()));
		JSONObject jsonRequest = new JSONObject();
		try {
			// post方式访问，post提交参数：dataJson包含值：
			// userid；激活用户id(可以是自己id，也可以是他人id)
			// year：缴费年份（数字类型，如果是趸交，传值1000）
			// money：缴费金额（浮点数字类型）"
			if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
				jsonRequest.put("userId", userInfoPreferences.getUsercardId());
			} else {
				jsonRequest.put("userId", userInfoPreferences.getHeruniqueid());
			}
			jsonRequest.put("year", money / 10);
			jsonRequest.put("money", money);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(activity, HttpUrlConstants.ACTIVATE_CURRENCY_INDENT, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				userInfoPreferences.setHeruniqueid("");
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					if (type == Constent.RENEW) {
						MyToast.makeText(activity, R.string.renew_success, Toast.LENGTH_LONG).show();
					} else {
						MyToast.makeText(activity, R.string.zhifu1, Toast.LENGTH_LONG).show();
					}
					new UserInfoPreferences(activity).setUserStatus(Constants.SHARE_ACTIVED);
					EventBus.getDefault().post(new MineUpdateInfo());
					activity.setResult(0x12123);
					activity.finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response2.getCode(), response2.getMessage());
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

}
