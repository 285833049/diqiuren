package com.earthman.app.manager;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-6-17 下午8:00:59
 * @Decription 好友管理类
 */
public class FriendManager extends BaseActivity {

	// 添加好友
	public static void addFriend(final Context context, String uniqueid) {
		UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(uniqueid);
		list.add(userInfoPreferences.getUserToken());
		list.add(Constants.ADD_FRIEND_TO_SOMEONE);
		String loginUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.ADD_FRIEND, list);

		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(context, R.string.add_friend2, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(context, response.getCode(), response.getMessage());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(context, R.string.error_request_inform, Toast.LENGTH_LONG).show();
			}
		});

		RequestManager.getInstance().addRequest(fastJsonRequest, context);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handclick(View v) {
		// TODO Auto-generated method stub

	}

}
