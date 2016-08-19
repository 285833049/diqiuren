package com.earthman.app.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.PhotoUploadProgressItem;
import com.earthman.app.imageloader.YwbImageLoader;

public class PhotoUploadAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private SparseArray<PhotoUploadProgressItem> list;
	private YwbImageLoader imageLoader;

	public PhotoUploadAdapter(Context context,
			SparseArray<PhotoUploadProgressItem> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		imageLoader = new YwbImageLoader();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.valueAt(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoUploadProgressItem item = list.valueAt(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_upload_list_item,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.loadImage("file://" + item.getImg(),
				holder.iv_upload_list_image);

		holder.tv_upload_list_title
				.setText(String.format(
						context.getString(R.string.photo_progress_name),
						item.getName()));
		holder.tv_upload_list_descript.setText(item.getDescript());
		holder.pb_upload_list_progressbar.setProgress(item.getProgress());
		return convertView;
	}

	private class ViewHolder {

		private TextView tv_upload_list_title;
		private ImageView iv_upload_list_image;
		private TextView tv_upload_list_descript;
		private ProgressBar pb_upload_list_progressbar;

		private ViewHolder(View convertView) {
			tv_upload_list_title = (TextView) convertView
					.findViewById(R.id.tv_upload_list_title);
			iv_upload_list_image = (ImageView) convertView
					.findViewById(R.id.iv_upload_list_image);
			tv_upload_list_descript = (TextView) convertView
					.findViewById(R.id.tv_upload_list_descript);
			pb_upload_list_progressbar = (ProgressBar) convertView
					.findViewById(R.id.pb_upload_list_progressbar);

		}
	}

}
