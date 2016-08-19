package com.earthman.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.earthman.app.R;
import com.earthman.app.bean.GetPhotosResponse.Photo;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.album.AlbumListActivity;
import com.earthman.app.ui.activity.album.PhotoListActivity;
import com.earthman.app.ui.activity.dynamic.ImageViewPage;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-30 下午8:15:17
 * @Decription
 */
public class PhotoAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Photo> photos;
	private HashMap<Integer, Boolean> hashMap;
	public static final int SELECT_ALL_STATUS = 1;//全部选中
	public static final int ALL_UNSELECT_STATUS = 0;//全部未选中
	private YwbImageLoader imageLoader;
	private int currentPage;
	private ViewHolder viewHolder;
	private String urls;
	private int mScreen;

	public PhotoAdapter(Context context, ArrayList<Photo> photos, int currentPage){
		this.context = context;
		inflater = LayoutInflater.from(context);
		hashMap = new HashMap<Integer, Boolean>();
		this.photos = photos;
		imageLoader = new YwbImageLoader();
		this.currentPage = currentPage;
		mScreen=(int)(AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 14));
	}
	
	/* 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < photos.size(); i++){
			buffer.append(photos.get(i).getImg());
			if(i != photos.size() -1){
				buffer.append(",");
			}
		}
		urls = buffer.toString();
		//Log.d("ImageUrlLYM", urls);
		/*switch (currentPage) {
		case AlbumListActivity.SEE_MINE_ALBUM://查看自己的相册
			return photos.size() + 1;
		case AlbumListActivity.SEE_FRIEND_ALBUM://查看好友的相册
			return photos.size();
		default:
			break;
		}*/		
		return photos.size();		
	}

	/* 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	/* 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {		
		return position;
	}


	/**
	 * 
	 * initHashMap(这里用一句话描述这个方法的作用)
	 * void
	 * @exception
	 */
	public void setSelectStatus(int action){
		hashMap.clear();
		for(int i = 0; i < photos.size(); i++){
			if(action == SELECT_ALL_STATUS){
				hashMap.put(i, true);
			}else{
				hashMap.put(i, false);
			}

		}
	}
	
	private int getNum(){		
		return getSelectList().size();
	}
	
	private ArrayList<Integer> getSelectList(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < photos.size(); i++){//除开上传照片
			if(hashMap.get(i)){
				list.add(photos.get(i).getId());
			}
		}
		return list;
	}
	
	
	
	/**
	 * 
	 * getSelectIds(获取选中的图片)
	 * void
	 * @exception
	 */
	public String getSelectIds(){
		StringBuffer buffer = new StringBuffer();
		ArrayList<Integer> list = getSelectList();
		for(int j = 0; j < list.size(); j++){
			buffer.append(list.get(j));
			if(j != list.size() - 1){
				buffer.append(",");
			}
		}
		return buffer.toString();
	}

	/* 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.photo_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams layoutParams = (LayoutParams) viewHolder.iv_photo.getLayoutParams();
		layoutParams.height = (int)mScreen/3;	
		layoutParams.width=(int)mScreen/3;	
		switch (currentPage) {
		case AlbumListActivity.SEE_MINE_ALBUM://查看自己的相册
			handMine(position, convertView);
			break;
		case AlbumListActivity.SEE_FRIEND_ALBUM://查看好友的相册
			handFriend(position, convertView);
			break;
		default:
			break;
		}
		return convertView;
	}
	
	/**
	 * 
	 * handFriend(处理好友的逻辑)
	 * @param position
	 * @param convertView
	 * void
	 * @exception
	 */
	private void handFriend(final int position, final View convertView){
		viewHolder.iv_photo.setVisibility(View.VISIBLE);
		viewHolder.iv_selected.setVisibility(View.GONE);
		imageLoader.loadImage(photos.get(position).getImg(), viewHolder.iv_photo);	
		
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO 跳转到图片预览界面				
				Intent intent = new Intent(context, ImageViewPage.class);
				intent.putExtra("pics", urls);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		
	}
	
	/**
	 * 
	 * handMine(处理我的逻辑)
	 * @param position
	 * @param convertView
	 * void
	 * @exception
	 */
	private void handMine(final int position, final View convertView){
		
			viewHolder.iv_photo.setVisibility(View.VISIBLE);
			imageLoader.loadImage(photos.get(position).getImg(), viewHolder.iv_photo);
			if(hashMap.get(position)){
				viewHolder.shade.setVisibility(View.VISIBLE);
				viewHolder.iv_selected.setVisibility(View.VISIBLE);
			}else{
				viewHolder.shade.setVisibility(View.GONE);
				viewHolder.iv_selected.setVisibility(View.GONE);
			}
		
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {				
				if(context instanceof PhotoListActivity){					
					PhotoListActivity activity = (PhotoListActivity) context;
					if(activity.isDeleteStatus){//此时activity处于编辑状态
						//TODO 改变图片的选中状态
						if(hashMap.get(position)){
							hashMap.put(position, false);
							v.findViewById(R.id.iv_selected).setVisibility(View.GONE);
						}else{
							hashMap.put(position, true);
							v.findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
						}
						
						activity.setSelect(getNum());
						//notifyDataSetChanged();
					}else{//此时activity处于非编辑状态
						//TODO 跳转到图片预览界面
						Intent intent = new Intent(context, ImageViewPage.class);
						intent.putExtra("pics", urls);
						intent.putExtra("position", position);
						intent.putExtra("currentPage", AlbumListActivity.SEE_MINE_ALBUM);
						context.startActivity(intent);
					}

				}
			}
		});
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(context instanceof PhotoListActivity){
					PhotoListActivity activity = (PhotoListActivity) context;
					if(activity.isDeleteStatus){//此时activity处于编辑状态
						return true;
					}
					hashMap.put(position, true);
					activity.setSelect(getNum());
					//notifyDataSetChanged();
					activity.setEditStatus();
					v.findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
	}


	private class ViewHolder{
		private View shade;
		private ImageView iv_selected;
		private ImageView iv_photo;
		private ViewHolder(View convertView){
			shade = convertView.findViewById(R.id.shade);
			iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
			iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
		}
	}

}
