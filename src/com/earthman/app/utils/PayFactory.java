package com.earthman.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.PaymentOrderInfo;
import com.earthman.app.bean.PaymentOrderInfo.PaymentOrderResult;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.mine.MinePhoneRecharge;
import com.earthman.app.utils.alipay.AliPay;
import com.earthman.app.utils.wxapi.WeiXinPay;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyToast;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-28 下午2:32:58
 * @Decription 支付工厂类
 */
public class PayFactory {

	public static final int CASE_ACTIVE_SELF = 1;// 激活自己的账户
	public static final int CASE_ACTIVE_FRIEND = 2;// 代替好友激活
	public static final int CASE_REGISTER_FOR_FRIEND = 3;// 代替好友注册
	public static final int CASE_PAY_FOR_LOOK = 4;// 付费查看
	public static final int CASE_ADDQINQING = 5;// 添加亲情号
	public static final int CASE_JINGXIAN = 6;// 敬献
	public static final int CASE_PAY_FOR_PHONE = 7;// 话费充值
	public static final int CASE_SEE_VIDEO_ALBUM = 8;// 查看相册或视频

	public static final int PAYTYPE_ACTIVATION = 1;// 激活币支付
	public static final int PAYTYPE_EARTH = 2;// 地球币支付
	public static final int PAYTYPE_WEIXIN = 3;// 微信支付
	public static final int PAYTYPE_ALIPAY = 4;// 支付宝支付
	public static final int PAYTYPE_YINLIAN = 5;// 银联支付
	public static final int PAYTYPE_PAYPAL = 6;// paypal支付

	private Context mContext;
	private UserInfoPreferences userInfoPreferences;
	private int currentPayCase;
	private int currentPayType;
	private String money;
	private JSONObject jsonRequest;
	private ArrayList<Object> list;
	private String postUrl;
	private String getUrl;
	private DialogCommon dialog;

	/**
	 * 
	 * 创建一个新的实例 PayFactory.
	 * 
	 * @param context
	 * @param payCase
	 * @param payType
	 * @param money
	 */
	public PayFactory(Context context, int payCase, int payType, String money) {
		mContext = context;
		userInfoPreferences = new UserInfoPreferences(mContext);
		currentPayCase = payCase;
		Constants.currentPayReason = currentPayCase;
		currentPayType = payType;
		this.money = money;
		jsonRequest = new JSONObject();
		list = new ArrayList<Object>();
	}

