package com.earthman.app.ui.activity.mine;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 支付密码修改－》忘记支付密码第二步输入卡号身份证和姓名
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.forget_pwd_two)
public class ForgetPasswordTwoActivity extends BaseActivity implements
		OnClickListener {
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.next_btn)
	Button next_btn;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			Intent intent = new Intent(this, ForgetPasswordThreesActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	@Override
	protected void initData() {

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		next_btn.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.forget_payment_pws);
	}

}
