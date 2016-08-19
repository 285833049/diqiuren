package com.earthman.app.ui.fragment.location;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.earthman.app.R;
import com.earthman.app.adapter.LocationAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.LocationPreference;
import com.earthman.app.eventbusbean.SearchBean;
import com.earthman.app.utils.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-20 下午5:51:27
 * @Decription  关键字搜索
 */
public class KeySearchFragment extends BaseFragment implements OnPoiSearchListener{

	@ViewInject(R.id.lv_location)
	private ListView lv_location;
	private View footView;
	private LayoutInflater inflater;
	private PoiSearch poiSearch;
	private com.amap.api.services.poisearch.PoiSearch.Query query;
	private int currentPage;
	private LocationPreference locationPreference;
	private ArrayList<PoiItem> poiItems;
	private LocationAdapter locationAdapter;
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
		locationPreference = new LocationPreference(activity);
	}
	
	
	/**
	 * 
	 * search(关键字检索)
	 * @param keyWord
	 * void
	 * @exception
	 */
	private void searchByKey(String keyWord){
		showFootView();
		locationPreference = new LocationPreference(activity);
		query = new PoiSearch.Query(keyWord, "", locationPreference.getCityCode());
		// keyWord表示搜索字符串，
		//第二个参数表示POI搜索类型，二者选填其一，
		//POI搜索类型共分为以下20种：汽车服务|汽车销售|
		//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		//cityCode表示POI搜索区域的编码，是必须设置参数
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);//设置查询页码
		poiSearch = new PoiSearch(activity,query);//初始化poiSearch对象
		poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
		poiSearch.searchPOIAsyn();//开始搜索
	}

	/* 
	 * @see com.earthman.app.base.BaseFragment#initView(android.view.View)
	 */
	@Override
	protected void initView(View convertView) {
		inflater = LayoutInflater.from(activity);
		footView = inflater.inflate(R.layout.location_foot, null);
		lv_location.addFooterView(footView);
		lv_location.setAdapter(locationAdapter);
		hideFootView();
		EventBus.getDefault().register(this);
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
		
	}

	public void showFootView(){
		footView.setVisibility(View.VISIBLE);
	}
	
	public void hideFootView(){
		footView.setVisibility(View.GONE);
	}

	/* 
	 * @see com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener#onPoiSearched(com.amap.api.services.poisearch.PoiResult, int)
	 */
	@Override
	public void onPoiSearched(PoiResult poiResult, int rCode) {
		if (rCode == 1000) {
			hideFootView();
            currentPage++;
            if(poiResult != null && poiResult.getPois() != null){
            	poiItems.addAll(poiResult.getPois());
            	locationAdapter.notifyDataSetChanged();
            }
		}
	}
	
	
	public void onEventMainThread(SearchBean searchBean){
		if(searchBean.getSearchType() == Constants.KEY_SEARCH){
			currentPage = 0;
			poiItems.clear();
			searchByKey(searchBean.getKey());
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
