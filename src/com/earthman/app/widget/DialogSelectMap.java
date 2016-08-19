package com.earthman.app.widget;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.DialogSelectMapAdapter;
import com.earthman.app.bean.MapInfo;
import com.earthman.app.listener.SelectMapDialogListener;

/**
 * @Title: MyDialogStyle1
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月15日
 */

public class DialogSelectMap extends Dialog implements OnClickListener, OnItemClickListener {
	private Context mContext;
	private Button mBtnBack;
	private TextView mTvTitle;
	private ListView mLvContent;
	private Button mBtnCancel;

	private List<MapInfo> mMapList;

	private SelectMapDialogListener mListener;

	// 最近方法
	public DialogSelectMap(Context context, boolean isInstallMap, List<MapInfo> mapList, SelectMapDialogListener listener) {
		super(context, R.style.upomp_bypay_MyDialog);
		setContentView(R.layout.dialog_select_map);
		setCancelable(true);
		mContext = context;
		mListener = listener;
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mLvContent = (ListView) findViewById(R.id.lv_content);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);

		mTvTitle.setText(isInstallMap ? "打开地图" : "本地暂无地图，请选择下载");
		mBtnCancel.setOnClickListener(this);
		mLvContent.setOnItemClickListener(this);
		mLvContent.setAdapter(new DialogSelectMapAdapter(mMapList = mapList));
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mListener.onMapItemListener(mMapList.get(position));
	}

}
