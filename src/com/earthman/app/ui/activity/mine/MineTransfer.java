package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
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
import com.earthman.app.adapter.TransferRecentAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.RecentlyResponse;
import com.earthman.app.bean.RecentlyResponse.RecentlyResult;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ZhuanZhangActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */
@ContentView(R.layout.activity_zhuanzhang)
public class MineTransfer extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	@ViewInject(R.id.recently_zhuanzhang_list)
	ListView mLvTransferRecently;
	@ViewInject(R.id.zhuanzhang_to_friend)
	TextView mTvTransferToFriend;
	@ViewInject(R.id.zhuanzhang_to_earthcount)
	TextView mTvTransferToEarhAccount;
	ArrayList<RecentlyResult> mTransferList = new ArrayList<RecentlyResponse.RecentlyResult>();
	
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			startActivity(new Intent(this,TransferAccountRecordActivity.class));
			break;
		case R.id.zhuanzhang_to_friend:
			startActivity(new Intent(this, Transfer2Friend.class));// 转账到我的朋友
			break;
		case R.id.zhuanzhang_to_earthcount:
			startActivity(new Intent(this, Transfer2EarthAccount.class));// 转账到地球人账户
			break;
		}
	}

	@Override
	protected void initData() {
		doGetZhuanZhangList();// 获取最近转账的列表
	}

	private TransferRecentAdapter mTransferRecentAdapter;

	/**
	 * doGetZhuanZhangList(获取最近转账的列表显示)
	 * void LIST_TRANS_USERS
	 * @exception {userid}/{token}
	 */
	private void doGetZhuanZhangList() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.LIST_TRANS_USERS, list);
		executeRequest(new FastJsonRequest<RecentlyResponse>(Method.GET, loginUrl, RecentlyResponse.class, new Response.Listener<RecentlyResponse>() {
			@Override
			public void onResponse(RecentlyResponse response) {
				if ("000000".equals(response.getCode())) {
					mTransferList.addAll(response.getResult());
					mTransferRecentAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(MineTransfer.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleLeft.setOnClickListener(this);
		mTvTitleLeft.setText(getString(R.string.mine_transfer_money_title));
		mBtnTitleRight.setOnClickListener(this);
		mBtnTitleRight.setText(getString(R.string.mine_transfer_money_record));
		mTvTransferToEarhAccount.setOnClickListener(this);
		mTvTransferToFriend.setOnClickListener(this);
		mLvTransferRecently.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MineTransfer.this, TransferMoneyActivity.class);
				intent.putExtra("otherAccount", mTransferList.get(position).getCardId());//地球人ID
//				intent.putExtra("nice", mTransferList.get(position).getNice());//昵称
//				intent.putExtra("icon", mTransferList.get(position).getAvatar());//头像
				startActivity(intent);
			}
		});
		mTransferRecentAdapter = new TransferRecentAdapter(MineTransfer.this, mTransferList);
		mLvTransferRecently.setAdapter(mTransferRecentAdapter);
	}

	@Override
	protected void setAttribute() {
		
	}

}
