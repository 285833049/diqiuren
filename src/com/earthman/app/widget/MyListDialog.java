package com.earthman.app.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.listener.EditTextDialogListener;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Title: MyListDialog
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月9日
 */

public class MyListDialog extends Dialog {

	private List<String> lists;
	private Context context;
	private TextView textView;
	private int type;

	/**
	 * @param context
	 */
	public MyListDialog(Context context, ArrayList<String> list) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.lists = list;
		this.context = context;
		initview();
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public MyListDialog(Context context, ArrayList<String> list, int type) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.lists = list;
		this.context = context;
		this.type = type;
		initview();
	}

	/**
	 * 初始化dialog
	 */
	private void initview() {
		setContentView(R.layout.dialog_list);
		listview = (ListView) findViewById(R.id.dialog_list);
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 70));
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				textView.setText(lists.get(position));
				if (type == 212 && position == lists.size() - 1) {
					new DialogEditText(context, context.getString(R.string.edit_job_title), context.getString(R.string.edit_job_hint), "",
							new EditTextDialogListener() {

						@Override
						public void onRightButtonClick(EditText etContent) {
							textView.setText(etContent.getText().toString());
						}

						@Override
						public void onLeftButtonClick(EditText etContent) {

						}
					}).show();
				}
				dismiss();
			}
		});
	}

	/*
	 * @see android.app.Dialog#show()
	 */
	@Override
	public void show() {
		super.show();
		adapter.notifyDataSetChanged();
	}

	public View getListView() {
		return listview;
	}

	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.dialog_list_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.dialog_list_itemlist);
			tv.setText(lists.get(position));
			return convertView;
		}
	};
	private ListView listview;

}
