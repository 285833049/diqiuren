package com.earthman.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.utils.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-3-30 下午5:25:33
 * @Decription
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, OnClickListener {
	private IWXAPI api;
	private Button btn_back;
	private Button btn_right;
	private TextView wx_pay_text;
	private TextView mTvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wxpay_entry_activity);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_right = (Button) findViewById(R.id.btn_right);
		wx_pay_text = (TextView) findViewById(R.id.wx_pay_text);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		btn_right.setText(R.string.finish);
		mTvTitle.setText(R.string.wx_pay);

		api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID);
		api.handleIntent(getIntent(), this);
		btn_back.setOnClickListener(this);
		btn_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();

			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	public static final String TAG = "WXEntryActivity";

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case 0:
				wx_pay_text.setText(R.string.pay_success);
				// Toast.makeText(WXPayEntryActivity.this, R.string.pay_success,
				// Toast.LENGTH_SHORT).show();
				sendBroadcast(new Intent(Constants.PAY_SUCCESS_ACTION));
				finish();
				/*
				 * if (Constants.ACTIVE_PAY == Constants.currentPayReason) {
				 * new
				 * UserInfoPreferences(WXPayEntryActivity.this).setUserStatus
				 * (Constants.ACTIVED);
				 * Constants.currentPayReason = 0;
				 * }
				 */
				break;

			case -2:
				// 取消支付
				wx_pay_text.setText(R.string.pay_cancel);
				// Toast.makeText(getApplicationContext(), "支付失败",
				// Toast.LENGTH_LONG).show();
				break;

			default:
				wx_pay_text.setText(R.string.pay_fail);
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			finish();

			break;

		default:
			break;
		}

	}
}