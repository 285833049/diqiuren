package com.earthman.app.ui.activity.video;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.LocalVideoListAdapter;
import com.earthman.app.bean.LoadedImage;
import com.earthman.app.bean.Video;
import com.earthman.app.bean.Video.AbstructProvider;
import com.earthman.app.bean.VideoProvider;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-5 下午6:33:03
 * @Decription
 */
public class LocalVideoListActivity extends Activity{

	public LocalVideoListActivity instance = null;
	ListView mJieVideoListView;
	LocalVideoListAdapter mJieVideoListViewAdapter;
	List<Video> listVideos;
	int videoSize;
	private Button btn_back;
	private TextView tv_left;
	private String name;
	private int privacy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localvideolist);
		instance = this;
		AbstructProvider provider = new VideoProvider(instance);
		listVideos = provider.getList();
		videoSize = listVideos.size();
		Intent intent = getIntent();
		if(intent != null){
			name = intent.getStringExtra("name");
			privacy = intent.getIntExtra("privacy", 0);
		}
		mJieVideoListViewAdapter = new LocalVideoListAdapter(this, listVideos, name, privacy);
		mJieVideoListView = (ListView)findViewById(R.id.jievideolistfile);
		btn_back = (Button) findViewById(R.id.btn_back);
		mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_left.setText(R.string.video);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		loadImages();
	}
	/**
	 * Load images.
	 */
	private void loadImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}
	}
	
	
	private void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			mJieVideoListViewAdapter.addPhoto(image);
			mJieVideoListViewAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
		final ListView grid = mJieVideoListView;
		final int count = grid.getChildCount();
		final LoadedImage[] list = new LoadedImage[count];

		for (int i = 0; i < count; i++) {
			final ImageView v = (ImageView) grid.getChildAt(i);
			list[i] = new LoadedImage(
					((BitmapDrawable) v.getDrawable()).getBitmap());
		}

		return list;
	}
	
	
	
	/** 
	 * 获取视频缩略图 
	 * @param videoPath 
	 * @param width 
	 * @param height 
	 * @param kind 
	 * @return 
	 */  
	private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){  
		Bitmap bitmap = null;  
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
		return bitmap;  
	} 

	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			for (int i = 0; i < videoSize; i++) {
				bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
				//if (bitmap != null) {
					publishProgress(new LoadedImage(bitmap));
				//}
			}
			return null;
		}

		@Override
		public void onProgressUpdate(LoadedImage... value) {
			addImage(value);
		}
	}
}
