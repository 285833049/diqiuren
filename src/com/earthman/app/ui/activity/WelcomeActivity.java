package com.earthman.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.ui.activity.login.LoginActivity;

/**
 * 欢迎页面
 * 
 * @author Administrator
 * 
 */
public class WelcomeActivity extends Activity {
	private UserInfoPreferences userInfoPreferences;
	public final static int STATE = 0x01;
	private final static long stateTime = 2000l;// 欢迎页停留时间（2秒）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_activity);
        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }
	    userInfoPreferences = new UserInfoPreferences(this);		
		mHandler.sendEmptyMessageDelayed(STATE, stateTime);
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/**
			 * 如果用户是第一次进入当前的app，就需要跳到引导界面 如果用户不是第一次进入当前的app，就跳到主页面
			 */
			if (!userInfoPreferences.getIsFirstOpen()) {
				onIntent();
			} else {
				userInfoPreferences.setIsfirstLogin(false);
				startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
			}
			finish();
		};
	};
	
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        setIntent(intent);
        onIntent();
    }
    
    private void onIntent(){
		if (userInfoPreferences.getIsLogin()) {
			startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
		}else {
		    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));											
		}	
    }
    
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 移除消息池中的消息
		if (mHandler != null && mHandler.hasMessages(STATE)) {
			mHandler.removeMessages(STATE);
		}
	}

}
