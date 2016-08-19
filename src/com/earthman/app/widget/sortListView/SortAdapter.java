package com.earthman.app.widget.sortListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.imageloader.YwbImageLoader;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private int type;
	private ArrayList<String> list_quhao;
	private UserInfoPreferences userInfoPreferences;

	public SortAdapter(Context mContext, List<SortModel> list, int type) {
		this.mContext = mContext;
		this.list = list;
		this.type = type;
		ywbImageLoader = new YwbImageLoader();
		userInfoPreferences = new UserInfoPreferences(mContext);
	}

	public SortAdapter(Context mContext, List<SortModel> list, int type, ArrayList<String> list_quhao) {
		this.mContext = mContext;
		this.list = list;
		this.type = type;
		this.list_quhao = list_quhao;
	}

	ArrayList<Integer> positioncolor = new ArrayList<Integer>();
	Map<Integer, ArrayList<Integer>> myMap = new HashMap<Integer, ArrayList<Integer>>();

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
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	Integer bg_array[] = { R.drawable.friend_tips_1, R.drawable.friend_tips_2, R.drawable.friend_tips_3, R.drawable.friend_tips_4, R.drawable.friend_tips_5,
			R.drawable.friend_tips_6, R.drawable.friend_tips_7, R.drawable.friend_tips_8, R.drawable.friend_tips_9 };

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		ViewHolder2 viewHolder2 = null;
		ArrayList<Integer> random = null;
		final SortModel mContent = list.get(position);
		if (view == null) {

			if (type == 2) {
				viewHolder2 = new ViewHolder2();
				view = LayoutInflater.from(mContext).inflate(R.layout.country_item, null);
				viewHolder2.tvTitle2 = (TextView) view.findViewById(R.id.title_list);
				viewHolder2.quhao = (TextView) view.findViewById(R.id.country_quhao);
				viewHolder2.tvLetter = (TextView) view.findViewById(R.id.catalog);
				view.setTag(viewHolder2);
			} else {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, null);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.friend_list_item_name);
				viewHolder.usericon = (ImageView) view.findViewById(R.id.img_user_icon);
				viewHolder.tips_1 = (TextView) view.findViewById(R.id.friend_tip_1);
				viewHolder.tips_2 = (TextView) view.findViewById(R.id.friend_tip_2);
				viewHolder.tips_3 = (TextView) view.findViewById(R.id.friend_tip_3);
				viewHolder.btn_send_message = (Button) view.findViewById(R.id.btn_send_message);
				view.setTag(R.id.tag_second, viewHolder);
			}
			// viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);

		} else {
			viewHolder = (ViewHolder) view.getTag(R.id.tag_second);
			viewHolder2 = (ViewHolder2) view.getTag();
		}
	
		if (type == 2) {
			viewHolder2.tvTitle2.setText(this.list.get(position).getName());
			viewHolder2.quhao.setText(this.list.get(position).getPahonecode());

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				viewHolder2.tvLetter.setVisibility(View.VISIBLE);
				viewHolder2.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder2.tvLetter.setVisibility(View.GONE);
			}

		}
		return view;

	}

	private YwbImageLoader ywbImageLoader;

	// 在一定范围内生成不重复的随机数
	// 在testRandom2中生成的随机数可能会重复.
	// 在此处避免该问题
	private ArrayList getRandom() {
		ArrayList<Integer> integerHashSet = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			int randomInt = random.nextInt(9);
			if (!integerHashSet.contains(randomInt)) {
				integerHashSet.add(randomInt);
			}
		}
		return integerHashSet;
	}

	final static class ViewHolder {
		// TextView tvLetter;
		TextView tvTitle;
		TextView tips_1;
		TextView tips_2;
		TextView tips_3;
		ImageView usericon;
		Button btn_send_message;

	}

	final static class ViewHolder2 {
		TextView tvLetter;
		TextView tvTitle2;
		TextView quhao;
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