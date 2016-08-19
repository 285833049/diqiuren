package com.earthman.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.GetHomeNewsResponse.GetHomeNewsResult.News;
import com.earthman.app.imageloader.YwbImageLoader;

/**
 * 作者：Zhou
 * 日期：2016-2-24 下午6:10:33
 * 描述：（）
 */
public class NewsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<News> arrayList;
	private YwbImageLoader loader;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public NewsAdapter(Context context, ArrayList<News> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
		loader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		News news = arrayList.get(position);

		loader.loadImage(news.getImg(), viewHolder.ivNews);
		viewHolder.tvTitle.setText(news.getTitle());
		viewHolder.tvContent.setText(Html.fromHtml(news.getDescript()));
		viewHolder.tvTime.setText(format.format(new Date(news.getCreatedAt())));
		return convertView;
	}

	class ViewHolder {
		private ImageView ivNews;
		private TextView tvTitle;
		private TextView tvContent;
		private TextView tvTime;

		private ViewHolder(View convertView) {
			ivNews = (ImageView) convertView.findViewById(R.id.iv_news);
			tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			tvTime = (TextView) convertView.findViewById(R.id.tv_time);
		}

	}

}
