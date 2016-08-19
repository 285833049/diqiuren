package com.earthman.app.nim.uikit.team.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.AllMembersAdapter;
import com.earthman.app.pulltoreflesh.XListView;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * 群设置——>全部成员显示
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-6-24 上午11:52:37
 * @Decription
 */
public class AllMembersActivity extends Activity implements OnClickListener {
	private Button mTitleBackBtn;
	private TextView mTitleLiftText;
	private TextView mTitleText;
	private String titleName;
	private List<String> accounts;
	private List<TeamMember> groupUserInfo;
	private AllMembersAdapter adapter;
	private XListView mAllMenberslist;
	int groupNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_all_menbers_activity);
		titleName = getIntent().getStringExtra("titleName");
		accounts = getIntent().getStringArrayListExtra("accounts");
		groupUserInfo = (List<TeamMember>) getIntent().getSerializableExtra("groupUserInfo");
		groupNumber = getIntent().getIntExtra("groupNumber", groupNumber);
		initView();
	}

	private void initView() {
		mAllMenberslist = (XListView) findViewById(R.id.all_menbers_list);
		mTitleBackBtn = (Button) findViewById(R.id.btn_back);
		mTitleLiftText = (TextView) findViewById(R.id.tv_left);
		mTitleText = (TextView) findViewById(R.id.tv_title);
		mTitleBackBtn.setOnClickListener(this);
		mTitleText.setText(titleName + "(" + groupNumber + ")");
		adapter = new AllMembersAdapter(AllMembersActivity.this, groupUserInfo);
		mAllMenberslist.setAdapter(adapter);
		mAllMenberslist.setAutoLoadEnable(false);
		mAllMenberslist.setPullLoadEnable(false);
		mAllMenberslist.setPullRefreshEnable(false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:// 返回键
			onBackPressed();
			finish();
			break;

		default:
			break;
		}

	}

}
