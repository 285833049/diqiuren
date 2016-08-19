package com.earthman.app.ui.activity.circle;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.CircleDetailAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CircleFriendInfo;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午4:57:45
 * @Decription 亲友圈:所有圈子好友列表
 */
@ContentView(R.layout.circle_detail_activity)
public class CircleDetailActivity extends BaseActivity implements OnItemClickListener {

	@ViewInject(R.id.lv_content)
	private XListView mLvContent;
	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;
	private List<CircleFriendInfo> mFriendList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnBack.setOnClickListener(this);
		mTvTitle.setText(getIntent().getStringExtra("title"));
		mLvContent.setPullLoadEnable(false);
		mLvContent.setPullRefreshEnable(false);//下拉刷新
		mLvContent.setAdapter(new CircleDetailAdapter(mFriendList = (List<CircleFriendInfo>) getIntent().getSerializableExtra("friendList")));
		mLvContent.setOnItemClickListener(this);
	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void setAttribute() {
		
	}

	@Override
	public void onItemClick(AdapterView<?> group, View view, int position, long id) {
		CircleFriendInfo info = mFriendList.get(position-1);//0为headView
		Intent intent = new Intent(this, PeronalDetialActivity.class);
		intent.putExtra("friendsuserid", String.valueOf(info.getId()));
		startActivity(intent);
	}

}
