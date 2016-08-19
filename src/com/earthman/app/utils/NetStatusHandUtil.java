package com.earthman.app.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.ui.activity.login.LoginActivity;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-15 下午5:35:50
 * @Decription 网络状态处理
 */
public class NetStatusHandUtil {

	private static NetStatusHandUtil util;

	private DialogOK dialogOK;

	private NetStatusHandUtil() {

	}

	public static NetStatusHandUtil getInstance() {
		if (util == null) {
			util = new NetStatusHandUtil();
		}
		return util;
	}

	/**
	 * 
	 * handStatus(处理网络请求返回状态)
	 * 
	 * @param context
	 * @param code
	 *            void
	 * @exception
	 */
	public void handStatus(final Context context, String code, String message) {
		if (TextUtils.isEmpty(code)) {
			return;
		}
		switch (Integer.parseInt(code)) {
		case 000002:// 身份认证失败，返回重新登录
			dialogOK = new DialogOK(context, context.getResources().getString(R.string.wenxin_inform), context.getResources().getString(R.string.relogin_inform));
			dialogOK.setCanceledOnTouchOutside(false);
			dialogOK.getView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					doLogout(context);
				}
			});
			dialogOK.show();
			break;
		case 000001:// 请求失败
		case 000006:// 余额不足
			if (!TextUtils.isEmpty(message)) {
				MyToast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * doLogout(这里用一句话描述这个方法的作用)
	 * void
	 * 
	 * @exception
	 */
	private void doLogout(final Context context) {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		ArrayList<Object> list = new ArrayList<Object>();
		final UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
		list.add(userInfoPreferences.getUserID());
		String getUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.LOGIN_OUT, list);
		RequestManager.getInstance().addRequest(new FastJsonRequest<CommonResponse>(Method.GET, getUrl, CommonResponse.class, new Response.Listener<CommonResponse>() {

			@Override
			public void onResponse(CommonResponse response) {
				if ("000000".equals(response.getCode())) {
					userInfoPreferences.clearData();
					userInfoPreferences.setIsLogin(true);
					userInfoPreferences.setIsfirstLogin(false);

					Activity activity = (Activity) context;
					if (activity != null) {
						Intent intent = new Intent(activity, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						activity.startActivity(intent);
						activity.finish();
					}
				} else {
					NetStatusHandUtil.getInstance().handStatus(context, response.getCode(), response.getMessage());
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(context, R.string.error_request_inform, Toast.LENGTH_LONG).show();
			}
		}), "doLogout");
	}

	public void dimissDialog() {
		if (dialogOK != null && dialogOK.isShowing()) {
			dialogOK.dismiss();
		}
	}

}
