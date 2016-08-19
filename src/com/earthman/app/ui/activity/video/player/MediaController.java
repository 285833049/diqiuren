package com.earthman.app.ui.activity.video.player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.earthman.app.R;

public class MediaController extends FrameLayout implements
		SeekBar.OnSeekBarChangeListener, View.OnClickListener {
	private ImageView mPlayImg;// 播放按钮
	private SeekBar mProgressSeekBar;// 播放进度条
	private TextView mTimeTxt;// 播放时间
	private ImageView mExpandImg;// 最大化播放按钮
	private ImageView mShrinkImg;// 缩放播放按钮
	private Button mBtnBack;//返回按钮
	private TextView mTvTitle;//视频标题
	private View mMenuViewPlaceHolder;

	private LinearLayout mLltControllerTitle;// 顶部标题栏
	private LinearLayout mLltControllerToolBar;// 底部控制栏
	

	private MediaControlImpl mMediaControl;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean isFromUser) {
		if (isFromUser)
			mMediaControl.onProgressTurn(ProgressState.DOING, progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mMediaControl.onProgressTurn(ProgressState.START, 0);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mMediaControl.onProgressTurn(ProgressState.STOP, 0);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pause:
				mMediaControl.onPlayTurn();
				break;
			case R.id.expand:
				mMediaControl.onPageTurn();
				break;
			case R.id.shrink:
				mMediaControl.onPageTurn();
				break;
			case R.id.btn_back:
				mMediaControl.onBack();
				break;
		}

	}

	/**
	 * 初始化精简模式
	 */
	public void initTrimmedMode() {
		mMenuViewPlaceHolder.setVisibility(GONE);
		mExpandImg.setVisibility(INVISIBLE);
		mShrinkImg.setVisibility(INVISIBLE);
	}

	/***
	 * 强制横屏模式
	 */
	public void forceLandscapeMode() {
		mExpandImg.setVisibility(INVISIBLE);
		mShrinkImg.setVisibility(INVISIBLE);
	}

	public void setProgressBar(int progress, int secondProgress) {
		if (progress < 0)
			progress = 0;
		if (progress > 100)
			progress = 100;
		if (secondProgress < 0)
			secondProgress = 0;
		if (secondProgress > 100)
			secondProgress = 100;
		mProgressSeekBar.setProgress(progress);
		mProgressSeekBar.setSecondaryProgress(secondProgress);
	}

	public void setPlayState(PlayState playState) {
		mPlayImg.setImageResource(playState.equals(PlayState.PLAY) ? R.drawable.biz_video_pause
				: R.drawable.biz_video_play);
	}

	public void setPageType(PageType pageType) {
		mExpandImg.setVisibility(pageType.equals(PageType.EXPAND) ? GONE
				: VISIBLE);
		mShrinkImg.setVisibility(pageType.equals(PageType.SHRINK) ? GONE
				: VISIBLE);
	}

	public void setPlayProgressTxt(int nowSecond, int allSecond) {
		mTimeTxt.setText(getPlayTime(nowSecond, allSecond));
	}

	public void playFinish(int allTime) {
		mProgressSeekBar.setProgress(0);
		setPlayProgressTxt(0, allTime);
		setPlayState(PlayState.PAUSE);
	}

	public void setMediaControl(MediaControlImpl mediaControl) {
		mMediaControl = mediaControl;
	}

	public LinearLayout getmLltControllerToolBar() {
		return mLltControllerToolBar;
	}

	public LinearLayout getmLltControllerTitle() {
		return mLltControllerTitle;
	}
	
	public void setTileText(String title){
		mTvTitle.setText(title);
	}

	public MediaController(Context context) {
		super(context);
		initView(context);
	}

	public MediaController(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.biz_video_media_controller, this);
		mPlayImg = (ImageView) findViewById(R.id.pause);
		mProgressSeekBar = (SeekBar) findViewById(R.id.media_controller_progress);
		mTimeTxt = (TextView) findViewById(R.id.time);
		mExpandImg = (ImageView) findViewById(R.id.expand);
		mShrinkImg = (ImageView) findViewById(R.id.shrink);
		mMenuViewPlaceHolder = findViewById(R.id.view_menu_placeholder);
		mBtnBack=(Button) findViewById(R.id.btn_back);
		mTvTitle=(TextView) findViewById(R.id.tv_video_title);
		
		mLltControllerTitle = (LinearLayout) findViewById(R.id.llt_controller_title);
		mLltControllerToolBar = (LinearLayout) findViewById(R.id.llt_controller_toolBar);
		initData();
	}

	private void initData() {
		mProgressSeekBar.setOnSeekBarChangeListener(this);
		mPlayImg.setOnClickListener(this);
		mShrinkImg.setOnClickListener(this);
		mExpandImg.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		
		setPageType(PageType.SHRINK);
		setPlayState(PlayState.PAUSE);
	}

	@SuppressLint("SimpleDateFormat")
	private String formatPlayTime(long time) {
		DateFormat formatter = new SimpleDateFormat("mm:ss");
		return formatter.format(new Date(time));
	}

	private String getPlayTime(int playSecond, int allSecond) {
		String playSecondStr = "00:00";
		String allSecondStr = "00:00";
		if (playSecond > 0) {
			playSecondStr = formatPlayTime(playSecond);
		}
		if (allSecond > 0) {
			allSecondStr = formatPlayTime(allSecond);
		}
		return playSecondStr + "/" + allSecondStr;
	}

	/**
	 * 播放样式 展开、缩放
	 */
	public enum PageType {
		EXPAND, SHRINK
	}

	/**
	 * 播放状态 播放 暂停
	 */
	public enum PlayState {
		PLAY, PAUSE
	}

	public enum ProgressState {
		START, DOING, STOP
	}

	public interface MediaControlImpl {
		/**
		 * 点击播放按钮
		 */
		void onPlayTurn();

		/**
		 * 缩放的页面变化
		 */
		void onPageTurn();

		/**
		 * 进度条点击
		 * 
		 * @param state
		 *            进度条状态
		 * @param progress
		 *            进度
		 */
		void onProgressTurn(ProgressState state, int progress);

		/**
		 * 返回按钮
		 */
		void onBack();

	}

}
