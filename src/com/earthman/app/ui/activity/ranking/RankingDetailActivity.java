package com.earthman.app.ui.activity.ranking;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.RankingDetailAdapter;
import com.earthman.app.adapter.RankingTypeAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.RankingModelTopinfo;
import com.earthman.app.bean.RankingModelTopinfo.RankingModelTopinfoEntity;
import com.earthman.app.bean.RankingTypeInfo;
import com.earthman.app.enums.RankingDataType;
import com.earthman.app.enums.RankingDateType;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

@ContentView(R.layout.ranking_detail_activity)
public class RankingDetailActivity extends BaseActivity implements OnItemClickListener {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	@ViewInject(R.id.gv_raning)
	private GridView mGvRankingType;
	private ArrayList<RankingModelTopinfoEntity> mItemList;
	private RankingTypeAdapter mRankingTypeAdapter;
	private List<RankingTypeInfo> mRankingTypeList;
	private int rankingType;

	@ViewInject(R.id.lv_ranking)
	private ListView mLvRanking;

	@Override
	protected void initData() {
		rankingType = getIntent().getIntExtra("rankingType", 0);

		mRankingTypeList = new ArrayList<RankingTypeInfo>();
		mRankingTypeList.add(new RankingTypeInfo(RankingDateType.WEEK, getString(R.string.ranking_week)));
		mRankingTypeList.add(new RankingTypeInfo(RankingDateType.MONTH, getString(R.string.ranking_month)));
		mRankingTypeList.add(new RankingTypeInfo(RankingDateType.QUARTER, getString(R.string.ranking_quarter)));
		mRankingTypeList.add(new RankingTypeInfo(RankingDateType.YEAR, getString(R.string.ranking_year)));
		mRankingTypeAdapter = new RankingTypeAdapter(mRankingTypeList);
		mRankingTypeAdapter.setSelection(getIntent().getIntExtra("dateType", 0));
		mGvRankingType.setAdapter(mRankingTypeAdapter);
		loadRanking(0);
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(getIntent().getStringExtra("title"));
	}

	private void loadRanking(final int period) {
		myLoadingDialog.show();
		UserInfoPreferences userInfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfo.getUserID());
		list.add(userInfo.getUserToken());
		list.add(rankingType);
		list.add(period);
		String urlString = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_RANKING_LIST, list);
		executeRequest(new FastJsonRequest<RankingModelTopinfo>(Method.GET, urlString, RankingModelTopinfo.class, new Response.Listener<RankingModelTopinfo>() {
			@Override
			public void onResponse(RankingModelTopinfo response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					mItemList=response.getResult().getTopinfo();
					switch (rankingType) {
					case 0:
							mLvRanking.setAdapter(new RankingDetailAdapter(RankingDataType.RECOMMEND,mItemList));
						break;
					case 1:
						mLvRanking.setAdapter(new RankingDetailAdapter(RankingDataType.POPULARITY, mItemList));
						break;
					case 2:
						mLvRanking.setAdapter(new RankingDetailAdapter(RankingDataType.VIDEO, mItemList));
						break;
					case 3:
						mLvRanking.setAdapter(new RankingDetailAdapter(RankingDataType.CONSUME, mItemList));
						break;
					}
				} else {
					NetStatusHandUtil.getInstance().handStatus(RankingDetailActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@OnClick(value = { R.id.btn_back })
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

	@OnItemClick(value = { R.id.gv_raning, R.id.lv_ranking })
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.gv_raning:
			if (mRankingTypeAdapter.getSelection() != position) {
				mRankingTypeAdapter.setSelection(position);
				mRankingTypeAdapter.notifyDataSetChanged();
				loadRanking(position);
			}
			break;
		case R.id.lv_ranking:
			Intent intent = new Intent(this, PeronalDetialActivity.class);
			intent.putExtra("friendsuserid", mItemList.get(position).getUniqueid());
			startActivity(intent);
			break;
		}
	}

}
