package com.earthman.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/** 
* @Title: StringUtils
* @Description: 
* @Company: 地球人
* @author    ERIC
* @date       2016年3月11日
*/

public class MyStringUtils {
	

	/**
	 * 获取银行卡的后四位尾号
	 * @param banknumber
	 * @return
	 */
	public static String getBankNnmEnd4(String banknumber){
		return banknumber.substring(banknumber.length()-4, banknumber.length());
	}
	
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unused")
	public static String mTimeFormatData(Long data){
		if (data!=null) {
//		if (data>0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date mdate=new Date(data);
			LogUtil.i("生日", dateFormat.format(mdate));
			return dateFormat.format(mdate);
//		}else {
////			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
////			Date mdate=new Date(data);
//			LogUtil.i("生日", data+"");
//			return data+"";
//		}
		}else {
			return "";
		}
	}
}
