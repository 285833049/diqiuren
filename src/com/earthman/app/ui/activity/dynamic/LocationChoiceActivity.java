package com.earthman.app.ui.activity.dynamic;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.LocationPreference;
import com.earthman.app.eventbusbean.ChooseResult;
import com.earthman.app.eventbusbean.SearchBean;
import com.earthman.app.ui.fragment.location.KeySearchFragment;
import com.earthman.app.ui.fragment.location.NearSearchFragment;
import com.earthman.app.utils.Constants;
import com.earthman.app.widget.sortListView.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-20 上午11:16:52
 * @Decription 所在位置界面
 */
@ContentView(R.layout.location_choice)
public class LocationChoiceActivity extends BaseActivity implements AMapLocationListener{

	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.btn_search)
	private Button btn_search;	
	@ViewInject(R.id.ll_key)
	private LinearLayout ll_key;
	@ViewInject(R.id.filter_edit)
	private ClearEditText filter_edit;
	@ViewInject(R.id.iv_search)
	private Button iv_search;		
	private int currentSearchType;
	private LocationPreference locationPreference;
	private FragmentManager fragmentManager;
	private NearSearchFragment nearSearchFragment;
	private KeySearchFragment keySearchFragment;
	private Fragment currentFragment;
	AMapLocationClient mlocationClient;
	private ChooseResult result;
	
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
         locationPreference = new LocationPreference(this);
         fragmentManager = getSupportFragmentManager();
         nearSearchFragment = new NearSearchFragment();
         keySearchFragment = new KeySearchFragment();
         currentFragment = nearSearchFragment;
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);		
		Intent intent = getIntent();
		if(intent != null){
			result = (ChooseResult) intent.getSerializableExtra("result");
		}
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		fragmentManager.beginTransaction().add(R.id.framelayout, nearSearchFragment).commitAllowingStateLoss();
        locate();
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		if(currentSearchType == Constants.KEY_SEARCH){
			showNearSearch();
		}else{
			finish();
		}
	}
	
	@OnClick(R.id.iv_search)
	private void onSearchIconClick(View v){
		showKeySearch();		
	}
	
	@OnClick(R.id.btn_search)
	private void onSearchBtnClick(View v){
		if(TextUtils.isEmpty(filter_edit.getText().toString())){
			return;
		}
		EventBus.getDefault().post(new SearchBean(Constants.KEY_SEARCH, filter_edit.getText().toString()));
	}
	
	
	private void showKeySearch(){
		currentSearchType = Constants.KEY_SEARCH;
		tv_left.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		ll_key.setVisibility(View.VISIBLE);
		btn_search.setVisibility(View.VISIBLE);
		showContent();
	}
	
	private void showNearSearch(){
		currentSearchType = Constants.NEAR_SEARCH;
		tv_left.setVisibility(View.VISIBLE);
		iv_search.setVisibility(View.VISIBLE);
		ll_key.setVisibility(View.GONE);
		btn_search.setVisibility(View.GONE);
		showContent();
	}
	
	
	private void showContent(){
		switch (currentSearchType) {
		case Constants.NEAR_SEARCH:
			gotoFragment(nearSearchFragment);
			break;
		case Constants.KEY_SEARCH:
			gotoFragment(keySearchFragment);
			break;
		default:
			break;
		}
	}
	
	private void gotoFragment(Fragment toFragment){
		if(currentFragment != toFragment){
			if(!toFragment.isAdded()){
				fragmentManager.beginTransaction().hide(currentFragment).add(R.id.framelayout, toFragment).commitAllowingStateLoss();
			}else{
				fragmentManager.beginTransaction().hide(currentFragment).show(toFragment).commitAllowingStateLoss();
			}
			currentFragment = toFragment;
		}
	}

		
	private void locate(){
		//声明mLocationOption对象
		AMapLocationClientOption mLocationOption = null;
		mlocationClient = new AMapLocationClient(this);
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位监听
		mlocationClient.setLocationListener(this);
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置定位间隔,单位毫秒,默认为2000ms
		//mLocationOption.setInterval(2000);
		//设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		//启动定位
		mlocationClient.startLocation();
	}

	/* 
	 * @see com.amap.api.location.AMapLocationListener#onLocationChanged(com.amap.api.location.AMapLocation)
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if(amapLocation != null) {
	        if (amapLocation.getErrorCode() == 0) {
	        //定位成功回调信息，设置相关消息
	        //amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
	        locationPreference.setLatitude(String.valueOf(amapLocation.getLatitude()));
	        locationPreference.setLongitude(String.valueOf(amapLocation.getLongitude()));
	        locationPreference.setCityCode(String.valueOf(amapLocation.getCityCode()));
	        locationPreference.setCityName(String.valueOf(amapLocation.getCity()));	
	        EventBus.getDefault().post(result);
	        EventBus.getDefault().post(new SearchBean(Constants.NEAR_SEARCH, ""));	        
	    } else {
	              //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
	        Log.e("AmapError","location Error, ErrCode:"
	            + amapLocation.getErrorCode() + ", errInfo:"
	            + amapLocation.getErrorInfo());
	        }
	    }
		mlocationClient.stopLocation();
		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}
	
}
