package com.earthman.app.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.GetAlbumListResponse.Album;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.album.AlbumListActivity;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-27 下午3:45:25
 * @Decription
 */
@SuppressLint("CutPasteId")
public class AlbumListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private ArrayList<Album> albums;
	private YwbImageLoader imageLoader;
	private int currentPage;
	private ViewHolder viewHolder;
	 	
	public AlbumListAdapter(Context context, ArrayList<Album> albums, int currentPage){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.albums = albums;
		this.currentPage = currentPage;
		imageLoader = new YwbImageLoader();
	}
	
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		switch (currentPage) {
		case AlbumListActivity.SEE_MINE_ALBUM://查看自己的相册
			return albums.size() + 1;
		case AlbumListActivity.SEE_FRIEND_ALBUM://查看好友的相册
			return albums.size();
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
			convertView = inflater.inflate(R.layout.album_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);		
			int width = (int)(AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 100))/2;
			int height = width * 3/4;
			viewHolder.iv_top.getLayoutParams().height = height;
			viewHolder.iv_middle.getLayoutParams().height = height;
			viewHolder.iv_bottom.getLayoutParams().height = height;
			
			viewHolder.iv_top.getLayoutParams().width = width;
			viewHolder.iv_middle.getLayoutParams().width = width;
			viewHolder.iv_bottom.getLayoutParams().width = width;
			
			viewHolder.tv_bg.getLayoutParams().height = height;
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}			
		switch (currentPage) {
		case AlbumListActivity.SEE_MINE_ALBUM://查看自己的相册
			handMine(position);
			break;
		case AlbumListActivity.SEE_FRIEND_ALBUM://查看好友的相册
			handFriend(position);
			break;
		default:
			break;
		}		
		return convertView;
	}
	
	/**
	 * 
	 * handMine(自己的相册处理逻辑)
	 * @param viewHolder
	 * @param position
	 * void
	 * @exception
	 */
	private void handMine(int position){
		if(position == 0){
			viewHolder.rl_album.setVisibility(View.GONE);
			viewHolder.rl_gray.setVisibility(View.VISIBLE);
			viewHolder.tv_add.setVisibility(View.VISIBLE);
			viewHolder.tv_no_img.setVisibility(View.GONE);
			viewHolder.tv_album_name.setVisibility(View.INVISIBLE);
		}else{			
			show(albums.get(position - 1));		
		}
	}
	
	/**
	 * 
	 * showImg(显示图片封面)
	 * @param album
	 * void
	 * @exception
	 */
	private void showImg(Album album){
		viewHolder.rl_album.setVisibility(View.VISIBLE);
		viewHolder.rl_gray.setVisibility(View.GONE);
		imageLoader.loadImage(album.getImg(), viewHolder.iv_top);
		imageLoader.loadImage(album.getImg(), viewHolder.iv_middle);
		imageLoader.loadImage(album.getImg(), viewHolder.iv_bottom);
		String privacy = album.getAlbumsClass();
		if(TextUtils.isEmpty(privacy)){
			viewHolder.tv_permission.setVisibility(View.GONE);
		}else if("1".equals(privacy)){
			viewHolder.tv_permission.setVisibility(View.VISIBLE);
			viewHolder.tv_permission.setText(context.getResources().getString(R.string.fufei));
		}else if("2".equals(privacy)){
			viewHolder.tv_permission.setVisibility(View.VISIBLE);
			viewHolder.tv_permission.setText(context.getResources().getString(R.string.five_level_protection));
		}		
	}
	
	/**
	 * 
	 * showNoImg(显示无图片状态)
	 * void
	 * @exception
	 */
	private void showNoImg(){
		viewHolder.rl_album.setVisibility(View.GONE);
		viewHolder.rl_gray.setVisibility(View.VISIBLE);
		viewHolder.tv_add.setVisibility(View.GONE);
		viewHolder.tv_no_img.setVisibility(View.VISIBLE);
	}
	
	private void show(Album album){
		//获取相册中相片的个数
		int num = album.getAmount();
		if(num == 0){//暂无图片
			showNoImg();
		}else{
			showImg(album);		
		}
		viewHolder.tv_album_name.setVisibility(View.VISIBLE);
		viewHolder.tv_album_name.setText(Html.fromHtml(String.format(context.getString(R.string.album_name), album.getName(), num)));
	}
	
	/**
	 * 
	 * handFriend(好友的处理逻辑)
	 * @param viewHolder
	 * @param position
	 * void
	 * @exception
	 */
	private void handFriend(int position){
		show(albums.get(position));				
	}
	
		
	private class ViewHolder{ 
        private ImageView iv_bottom;
        private ImageView iv_middle;
        private ImageView iv_top;
        private RelativeLayout rl_album;
        private RelativeLayout rl_gray;
        private TextView tv_no_img;
        private TextView tv_add;
        private TextView tv_album_name;
        private TextView tv_bg;
        private TextView tv_permission;
		private ViewHolder(View convertView){
			iv_bottom = (ImageView) convertView.findViewById(R.id.iv_bottom);
			iv_middle = (ImageView) convertView.findViewById(R.id.iv_middle);
			iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
			rl_album = (RelativeLayout) convertView.findViewById(R.id.rl_album);
			rl_gray = (RelativeLayout) convertView.findViewById(R.id.rl_gray);
			tv_no_img = (TextView) convertView.findViewById(R.id.tv_no_img);
			tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			tv_album_name = (TextView) convertView.findViewById(R.id.tv_album_name);
			tv_bg = (TextView) convertView.findViewById(R.id.tv_bg);
			tv_permission = (TextView) convertView.findViewById(R.id.tv_permission);
		}
	}

}
