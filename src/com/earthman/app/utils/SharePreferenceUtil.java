package com.earthman.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {

	private static SharedPreferences mPreferences;

	/**
	 * 默认文件名为EarthMan
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		mPreferences = context.getSharedPreferences("EarthMan", Context.MODE_PRIVATE);
	}

	/**
	 * 初始化sharedperfence
	 * 
	 * @param context
	 *            上下文
	 * @param SPName
	 *            sp文件名字
	 */
	public static SharedPreferences init(Context context, String SPName) {

		return mPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
	}

	public static void putString(String key, String value) {

		mPreferences.edit().putString(key, value).commit();
	}

	public static String getString(String key) {

		return mPreferences.getString(key, "");
	}

	public static void putBoolean(String key, boolean value) {
		mPreferences.edit().putBoolean(key, value).commit();

	}

	public static boolean getBoolean(String key) {

		return mPreferences.getBoolean(key, false);
	}

	public static void putInt(String key, int value) {
		mPreferences.edit().putInt(key, value).commit();
	}

	public static int getInt(String key) {

		return mPreferences.getInt(key, 0);
	}
	public static void putLong(String key, Long value) {
		mPreferences.edit().putLong(key, value).commit();
	}
	
	public static long getLong(String key) {
		
		return mPreferences.getLong(key, 0);
	}

	/**
	 * 清除sp所有数据
	 */
	public static void clearData() {
		mPreferences.edit().clear().commit();
	}

	/**
	 * 清除SharedPreferences临时信息 单条信息
	 */
	public static void delSharedPreferencesDate(Context context, String shpdate_str) {

		Editor editor = mPreferences.edit();
		editor.remove(shpdate_str);
		editor.commit();
	}
}
