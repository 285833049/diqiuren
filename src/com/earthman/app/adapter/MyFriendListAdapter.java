package com.earthman.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.widget.sortListView.SortModel;

/**
 * @Title: MyFriendListAdapter
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */

public class MyFriendListAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;

	public MyFriendListAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
		ywbImageLoader = new YwbImageLoader();
	}

	ArrayList<Integer> positioncolor = new ArrayList<Integer>();
	private YwbImageLoader ywbImageLoader;

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		LogUtil.i("listview",list.size()+"getcount" );
		return list.size();
	}

	public Object getItem(int position) {
		LogUtil.i("listview",position+"getItem" );
		return list.get(position);
	}

	public long getItemId(int position) {
		LogUtil.i("listview",position+"getItemid" );
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
	
		LogUtil.i("listview",list.size()+"getView" );
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.friend_items_zhuanzhang, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.friend_list_zhuanzhang_name);
			viewHolder.user_image_icon = (ImageView) view.findViewById(R.id.user_image_icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		ywbImageLoader.loadImage(list.get(position).getPahonecode(), viewHolder.user_image_icon,R.drawable.user_avatar);
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		return view;
	}

	final static class ViewHolder {
		// TextView tvLetter;
		TextView tvTitle;
		ImageView user_image_icon;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
