package com.earthman.app.ui.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ChangePhoneActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月8日
 */
@ContentView(R.layout.activity_change_phone)
public class AccountChangePhoneStep1 extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------------------
	@ViewInject(R.id.change_phoen_next)
	Button mBtnNext;
	@ViewInject(R.id.change_phone)
	TextView mTvUserphone;
	private int requestCode = 0x003;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.change_phoen_next:
			startActivityForResult(new Intent(AccountChangePhoneStep1.this, AccountChangePhoneStep2.class), requestCode);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestcode, int responseCode, Intent arg2) {
		if (requestcode == requestCode&&requestCode==responseCode) {
			finish();
		}
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvTitleLeft.setText(getString(R.string.change_phone_title));
		mTvUserphone.setText(preferences.getUserPhone());
		mBtnNext.setOnClickListener(this);
		mBtnTitleLeft.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {

	}

}
