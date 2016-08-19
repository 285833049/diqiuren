package com.earthman.app.zxing.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.earthman.app.zxing.camera.CameraManager;
import com.earthman.app.zxing.decoding.CaptureActivityHandler;
import com.earthman.app.zxing.decoding.InactivityTimer;
import com.earthman.app.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * 
 * 作者：Zhou
 * 日期：2015-10-14 下午2:46:08
 * 版权：地球人
 * 描述：（）
 */
public class CaptureActivity extends BaseActivity implements Callback {
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;
	private static final float BEEP_VOLUME = 0.10f;
	private Button btn_back;
	private TextView tv_title;

	/** Called when the activity is first created. */
	@Override
	protected void initData() {
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.capture);
		viewfinderView = (ViewfinderView) this.findViewById(R.id.viewfinder_view);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		btn_back = (Button) findViewById(R.id.btn_back);
	}

	@Override
	protected void setAttribute() {
		tv_title.setText(R.string.scan);
		btn_back.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (!isYwbCode(resultString)) {
			MyToast.makeText(CaptureActivity.this, R.string.scan_code_fail, Toast.LENGTH_LONG).show();
			finish();
		} else {
			doGetAddFriend(resultString);
		}
	}

	/**
	 * 添加好友请求
	 */
	private void doGetAddFriend(String resultString) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(resultString);
		list.add(preferences.getUserToken());
		list.add(Constants.ADD_FRIEND_TO_SOMEONE);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.ADD_FRIEND, list);

		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class,
				new Response.Listener<BaseResponse>() {

					@Override
					public void onResponse(BaseResponse response) {
						if ("000000".equals(response.getCode())) {
							MyToast.makeText(CaptureActivity.this, R.string.add_friend2, Toast.LENGTH_LONG).show();
							CaptureActivity.this.finish();
						} else {
							NetStatusHandUtil.getInstance().handStatus(CaptureActivity.this, response.getCode(), response.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MyToast.makeText(CaptureActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();

					}
				});

		executeRequest(fastJsonRequest);
	}

	/**
	 * 
	 * isYwbCode(判定扫描的结果是不是地球人二维码)
	 * 
	 * @param resultString
	 * @return
	 *         boolean
	 * @exception
	 */
	private boolean isYwbCode(String resultString) {
		if (TextUtils.isEmpty(resultString) || resultString.length() != 9) {// 二维码为空
			return false;
		}
		return true;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	// 扫描完成是的提示音
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}
}