package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-16 下午1:24:02
 * @Decription 转账记录
 */
@ContentView(R.layout.transfer_account_record_activity)
public class TransferAccountRecordActivity  extends BaseActivity{
	
	@ViewInject(R.id.lv_content)
	private ListView mLvContent;
	private TransferAccountRecordAdapter mAdapter;
	private TransferAccountRecordModel mModel;

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initView() {
		((TextView)findViewById(R.id.tv_left)).setText("转账记录");
		findViewById(R.id.btn_back).setOnClickListener(this);
		String cardid=getIntent().getStringExtra("cardid");
		loadRecord(cardid==null?new UserInfoPreferences(this).getUsercardId():cardid);
	}

	@Override
	protected void setAttribute() {

	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	private void loadRecord(String uniqueid) {
		myLoadingDialog.show();
		
		UserInfoPreferences userInfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfo.getUserID());
		list.add(userInfo.getUserToken());
		list.add(uniqueid);
		
		String urlString = HttpUrlConstants.getUrl(this, HttpUrlConstants.QUERY_TRANSFER_ACCOUNTS, list);
		executeRequest(new FastJsonRequest<TransferAccountRecordModel>(Method.GET, urlString, TransferAccountRecordModel.class, new Response.Listener<TransferAccountRecordModel>() {
			@Override
			public void onResponse(TransferAccountRecordModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					mLvContent.setAdapter(mAdapter=new TransferAccountRecordAdapter(response.getResult()));
				} else {
					NetStatusHandUtil.getInstance().handStatus(TransferAccountRecordActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

}
