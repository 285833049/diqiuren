package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.CompanyIntroItem;
import com.earthman.app.widget.BaseViewHolder;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-28 下午2:12:25
 * @Decription 企业介绍类型适配器
 */
public class CompanyIntroTypeAdapter extends BaseAdapter {

	private List<CompanyIntroItem> mIntroList;
	private CompanyIntroItem mInfo;
	private int mSelection;

	public CompanyIntroTypeAdapter(List<CompanyIntroItem> introList) {
		super();
		this.mIntroList = introList;
	}
	
	public void setSelection(int selection) {
		this.mSelection = selection;
	}
	
	public int getSelection(){
		return this.mSelection;
	}

	@Override
	public int getCount() {
		return mIntroList.size();
	}

	@Override
	public Object getItem(int position) {
		return mIntroList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_type_item, null);
		}
		mInfo = (CompanyIntroItem) getItem(position);
		TextView tvIntroName = BaseViewHolder.get(convertView, R.id.tv_ranking_name);
		TextView tvSelectedFlag = BaseViewHolder.get(convertView, R.id.tv_selected_flag);
		
		tvIntroName.setText(mInfo.getIntroName());
		tvSelectedFlag.setVisibility(mSelection == position ? View.VISIBLE : View.GONE);
		return convertView;
	}

}
