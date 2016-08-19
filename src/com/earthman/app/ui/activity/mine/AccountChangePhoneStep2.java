package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ChangePhoneNewPhoen
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月8日
 */
@ContentView(R.layout.activity_change_phone_newphone)
public class AccountChangePhoneStep2 extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//
	@ViewInject(R.id.change_phoen_next2)
	Button mBtnNext;
	@ViewInject(R.id.change_phone_choise_address)
	RelativeLayout mRlChoiseAddress;
	@ViewInject(R.id.change_phone_new_phone)
	EditText mEtNewPhone;
	@ViewInject(R.id.phone_code)
	TextView mPhoneAddressCode;
	private int requestCode=0x001;
	private int requestCodeNext=0x002;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.change_phoen_next2:
			String phone = mEtNewPhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				MyToast.makeText(AccountChangePhoneStep2.this, R.string.change_phone_edit_new_phone, Toast.LENGTH_LONG).show();
				return;
			}
			doGetSmsCode(phone);//获取验证码
			break;
		case R.id.change_phone_choise_address:
			startActivityForResult(new Intent(this, CountryChoiseActivity.class), requestCode);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent intent) {

		if (intent != null && requestcode == requestCode) {
			LogUtil.i("onactivityresult", "" + intent.getExtras().getString("phonecode"));
			mPhoneAddressCode.setText(intent.getExtras().getString("phonecode"));
		}
		if (resultcode == requestCodeNext) {
			setResult(requestCodeNext);
			finish();
		}

	}

	
	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleLeft.setOnClickListener(this);
		mTvTitleLeft.setText(getString(R.string.change_phone_title));
		mBtnNext.setOnClickListener(this);
		mRlChoiseAddress.setOnClickListener(this);

	}

	
	@Override
	protected void setAttribute() {

	}

	private void doGetSmsCode(final String phone) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_PHONE_CODE, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(AccountChangePhoneStep2.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
					Intent intent = new Intent(AccountChangePhoneStep2.this, AccountChangePhoneStep3.class);
					intent.putExtra("NEWPHONE", phone);// 新手机号码
					startActivityForResult(intent, requestCodeNext);
				} else {
					NetStatusHandUtil.getInstance().handStatus(AccountChangePhoneStep2.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

}
