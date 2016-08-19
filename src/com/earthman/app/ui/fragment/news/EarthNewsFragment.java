package com.earthman.app.ui.fragment.news;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.NewsAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.GetHomeNewsResponse;
import com.earthman.app.bean.GetHomeNewsResponse.GetHomeNewsResult;
import com.earthman.app.bean.GetHomeNewsResponse.GetHomeNewsResult.News;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.pulltoreflesh.XListView.IXListViewListener;
import com.earthman.app.ui.activity.MainActivity;
import com.earthman.app.ui.activity.news.NewsActivity;
import com.earthman.app.ui.activity.news.NewsDetailActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 作者：Zhou
 * 日期：2016-2-26 下午2:32:53
 * 描述：（）
 */
public class EarthNewsFragment extends BaseFragment implements IXListViewListener, OnItemClickListener{

	@ViewInject(R.id.lv_second_news)
	private XListView lv_second_news;
	@ViewInject(R.id.lv_home_news)
	private MyListView lv_home_news;
	private NewsAdapter adapter;
	private ArrayList<News> arrayList;
	private GetHomeNewsResult result;
	private UserInfoPreferences infoPreferences;
	private boolean isRefreshing = true;
	private int total;
	private int currentType;
	
	@Override
	protected View createView() {
		View convertView = LayoutInflater.from(activity).inflate(R.layout.news_fragment, null);		
		ViewUtils.inject(this, convertView);
		return convertView;
	}

	@Override
	protected void initView(View convertView) {
		if(currentType == 0){//首页新闻
			lv_second_news.setVisibility(View.GONE);
			lv_home_news.setVisibility(View.VISIBLE);
			lv_home_news.setAdapter(adapter);
		}else{//二级页面新闻
			lv_second_news.setVisibility(View.VISIBLE);
			lv_home_news.setVisibility(View.GONE);
			lv_second_news.setAutoLoadEnable(false);			
			lv_second_news.setAdapter(adapter);
			lv_second_news.setXListViewListener(this);
		}
		lv_home_news.setOnItemClickListener(this);
		lv_second_news.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {		
		arrayList = new ArrayList<News>();
		adapter = new NewsAdapter(activity, arrayList);	
		if(activity instanceof MainActivity){
			currentType = 0;
		}else if(activity instanceof NewsActivity){
			currentType = 1;
		}
		doGetHomeNews();
	}
	
	public void updateNews(ArrayList<News> newsList){
		if(currentType == 1){
			if(isRefreshing){
				arrayList.clear();
			}	
			pageNo++;
		}else{
			arrayList.clear();
		}
		arrayList.addAll(newsList);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		infoPreferences = new UserInfoPreferences(activity);		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setAttribute() {
		
	}
	
	/**
	 * 
	 * doGetHomeNews(获取新闻)
	 * void
	 * @exception
	 */
	private  void doGetHomeNews(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		list.add(currentType);
		list.add(pageNo);
		showLoading();
		String getNewsUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.GET_EARTH_NEWS, list);
		executeRequest(new FastJsonRequest<GetHomeNewsResponse>(Method.GET, getNewsUrl, GetHomeNewsResponse.class,
				new Response.Listener<GetHomeNewsResponse>() {

					@Override
					public void onResponse(GetHomeNewsResponse response) {
						if ("000000".equals(response.getCode())) {
							// 首页数据,保存数据
							result = response.getResult();		
							if(result != null){
								updateNews(result.getEarthManNews());
								total = result.getTotal();		
							}
						} else {
							NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(),response.getMessage());
						}
						dismissLoading();
						lv_second_news.stopRefresh();
						lv_second_news.stopLoadMore();
						if(total == arrayList.size()){
							lv_second_news.setPullLoadEnable(false);
						}else{
							lv_second_news.setPullLoadEnable(true);
						}
					}
				}, getErrorListener()));
		
	}

	@Override
	protected void handclick(View v) {
		
	}

	@Override
	public void onRefresh() {
		pageNo = 0;
		isRefreshing = true;
		doGetHomeNews();
		
	}

	@Override
	public void onLoadMore() {
		if(total > arrayList.size()){
			isRefreshing = false;
			doGetHomeNews();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
		News news = arrayList.get(parent.getId() == R.id.lv_home_news ? position : position - 1);
		intent.putExtra("title", news.getTitle());
		intent.putExtra("type", 1);
		intent.putExtra("id", news.getId());
		startActivity(intent);
	}
}
