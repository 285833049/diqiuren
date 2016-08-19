package com.earthman.app.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-7 下午2:14:35
 * @Decription
 */
public class UploadService extends Service{

	private String name;
	private String path;
	private int privacy;
	private Timer timer;
	private int percent;
	
	/* 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			name = intent.getStringExtra("name");
			path = intent.getStringExtra("path");
			privacy = intent.getIntExtra("privacy", 0);
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {			
				if(percent < 0){
					return;
				}
                sendBroadcast(new Intent(Constants.UPLOAD_LOAD_ACTION).putExtra("name", name).putExtra("percent", percent + "%"));				
			}
		}, 2000, 5000);
		doUploadVideo();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	/*
	 * 上传视频方法
	 */
	private void doUploadVideo() {
		if(path == null){
			return;
		}		
		//实例化HttpUtils对象， 参数设置链接超时
		HttpUtils httpUtils = new HttpUtils(60 * 1000);
		//实例化RequestParams对象
		RequestParams requestParams = new RequestParams();
				
		requestParams.addBodyParameter("name", name);
		//imageFile是File格式的对象， 将此File传递给RequestParams
		requestParams.addBodyParameter("file", new File(path));
		requestParams.addBodyParameter("privacy", String.valueOf(privacy));
		UserInfoPreferences preferences = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		//showLoading();
		String url = HttpUrlConstants.getUrl(this, HttpUrlConstants.UPLOAD_VIDEO,
				list);
		//通过HTTP_UTILS来发送post请求， 并书写回调函数
		httpUtils.send(HttpMethod.POST, url, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				sendBroadcast(new Intent(Constants.UPLOAD_FAIL_ACTION).putExtra("name", name));
				stopSelf();
			}

			@Override
			public void onLoading(long total, final long current, boolean isUploading) {
				percent = (int)((current*1.0)/total*100);				
				super.onLoading(total, current, isUploading);
			}



			@Override
			public void onStart() {
				sendBroadcast(new Intent(Constants.UPLOAD_START_ACTION).putExtra("name", name));
				super.onStart();
			}



			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if(timer != null){
					timer = null;
				}
				sendBroadcast(new Intent(Constants.UPLOAD_SUCCESS_ACTION).putExtra("name", name));
				stopSelf();
			}
		});
	}
	
	

}
