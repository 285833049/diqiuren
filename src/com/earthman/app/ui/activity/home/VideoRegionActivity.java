package com.earthman.app.ui.activity.home;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.VideoRegionAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.GetHomeVideoResponse;
import com.earthman.app.bean.GetHomeVideoResponse.GetHomeVideoResult.HomeVideo;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.pulltoreflesh.XListView.IXListViewListener;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-29 上午9:20:24
 * @Decription 视频专区
 */
@ContentView(R.layout.video_region)
public class VideoRegionActivity extends BaseActivity implements IXListViewListener{

	@ViewInject(R.id.lv_region)
	private XListView lv_region;
	private VideoRegionAdapter adapter;
	private ArrayList<HomeVideo> videos;
	private boolean isRefreshing = true;
	private int total;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		videos = new ArrayList<HomeVideo>();
		adapter = new VideoRegionAdapter(this, videos);
	}
	
	
	/**
	 * 
	 * doGetHomeVideos(获取首页视频专区数据)
	 * void
	 * 
	 * @exception
	 */
	private void doGetHomeVideos() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(1);
		list.add(pageNo);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_HOME_VIDEO, list);
		executeRequest(new FastJsonRequest<GetHomeVideoResponse>(Method.GET, loginUrl, GetHomeVideoResponse.class, new Response.Listener<GetHomeVideoResponse>() {

			@Override
			public void onResponse(GetHomeVideoResponse response) {
				if ("000000".equals(response.getCode())) {
					// 首页数据,保存数据
					//updateHomeVideo(response.getResult());
					if(response.getResult() == null){
						return;
					}
					if(isRefreshing){
						videos.clear();
					}
					total = response.getResult().getTotal();
					pageNo++;
					videos.addAll(response.getResult().getVideos());
					adapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(VideoRegionActivity.this, response.getCode(),response.getMessage());
				}
				lv_region.stopRefresh();
				lv_region.stopLoadMore();
				if(total == videos.size()){
					lv_region.setPullLoadEnable(false);
				}else{
					lv_region.setPullLoadEnable(true);
				}
			}
		}, getErrorListener()));
	}
	
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		lv_region.setAdapter(adapter);
		lv_region.setXListViewListener(this);
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.video_region);
		doGetHomeVideos();
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}


	/* 
	 * @see com.earthman.app.pulltoreflesh.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		pageNo = 0;
		isRefreshing = true;
		doGetHomeVideos();
	}


	/* 
	 * @see com.earthman.app.pulltoreflesh.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		if(total > videos.size()){
			isRefreshing = false;
			doGetHomeVideos();
		}
	}

}
