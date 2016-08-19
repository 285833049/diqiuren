package com.earthman.app.nim.location.activity;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.earthman.app.R;
import com.earthman.app.base.VinciBaseActivity;
import com.earthman.app.bean.MapInfo;
import com.earthman.app.enums.MapName;
import com.earthman.app.listener.SelectMapDialogListener;
import com.earthman.app.nim.uikit.LocationProvider;
import com.earthman.app.utils.MapUtils;
import com.earthman.app.utils.StringUtils;
import com.earthman.app.widget.DialogSelectMap;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-9 下午3:02:36
 * @Decription 高德地图定位
 */
@ContentView(R.layout.activity_baidumap)
public class AMapActivity extends VinciBaseActivity implements OnClickListener, LocationSource, AMapLocationListener, InfoWindowAdapter, SelectMapDialogListener {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;// 返回键
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	@ViewInject(R.id.btn_right)
	private Button mBtnRight;
	// --------------------------------
	private double mLatitude;
	private double mLongtitude;
	private String mAddress;

	private AMap aMap;
	@ViewInject(R.id.map)
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private AMapLocation mAMapLocation;

	private DialogSelectMap mSelectMap;
	private boolean isInstallMap;
	private List<MapInfo> mMapList;
	
	private static LocationProvider.Callback callback;
    public static void start(Context context, LocationProvider.Callback callback) {
    	AMapActivity.callback = callback;
        context.startActivity(new Intent(context, AMapActivity.class));
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
	}

	@Override
	public void initArguments() {
		Intent intent = getIntent();
		mLatitude = intent.getDoubleExtra("latitude", 0);
		mLongtitude = intent.getDoubleExtra("longitude", 0);
		mAddress = intent.getStringExtra("address");
		// -----------------------------------------
		if (aMap == null) {
			aMap = mapView.getMap();
			// 自定义系统定位小蓝点
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker));// 设置小蓝点的图标
			myLocationStyle.strokeColor(0x7F0070D9);// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(0x130070D9);// 设置圆形的填充颜色
			// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
			myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setLocationSource(this);// 设置定位监听
			aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
			aMap.setInfoWindowAdapter(this);
			aMap.moveCamera(CameraUpdateFactory.zoomTo(17.5f));
		}
		// -----------------------------添加位置标记
		if (StringUtils.isBlank(mAddress) == false) {
			mBtnRight.setVisibility(View.GONE);
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.title(mAddress);
			markerOptions.draggable(false);// 不让拖动
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark));
			markerOptions.position(new LatLng(mLatitude, mLongtitude));
			// aMap.addMarker(markerOptions);
			Marker marker = aMap.addMarker(markerOptions);
			marker.showInfoWindow();
//			aMap.moveCamera(CameraUpdateFactory.zoomTo(17.5f));
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(mLatitude, mLongtitude)));
		}else{
			aMap.setMyLocationEnabled(true);
		}
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(R.layout.map_pop_view, null);
		TextView tvAddress = (TextView) infoWindow.findViewById(R.id.tv_address);
		Button btnNavi = (Button) infoWindow.findViewById(R.id.btn_navi);
		tvAddress.setText(mAddress);
		btnNavi.setOnClickListener(this);
		return infoWindow;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public void initSubviews() {
		mTvLeft.setText("地图定位");
		mBtnRight.setText("发送");
	}

	@Override
	public void initData() {
		mMapList = MapUtils.getInstalledMap(this);
		if (mMapList.size() > 0) {
			isInstallMap = true;
		} else {
			isInstallMap = false;
			mMapList.add(new MapInfo(MapName.BAIDU, R.drawable.ic_map_baidu, "百度地图", "com.baidu.BaiduMap"));
			mMapList.add(new MapInfo(MapName.GAODE, R.drawable.ic_map_gaode, "高德地图", "com.autonavi.minimap"));
		}
	}

	@OnClick(value = { R.id.btn_back, R.id.btn_right })
	@Override
	public void onClickListener(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			if (mAMapLocation != null) {
				Intent intent = this.getIntent();
				intent.putExtra(LocationExtras.LATITUDE, mAMapLocation.getLatitude());
				intent.putExtra(LocationExtras.LONGITUDE, mAMapLocation.getLongitude());
				intent.putExtra(LocationExtras.ADDRESS, mAMapLocation.getAddress());
				intent.putExtra(LocationExtras.ZOOM_LEVEL, aMap.getCameraPosition().zoom);
				 intent.putExtra(LocationExtras.IMG_URL, getStaticMapUrl());
				 //----------------------------------------
				  if (callback != null) {
			            callback.onSuccess(mAMapLocation.getLongitude(), mAMapLocation.getLatitude(), mAMapLocation.getAddress());
			        }
				 //----------------------------------------
				this.setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
			break;
		case R.id.btn_navi:
			mSelectMap = new DialogSelectMap(this, isInstallMap, mMapList, this);
			mSelectMap.show();
			break;
		}
	}
	
    private String getStaticMapUrl() {
    	StringBuilder urlBuilder = new StringBuilder(LocationExtras.STATIC_MAP_URL_1);
    	urlBuilder.append(mAMapLocation.getLatitude());
    	urlBuilder.append(",");
    	urlBuilder.append(mAMapLocation.getLongitude());
    	urlBuilder.append(LocationExtras.STATIC_MAP_URL_2);
        return urlBuilder.toString();
    }

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(mAMapLocation = amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	@Override
	public void onMapItemListener(MapInfo info) {
		mSelectMap.dismiss();
		if (isInstallMap) {
			if(mAMapLocation!=null){
				if (info.getMapPageName().equals("com.baidu.BaiduMap")) {
					MapUtils.openBaiduMap(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
				} else if (info.getMapPageName().equals("com.autonavi.minimap")) {
					MapUtils.openGaoDeMap(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
				}
			}else{
				if (info.getMapPageName().equals("com.baidu.BaiduMap")) {
					MapUtils.openBaiduMap(mLatitude, mLongtitude);
				} else if (info.getMapPageName().equals("com.autonavi.minimap")) {
					MapUtils.openGaoDeMap(mLatitude, mLongtitude);
				}
			}
	
		} else {
			Intent installIntent = new Intent("android.intent.action.VIEW");
			installIntent.setData(Uri.parse("market://details?id=" + info.getMapPageName()));
			startActivity(installIntent);
		}
	}

}
