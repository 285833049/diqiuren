package com.earthman.app.contactsave;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.ContactURLBean;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月25日 上午11:08:35
 * @Decription
 */
@ContentView(R.layout.activity_contact_save)
public class ContactSaveActivity extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//
	@ViewInject(R.id.contact_backup)
	LinearLayout mBtnContactBackUp;
	@ViewInject(R.id.contact_recover)
	LinearLayout mBtnContactRecover;
	private ContactUpAndDown contactUpAndDown;

	/*
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.contact_backup:
			contactUpAndDown.saveContact();
			break;
		case R.id.contact_recover:
			contactUpAndDown.doGetContactDownloadURl();
			break;
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	

	/*
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		contactUpAndDown = new ContactUpAndDown(this);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnContactBackUp.setOnClickListener(this);
		mBtnContactRecover.setOnClickListener(this);
		mTvTitleLeft.setText(R.string.title_contect);
		mBtnTitleLeft.setOnClickListener(this);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

	}

}
