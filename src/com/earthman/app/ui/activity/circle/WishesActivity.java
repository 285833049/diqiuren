package com.earthman.app.ui.activity.circle;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.DeadWishesAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.DeadFewWishesInfo;
import com.earthman.app.bean.DeadWishesModel;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.pullview.PullToRefreshBase;
import com.earthman.app.widget.pullview.PullToRefreshBase.OnRefreshListener;
import com.earthman.app.widget.pullview.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 上午9:53:32
 * @Decription 寄语列表
 */
@ContentView(R.layout.wishes_activity)
public class WishesActivity extends BaseActivity implements OnItemClickListener {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	private List<DeadFewWishesInfo> mWishesInfoList;
	private ListView mLvContent;
	@ViewInject(R.id.lv_content)
	private PullToRefreshListView mPullListView;
	private DeadWishesAdapter mAdapter;
	
	private int index;

	@Override
	protected void handclick(View v) {

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);
		mLvContent = mPullListView.getRefreshableView();
		mLvContent.setOnItemClickListener(this);
		
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getRecord(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getRecord(false);
			}
		});
		mPullListView.doPullRefreshing(true, 200);
	}

	@Override
	protected void setAttribute() {
		mTvLeft.setText(R.string.flowers_message);
	}

	private void getRecord(final boolean isRefresh) {
		UserInfoPreferences userinfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userinfo.getUserID());
		list.add(userinfo.getUserToken());
		list.add(getIntent().getStringExtra("friendsuserid"));
		list.add("1");// type
		list.add(String.valueOf(index = isRefresh ? 0 : ++index));
		String url = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_DEAD_RECORD_LIST, list);
		executeRequest(new FastJsonRequest<DeadWishesModel>(Method.GET, url, DeadWishesModel.class, new Response.Listener<DeadWishesModel>() {

			@Override
			public void onResponse(DeadWishesModel response) {
				mPullListView.onPullDownRefreshComplete();
				mPullListView.onPullUpRefreshComplete();
				mPullListView.setHasMoreData(response.getResult().getResultlist().size() > 0 ? true : false);
				if ("000000".endsWith(response.getCode())) {
					if(isRefresh){
						mLvContent.setAdapter(mAdapter = new DeadWishesAdapter(mWishesInfoList=response.getResult().getResultlist()));
					}else{
						for (DeadFewWishesInfo info : response.getResult().getResultlist()) {
							mWishesInfoList.add(info);
						}
						mAdapter.notifyDataSetChanged();
					}
				} else {
					NetStatusHandUtil.getInstance().handStatus(WishesActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}

}
