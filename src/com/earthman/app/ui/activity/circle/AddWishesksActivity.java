package com.earthman.app.ui.activity.circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseSaveCallbackModel;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 上午9:36:13
 * @Decription 写寄语页面
 */
@ContentView(R.layout.new_remarks_activity)
public class AddWishesksActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;

	@ViewInject(R.id.et_content)
	private EditText mEtContent;
	@ViewInject(R.id.tv_input_num)
	private TextView mTvInputNum;

	@ViewInject(R.id.btn_commit)
	private Button mBtnCommit;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_commit:
			// Toast.makeText(this, "待完成", Toast.LENGTH_SHORT).show();
			saveWishesks();
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnBack.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
		mTvLeft.setText(R.string.death_title);
		mEtContent.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;// 监听前的文本
			private final int charMaxNum = 300;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				temp = s;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mTvInputNum.setText(getString(R.string.text_1) + (300 - s.length()) + getString(R.string.text_2));
			}

			@Override
			public void afterTextChanged(Editable s) {
				/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
				if (temp.length() > charMaxNum) {
					Toast.makeText(getApplicationContext(), getString(R.string.huafei_tips), Toast.LENGTH_LONG).show();
				}
			}

		});
	}

	@Override
	protected void setAttribute() {

	}

	private void saveWishesks() {
		if (mEtContent.getText().toString().length() == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.input_content), Toast.LENGTH_LONG).show();
			return;
		}
		UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("deadid", getIntent().getStringExtra("frienduserid"));
			jsonRequest.put("wishes", mEtContent.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.ADD_WISHES, list);

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				BaseSaveCallbackModel callBackModel = JSON.parseObject(response.toString(), BaseSaveCallbackModel.class);
				if ("000000".equals(callBackModel.getCode())) {
					MyToast.makeText(AddWishesksActivity.this, R.string.hint_save_success, Toast.LENGTH_LONG).show();
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AddWishesksActivity.this, callBackModel.getCode(),callBackModel.getMessage());			
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
}
