package com.earthman.app.ui.activity.home;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.GetVideoDetailResponse;
import com.earthman.app.bean.GetVideoDetailResponse.VideoDetail;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-23 下午3:30:50
 * @Decription  视频专区视频的详情
 */
@ContentView(R.layout.video_detail)
public class VideoRegionDetailActivity extends BaseActivity{

	@ViewInject(R.id.iv_video)
	private ImageView iv_video;
	@ViewInject(R.id.tv_video_name)
	private TextView tv_video_name;
	@ViewInject(R.id.tv_screenwriter)
	private TextView tv_screenwriter;
	@ViewInject(R.id.tv_introduce)
	private TextView tv_introduce;
	private int id;
	private String link;
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View view){
		finish();
	}
	
	@OnClick(R.id.rl_video)
	private void onVideoClick(View view){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(link), "video/mp4");
		startActivity(intent);
	}
	
	

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		Intent intent = getIntent();
		if(intent != null){
			id = intent.getIntExtra("id", 0);
		}		
		doGetVideoDetail();
		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}
	
	/**
	 * 
	 * doGetVideoDetail(获取首页视频专区数据)
	 * void
	 * 
	 * @exception
	 */
	private void doGetVideoDetail() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(id);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_VIDEO_DETAIL, list);
		executeRequest(new FastJsonRequest<GetVideoDetailResponse>(Method.GET, loginUrl, GetVideoDetailResponse.class, new Response.Listener<GetVideoDetailResponse>() {

			@Override
			public void onResponse(GetVideoDetailResponse response) {
				if ("000000".equals(response.getCode())) {
					VideoDetail homeVideo = response.getResult();
					tv_screenwriter.setText(String.format(getString(R.string.screenwriter), homeVideo.getAuthor()));
					tv_introduce.setText(String.format(getString(R.string.video_introduce), homeVideo.getDetails()));
					tv_video_name.setText(homeVideo.getName());
					new YwbImageLoader().loadImage(homeVideo.getImg(), iv_video);
					link = homeVideo.getLink();
				} else {
					NetStatusHandUtil.getInstance().handStatus(VideoRegionDetailActivity.this, response.getCode(),response.getMessage());
				}				
			}
		}, getErrorListener()));
	}

}
