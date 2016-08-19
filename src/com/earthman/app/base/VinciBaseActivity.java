package com.earthman.app.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-5 下午4:55:48
 * @Decription 基础类
 */
public abstract class VinciBaseActivity extends FragmentActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		initArguments();
		initSubviews();
		initData();
	}

	public abstract void initArguments();

	public abstract void initSubviews();
	
	public abstract void initData();

	public abstract void onClickListener(View v);
	
	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}
		onClickListener(v);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	//....
}
