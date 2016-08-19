package com.earthman.app.nim.session;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.View;

import com.earthman.app.nim.location.activity.AMapActivity;
import com.earthman.app.nim.location.activity.LocationExtras;
import com.earthman.app.nim.uikit.LocationProvider;
import com.earthman.app.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.earthman.app.nim.uikit.common.util.log.LogUtil;

/**
 * Created by zhoujianghua on 2015/8/11.
 */
public class NimDemoLocationProvider implements LocationProvider {
	
	public static boolean isLocationEnable(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria();
		cri.setAccuracy(Criteria.ACCURACY_COARSE);
		cri.setAltitudeRequired(false);
		cri.setBearingRequired(false);
		cri.setCostAllowed(false);
		String bestProvider = locationManager.getBestProvider(cri, true);
		return !TextUtils.isEmpty(bestProvider);
	}
	
    @Override
    public void requestLocation(final Context context, Callback callback) {
        if (!isLocationEnable(context)) {
            final EasyAlertDialog alertDialog = new EasyAlertDialog(context);
            alertDialog.setMessage("位置服务未开启");
            alertDialog.addNegativeButton("取消", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.addPositiveButton("设置", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            try {
                                context.startActivity(intent);
                            } catch (Exception e) {
                                LogUtil.e("LOC", "start ACTION_LOCATION_SOURCE_SETTINGS error");
                            }

                        }
                    });
            alertDialog.show();
            return;
        }

        AMapActivity.start(context, callback);
    }

    @Override
    public void openMap(Context context, double longitude, double latitude, String address) {
        Intent intent = new Intent(context, AMapActivity.class);
        intent.putExtra(LocationExtras.LONGITUDE, longitude);
        intent.putExtra(LocationExtras.LATITUDE, latitude);
        intent.putExtra(LocationExtras.ADDRESS, address);
        context.startActivity(intent);
    }
}
