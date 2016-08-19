package com.earthman.app.ui.activity.bankcard;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.MD5;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.passwordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-6-12 下午3:17:13
 * @Decription 支付密码校验页面
 */
@ContentView(R.layout.paypwdverify)
public class PayPwdVerifyActivity extends BaseActivity{

	@ViewInject(R.id.gridpwdview)
	private GridPasswordView gridpwdview;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	
	
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.verify_paypwd);
		gridpwdview.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
			
			@Override
			public void onTextChanged(String psw) {
			     
			}
			
			@Override
			public void onInputFinish(String psw) {
				 startActivity(new Intent(PayPwdVerifyActivity.this, AddCardStepOneActivity.class));//跳转到绑卡页面
				 //doVerifyGridPwd(psw);		
			}
		});
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}
	
	/**
	 * 
	 * doVerifyGridPwd(验证支付密码)
	 * void
	 * @exception
	 */
	private void doVerifyGridPwd(String psw){
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());		
		list.add(preferences.getUserToken());		
		list.add(MD5.getMessageDigest(psw.getBytes()));
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.VERIFY_PAYPWD, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<CommonResponse>(Method.GET, loginUrl, CommonResponse.class, new Response.Listener<CommonResponse>() {
			@Override
			public void onResponse(CommonResponse response) {
				dismissLoading();
				if ("000000".equals(response.getCode())) {
					//支付密码验证成功
					LogUtil.i(TAG, response.toString());
					startActivity(new Intent(PayPwdVerifyActivity.this, AddCardStepOneActivity.class));//跳转到绑卡页面
				} else {
					NetStatusHandUtil.getInstance().handStatus(PayPwdVerifyActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}
	
	

}
