package com.earthman.app.ui.activity.login;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_forget_psw)
public class FindPswActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.tv_title)
	TextView mTVTitleCenter;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.foeget_psw_commit)
	Button mBtnCommit;
	@ViewInject(R.id.forget_psw_psw_1)
	EditText mEtPsw_1;
	@ViewInject(R.id.forget_psw_psw_2)
	EditText mEtPsw_2;
	@ViewInject(R.id.forget_psw_code)
	EditText mEtCode;
	@ViewInject(R.id.forget_psw_getcode)
	TextView mTvGetCode;
	@ViewInject(R.id.forgetpsw_phone)
	EditText mEtPhone;
	
	private TimeCount time;
	private String phone_numStr;
	private String codeStr;
	private String psw_1Str;
	private String psw_2Str;

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_psw_getcode:
			if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
				MyToast.makeText(FindPswActivity.this, R.string.Lost_psw_phone, Toast.LENGTH_LONG).show();
				return;
			}
			doGetSmsCode();
			
//			mEtCode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao_bg));
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.foeget_psw_commit:
			getDataFromUI();
			if (TextUtils.isEmpty(phone_numStr) || TextUtils.isEmpty(codeStr) || TextUtils.isEmpty(psw_1Str) || TextUtils.isEmpty(psw_2Str)) {
				MyToast.makeText(FindPswActivity.this, R.string.Lost_psw_toast, Toast.LENGTH_LONG).show();
				return;
			}
			if (!psw_1Str.equals(psw_2Str)) {
				MyToast.makeText(FindPswActivity.this, R.string.regist_toast2, Toast.LENGTH_LONG).show();
				return;
			}
			doGetLostPsw();
			break;
		default:
			break;
		}
	}

	/**
	 * 执行忘记密码找回请求
	 */
	private void doGetLostPsw() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone_numStr);
		list.add(codeStr);
//		list.add(MD5.getMessageDigest(psw_1Str.getBytes()));
		list.add(psw_1Str);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.LOST_PSW, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(FindPswActivity.this, R.string.psw_reset_success, Toast.LENGTH_LONG).show();
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(FindPswActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 获取输入数据
	 */
	private void getDataFromUI() {
		phone_numStr = mEtPhone.getText().toString().trim();
		codeStr = mEtCode.getText().toString().trim();
		psw_1Str = mEtPsw_1.getText().toString().trim();
		psw_2Str = mEtPsw_2.getText().toString().trim();
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
			mTvGetCode.setText(getString(R.string.get_code));
			mTvGetCode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao));
			mTvGetCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			mTvGetCode.setClickable(false);// 防止重复点击

			mTvGetCode.setText(millisUntilFinished / 1000 + getString(R.string.code_second));
		}
	}

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		time = new TimeCount(60000, 1000);
		mTVTitleCenter.setText(getResources().getString(R.string.find_psw_title));
		mBtnTitleLeft.setOnClickListener(this);
		mTvGetCode.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {

	}

	private void doGetSmsCode() {
		String phone = mEtPhone.getText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.LOST_PSW, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					time.start();
					MyToast.makeText(FindPswActivity.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(FindPswActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}
}
