package com.earthman.app.ui.fragment.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.MineUpdateInfo;
import com.earthman.app.bean.TotalFreeResponse;
import com.earthman.app.constant.Constent;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.mine.RegistReplacePay;
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

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou 日期：2016-2-26 下午2:29:22 描述：（）
 */
public class LifePayFragment extends BaseFragment {

	@ViewInject(R.id.zhongsheng)
	private TextView zhongsheng;
	private UserInfoPreferences userInfoPreferences;
	int paytype;
	private PaySuccessReceiver paySuccessReceiver;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what > 0) {
				PayActivity activity2 = (PayActivity) activity;
				activity2.setResult(0x1212);// 关闭支付界面
				activity2.finish();

			}
		};
	};

	@Override
	protected View createView() {
		View view = LayoutInflater.from(activity).inflate(R.layout.lifepay_fragment, null);
		return view;
	}

	@Override
	protected void initView(View convertView) {
		ViewUtils.inject(this, convertView);
		final PayActivity payactivity = (PayActivity) getActivity();
		Button pay = payactivity.mBtnLifePay;
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
					if (type == Constent.REGIST_TO_SELF ) {
						Intent intent = new Intent(activity, RegistReplacePay.class);
						intent.putExtra("money", totalFee);
						intent.putExtra("paymentType", Constants.LIFEPAY);
						intent.putExtra("paymentMethod", Constants.PAYSTYLE_ACTIVATE);
						intent.putExtra("month", 0);
						startActivityForResult(intent, 12);
					} else if (type == Constent.REGIST_TO_FAMILY || type == Constent.REGIST_TO_FRIEND|| type == Constent.RENEW) {
						if (0 == userInfoPreferences.getIsSetPwd()) {
							PayActivity BaseActivity = (PayActivity) activity;
							BaseActivity.showSetPayPwdAlert();
						} else {
							PayPwdDialog payPwdDialog = new PayPwdDialog(activity, totalFee, new OnSurePayClickListener() {
								@Override
								public void onSurePayClick(int pageType, String pwd) {
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

				} else if (2 == Pay_method) {// 地球币代付
					if (type == Constent.REGIST_TO_SELF) {
						Intent intent = new Intent(activity, RegistReplacePay.class);
						intent.putExtra("money", totalFee);
						intent.putExtra("paymentType", Constants.LIFEPAY);//缴费方式（0:趸缴，1:年缴）
						intent.putExtra("paymentMethod", Constants.PAYSTYLE_EARTH);//支付方式：（0:激活币；1:地球币:2:微信；3:支付宝:4:银联:5:paypal）
						intent.putExtra("month", 0);
						startActivityForResult(intent, 12);
					} else if (type == Constent.REGIST_TO_FAMILY || type == Constent.REGIST_TO_FRIEND || type == Constent.RENEW) {
						if (0 == userInfoPreferences.getIsSetPwd()) {
							PayActivity BaseActivity = (PayActivity) activity;
							BaseActivity.showSetPayPwdAlert();
						} else {
							PayPwdDialog payPwdDialog = new PayPwdDialog(activity, totalFee, new OnSurePayClickListener() {
								@Override
								public void onSurePayClick(int pageType, String pwd) {
									doPostCoinPay(pwd);
								}
							});
							payPwdDialog.setPayWay(BankConstants.PAY_WAY_DIQIUBI);
							payPwdDialog.setCancelable(false);
							payPwdDialog.dismissChange();
							payPwdDialog.show();
						}
					}
				} else if (3 == Pay_method) {// 微信
					String year = "" + 1000;
					if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
						new PayFactory(activity, PayFactory.CASE_ACTIVE_SELF, PayFactory.PAYTYPE_WEIXIN, totalFee).genActiveRequest(year, "");
					} else if (type == Constent.REGIST_TO_FAMILY) {
						new PayFactory(activity, PayFactory.CASE_ADDQINQING, PayFactory.PAYTYPE_WEIXIN, totalFee).genActiveRequest(year, "");
					} else if (type == Constent.REGIST_TO_FRIEND) {
						new PayFactory(activity, PayFactory.CASE_REGISTER_FOR_FRIEND, PayFactory.PAYTYPE_WEIXIN, totalFee).genActiveRequest(year, "");
					}

				} else if (4 == Pay_method) {// 支付宝
					String year = "" + 1000;
					if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
						new PayFactory(activity, PayFactory.CASE_ACTIVE_SELF, PayFactory.PAYTYPE_ALIPAY, totalFee).genActiveRequest(year, "");
					} else if (type == Constent.REGIST_TO_FAMILY) {
						new PayFactory(activity, PayFactory.CASE_ADDQINQING, PayFactory.PAYTYPE_ALIPAY, totalFee).genActiveRequest(year, "");
					} else if (type == Constent.REGIST_TO_FRIEND) {
						new PayFactory(activity, PayFactory.CASE_REGISTER_FOR_FRIEND, PayFactory.PAYTYPE_ALIPAY, totalFee).genActiveRequest(year, "");
					}
				} else if (5 == Pay_method) {
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
			jsonRequest.put("year", 1000);
			jsonRequest.put("money", totalFee);
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

	private void doPostCoinPay(String psw) {
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
			jsonRequest.put("year", 1000);
			jsonRequest.put("money", totalFee);
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
		doGetLifePay(userInfoPreferences);
	}

	private String totalFee;
	private int type;

	/**
	 * 获取终身服务费多少
	 */
	private void doGetLifePay(UserInfoPreferences userInfoPreferences) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (type == Constent.REGIST_TO_SELF || type == Constent.RENEW) {
			list.add(userInfoPreferences.getUserID());
		}
		if (type == Constent.REGIST_TO_FAMILY || type == Constent.REGIST_TO_FRIEND) {
			list.add(userInfoPreferences.getUserpayID());
		}
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.MY_LIFE_TOTAL_MONEY, list);
		executeRequest(new FastJsonRequest<TotalFreeResponse>(Method.GET, loginUrl, TotalFreeResponse.class, new Response.Listener<TotalFreeResponse>() {

			@Override
			public void onResponse(TotalFreeResponse response) {
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					LogUtil.i("终生服务费", response.toString());
					totalFee = response.getResult().getTotalFee();
					zhongsheng.setText(Html.fromHtml(String.format(getString(R.string.life_service_pay), totalFee)));
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	/*
	 * @see com.ywb.earthman.app.base.BaseFragment#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
	}

	public void runOnUiThread(Runnable runnable) {

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		activity.unregisterReceiver(paySuccessReceiver);
		super.onDestroy();
	}

}
