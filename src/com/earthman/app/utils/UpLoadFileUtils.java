package com.earthman.app.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.mime.MultipartRequestParams;
import com.charlie.lee.androidcommon.http.request.MultipartRequest;
import com.earthman.app.R;
import com.earthman.app.bean.UploadImgResponse;
import com.earthman.app.bean.UploadImgResponse.UploadImgResult;
import com.earthman.app.enums.CompressType;
import com.earthman.app.widget.MyToast;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月28日 上午10:39:09
 * @Decription
 */
public class UpLoadFileUtils  {

	/*
	 * 上传文件
	 */

	public static void doUploadImg(final Context context, final String path, String userid, String token, final Handler mHandler) {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		MultipartRequestParams params = new MultipartRequestParams();
		params.put("contactsfile", new File(path));
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userid);
		list.add(token);
		String url = HttpUrlConstants.getUrl(context, HttpUrlConstants.CONTACT_BACKUP, list);
		RequestManager.getInstance().addRequest(new MultipartRequest(url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				UploadImgResponse bean = JSON.parseObject(object.toString(), UploadImgResponse.class);
				Message msg = new Message();
				if ("000000".equals(bean.getCode())) {
					// Toast.makeText(context,
					// context.getResources().getString(R.string.upload_contact_success),
					// Toast.LENGTH_SHORT).show();
					// LogUtil.d("EarthMan",
					// "通讯录上传返回路径"+bean.getResult().getFilelink());
					UploadImgResult result = bean.getResult();
					msg.what = 0x99;
					if (result != null) {
						if (mHandler != null) {
							msg.obj = result.getFilelink();
						}
					}
				} else {
					msg.what = 0x100;
					Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();
				}
				mHandler.sendMessage(msg);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
			}
		}), "uploadContact");

	}

	/**
	 * 
	 * doUploadfiles(发布动态图片上传)
	 * @param context
	 * @param paths
	 * @param userid
	 * @param token
	 * @param mHandler
	 * void
	 * @exception
	 */
	public static void doUploadfiles(final Context context, final ArrayList<String> paths, String userid, String token, String dynamicContent, final Handler mHandler, double longitude, double latitude, String location) {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		final MultipartRequestParams params = new MultipartRequestParams();
		params.put("title", dynamicContent);
		params.put("agent", "android");
		params.put("location", location);
		params.put("longitude", String.valueOf(longitude));
		params.put("latitude", String.valueOf(latitude));
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userid);
		list.add(token);
		final String url = HttpUrlConstants.getUrl(context, HttpUrlConstants.DYNAMIC_PICS, list);
		new Thread(new Runnable() {
			@Override
			public void run() {
			if(paths.size() != 0){//压缩动态图片默认高清
				InputStream [] is=new ImageCompressUtils().Compressed(paths.toArray(new String[paths.size()]), CompressType.HD);
				if(is!=null){
					for(int i=0;i<paths.size();i++){
						params.put("files"+i,is[i],System.currentTimeMillis()+".jpg");
					}
				}
			}
			doUpload(context,url,params,mHandler);
		}}).start();
	}
	
	private static void doUpload(final Context context,String url,MultipartRequestParams params,final Handler mHandler){
		RequestManager.getInstance().addRequest(new MultipartRequest(url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject object) {
				UploadImgResponse bean = JSON.parseObject(object.toString(), UploadImgResponse.class);
				if ("000000".equals(bean.getCode())) {
					// Toast.makeText(context,
					// context.getResources().getString(R.string.upload_contact_success),
					// Toast.LENGTH_SHORT).show();
					// LogUtil.d("EarthMan",
					// "通讯录上传返回路径"+bean.getResult().getFilelink());
					UploadImgResult result = bean.getResult();
					Message msg = new Message();
					msg.what = 0x99;
					if (result != null) {
						if (mHandler != null) {
							msg.obj = result.getFilelink();
						}
					}
					mHandler.sendMessage(msg);
				} else {
					Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				LogUtil.i("eadas", arg0.toString());
				MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
			}
		}), "uploadPics");
	}
	
	
}
