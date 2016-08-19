package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: EarthManDaiFuActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */
@ContentView(R.layout.activity_diqiuren_daifu)
public class RegistReplacePay extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	@ViewInject(R.id.daifu_commit)
	Button mBtnCommit;
	@ViewInject(R.id.friend_message)
	EditText mEtMessage;
	@ViewInject(R.id.friend_id)
	EditText mEtFriendID;
	private float money;
	private int year;
	private int paymethod;
	private int paytype;
	private int RESULTCODE=0x1212;
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.daifu_commit:
			String userid = mEtFriendID.getText().toString().trim();
			if (TextUtils.isEmpty(userid)) {
				MyToast.makeText(this, R.string.daifu_phone_tips, Toast.LENGTH_SHORT).show();
				return;
			}
			doPostRegistDaiFu(userid, mEtMessage.getText().toString());
			break;
		default:
			break;
		}
	}

	/**
	 * 地球人币代付
	 */
	private void doPostRegistDaiFu(String userid,String message) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		String geturl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.REGIST_PAY, list);
		LogUtil.i(TAG, geturl);
		   JSONObject jsonRequest= new JSONObject();
	        try {
	            jsonRequest.put("fee",money);
	            jsonRequest.put("paymentType", paytype);
	            jsonRequest.put("month", year);
	            jsonRequest.put("otherUserId", userid);
	            jsonRequest.put("message", message);
	            jsonRequest.put("paymentMethod", paymethod);
	            LogUtil.i(TAG, "fee"+money+"paymentType"+paytype+"month"+year+"otherUserId"+userid+"paymentMethod"+paymethod);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        Listener<JSONObject> listener=new Listener<JSONObject>() {

	            @Override
	            public void onResponse(JSONObject response) {
	                BaseResponse response2 = JSON.parseObject(response.toString(),BaseResponse.class);
	                LogUtil.i("注册返回结果", response.toString());
	                if ("000000".equals(response2.getCode())) {
	                	LogUtil.i(TAG, response2.toString());
	                	showMyDialog();
	                }else {
	                	NetStatusHandUtil.getInstance().handStatus(RegistReplacePay.this, response2.getCode(),response2.getMessage());
	                    
	                }
	            }
	        };
	        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, geturl, jsonRequest, listener , getErrorListener()){

	            @Override
	            public Map<String, String> getHeaders() throws AuthFailureError {
	                Map<String, String> map = new HashMap<String, String>();
	                map.put("Content-Type", "application/json");
	                return map;
	            }

	        };
	        executeRequest(jsonObjectRequest);
	    }
	
//	post方式访问，post提交参数：dataJson包含值：
//	paymentType：缴费方式（0:趸缴，1:年缴）
//	month：缴费年份（数字类型）
//	fee：缴费金额（数字类型）
//	otherUserId:地球人ID或手机号
//	message:留言信息
//	paymentMethod：支付方式：0（0:地球币:1:微信；2:支付宝:3:银联:4:paypal）
	/**
	 * 
	 */
	private void showMyDialog() {
		final DialogOK myDialog = new DialogOK(this, getString(R.string.dialog_title), getString(R.string.registSuccess));
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		myDialog.getView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
//				startActivity(new Intent(EarthManDaiFuActivity.this,LoginActivity.class));
				setResult(RESULTCODE);
				RegistReplacePay.this.finish();
			}
		});;
	}

	@Override
	protected void initData() {
		if (getIntent().getExtras()!=null) {
		money = Float.parseFloat(getIntent().getExtras().getString("money"));
		year = getIntent().getExtras().getInt("month");
		paymethod = getIntent().getExtras().getInt("paymentMethod");
		paytype = getIntent().getExtras().getInt("paymentType");
		}
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvTitleLeft.setText(getString(R.string.daifu_title));
		mBtnTitleLeft.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
	}

}
