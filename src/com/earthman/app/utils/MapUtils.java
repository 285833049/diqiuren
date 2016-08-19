package com.earthman.app.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.earthman.app.MyApplication;
import com.earthman.app.R;
import com.earthman.app.bean.MapInfo;
import com.earthman.app.enums.MapName;

public class MapUtils {

	/**
	 * gotoBaiduMap(这里用一句话描述这个方法的作用)
	 * 
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            精度
	 * @exception
	 */
	public static void openBaiduMap(double latitude, double longitude) {
		try {
			Intent intent = Intent.getIntent("intent://map/marker?location=" + latitude + "," + longitude
					+ "&title=我的位置&content=目标位置&src=湖南球谱科技|地球人#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (isInstallByread("com.baidu.BaiduMap")) {
				MyApplication.getInstance().startActivity(intent); // 启动调用
//				Toast.makeText(MyApplication.getInstance(), "百度地图客户端已经安装", Toast.LENGTH_LONG).show();
			} else {
//				Toast.makeText(MyApplication.getInstance(), "没有安装百度地图客户端", Toast.LENGTH_LONG).show();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public static void openGaoDeMap(double latitude, double longitude) {
		try {
			Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication=地球人&poiname=目标位置&lat=" + latitude + "&lon=" + longitude + "&dev=0");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				MyApplication.getInstance().startActivity(intent);
//				Toast.makeText(MyApplication.getInstance(), "高德地图客户端已经安装", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
//				Toast.makeText(MyApplication.getInstance(), "高德地图客户端未安装", Toast.LENGTH_LONG).show();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 获取已安装的地图
	 * 
	 * @param context
	 * @return
	 *         List<String>
	 * @exception
	 */
	public static List<MapInfo> getInstalledMap(Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> mAllPackages = packageManager.getInstalledPackages(0);
		List<MapInfo> mapList = new ArrayList<MapInfo>();
		for (PackageInfo info : mAllPackages) {
			if (info.packageName.equals("com.baidu.BaiduMap")) {
				mapList.add(new MapInfo(MapName.BAIDU,R.drawable.ic_map_baidu, info.applicationInfo.loadLabel(packageManager).toString(), info.packageName));
			} else if (info.packageName.equals("com.autonavi.minimap")) {
				mapList.add(new MapInfo(MapName.GAODE,R.drawable.ic_map_gaode, info.applicationInfo.loadLabel(packageManager).toString(), info.packageName));
			}
		}
		return mapList;
	}

}
