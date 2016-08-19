package com.earthman.app.ui.activity.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.eventbusbean.ModifyMark;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @Title: SetFriendGroupsActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月15日
 */
@ContentView(R.layout.setgroup_friend)
public class SetFriendGroupsActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.rl_add_quanzi)
	RelativeLayout mRlChoiseCicle;
	@ViewInject(R.id.quanzitype)
	TextView mTvCicleType;
	@ViewInject(R.id.friend_beizhu)
	EditText mEtRemark;
	@ViewInject(R.id.relative_friend)
	EditText mEtRelative;
	private String cicleTypeStr;
	private String relativeStr;
	private String remarkStr;
	private String friendId;
	private String cicleId;
	private String nice;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		if (v.getId() == R.id.btn_back) {
			finish();
		}
		if (v.getId() == R.id.rl_add_quanzi) {
			startActivityForResult(new Intent(this, QuanziChoiseActivity.class), 0x012);
		}
		if (v.getId() == R.id.btn_right) {
			// 保存分组信息
			getDataFromUI();
//			if (TextUtils.isEmpty(cicleTypeStr) || TextUtils.isEmpty(relativeStr) || TextUtils.isEmpty(remarkStr)) {
//				MyToast.makeText(this, R.string.regist_toast1, Toast.LENGTH_SHORT).show();
//				return;
//			}
			doPostSave();
		}
	}

	/**
	 * getDataFromUI(获取UI信息)
	 * void
	 * 
	 * @exception
	 */
	private void getDataFromUI() {
		cicleTypeStr = mTvCicleType.getText().toString().trim();
		relativeStr = mEtRelative.getText().toString().trim();
		remarkStr = mEtRemark.getText().toString().trim();

	}

	/**
	 * doPostSave(保存分组备注信息)
	 * void
	 * 
	 * @exception
	 */
	private void doPostSave() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("userId", preferences.getUserID());
			jsonRequest.put("friendsUserId", friendId);
			jsonRequest.put("matter", relativeStr);
			jsonRequest.put("remarks", remarkStr);
			jsonRequest.put("groupId", cicleId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.SET_FRIEND, list);
		LogUtil.i(TAG, "设置好友的分组备注关系" + getUrl);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					finish();
					EventBus.getDefault().post(new ModifyMark(friendId, remarkStr));
					MyToast.makeText(SetFriendGroupsActivity.this, R.string.set_friend, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(SetFriendGroupsActivity.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, getUrl, jsonRequest, listener, getErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}

		};
		executeRequest(jsonObjectRequest);
	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 0x012 && arg1 == 0x0012) {
			if (arg2.getExtras() != null) {
				cicleTypeStr = (String) arg2.getExtras().get("quanzitype");
				cicleId = (String) arg2.getExtras().get("quanziid");
				mTvCicleType.setText(cicleTypeStr);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			friendId = (String) getIntent().getExtras().get("friendid");
			remarkStr = getIntent().getExtras().getString("remarks");
			nice = getIntent().getExtras().getString("nice");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mEtRemark.setText(nice);
		mBtnTitleLeft.setOnClickListener(this);
		mRlChoiseCicle.setOnClickListener(this);
		mTvTitleLeft.setText(R.string.set_groupstitle);
		mBtnTitleRight.setOnClickListener(this);
		mBtnTitleRight.setText(R.string.set_groups4);
		if (!TextUtils.isEmpty(remarkStr)) {
			mEtRemark.setText(remarkStr);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

	}

}
