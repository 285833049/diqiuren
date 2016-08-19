package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.RankingTypeInfo;
import com.earthman.app.widget.BaseViewHolder;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-28 下午2:12:25
 * @Decription 排名类型适配器
 */
public class RankingTypeAdapter extends BaseAdapter {

	private List<RankingTypeInfo> mRankingList;
	private RankingTypeInfo mInfo;
	private int mSelection;

	public RankingTypeAdapter(List<RankingTypeInfo> rankingList) {
		super();
		this.mRankingList = rankingList;
	}
	
	public int getSelection() {
		return mSelection;
	}

	public void setSelection(int selection) {
		this.mSelection = selection;
	}



	@Override
	public int getCount() {
		return mRankingList.size();
	}

	@Override
	public Object getItem(int position) {
		return mRankingList.get(position);
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
		mInfo = (RankingTypeInfo) getItem(position);
		TextView tvRankingName = BaseViewHolder.get(convertView, R.id.tv_ranking_name);
		TextView tvSelectedFlag = BaseViewHolder.get(convertView, R.id.tv_selected_flag);
		tvRankingName.setText(mInfo.getRankingName());
		tvSelectedFlag.setVisibility(mSelection == position ? View.VISIBLE : View.GONE);
		return convertView;
	}

}
