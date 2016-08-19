package com.earthman.app.utils;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * 作者：Zhou
 * 日期：2016-3-9 上午9:59:07
 * 描述：（）
 */
public class NetworkUtil {

	//private static final String GET_ENCODE = "UTF-8";
	
	private NetworkUtil() {
		
	}
	//判断网络
	public static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 生成GET请求URL
	 * @param context 上下文环境
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return
	 */
	public static String getRequestUrl(Context context, String url, ArrayList<Object> list) {
		return url + generaterGetRequestParams(context, list);
	}
	
	
		
	/**
	 * 生成GET请求参数
	 * @param context
	 * @param params
	 * @return
	 */
	private static String generaterGetRequestParams(Context context, ArrayList<Object> list) {
		String requestParams = null;
		StringBuffer buffer = new StringBuffer();
		for(Object object : list){
			buffer.append("/").append(object);
		}
		requestParams = buffer.toString();
		/*try {
			StringBuffer buffer = new StringBuffer();
			for(Object object : list){
				buffer.append("/").append(object);
			}
			requestParams = URLEncoder.encode(buffer.toString(), GET_ENCODE);
		} catch (UnsupportedEncodingException e) {
		}*/
		return requestParams;
	}	
}
