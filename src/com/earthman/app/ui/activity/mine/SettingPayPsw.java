package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
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
import com.earthman.app.utils.MD5;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 修改支付密码
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.payment_pwd_modify)
public class SettingPayPsw extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------
	@ViewInject(R.id.ForgetPayPsw)
	TextView mTvForgetPayPsw;
	@ViewInject(R.id.modify_btn)
	Button mBtnCommit;
	@ViewInject(R.id.pay_psw_old)
	EditText mEtOldPsw;
	@ViewInject(R.id.pay_psw_new1)
	EditText mEtNewPsw1;
	@ViewInject(R.id.pay_psw_new2)
	EditText mEtNewPsw2;
	private String oldPsw;
	private String newPsw1;
	private String newPsw2;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.ForgetPayPsw:
			startActivity(new Intent(this, ForgetPasswordActivity.class));
			break;
		case R.id.modify_btn:
			getDataFromUI();
			if (TextUtils.isEmpty(oldPsw) || TextUtils.isEmpty(newPsw1) || TextUtils.isEmpty(newPsw2)) {
				MyToast.makeText(this, R.string.toast1, Toast.LENGTH_LONG).show();
				return;
			}
			if (!newPsw1.equals(newPsw2)) {
				MyToast.makeText(this, R.string.regist_toast2, Toast.LENGTH_LONG).show();
				return;
			}
			doGetModiffyPsw();
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	/*
	 * @see com.earthman.app.base.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (preferences.getIsSetPwd() == 0) {
			showSetPayPwdAlert();
		}
	}

	/**
	 * 获取输入数据
	 */
	private void getDataFromUI() {
		oldPsw = mEtOldPsw.getText().toString().trim();
		newPsw1 = mEtNewPsw1.getText().toString().trim();
		newPsw2 = mEtNewPsw2.getText().toString().trim();
	}

	/**
	 * 修改支付密码
	 */
	private void doGetModiffyPsw() {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(MD5.getMessageDigest(oldPsw.getBytes()));
		list.add(MD5.getMessageDigest(newPsw1.getBytes()));
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MODIFIED_PAY_PSW, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				dismissLoading();
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					showMyDialog();
				} else {
					NetStatusHandUtil.getInstance().handStatus(SettingPayPsw.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvForgetPayPsw.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
		mBtnTitleLeft.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.payment_password_modification);
	}

	private void showMyDialog() {
		DialogOK myDialog = new DialogOK(this, getString(R.string.dialog_title), getString(R.string.modifyPswSuccess));
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		myDialog.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingPayPsw.this.finish();
			}
		});
	}
}
