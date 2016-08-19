package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 支付密码修改－》忘记支付密码第一步
 * 
 * @author xiexianyong
 * 
 */

@ContentView(R.layout.forget_pwd_activity)
public class ForgetPasswordActivity extends BaseActivity implements
		OnClickListener {
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.modify_pwd_getcode)
	TextView getcode;
	@ViewInject(R.id.next_btn)
	Button next_btn;
	private TimeCount time;
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	@ViewInject(R.id.et_smscode)
	private EditText et_smscode;

	@SuppressLint("NewApi")
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.modify_pwd_getcode:
			time.start();
			getcode.setBackground(getResources().getDrawable(
					R.drawable.rect_yuanjiao_bg));
             doGetSmsCode();
			break;
		case R.id.next_btn:
			String code = et_smscode.getText().toString();
			if(TextUtils.isEmpty(code)){
				Toast.makeText(ForgetPasswordActivity.this, R.string.input_smscode1, Toast.LENGTH_SHORT).show();
				return;
			}
			doValidateCode();
			break;

		default:
			break;
		}

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		time = new TimeCount(60000, 1000);
		getcode.setOnClickListener(this);
		next_btn.setOnClickListener(this);

	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.forget_paypwd);

	}
	
	private void doGetSmsCode() {
		String phone = et_phone.getText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		list.add(Constants.TYPE_FORGET_PAYPWD);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GENERATE_SMSCODE, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(ForgetPasswordActivity.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(ForgetPasswordActivity.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}
	
	private  void doValidateCode(){
		String code = et_smscode.getText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(Constants.TYPE_FORGET_PAYPWD);
		list.add(et_phone.getText().toString());		
		list.add(code);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.VALIDATE_SMSCODE, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPasswordThreesActivity.class);
					startActivity(intent);
					finish();
				} else {
					MyToast.makeText(ForgetPasswordActivity.this, R.string.smscode_validate_faile, Toast.LENGTH_LONG).show();
				}
			}
		}, getErrorListener()));
	}

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
			getcode.setText("获取验证码");
			getcode.setBackground(getResources().getDrawable(
					R.drawable.rect_yuanjiao));
			getcode.setClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			getcode.setClickable(false);// 防止重复点击
			getcode.setText(millisUntilFinished / 1000 + "s后再次获取");
		}
	}

}
