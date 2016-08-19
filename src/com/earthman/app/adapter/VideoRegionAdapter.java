package com.earthman.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.GetHomeVideoResponse.GetHomeVideoResult.HomeVideo;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.home.VideoRegionDetailActivity;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-29 上午9:30:34
 * @Decription
 */
public class VideoRegionAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HomeVideo> videos;
	private LayoutInflater inflater;
	private YwbImageLoader ywbImageLoader;
	
	public VideoRegionAdapter(Context context, ArrayList<HomeVideo> videos){
		this.context = context;
		this.videos = videos;
		inflater = LayoutInflater.from(context);
		ywbImageLoader = new YwbImageLoader();
	}
	
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if(videos.size()%2 != 0){
			return videos.size()/2 + 1;
		}
		return videos.size()/2;
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return videos.get(position);
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.video_region_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//ywbImageLoader.loadImage(videos.get(position * 2), viewHolder.vide_no2);
		//TODO 需要添加图片加载，目前服务器暂未提供图片字段
		viewHolder.tv_title2.setText(videos.get(position * 2).getName());
		if(videos.size()%2 != 0 && position == videos.size()/2){//
			viewHolder.rl_video_no3.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.rl_video_no3.setVisibility(View.VISIBLE);
			viewHolder.tv_title3.setText(videos.get(position * 2 + 1).getName());
		}
		viewHolder.rl_video_no2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					/*Intent intent = new Intent(Intent.ACTION_VIEW);	
					intent.setData(Uri.parse(videos.get(position*2).getLink()));
					context.startActivity(intent);
				Intent intent = new Intent(context, WebActivity.class);
				intent.putExtra("url", videos.get(position*2).getLink());
				context.startActivity(intent);*/
				Intent intent = new Intent(context, VideoRegionDetailActivity.class);
				intent.putExtra("id", videos.get(position*2).getId());
				context.startActivity(intent);
			}
		});
		viewHolder.rl_video_no3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(Intent.ACTION_VIEW);	
				intent.setData(Uri.parse(videos.get(position*2 + 1).getLink()));
				context.startActivity(intent);
				Intent intent = new Intent(context, WebActivity.class);
				intent.putExtra("url", videos.get(position*2 + 1).getLink());
				context.startActivity(intent);*/
				Intent intent = new Intent(context, VideoRegionDetailActivity.class);
				intent.putExtra("id", videos.get(position*2 + 1).getId());
				context.startActivity(intent);
			}
		});		
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView vide_no2;
		private ImageView vide_no3;
		private TextView tv_title2;
		private TextView tv_title3;
		private RelativeLayout rl_video_no3;
		private RelativeLayout rl_video_no2;
		private TextView tv_futitle2;
		private TextView tv_futitle3;
		private ViewHolder(View convertView){
			vide_no2 = (ImageView) convertView.findViewById(R.id.vide_no2);
			vide_no3 = (ImageView) convertView.findViewById(R.id.vide_no3);
			tv_title2 = (TextView) convertView.findViewById(R.id.tv_title2);
			tv_title3 = (TextView) convertView.findViewById(R.id.tv_title3);
			rl_video_no3 = (RelativeLayout) convertView.findViewById(R.id.rl_video_no3);
			rl_video_no2 = (RelativeLayout) convertView.findViewById(R.id.rl_video_no2);
			tv_futitle2 = (TextView) convertView.findViewById(R.id.tv_futitle2);
			tv_futitle3 = (TextView) convertView.findViewById(R.id.tv_futitle3);
		}
		
	}

}
