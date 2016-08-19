package com.earthman.app.base;

import android.content.ContentValues;
import android.content.Context;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-20 下午6:23:51
 * @Decription
 */
public class LocationPreference extends BasePreferences{

	// 保存用户信息的表名
    private final static String PERFERENCE_NAME = "location";
    private Context context;
    private static final String LONGITUDE = "longitude";//经度
    private static final String LATITUDE = "latitude";//纬度
    private static final String CITYCODE = "cityCode";//城市编码
    private static final String CITYNAME = "cityName";//城市名称
	/**
	 * 创建一个新的实例 LocationPreference.
	 *
	 * @param context
	 * @param table
	 */
	public LocationPreference(Context context) {
		super(context, PERFERENCE_NAME);
		this.context = context;
		
	}
		
	/**
	 * 存放单个数据Str
	 * 
	 * @param value
	 * @param key
	 */
	private void putValueStr(String value, String key) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(key, value);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLongitude(String longitude){
		putValueStr(longitude, LONGITUDE);
	}
	
	
	public void setLatitude(String latitude){
		putValueStr(latitude, LATITUDE);
	}
	
	public String getLongitude(){
		return getString(LONGITUDE);
	}
	
	public String getLatitude(){
		return getString(LATITUDE);
	}
	
	public void setCityCode(String cityCode){
		putValueStr(cityCode, CITYCODE);
	}
	
	public void setCityName(String cityName){
		putValueStr(cityName, CITYNAME);
	}
	
	public String getCityCode(){
		return getString(CITYCODE);
	}
	
	public String getCityName(){
		return getString(CITYNAME);
	}

}
