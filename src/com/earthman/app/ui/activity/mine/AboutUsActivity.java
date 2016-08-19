package com.earthman.app.ui.activity.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.enums.NetworkType;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.about_us_activity)
public class AboutUsActivity extends BaseActivity {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	// -------------------
	@ViewInject(R.id.tv_environment)
	private TextView mTvEnvironment;
	@ViewInject(R.id.tv_version)
	private TextView mTvVersion;
	@ViewInject(R.id.tv_website)
	private TextView mTvWebsite;

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		mTvEnvironment.setVisibility(HttpUrlConstants.mNetworkType==NetworkType.OFFICIAL?View.GONE:View.VISIBLE);
	}

	@Override
	protected void setAttribute() {
		mTvLeft.setText(R.string.about_us);
		mTvVersion.setText("For Android V"+AndroidUtils.getVersionName(this)+" build "+AndroidUtils.getVersionCode(this));
		mTvWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
		mTvWebsite.getPaint().setAntiAlias(true);//抗锯齿
	}

	@OnClick(value = { R.id.btn_back ,R.id.tv_website})
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_website:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("http://www.dqr2015.com"));
			startActivity(intent);
			break;
		}
	}

}
