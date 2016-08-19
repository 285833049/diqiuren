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
import com.earthman.app.adapter.MyShareListAdapter;
import com.earthman.app.adapter.SharedMineAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.ShareListInfo;
import com.earthman.app.bean.ShareListInfo.ShareListresult.ShareListEntity;
import com.earthman.app.constant.Constent;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyLoadingDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: MineShareActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月15日
 */
@ContentView(R.layout.activity_share_mine)
public class MineShareActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button back;
	@ViewInject(R.id.tv_left)
	TextView tv_left;

	@ViewInject(R.id.share_mine_to_me)
	ListView share_mine_to_me;
	@ViewInject(R.id.share_mine_to_friend)
	ListView share_mine_to_friend;

	private UserInfoPreferences infoPreferences;
	MyLoadingDialog myLoadingDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		infoPreferences = new UserInfoPreferences(this);
		myLoadingDialog = new MyLoadingDialog(this, "加载中...");
		myLoadingDialog.show();
		getSharedata();

	}

	private ArrayList<ShareListEntity> myOffline;

	private void getSharedata() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_SHARE_LIST, list);
		LogUtil.i(TAG, loginUrl);
		executeRequest(new FastJsonRequest<ShareListInfo>(Method.GET, loginUrl, ShareListInfo.class, new Response.Listener<ShareListInfo>() {
			@Override
			public void onResponse(ShareListInfo response) {
				if ("000000".equals(response.getCode())) {
					ArrayList<ShareListEntity> myUpline = response.getResult().getMyUpline();
					myOffline = response.getResult().getMyOffline();
					if (myUpline != null || myUpline.size() != 0) {
						share_mine_to_me.setAdapter(new SharedMineAdapter(MineShareActivity.this, myUpline));
					}
					if (myOffline != null || myOffline.size() != 0) {
						share_mine_to_friend.setAdapter(new MyShareListAdapter(MineShareActivity.this, myOffline));
					}
					myLoadingDialog.dismiss();
				} else {
					myLoadingDialog.dismiss();
					NetStatusHandUtil.getInstance().handStatus(MineShareActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		back.setOnClickListener(this);
		tv_left.setText(R.string.mine_share);
		share_mine_to_friend.setOnItemClickListener(mShareItemListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
	}

	private OnItemClickListener mShareItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			// 未激活的---->激活
			if (Constants.UNACTIVE.equals(myOffline.get(position).getStatus())) {
				DialogCommon dialogCommon = new DialogCommon(MineShareActivity.this, "", getString(R.string.friend_avater_confirm), "",
						new CommonDialogListener() {
							@Override
							public void onRightButtonClick() {
								preferences.setHeruniqueid(myOffline.get(position).getCardId());//保存代替用户激活的uniqueid
								preferences.setUserpayID(myOffline.get(position).getId());// 保存代替用户激活的id
								Intent intent = new Intent(MineShareActivity.this, PayActivity.class);
								intent.putExtra("type", Constent.REGIST_TO_FRIEND);
								startActivity(intent);
							}
						});
				dialogCommon.show();
			}
			// 已激活----->详情
			if (Constants.SHARE_ACTIVED.equals(myOffline.get(position).getStatus())) {
				Intent personalDetialIntent = new Intent(MineShareActivity.this, PeronalDetialActivity.class);
				personalDetialIntent.putExtra("friendsuserid", myOffline.get(position).getUniqueid());
				startActivity(personalDetialIntent);
			}
		}
	};
}
