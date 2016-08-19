package com.earthman.app.ui.activity.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.utils.BankConstants;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.MyStringUtils;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 激活详情
 * 
 * @author xiexianyong
 * 
 */
@SuppressLint("InlinedApi")
@ContentView(R.layout.activation_details)
public class ActivationDetailsActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.go_pay)
	Button go_pay;
	@ViewInject(R.id.cardID)
	TextView cardID;
	@ViewInject(R.id.creadtime)
	TextView creadtime;
	@ViewInject(R.id.fee)
	TextView fee_tv;
	@ViewInject(R.id.message)
	TextView message_tv;
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.go_pay:
			// checkPayPswSet();
			if (preferences.getIsSetPwd() == 0) {
				showSetPayPwdAlert();
			} else {
				showMyDialog();
			}
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
		paySuccessReceiver = new PaySuccessReceiver();
		paySuccessReceiver.setHandler(handler);
		registerReceiver(paySuccessReceiver, new IntentFilter(Constants.PAY_SUCCESS_ACTION));
		if (getIntent().getExtras() != null) {
			fee = getIntent().getStringExtra("fee");
			cardId = getIntent().getStringExtra("cardId");
			message = getIntent().getStringExtra("message");
			id = getIntent().getStringExtra("id");
			createdAt = getIntent().getStringExtra("createdAt");
		}

	}
	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what>0) {
				finish();
			}
		};
	};
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		message_tv.setText(message);
		fee_tv.setText(fee);
		cardID.setText(cardId);
		creadtime.setText(MyStringUtils.mTimeFormatData(Long.parseLong(createdAt)));
		go_pay.setOnClickListener(this);

	}

	boolean isfirst = true;
	private String fee;
	private String cardId;
	private String message;
	private String id;
	private String createdAt;
	private PayPwdDialog payPwdDialog;
	private ActivityResultListener listener;
	private PaySuccessReceiver paySuccessReceiver;

	private void showMyDialog() {
		payPwdDialog = new PayPwdDialog(this, fee, new OnSurePayClickListener() {
			@Override
			public void onSurePayClick(int pagType, String pwd) {
				new PayFactory(ActivationDetailsActivity.this, PayFactory.CASE_ACTIVE_FRIEND, pagType, pwd).genPayForFriend(id, pwd);
			}
		});
		payPwdDialog.setPayWay(BankConstants.PAY_WAY_DIQIUBI);
		payPwdDialog.setCanceledOnTouchOutside(false);
		listener = payPwdDialog;
		payPwdDialog.show();
	}

	/**
	 * 代激活地球比支付
	 *//*
	private void doGetPostCoin(String pageType, String pwd) {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(MD5.getMessageDigest(pwd.getBytes()));
		list.add(id);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.PAY_FOR_FRIENDS_DIQIUBI, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@Override
			public void onResponse(BaseResponse response) {
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(ActivationDetailsActivity.this, R.string.zhifu1, Toast.LENGTH_LONG).show();
					go_pay.setVisibility(View.GONE);
				} else {
					MyToast.makeText(ActivationDetailsActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		}, getErrorListener()));
	}*/

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.activation_details);

	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PayPwdDialog.CHANGE_PAYTYPE:
				if(listener != null){
					listener.onActivityResult(requestCode, resultCode, data);
				}
				break;

			default:
				break;
			}
		}
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(paySuccessReceiver);
		super.onDestroy();
		
	}
}
