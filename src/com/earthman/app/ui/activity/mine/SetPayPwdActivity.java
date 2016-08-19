package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.MD5;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-18 下午8:52:25
 * @Decription
 */
@ContentView(R.layout.set_paypwd)
public class SetPayPwdActivity extends BaseActivity{

	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.et_pwd)
	private EditText et_pwd;
	@ViewInject(R.id.et_pwd_sure)
	private EditText et_pwd_sure;
	
	@Override
	protected void initData() {
		
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}
	
	@Override
	protected void handclick(View v) {
		
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View view){
		this.finish();
	}
	
	@OnClick(R.id.modify_btn)
	private void onMoidfyClick(View view){
		if(checkData()){
			doSetPayPwd();
		}		
	}
	
	private boolean checkData(){
		String pwd = et_pwd.getText().toString().trim();
		String pwd_sure = et_pwd_sure.getText().toString().trim();
		if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_sure)){
			MyToast.makeText(SetPayPwdActivity.this, R.string.paypwd_inform, Toast.LENGTH_LONG).show();
			return false;
		}else if(!pwd.equals(pwd_sure)){
			MyToast.makeText(SetPayPwdActivity.this, R.string.regist_toast2, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private void doSetPayPwd(){
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		final String payPwd = et_pwd.getText().toString().trim();
		preferences.setUserPayPsw(payPwd);
		list.add(preferences.getUserID());
		list.add(MD5.getMessageDigest(payPwd.getBytes()));
		list.add(preferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.SETPAYPWD, list);
		executeRequest(new FastJsonRequest<CommonResponse>(Method.GET, getUrl, CommonResponse.class,
				new Response.Listener<CommonResponse>() {

			@Override
			public void onResponse(CommonResponse response) {
				myLoadingDialog.dismiss();
				if ("000000".equals(response.getCode())) {		
					MyToast.makeText(SetPayPwdActivity.this, R.string.set_paypwd_success, Toast.LENGTH_LONG).show();
					
					preferences.setIsSetPwd(1);
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(SetPayPwdActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_title.setText(R.string.set_paypwd);
	}

}
