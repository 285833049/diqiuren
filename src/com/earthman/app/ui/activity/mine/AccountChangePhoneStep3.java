package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.Html;
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
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ChangePhoneCode
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月8日
 */
@ContentView(R.layout.changephone_code)
public class AccountChangePhoneStep3 extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//-----------
	@ViewInject(R.id.change_phoen_next3)
	Button mBtnNext;
	@ViewInject(R.id.code)
	EditText mEtcode;
	@ViewInject(R.id.tv_get_code_again)
	TextView mTvGetCode;
	@ViewInject(R.id.change_phone_tv)
	TextView mTvNewPhone;
	private TimeCount mTimeCount;
	private String phone;
	private int resultcode=0x003;
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.change_phoen_next3:
			if (TextUtils.isEmpty(mEtcode.getText().toString().trim())) {
				MyToast.makeText(AccountChangePhoneStep3.this, R.string.input_smscode1, Toast.LENGTH_LONG).show();
				return;
			}
			doGetCommit();

			break;
		case R.id.tv_get_code_again:
			doGetSmsCode();
			mTimeCount.start();
			break;
		default:
			break;
		}
	}

	private void doGetSmsCode() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_PHONE_CODE, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(AccountChangePhoneStep3.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AccountChangePhoneStep3.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			phone = (String) getIntent().getExtras().get("NEWPHONE");
		}
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTimeCount = new TimeCount(60000, 1000);
		mTvNewPhone.setText(Html.fromHtml(String.format(getString(R.string.change_phone_code_tips), phone)));
		mTvTitleLeft.setText(getString(R.string.change_phone_title));
		mBtnTitleLeft.setOnClickListener(this);
		mTvGetCode.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

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
			mTvGetCode.setText(getString(R.string.getcode_again));
			mTvGetCode.setClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			mTvGetCode.setClickable(false);// 防止重复点击
			mTvGetCode.setText(millisUntilFinished / 1000 + getString(R.string.code_second));
		}
	}

	private void doGetCommit() {
		String authcode = mEtcode.getEditableText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		list.add(authcode);
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_MDFPHONE, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					preferences.setUserPhone(phone);
					alert();
				}else {
					NetStatusHandUtil.getInstance().handStatus(AccountChangePhoneStep3.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

	private void alert() {
		final DialogOK myDialog = new DialogOK(AccountChangePhoneStep3.this, getString(R.string.dialog_title), getString(R.string.dialog_content));
		myDialog.setCancelable(false);
		myDialog.show();
		Button commit = (Button) myDialog.getView();
		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
				setResult(resultcode);
				finish();
			}
		});
	}

}
