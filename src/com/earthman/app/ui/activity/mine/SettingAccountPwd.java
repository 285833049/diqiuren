package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
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
import com.earthman.app.ui.activity.login.FindPswActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PassWordUtils;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 帐户密码修改
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.account_pwd_modify)
public class SettingAccountPwd extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------修改用户密码-----------------
	@ViewInject(R.id.forget_password_btn)
	TextView mTvForgetPsw;
	@ViewInject(R.id.modify_pwd)
	Button mBtnCommit;
	@ViewInject(R.id.modify_psw1_new)
	EditText mEtNewPsw1;
	@ViewInject(R.id.modify_psw2_new)
	EditText mEtNewPsw2;
	@ViewInject(R.id.psw_modify_old)
	EditText mEtOldPsw;
	private String oldPswStr;
	private String newPsw1Str;
	private String newPsw2Str;

	@Override
	protected void handclick(View v) {

		switch (v.getId()) {
		case R.id.forget_password_btn:// 找回密码
			startActivity(new Intent(this, FindPswActivity.class));
			break;
		case R.id.modify_pwd:
			getDataFromUi();
			if (!newPsw1Str.equals(newPsw2Str)) {
				MyToast.makeText(SettingAccountPwd.this, R.string.regist_toast2, Toast.LENGTH_LONG).show();
				return;
			}
			if (!PassWordUtils.isPassWord(newPsw1Str)) {
				MyToast.makeText(SettingAccountPwd.this, R.string.regist_toast4, Toast.LENGTH_LONG).show();
				return;
			}
			doGetChangePsw();// 修改密码
			break;
		case R.id.btn_back:// 返回
			this.finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 请求服务器修改密码
	 */
	private void doGetChangePsw() {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		// list.add(MD5.getMessageDigest(pswoldStr.getBytes()));
		list.add(oldPswStr);// 涉及环信的使用明文密码
		// list.add(MD5.getMessageDigest(new_psw1.getBytes()));
		list.add(newPsw1Str);// 涉及环信的使用明文密码
		list.add(preferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MODIFY_PSW, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				myLoadingDialog.dismiss();
				if ("000000".equals(response.getCode())) {
					showMyDialog();
				} else {
					NetStatusHandUtil.getInstance().handStatus(SettingAccountPwd.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 获取输入数据
	 */
	private void getDataFromUi() {
		oldPswStr = mEtOldPsw.getText().toString().trim();
		newPsw1Str = mEtNewPsw1.getText().toString().trim();
		newPsw2Str = mEtNewPsw2.getText().toString().trim();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvForgetPsw.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
		mBtnTitleLeft.setOnClickListener(this);

	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.account_password_modification);
	}

	private void showMyDialog() {
		final DialogOK myDialog = new DialogOK(this, getString(R.string.dialog_title), getString(R.string.modifyPswSuccess));
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		Button commit = (Button) myDialog.getView();
		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
				SettingAccountPwd.this.finish();
			}
		});
	}
}
