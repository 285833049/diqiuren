package com.earthman.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.LoadedImage;
import com.earthman.app.bean.Video;
import com.earthman.app.service.UploadService;
import com.earthman.app.ui.activity.video.LocalVideoListActivity;
import com.earthman.app.ui.activity.video.VideoListActivity;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-5 下午6:36:26
 * @Decription
 */
public class LocalVideoListAdapter extends BaseAdapter{

	List<Video> listVideos;
	int local_postion = 0;
	boolean imageChage = false;
	private LayoutInflater mLayoutInflater;
	private LocalVideoListActivity activity;
	private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

	private String name;
	private int privacy;

	public LocalVideoListAdapter(Context context, List<Video> listVideos, String name, int privacy){
		mLayoutInflater = LayoutInflater.from(context);
		this.listVideos = listVideos;
		if(context instanceof LocalVideoListActivity){
			activity = (LocalVideoListActivity) context;
		}
		this.name = name;
		this.privacy = privacy;
	}
	@Override
	public int getCount() {
		return photos.size();
	}
	public void addPhoto(LoadedImage image){
		photos.add(image);
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



	public final class ViewHolder{
		public ImageView img;
		public TextView title;
		public TextView time;
		public ProgressBar progressBar;
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.video_list_item, null);
			holder.img = (ImageView)convertView.findViewById(R.id.video_img);
			holder.title = (TextView)convertView.findViewById(R.id.video_title);
			holder.time = (TextView)convertView.findViewById(R.id.video_time);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}

		holder.title.setText(listVideos.get(position).getTitle());//ms
		long min = listVideos.get(position).getDuration() /1000 / 60;
		long sec = listVideos.get(position).getDuration() /1000 % 60;
		holder.time.setText(min+" : "+sec);
		holder.img.setImageBitmap(photos.get(position).getBitmap());
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, UploadService.class);
				intent.putExtra("name", name);
				intent.putExtra("privacy", privacy);
				intent.putExtra("path", listVideos.get(position).getPath());
                activity.startService(intent);
				Intent intent1 = new Intent(activity, VideoListActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				activity.startActivity(intent1);
				activity.finish();
				
			}
		});
		return convertView;
	}

	
}
