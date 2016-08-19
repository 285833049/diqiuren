package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.GetVideoListResponse.VideoItem;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.video.VideoListActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-1 上午10:59:47
 * @Decription
 */
public class VideoListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<VideoItem> videoItems;
	private int currentPage;
	ViewHolder viewHolder;
	private YwbImageLoader imageLoader;

	public VideoListAdapter(Context context, ArrayList<VideoItem> videoItems, int currentPage){
		this.context = context;
		this.videoItems = videoItems;
		this.currentPage = currentPage;
		imageLoader = new YwbImageLoader();
	}
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		switch (currentPage) {
		case VideoListActivity.SEE_MINE_VIDEO:
			return (videoItems.size() + 1);
		case VideoListActivity.SEE_FRIEND_VIDEO:
			return videoItems.size();
		default:
			break;
		}
		return 0;
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.video_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
			int width = (int)(AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 60))/2;
			int height = width * 3 / 4;
			LayoutParams params1 = (LayoutParams) viewHolder.rl_upload_video.getLayoutParams();
			params1.width = width;
			params1.height = height;
			LayoutParams params = (LayoutParams) viewHolder.rl_video.getLayoutParams();
			params.width = width;
			params.height = height;
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}		
		switch (currentPage) {
		case VideoListActivity.SEE_MINE_VIDEO:
			handMine(position);
			break;
		case VideoListActivity.SEE_FRIEND_VIDEO:
			handFriend(position);
			break;
		default:
			break;
		}		
		return convertView;
	}

	private void handMine(int position){
		if(position == 0){
			showUpload();	
		}else{
			showImg(videoItems.get(position - 1));			
		}
	}


	private void showUpload(){
		viewHolder.rl_upload_video.setVisibility(View.VISIBLE);
		viewHolder.rl_video.setVisibility(View.INVISIBLE);
		viewHolder.tv_video_name.setVisibility(View.INVISIBLE);	
	}

	private void showImg(VideoItem videoItem){
		viewHolder.rl_upload_video.setVisibility(View.GONE);
		viewHolder.rl_video.setVisibility(View.VISIBLE);
		viewHolder.tv_video_name.setVisibility(View.VISIBLE);	
		viewHolder.tv_video_name.setText(videoItem.getTitle());
		imageLoader.loadImage(videoItem.getSmallimg(), viewHolder.iv_video, R.drawable.user_avatar);
		String status = videoItem.getStatus();
		if(TextUtils.isEmpty(status)||"1".equals(status)||"0".equals(status)){
			viewHolder.tv_quanxuan.setVisibility(View.GONE);			
		}else{
			viewHolder.tv_quanxuan.setVisibility(View.VISIBLE);
			switch (Integer.parseInt(status)) {
			case Constants.PAY_PRIORITY:
				viewHolder.tv_quanxuan.setText(context.getResources().getString(R.string.fufei));
				break;
			case Constants.PROTECT_PRIORITY:
				viewHolder.tv_quanxuan.setText(context.getResources().getString(R.string.five_level_protection));
				break;		
			default:
				break;
			}
		}
	}


	private void handFriend(int position){
		showImg(videoItems.get(position));
	}


	private class ViewHolder{
		private RelativeLayout rl_upload_video;
		private RelativeLayout rl_video;
		private ImageView iv_video;
		private TextView tv_quanxuan;
		private TextView tv_video_name;
		private ViewHolder(View convertView){
			rl_upload_video = (RelativeLayout) convertView.findViewById(R.id.rl_upload_video);
			rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
			iv_video = (ImageView) convertView.findViewById(R.id.iv_video);
			tv_quanxuan = (TextView) convertView.findViewById(R.id.tv_quanxuan);
			tv_video_name = (TextView) convertView.findViewById(R.id.tv_video_name);				
		}
	}

}
