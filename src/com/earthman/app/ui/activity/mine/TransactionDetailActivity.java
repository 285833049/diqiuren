package com.earthman.app.ui.activity.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.utils.MyStringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 交易详情
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.transaction_detail)
public class TransactionDetailActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.tv_title)
	TextView tv_title;
	@ViewInject(R.id.transaction_type_text)
	TextView transaction_type_text;
	@ViewInject(R.id.transaction_time_text)
	TextView transaction_time_text;
	@ViewInject(R.id.transaction_money_text)
	TextView transaction_money_text;
	@ViewInject(R.id.transaction_pay_type)
	TextView transaction_pay_type;
	@ViewInject(R.id.transaction_method_text)
	TextView transaction_method_text;
	@ViewInject(R.id.transaction_nick_text)
	TextView transaction_nick_text;
	@ViewInject(R.id.textView1)
	TextView textView1;
	@ViewInject(R.id.transaction_method_iv)
	ImageView transaction_method_iv;
	@ViewInject(R.id.rl_otheraccount)
	RelativeLayout rl_otheraccount;
	private Bundle bundleExtra;

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
//			private String createdAt;
//			private String id;
//			private String money;
//			private String otherAccount;
//			private String payWay;
//			private String type;
			
			bundleExtra = getIntent().getBundleExtra("billResult");
			
		}
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@SuppressLint("NewApi")
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		if (TextUtils.isEmpty(bundleExtra.getString("otherAccount"))) {
			rl_otheraccount.setVisibility(View.GONE);
		} else {
			rl_otheraccount.setVisibility(View.VISIBLE);
			String[] datas = bundleExtra.getString("otherAccount").split(" ");
			transaction_nick_text.setText(String.format(getString(R.string.neichen),datas[0]));
			textView1.setText(datas[1]);
		}
		transaction_type_text.setText(bundleExtra.getString("type"));
		transaction_time_text.setText(MyStringUtils.mTimeFormatData(Long.parseLong(bundleExtra.getString("createdAt"))));
		transaction_money_text.setText(bundleExtra.getString("money"));
		if ("支付宝".equals(bundleExtra.getString("payWay"))) {
			transaction_method_iv.setBackground(getResources().getDrawable(R.drawable.zhifubao));
		}
		if ("地球币".equals(bundleExtra.getString("payWay"))) {
			transaction_method_iv.setBackground(getResources().getDrawable(R.drawable.diqiuren));
		}
		if ("微信".equals(bundleExtra.getString("payWay"))) {
			transaction_method_iv.setBackground(getResources().getDrawable(R.drawable.weixin));
		}
		if ("银联".equals(bundleExtra.getString("payWay"))) {
			transaction_method_iv.setBackground(getResources().getDrawable(R.drawable.yinlian));
		}
		if ("paypal".equals(bundleExtra.getString("payWay"))) {
			transaction_method_iv.setBackground(getResources().getDrawable(R.drawable.paypal));
		}
		transaction_method_text.setText(bundleExtra.getString("payWay"));
	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.record_detail);
	}

}
