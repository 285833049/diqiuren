package com.earthman.app.ui.activity.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ZhuanZhangToEarthManAccount
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */
@ContentView(R.layout.zhuanzhang2earthmanaccount)
public class Transfer2EarthAccount extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	@ViewInject(R.id.et_zhanzhang_to_id)
	EditText mEtOtherAccountID;
	@ViewInject(R.id.zhuanzhang_next)
	Button mBtnNext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@SuppressLint("DefaultLocale")
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.zhuanzhang_next:
			String otherAccount = mEtOtherAccountID.getText().toString().trim();
			if (TextUtils.isEmpty(otherAccount)) {
				MyToast.makeText(this, R.string.zhuanzhang1, Toast.LENGTH_SHORT).show();
				return;
			}
			if (otherAccount.toUpperCase().equals(preferences.getUsercardId())) {
				MyToast.makeText(this, R.string.zhuanzhang4, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent transferintent = new Intent(Transfer2EarthAccount.this, TransferMoneyActivity.class);
			transferintent.putExtra("otherAccount", otherAccount);
			startActivity(transferintent);
			finish();
		default:
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		mTvTitleLeft.setText(getString(R.string.zhuanzhang_toearthmanaccount));
		mBtnTitleLeft.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		mEtOtherAccountID.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 9){
					MyToast.makeText(Transfer2EarthAccount.this, R.string.carid_error, Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	@Override
	protected void setAttribute() {

	}

}
