package com.earthman.app.nim.contact.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.AddFriendAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.FindFriendResponse;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加好友页面
 * Created by huangjun on 2015/8/11.
 */
@ContentView(R.layout.add_friend)
public class AddFriendActivity extends BaseActivity {

	@ViewInject(R.id.lv_friend)
	private XListView mLvFriend;
	private AddFriendAdapter adapter;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.btn_right)
	private Button btn_right;
	@ViewInject(R.id.et_add_friend_id)
	EditText friendID;

	public static final void start(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AddFriendActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
		adapter = new AddFriendAdapter(this);
		mLvFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@OnClick(R.id.btn_right)
	private void onFindClick(View v) {
		String friendid = friendID.getText().toString().trim();
		if (friendid.length() < 2) {
			MyToast.makeText(this, R.string.add_friend1, Toast.LENGTH_SHORT).show();
			return;
		}
		if (friendid.equals(preferences.getUsercardId()) || friendid == preferences.getUsercardId()) {
			Toast.makeText(getApplicationContext(), "不能添加自己为好友哦", Toast.LENGTH_LONG).show();
		} else {
			doGetFindFriend(friendid);
		}

	}

	/**
	 * 查找好友
	 */
	private void doGetFindFriend(final String friendid) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("keyword", friendid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.FIND_FRIEND, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				final FindFriendResponse response2 = JSON.parseObject(response.toString(), FindFriendResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					if (response2.getResult().size() != 0) {
						adapter.setData(response2.getResult(), friendid);
						mLvFriend.setAdapter(adapter);
						mLvFriend.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

								String friendsuserid = response2.getResult().get(position - 1).getUniqueid();
								Intent intent = new Intent(AddFriendActivity.this, PeronalDetialActivity.class);
								intent.putExtra("friendsuserid", friendsuserid);
								startActivity(intent);

							}

						});
					} else {
						MyToast.makeText(AddFriendActivity.this, R.string.add_friend3, Toast.LENGTH_LONG).show();
					}
				} else {
					NetStatusHandUtil.getInstance().handStatus(AddFriendActivity.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getUrl, jsonRequest, listener, getErrorListener()) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}
		};
		executeRequest(jsonObjectRequest);
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mLvFriend.setAutoLoadEnable(false);
		mLvFriend.setPullLoadEnable(false);
		mLvFriend.setPullRefreshEnable(false);

	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.pop_add_friend);
		btn_right.setText(R.string.look);
	}
}