	/**
	 * 
	 * genActiveRequest(生成激活账户、代替好友注册、添加亲情号相关的请求参数)
	 * 
	 * @param year
	 * @param payPwd
	 *            void
	 * @exception
	 */
	public void genActiveRequest(String year, String payPwd) {
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		switch (currentPayType) {
		case PAYTYPE_ACTIVATION:// 激活币
			if (!TextUtils.isEmpty(payPwd)) {
				list.add(MD5.getMessageDigest(payPwd.getBytes()));
			}
			postUrl = HttpUrlConstants.getPostUrl(mContext,
					HttpUrlConstants.QUERY_FOR_MEPAY, list);
			break;
		case PAYTYPE_EARTH:// 地球币
			if (!TextUtils.isEmpty(payPwd)) {
				list.add(MD5.getMessageDigest(payPwd.getBytes()));
			}
			postUrl = HttpUrlConstants.getPostUrl(mContext,
					HttpUrlConstants.ACTIVE_PAYBY_EARTHCOIN, list);
			break;
		case PAYTYPE_WEIXIN:// 微信
		case PAYTYPE_ALIPAY:// 支付宝
			list.add(currentPayType == PAYTYPE_WEIXIN ? 1 : 2);
			postUrl = HttpUrlConstants.getPostUrl(mContext,
					HttpUrlConstants.ACTIVE_PAY, list);
			break;
		default:
			break;
		}
		try {
			switch (currentPayCase) {
			case CASE_ACTIVE_SELF:// 激活账户
				jsonRequest.put("userId", userInfoPreferences.getUserID());
				break;
			case CASE_REGISTER_FOR_FRIEND:// 替好友注册
			case CASE_ADDQINQING:// 亲情号
				jsonRequest.put("userId", userInfoPreferences.getUserpayID());
				break;
			default:
				break;
			}
			jsonRequest.put("year", year);
			jsonRequest.put("money", money);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		doPost();
	}

	/**
	 * 
	 * genPayPhoneRequest(生成话费充值相关请求参数)
	 * 
	 * @param phonenumber
	 * @param paypasswd
	 *            void
	 * @exception
	 */
	public UserInfoPreferences preferences;

	public void genPayPhoneRequest(String phonenumber, String payPwd) {
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		list.add(phonenumber);
		list.add(money);
		if (currentPayType == 4 || currentPayType == 3) {
		} else {
			if(mContext instanceof MinePhoneRecharge){
				list.add(0);//地球幣支付
			}else{
				list.add(currentPayType);
			}
		}
		switch (currentPayType) {
		case PAYTYPE_EARTH:// 地球币
			if (payPwd == null || payPwd.equals("")) {
				PayActivity BaseActivity = (PayActivity) mContext;
				BaseActivity.showSetPayPwdAlert();
			} else {
				list.add(MD5.getMessageDigest(payPwd.getBytes()));
				break;
			}
		case PAYTYPE_WEIXIN:// 微信
		case PAYTYPE_ALIPAY:// 支付宝

			list.add(currentPayType == PAYTYPE_WEIXIN ? 1 : 2);
			// postUrl = HttpUrlConstants.getPostUrl(mContext,
			// HttpUrlConstants.ACTIVE_PAY, list);
			list.add("1");
			break;
		default:
			break;
		}
		if(list.size()==5)list.add(MD5.getMessageDigest(payPwd.getBytes()));
		getUrl = HttpUrlConstants.getUrl(mContext,
				HttpUrlConstants.CHONGZHI_HUAFEI, list);
		doGet();
	}

	/**
	 * 
	 * genUserInfoPayRequest(生成付费查看相关的请求参数)
	 * 
	 * @param paypasswd
	 * @param userId
	 * @param userInfoType
	 *            void
	 * @exception
	 */
	public void genUserInfoPayRequest(String paypasswd, int userId,
			String userInfoType) {
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		try {
			jsonRequest.put("userId", userId);// 好友id
			jsonRequest.put("money", money);
			if (currentPayType == 3) {// 微信
				jsonRequest.put("paytype", 1);
			} else if (currentPayType == 4) {// 支付宝
				jsonRequest.put("paytype", 2);
			} else {
				jsonRequest.put("paytype", 0);
			}

			jsonRequest.put("userInfoType", userInfoType);
			if (currentPayType == 0 || currentPayType == 2) {
				jsonRequest.put("paypasswd",
						MD5.getMessageDigest(paypasswd.getBytes()));
			}
			// switch (currentPayType) {
			// case PAYTYPE_EARTH:// 地球币
			// jsonRequest.put("paypasswd",
			// MD5.getMessageDigest(paypasswd.getBytes()));
			// break;
			// default:
			// break;
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		postUrl = HttpUrlConstants.getPostUrl(mContext,
				HttpUrlConstants.USERINFO_PAY, list);
		doPost();
	}

	/**
	 * 
	 * genJingxuanRequest(生成敬献香花的请求参数)
	 * 
	 * @param deadid
	 * @param amount
	 * @param tributeid
	 * @param paypasswd
	 *            void
	 * @exception
	 */
	public void genJingxuanRequest(String deadid, int amount, String tributeid,
			String paypasswd) {
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		try {
			jsonRequest.put("deadid", deadid);// 逝者用户id
			jsonRequest.put("money", money);
			jsonRequest.put("paytype", currentPayType);
			jsonRequest.put("tributeid", tributeid);// 鲜花id
			jsonRequest.put("amount", amount);
			switch (currentPayType) {
			case PAYTYPE_EARTH:// 地球币
				jsonRequest.put("paypasswd",
						MD5.getMessageDigest(paypasswd.getBytes()));
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		postUrl = HttpUrlConstants.getPostUrl(mContext,
				HttpUrlConstants.PRESENTED, list);
		doPost();
	}

	/**
	 * 
	 * genVideoAlbumRequest(这里用一句话描述这个方法的作用) void
	 * 
	 * @exception
	 */
	public void genVideoAlbumRequest(int type, int userId, int id,
			String paypasswd) {
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		list.add(type);
		try {
			jsonRequest.put("userId", userId);// 用户id
			jsonRequest.put("paytype", currentPayType);
			jsonRequest.put("id", id);// 朋友的id
			switch (currentPayType) {
			case PAYTYPE_EARTH:// 地球币
				jsonRequest.put("paypasswd",
						MD5.getMessageDigest(paypasswd.getBytes()));
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		postUrl = HttpUrlConstants.getPostUrl(mContext,
				HttpUrlConstants.PAYFORZONE, list);
		doPost();
	}

	/**
	 * 
	 * genPayForFriend(生成代替好友激活的请求参数)
	 * 
	 * @param id
	 * @param paypasswd
	 *            void
	 * @exception
	 */
	public void genPayForFriend(String id, String paypasswd) {
		UserInfoPreferences userInfo = new UserInfoPreferences(mContext);
		if (TextUtils.isEmpty(paypasswd)) {
			paypasswd = userInfo.getUserPayPsw();
		}
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		switch (currentPayType) {
		case PAYTYPE_ACTIVATION:// 激活币
			list.add(MD5.getMessageDigest(paypasswd.getBytes()));
			list.add(id);
			getUrl = HttpUrlConstants.getUrl(mContext,
					HttpUrlConstants.QUERY_FOR_MEPAY, list);
			break;
		case PAYTYPE_EARTH:// 地球币
			list.add(MD5.getMessageDigest(paypasswd.getBytes()));
			list.add(id);
			getUrl = HttpUrlConstants.getUrl(mContext,
					HttpUrlConstants.PAY_FOR_FRIENDS_DIQIUBI, list);
			break;
		case PAYTYPE_WEIXIN:// 微信
		case PAYTYPE_ALIPAY:// 支付宝
			list.add(id);
			list.add(currentPayType == PAYTYPE_WEIXIN ? 1 : 2);
			getUrl = HttpUrlConstants.getUrl(mContext,
					HttpUrlConstants.THIRDPAYFORFRIEND, list);
		default:
			break;
		}
		doGet();

	}

	private void handResponse(PaymentOrderInfo response) {
		if ("000000".equals(response.getCode())) {
			PaymentOrderResult result = response.getResult();
			switch (currentPayType) {
			case PAYTYPE_ACTIVATION:// 激活币支付
			case PAYTYPE_EARTH:// 地球币支付
				Toast.makeText(mContext,
						mContext.getString(R.string.pay_success),
						Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mContext.sendBroadcast(new Intent(
								Constants.PAY_SUCCESS_ACTION));
					}
				}, 1500);

				break;
			case PAYTYPE_WEIXIN:// 微信
				if (result == null) {
					return;
				}
				WeiXinPay.getInstance(mContext).dopostWxPay(
						result.getBody(),
						String.valueOf((int) (Float.parseFloat(result
								.getTotal_fee()) * 100)), result.getSubject(),
						result.getOut_trade_no());
				break;
			case PAYTYPE_ALIPAY:// 支付宝
				if (result == null) {
					return;
				}
				if (mContext instanceof Activity) {
					Activity activity = (Activity) mContext;
					AliPay.getInstance(activity).pay(
							result.getBody(),
							FormatNumberUtil.getInstance(
									FormatNumberUtil.TYPE_NUMBER).format(
									Double.parseDouble(result.getTotal_fee())),
							result.getSubject(), result.getOut_trade_no());
				}

				break;
			default:
				break;
			}
		} else {
			NetStatusHandUtil.getInstance().handStatus(mContext,
					response.getCode(), response.getMessage());
		}
	}

	private void showDownloadAppDilaog() {
		Resources resources = mContext.getResources();
		dialog = new DialogCommon(mContext, resources.getString(R.string.hint),
				resources.getString(R.string.uninstall_weixinapp),
				resources.getString(R.string.go_download),
				new CommonDialogListener() {

					@Override
					public void onRightButtonClick() {
						dialog.hide();
						Intent installIntent = new Intent(
								"android.intent.action.VIEW");
						installIntent.setData(Uri
								.parse("market://details?id=com.tencent.mm"));
						mContext.startActivity(installIntent);
					}
				});

		if (!((Activity) mContext).isFinishing())
			dialog.show();
	}

	/**
	 * 
	 * doPost(处理post请求) void
	 * 
	 * @exception
	 */
	private void doPost() {
		if (!AndroidUtils.isNetworkAvailable(mContext)) {
			return;
		}
		if (currentPayType == PAYTYPE_WEIXIN && !isWXAppInstalledAndSupported()) {
			// MyToast.makeText(mContext, R.string.uninstall_weixinapp,
			// Toast.LENGTH_SHORT).show();
			showDownloadAppDilaog();
			return;
		}
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				PaymentOrderInfo response2 = JSON.parseObject(
						response.toString(), PaymentOrderInfo.class);
				handResponse(response2);
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Method.POST, postUrl, jsonRequest, listener, getErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}
		};
		RequestManager.getInstance().addRequest(jsonObjectRequest, mContext);
	}

	/**
	 * 
	 * doGet(处理get请求) void
	 * 
	 * @exception
	 */
	private void doGet() {
		if (!AndroidUtils.isNetworkAvailable(mContext)) {
			return;
		}
		if (currentPayType == PAYTYPE_WEIXIN && !isWXAppInstalledAndSupported()) {
			showDownloadAppDilaog();
			return;
		}
		RequestManager.getInstance().addRequest(
				new FastJsonRequest<PaymentOrderInfo>(Method.GET, getUrl,
						PaymentOrderInfo.class,
						new Response.Listener<PaymentOrderInfo>() {

							@Override
							public void onResponse(PaymentOrderInfo response) {
								handResponse(response);
							}

						}, getErrorListener()), mContext);
	}

	private boolean isWXAppInstalledAndSupported() {
		IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null, true);
		msgApi.registerApp(Constants.WX_APPID);

		boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
				&& msgApi.isWXAppSupportAPI();

		return sIsWXAppInstalledAndSupported;
	}

	/**
	 * 处理网络请求失败时的监听器
	 * 
	 * @return
	 */
	protected Response.ErrorListener getErrorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(mContext, R.string.server_error,
						Toast.LENGTH_LONG).show();
			}
		};
	}

}
