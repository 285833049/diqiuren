package com.earthman.app.ui.activity.bankcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.FindFriendResponse;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者：Zhou
 * 日期：2016-3-3 下午8:57:55
 * 描述：（绑定银行卡验证手机号页面）
 */
@ContentView(R.layout.verify_mobile)
public class VerifyMobileActivity extends BaseActivity{

	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.phone)
	private TextView phone;
	@ViewInject(R.id.getcode)
	private Button  getcode;
	@ViewInject(R.id.et_smscode)
	private EditText et_smscode;
	private String bankcardNum;
	private String idNum;
	private String customerNm;
	private String phoneNo;
	
	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		time = new TimeCount(60000, 1000);
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		this.finish();
	}
	
	@OnClick(R.id.btn_next)
	private void onNextClick(View v){
		doBindCard();
	}
	
	private void doBindCard(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("pan", bankcardNum);
			jsonRequest.put("certifId", idNum);
			jsonRequest.put("customerNm", customerNm);
			jsonRequest.put("phoneNo", phoneNo);
			jsonRequest.put("smsCode", et_smscode.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.BIND_CARD, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				FindFriendResponse response2 = JSON.parseObject(response.toString(), FindFriendResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					
				} else {
					NetStatusHandUtil.getInstance().handStatus(VerifyMobileActivity.this, response2.getCode(),response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getUrl, jsonRequest, listener, getErrorListener()) {

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
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.add_bankcard);
		Intent intent = getIntent();
		if(intent != null){
			bankcardNum = intent.getStringExtra("bankcardNum");
			idNum = intent.getStringExtra("idNum");
			customerNm = intent.getStringExtra("customerNm");
			phoneNo = intent.getStringExtra("phoneNo");			
		}
		phone.setText(String.format(getString(R.string.input_smscode), AndroidUtils.getHideMobileNum(phoneNo)));
	}
	
	@OnClick(R.id.getcode)
	private void doGetCode(View v){
		time.start();
		getcode.setTextColor(getResources().getColor(R.color.black5));
	}
	
	
	private TimeCount time;

	/**
	 * 验证码倒计时
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onFinish() {// 计时完毕
			getcode.setText(R.string.reget_code);
			getcode.setTextColor(getResources().getColor(R.color.tab_text_hover));
			getcode.setClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			getcode.setClickable(false);// 防止重复点击
			getcode.setText(millisUntilFinished / 1000 + getString(R.string.code_second));
		}
	}

}
