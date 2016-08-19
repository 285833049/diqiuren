package com.earthman.app.nim.contact.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.common.ui.imageview.NImImageView;

/**
 * 用户点头像放大用户头像
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-7-13 上午11:00:37
 * @Decription
 */
public class UserImageShower extends Activity {

	String account;
	private NImImageView mUserNimImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_image_show);
		account = getIntent().getStringExtra("account");
		initView();

	}

	private void initView() {
		mUserNimImg = (NImImageView) findViewById(R.id.user_nim_image);
		mUserNimImg.loadBuddyAvatar(account);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
