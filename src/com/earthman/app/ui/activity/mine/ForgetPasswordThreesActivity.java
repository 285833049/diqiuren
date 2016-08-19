package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

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
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 支付密码修改－》忘记支付密码第三步》请输入新密码
 * 
 * @author xiexianyong
 * 
 */

@ContentView(R.layout.forget_pwd_three)
public class ForgetPasswordThreesActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.modify_btn)
	Button modify;
	@ViewInject(R.id.psw1)
	EditText psw1;
	@ViewInject(R.id.psw2)
	EditText psw2;
	private UserInfoPreferences userInfoPreferences;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.modify_btn:
			String psw11 = psw1.getText().toString().trim();
			String psw22 = psw2.getText().toString().trim();
			if(TextUtils.isEmpty(psw11) || TextUtils.isEmpty(psw22)){
				MyToast.makeText(ForgetPasswordThreesActivity.this, R.string.paypwd_inform, Toast.LENGTH_LONG).show();
				return;
			}else if(!psw11.equals(psw22)){
				MyToast.makeText(ForgetPasswordThreesActivity.this, R.string.regist_toast2, Toast.LENGTH_LONG).show();
				return;
			}
			doGetSetPayPsw(com.earthman.app.utils.MD5.getMessageDigest(psw11.getBytes()));
			break;

		default:
			break;
		}

	}

	@Override
	protected void initData() {
		
	}
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		modify.setOnClickListener(this);
		userInfoPreferences = new UserInfoPreferences(this);

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.forget_paypwd);		
		psw1.setHint(R.string.set_pay_pws_hint1);
		psw2.setHint(R.string.set_pay_pws_hint2);
	}

	/**
	 * 
	 */
	private void showMyDialog() {
		final DialogOK myDialog = new DialogOK(this, getString(R.string.wenxin_inform), getString(R.string.set_paypwd_success));
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		myDialog.getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myDialog.dismiss();
				finish();
			}
		});
	}

	private void doGetSetPayPsw(String psw) {
		ArrayList<Object>list=new ArrayList<Object>();
        list.add(userInfoPreferences.getUserID());
        list.add(psw);
        list.add(userInfoPreferences.getUserToken());
        String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.SET_PAYPWD, list);
        executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>(){
            @Override
            public void onResponse(BaseResponse response) {
                if ("000000".equals(response.getCode())) {
                    //登陆成功,保存数据
                	showMyDialog();
                }else {
                	NetStatusHandUtil.getInstance().handStatus(ForgetPasswordThreesActivity.this, response.getCode(),response.getMessage());                
                }
            }},getErrorListener()));

	}
}
