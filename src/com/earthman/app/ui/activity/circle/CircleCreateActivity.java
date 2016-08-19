package com.earthman.app.ui.activity.circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.CircleCreateAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseSaveCallbackModel;
import com.earthman.app.bean.CircleListInfo;
import com.earthman.app.bean.CircleListModel;
import com.earthman.app.enums.CircleHandle;
import com.earthman.app.listener.EditTextDialogListener;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogEditText;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-26 下午5:50:58
 * @Decription 添加圈子活动类
 */
@ContentView(R.layout.circle_create_activity)
public class CircleCreateActivity extends BaseActivity {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	@ViewInject(R.id.rl_circle_create)
	private RelativeLayout mRlCircleCreate;
	@ViewInject(R.id.ll_circle_layout)
	private LinearLayout mLlCircleLayout;
	@ViewInject(R.id.lv_content)
	private ListView mLvContent;
	private CircleCreateAdapter mAdapter;
	private List<CircleListInfo> mCircleList;
	
	public int mPosition;
	private boolean isModify;//是否修改标记，修改过后返回时刷新上级页面

	@OnClick(value = { R.id.btn_back, R.id.rl_circle_create })
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.rl_circle_create:
			DialogEditText createDialog = new DialogEditText(this, getString(R.string.circle_create), getString(R.string.please_enter_circle_name), "",
					new EditTextDialogListener() {

						@Override
						public void onLeftButtonClick(EditText etContent) {

						}

						@Override
						public void onRightButtonClick(EditText etContent) {
							operationCircle(CircleHandle.CREATE, etContent.getText().toString(),"");//创建圈子
						}
					});
			createDialog.show();
			break;
		}
	}

	@Override
	protected void initData() {
		doGetCirleList();
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void setAttribute() {
		mTvLeft.setText(R.string.circle_edit);
	}

	private void doGetCirleList() {
		myLoadingDialog.show();
		UserInfoPreferences userinfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userinfo.getUserID());
		list.add(userinfo.getUserToken());
		String url = HttpUrlConstants.getUrl(this, HttpUrlConstants.CIRCLE_ALL_MY_GROUPS, list);
		executeRequest(new FastJsonRequest<CircleListModel>(Method.GET, url, CircleListModel.class, new Response.Listener<CircleListModel>() {

			@Override
			public void onResponse(CircleListModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					mLvContent.setAdapter(mAdapter = new CircleCreateAdapter(CircleCreateActivity.this, mCircleList = response.getResult()));
					mLlCircleLayout.setVisibility(response.getResult().size() == 0 ? View.GONE : View.VISIBLE);
				} else {
					NetStatusHandUtil.getInstance().handStatus(CircleCreateActivity.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

	public void operationCircle(final CircleHandle handle, final String currentName ,final String newName) {
		UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		final CircleListInfo info=mCircleList.get(mPosition);
		String getURL = null;
		try {
			switch (handle) {
			case CREATE:
				getURL = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.CIRCLE_CREATE, list);
				jsonRequest.put("keyword", currentName);
				break;
			case DELETE:
				getURL = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.CIRCLE_DELETE, list);
				jsonRequest.put("keyword", currentName);
				break;
			case EDIT:
				getURL = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.CIRCLE_EDIT, list);
				jsonRequest.put("value1", currentName);
				jsonRequest.put("value2", newName);
				break;
			}
			isModify=true;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseSaveCallbackModel callBackModel = JSON.parseObject(response.toString(), BaseSaveCallbackModel.class);
				if ("000000".equals(callBackModel.getCode())) {
					switch (handle) {
					case CREATE:
						MyToast.makeText(CircleCreateActivity.this, R.string.hint_save_success, Toast.LENGTH_LONG).show();
						mCircleList.add(new CircleListInfo(currentName));
						break;
					case DELETE:
						MyToast.makeText(CircleCreateActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
						mCircleList.remove(mPosition);
						break;
					case EDIT:
						MyToast.makeText(CircleCreateActivity.this, R.string.edit_success, Toast.LENGTH_LONG).show();
						info.setName(newName);
						break;
					}
					mAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(CircleCreateActivity.this, callBackModel.getCode(),callBackModel.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getURL, jsonRequest, listener, getErrorListener()) {

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
	protected void onDestroy() {
		super.onDestroy();
		if(isModify){
			setResult(1);
		}
	}
}
