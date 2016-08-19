package com.earthman.app.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.widget.MyToast;

/**
 * @Title: LoginOutUtils
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月16日
 */
/// account/checkPaypwdset
public class LoginOutUtils {

	public static void loginout(final UserInfoPreferences userInfoPreferences, final Activity activity) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.MY_LIFE_TOTAL_MONEY, list);
		new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					// 退出登录成功,清除数据
					userInfoPreferences.setIsLogin(true);
					userInfoPreferences.clearData();
					userInfoPreferences.setIsfirstLogin(false);
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(),response.getMessage());
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(activity, R.string.error_request_inform, Toast.LENGTH_LONG).show();
			}
		});
	}

	private UserInfoPreferences userInfoPreferences;
	private Context context;

	public LoginOutUtils(final UserInfoPreferences userInfoPreferences, final Context context) {
		this.userInfoPreferences = userInfoPreferences;
		this.context = context;
	}

}
