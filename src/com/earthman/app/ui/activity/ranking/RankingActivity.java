package com.earthman.app.ui.activity.ranking;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.RankingAdapter;
import com.earthman.app.adapter.RankingTypeAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.RankingContentModel;
import com.earthman.app.bean.RankingModel;
import com.earthman.app.bean.RankingTypeInfo;
import com.earthman.app.enums.RankingDataType;
import com.earthman.app.enums.RankingDateType;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyListView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-29 下午5:55:34
 * @Decription 排行榜
 */
@ContentView(R.layout.ranking_activity)
public class RankingActivity extends BaseActivity implements OnItemClickListener {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	@ViewInject(R.id.gv_raning)
	private GridView mGvRankingType;
	private RankingTypeAdapter mRankingTypeAdapter;
	private List<RankingTypeInfo> mRankingList;

	@ViewInject(R.id.lv_recommend)
	private MyListView mLvRankingRecommend;
	@ViewInject(R.id.lv_popularity)
	private MyListView mLvRankingPopularity;
	@ViewInject(R.id.lv_video)
	private MyListView mLvRankingVideo;
	@ViewInject(R.id.lv_consume)
	private MyListView mLvRankingConsume;

	@ViewInject(R.id.tv_recommend_more)
	private TextView mTvRecommendMore;
	@ViewInject(R.id.tv_popularity_more)
	private TextView mTvPopularityMore;
	@ViewInject(R.id.tv_video_more)
	private TextView mTvVideoMore;
	@ViewInject(R.id.tv_consume_more)
	private TextView mTvmTvRecommendMoreMore;

	private RankingContentModel mRankingContentModel;
	private int mCurrRankingType;

	@Override
	protected void initData() {
		mRankingList = new ArrayList<RankingTypeInfo>();
		mRankingList.add(new RankingTypeInfo(RankingDateType.WEEK, getString(R.string.ranking_week)));
		mRankingList.add(new RankingTypeInfo(RankingDateType.MONTH, getString(R.string.ranking_month)));
		mRankingList.add(new RankingTypeInfo(RankingDateType.QUARTER, getString(R.string.ranking_quarter)));
		mRankingList.add(new RankingTypeInfo(RankingDateType.YEAR, getString(R.string.ranking_year)));
		mGvRankingType.setAdapter(mRankingTypeAdapter = new RankingTypeAdapter(mRankingList));
		mGvRankingType.setOnItemClickListener(this);
		loadRanking(0);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(getString(R.string.ranking));
	}

	@OnClick(value = { R.id.btn_back, R.id.tv_recommend_more, R.id.tv_popularity_more, R.id.tv_video_more, R.id.tv_consume_more })
	@Override
	protected void handclick(View v) {
		Intent intent = new Intent(this, RankingDetailActivity.class);
		intent.putExtra("dateType", mRankingTypeAdapter.getSelection());
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			return;
		case R.id.tv_recommend_more:
			intent.putExtra("title", getString(R.string.ranking_recommend));
			intent.putExtra("rankingType", 0);
			break;
		case R.id.tv_popularity_more:
			intent.putExtra("title", getString(R.string.ranking_popularity));
			intent.putExtra("rankingType", 1);
			break;
		case R.id.tv_video_more:
			intent.putExtra("title", getString(R.string.ranking_video));
			intent.putExtra("rankingType", 2);
			break;
		case R.id.tv_consume_more:
			intent.putExtra("title", getString(R.string.ranking_consume));
			intent.putExtra("rankingType", 3);
			break;
		}
		startActivity(intent);
	}

	@OnItemClick(value = { R.id.lv_recommend, R.id.lv_popularity, R.id.lv_video, R.id.lv_consume })
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, PeronalDetialActivity.class);
		switch (parent.getId()) {
		case R.id.gv_raning:
			mRankingTypeAdapter.setSelection(position);
			mRankingTypeAdapter.notifyDataSetChanged();
			if (mCurrRankingType != position)
				loadRanking(position);
			return;
		case R.id.lv_recommend:
			// intent.putExtra("title", getString(R.string.ranking_recommend));
			// intent.putExtra("ranking", (Serializable)
			// mRankingContentModel.getRecommendTop().get(position));
			intent.putExtra("friendsuserid", mRankingContentModel.getRecommendTop().get(position).getUniqueid());
			break;
		case R.id.lv_popularity:
			// intent.putExtra("title", getString(R.string.ranking_popularity));
			// intent.putExtra("ranking", (Serializable)
			// mRankingContentModel.getMoodsTop().get(position));
			intent.putExtra("friendsuserid", mRankingContentModel.getMoodsTop().get(position).getUniqueid());
			break;
		case R.id.lv_video:
			// intent.putExtra("title", getString(R.string.ranking_video));
			// intent.putExtra("ranking", (Serializable)
			// mRankingContentModel.getVideoTop().get(position));
			intent.putExtra("friendsuserid", mRankingContentModel.getVideoTop().get(position).getUniqueid());
			break;
		case R.id.lv_consume:
			// intent.putExtra("title", getString(R.string.ranking_consume));
			// intent.putExtra("ranking", (Serializable)
			// mRankingContentModel.getIncomesTop().get(position));
			intent.putExtra("friendsuserid", mRankingContentModel.getIncomesTop().get(position).getUniqueid());
			break;
		}
		startActivity(intent);
	}

	private void loadRanking(int rankingType) {
		myLoadingDialog.show();
		UserInfoPreferences userInfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfo.getUserID());
		list.add(userInfo.getUserToken());
		list.add(mCurrRankingType = rankingType);
		String urlString = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_RANKING_LIST, list);
		executeRequest(new FastJsonRequest<RankingModel>(Method.GET, urlString, RankingModel.class, new Response.Listener<RankingModel>() {
			@Override
			public void onResponse(RankingModel response) {
				myLoadingDialog.dismiss();
				mRankingContentModel = response.getResult();
				if ("000000".endsWith(response.getCode())) {
					mLvRankingRecommend.setAdapter(new RankingAdapter(true, RankingDataType.RECOMMEND, mRankingContentModel.getRecommendTop()));
					mLvRankingPopularity.setAdapter(new RankingAdapter(true, RankingDataType.POPULARITY, mRankingContentModel.getMoodsTop()));
					mLvRankingVideo.setAdapter(new RankingAdapter(true, RankingDataType.VIDEO, mRankingContentModel.getVideoTop()));
					mLvRankingConsume.setAdapter(new RankingAdapter(true, RankingDataType.CONSUME, mRankingContentModel.getIncomesTop()));
				} else {
					NetStatusHandUtil.getInstance().handStatus(RankingActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

}
