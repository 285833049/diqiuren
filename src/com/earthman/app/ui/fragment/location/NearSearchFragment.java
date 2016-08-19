package com.earthman.app.ui.fragment.location;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.earthman.app.R;
import com.earthman.app.adapter.LocationAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.LocationPreference;
import com.earthman.app.eventbusbean.ChooseResult;
import com.earthman.app.eventbusbean.SearchBean;
import com.earthman.app.utils.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-20 下午5:51:12
 * @Decription  周边搜索
 */
public class NearSearchFragment extends BaseFragment implements OnPoiSearchListener{

	@ViewInject(R.id.lv_location)
	private ListView lv_location;
	private View headView;
	private View footView;
	private LayoutInflater inflater;
	private PoiSearch poiSearch;
	private com.amap.api.services.poisearch.PoiSearch.Query query;
	private int currentPage;
	private LocationPreference locationPreference;
	private ArrayList<PoiItem> poiItems;
	private LocationAdapter locationAdapter;
	private RelativeLayout rl_city;
	private RelativeLayout rl_last_poi;
	private TextView tv_city;
	private TextView tv_title;
	private TextView tv_address;
	private ImageView iv_item;
	private ImageView iv_no_poi;
	private ImageView iv_city;
	private int lastChooseType;
	private RelativeLayout rl_no_poi; 


	/* 
	 * @see com.earthman.app.base.BaseFragment#createView()
	 */
	@Override
	protected View createView() {
		convertView = LayoutInflater.from(activity).inflate(R.layout.search_result_list, null);
		ViewUtils.inject(this, convertView);
		return convertView;
	}

	/* 
	 * @see com.earthman.app.base.BaseFragment#initData()
	 */
	@Override
	protected void initData() {
		poiItems = new ArrayList<PoiItem>();
		locationAdapter = new LocationAdapter(activity, poiItems);		
	}

	/* 
	 * @see com.earthman.app.base.BaseFragment#initView(android.view.View)
	 */
	@Override
	protected void initView(View convertView) {		
		inflater = LayoutInflater.from(activity);
		headView = inflater.inflate(R.layout.location_head, null);
		footView = inflater.inflate(R.layout.location_foot, null);	
		rl_no_poi = (RelativeLayout) headView.findViewById(R.id.rl_no_poi);
		rl_city = (RelativeLayout) headView.findViewById(R.id.rl_city);
		rl_last_poi = (RelativeLayout) headView.findViewById(R.id.rl_last_poi);
		tv_city = (TextView) headView.findViewById(R.id.current_city);
		iv_no_poi = (ImageView) headView.findViewById(R.id.iv_no_poi);
		iv_item = (ImageView) headView.findViewById(R.id.iv_item);
		iv_city = (ImageView) headView.findViewById(R.id.iv_city);
		tv_title = (TextView) headView.findViewById(R.id.tv_title);
		tv_address = (TextView) headView.findViewById(R.id.tv_address);
		lv_location.addHeaderView(headView);
		lv_location.addFooterView(footView);
		lv_location.setAdapter(locationAdapter);
		EventBus.getDefault().register(this);
		lv_location.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem + visibleItemCount == totalItemCount - 1){
					searchByLocation();
				}
				
			}
		});
		rl_no_poi.setOnClickListener(this);
		rl_city.setOnClickListener(this);
		rl_last_poi.setOnClickListener(this);
	}

	public void showFootView(){
		footView.setVisibility(View.VISIBLE);
	}
	
	public void hideFootView(){
		footView.setVisibility(View.GONE);
	}

	/* 
	 * @see com.earthman.app.base.BaseFragment#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	/* 
	 * @see com.earthman.app.base.BaseFragment#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
        switch (v.getId()) {
		case R.id.rl_no_poi:
			EventBus.getDefault().post(new ChooseResult(1, null, null, null));
			activity.finish();
			break;
		case R.id.rl_city:
			EventBus.getDefault().post(new ChooseResult(2, locationPreference.getCityName(), null, null));
			activity.finish();
			break;
	    case R.id.rl_last_poi:
	    	EventBus.getDefault().post(new ChooseResult(3, null, tv_title.getText().toString(), null));
	    	activity.finish();
	    	break;
		default:
			break;
		}
	}

	/* 
	 * @see com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener#onPoiSearched(com.amap.api.services.poisearch.PoiResult, int)
	 */
	@Override
	public void onPoiSearched(PoiResult poiResult, int rCode) {
		if (rCode == 1000) {
			switch (lastChooseType) {//上次选择的是不显示位置或者显示详情位置搜索完后都要显示当前所在城市
			case 1:
			case 3:
				rl_city.setVisibility(View.VISIBLE);
				iv_city.setVisibility(View.GONE);
				tv_city.setText(locationPreference.getCityName());
				break;
			default:
				break;
			}
            hideFootView();
            currentPage++;
            if(poiResult != null && poiResult.getPois() != null){
            	poiItems.addAll(poiResult.getPois());
            	locationAdapter.notifyDataSetChanged();
            }
            
		}
	}

	/*
	 * 搜索周边
	 */
	private void searchByLocation(){
		showFootView();
		locationPreference = new LocationPreference(activity);
		query = new PoiSearch.Query("", "", locationPreference.getCityCode());
		// keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
		//共分为以下20种：汽车服务|汽车销售|
		//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		//cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);//设置查第一页
		poiSearch = new PoiSearch(activity,query);
		String latitude = locationPreference.getLatitude();
		String longitude = locationPreference.getLongitude();
		poiSearch.setBound(new SearchBound(new LatLonPoint(Double.parseDouble(latitude),Double.parseDouble(longitude)), 1000));//设置周边搜索的中心点以及区域
		poiSearch.setOnPoiSearchListener(this);//设置数据返回的监听器
		poiSearch.searchPOIAsyn();//开始搜索
	}


	public void onEventMainThread(SearchBean searchBean){
		if(searchBean.getSearchType() == Constants.NEAR_SEARCH){			
			searchByLocation();
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	public void onEventMainThread(ChooseResult result){
		lastChooseType = result.getChooseType();
		switch (lastChooseType) {
		case 2:	//显示城市
			rl_city.setVisibility(View.VISIBLE);
			tv_city.setText(result.getCityName());
			iv_no_poi.setVisibility(View.GONE);
			break;
		case 3://显示详细位置
			rl_last_poi.setVisibility(View.VISIBLE);
			tv_title.setText(result.getTitle());
			tv_address.setText(result.getAddress());
			iv_no_poi.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}



}
