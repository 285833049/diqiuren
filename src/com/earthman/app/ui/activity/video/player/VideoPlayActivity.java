package com.earthman.app.ui.activity.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.earthman.app.R;
import com.earthman.app.ui.activity.video.player.SuperVideoPlayer.VideoPlayCallbackImpl;
import com.earthman.app.utils.LogUtil;
//"http://7xpe5y.com1.z0.glb.clouddn.com/201601301622546712.mp4"
/**
 * 演示demo
 */
public class VideoPlayActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "VideoPlayActivity";
	private static final String VIDEO_URL_EXTRA = "VIDEO_URL_EXTRA";
	private static final String VIDEO_TITLE_EXTRA="VIDEO_TITLE_EXTRA";

	private SuperVideoPlayer mSuperVideoPlayer;
	private ImageView mPlayBtnView;

	private String mUrl;
	private String mTitle;

	public static Intent newIntent(Context context, String url,String videoTitle) {
		Intent intent = new Intent(context, VideoPlayActivity.class);
		intent.putExtra(VIDEO_URL_EXTRA, url);
		intent.putExtra(VIDEO_TITLE_EXTRA, videoTitle);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_video_activity);
		mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
		mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
		mPlayBtnView.setOnClickListener(this);
		mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
		mUrl = getIntent().getStringExtra(VIDEO_URL_EXTRA);
		mTitle=getIntent().getStringExtra(VIDEO_TITLE_EXTRA);
		
		startPlay();
		LogUtil.d(TAG, "URL:"+mUrl+" title:"+mTitle);
	}

	/**
	 * 播放器的回调函数
	 */
	private VideoPlayCallbackImpl mVideoPlayCallback = new VideoPlayCallbackImpl() {
		/**
		 * 播放器关闭按钮回调
		 */
		@Override
		public void onCloseVideo() {
			mPlayBtnView.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.setVisibility(View.GONE);
			resetPageToPortrait();
		}

		/**
		 * 播放器横竖屏切换回调
		 */
		@Override
		public void onSwitchPageType() {
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);

			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
			}
			// mCurrentProgress=mSuperVideoPlayer.mVideoView.getCurrentPosition();
		}

		/**
		 * 播放完成回调
		 */
		@Override
		public void onPlayFinish() {
			
		}

		@Override
		public void onBack() {
			// TODO Auto-generated method stub
			finish();
		}
	};

	@Override
	public void onClick(View view) {
		startPlay();
	}
	
	
	private void startPlay(){
		mPlayBtnView.setVisibility(View.GONE);
		mSuperVideoPlayer.setVisibility(View.VISIBLE);
		mSuperVideoPlayer.setAutoHideController(true);
		Uri uri = Uri.parse(mUrl);
		mSuperVideoPlayer.loadAndPlay(uri, 0,mTitle);
	}

	/***
	 * 旋转屏幕之后回调
	 * 
	 * @param newConfig
	 *            newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (null == mSuperVideoPlayer)
			return;
		/***
		 * 根据屏幕方向重新设置播放器的大小
		 */
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			OrientationConfig();

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			/*
			 * final WindowManager.LayoutParams attrs =
			 * getWindow().getAttributes(); attrs.flags &=
			 * (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			 * getWindow().setAttributes(attrs);
			 * getWindow().clearFlags(WindowManager
			 * .LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			 *//*
				 * float width = DensityUtil.getWidthInPx(this); float height =
				 * DensityUtil.dip2px(this, 200.f);
				 *//*
					 * 
					 * float height = DensityUtil.getWidthInPx(this); float
					 * width = DensityUtil.getHeightInPx(this);
					 * mSuperVideoPlayer.getLayoutParams().height = (int)
					 * height; mSuperVideoPlayer.getLayoutParams().width = (int)
					 * width;
					 */

			OrientationConfig();

		}
	}

	private void OrientationConfig() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getDecorView().invalidate();
		float height = DensityUtil.getWidthInPx(this);
		float width = DensityUtil.getHeightInPx(this);
		mSuperVideoPlayer.getLayoutParams().height = (int) width;
		mSuperVideoPlayer.getLayoutParams().width = (int) height;
	}

	/***
	 * 恢复屏幕至竖屏
	 */
	private void resetPageToPortrait() {
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
		}
	}
}