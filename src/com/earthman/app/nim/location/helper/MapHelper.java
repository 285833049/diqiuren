package com.earthman.app.nim.location.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class MapHelper {
	private static final String Autonavi_Map = "com.autonavi.minimap";
	
	private static List<PackageInfo> initComponentInfo(Context context) {
		List<String> maps = new ArrayList<String>();
		maps.add(Autonavi_Map);
		return getComponentInfo(context, maps);
	}
	
	private static List<PackageInfo> getComponentInfo(Context context,
			List<String> maps) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> infos = pm.getInstalledPackages(0);
		List<PackageInfo> available = new ArrayList<PackageInfo>();
		if ((infos != null) && (infos.size() > 0))
			for (PackageInfo info : infos) {
				String packName = info.packageName;
				if (!TextUtils.isEmpty(packName) && maps.contains(packName)) {
					if (packName.equals(Autonavi_Map)) {
						if (info.versionCode >= 161)
							available.add(info);
					} else {
						available.add(info);
					}
				}
			}
		return available;
	}
	
	
	public static List<PackageInfo> getAvailableMaps(Context context) {
		return initComponentInfo(context);
	}
	
}
