package com.earthman.app.ui.activity.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.bean.GetVideoListResponse.VideoItem;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-1 上午11:28:31
 * @Decription (新增视频)
 */
@ContentView(R.layout.add_video)
public class AddVideoActivity extends BaseActivity{

	@ViewInject(R.id.tv_left)
	private TextView mTvLeftTitle;
	@ViewInject(R.id.tv_wuji)
	private TextView tv_wuji;
	@ViewInject(R.id.rl_public)
	private RelativeLayout rl_public;
	@ViewInject(R.id.rl_pay)
	private RelativeLayout rl_pay;
	@ViewInject(R.id.rl_wuji)
	private RelativeLayout rl_wuji;
	@ViewInject(R.id.checkbox_public)
	private CheckBox checkbox_public;
	@ViewInject(R.id.checkbox_pay)
	private CheckBox checkbox_pay;
	@ViewInject(R.id.checkbox_wuji)
	private CheckBox checkbox_wuji;
	@ViewInject(R.id.et_video_name)
	private EditText et_video_name;
	@ViewInject(R.id.tv_word_num)
	private TextView tv_word_num;
	public static final int ADD_VIDEO = 0;// 新增视频
	public static final int EDIT_VIDEO = 1;// 编辑视频
	private int currentSelected = 0;
	@ViewInject(R.id.btn_finish)
	private Button btn_finish;
	private int currentPageType;
	private VideoItem videoItem;

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {

	}
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		Intent intent = getIntent();
		if(intent != null){
			currentPageType = intent.getIntExtra("pageType", -1);
			videoItem = (VideoItem) intent.getSerializableExtra("videoItem");
		}

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		finish();
	}

	@OnClick(R.id.rl_public)
	private void onPublicClick(View v) {
		if(currentSelected == 1){
			currentSelected = 0;
		}else{
			currentSelected = 1;
		}

		setSelected();
	}

	@OnClick(R.id.rl_pay)
	private void onPayClick(View v) {
		if(currentSelected == 2){
			currentSelected = 0;
		}else{
			currentSelected = 2;
		}		
		setSelected();
	}

	@OnClick(R.id.rl_wuji)
	private void onWujiClick(View v) {
		if(currentSelected == 3){
			currentSelected = 0;
		}else{
			currentSelected = 3;
		}		
		setSelected();
	}

	@OnClick(R.id.btn_finish)
	private void onFinishClick(View v){
		if(checkData()){
			switch (currentPageType) {
			case ADD_VIDEO:			
				Intent intent = new Intent(AddVideoActivity.this, LocalVideoListActivity.class);
				intent.putExtra("name", et_video_name.getText().toString().trim());
				intent.putExtra("privacy", currentSelected);
				startActivityForResult(intent, 0x01);
				break;
			case EDIT_VIDEO:	
				doEditVideo();
				break;
			default:
				break;
			}
		}

	}


	private boolean checkData(){
		String name = et_video_name.getText().toString().trim();
		if(TextUtils.isEmpty(name)){
			Toast.makeText(AddVideoActivity.this, R.string.input_video_name, Toast.LENGTH_SHORT).show();
			return false;
		}else if(currentSelected == 0){
			Toast.makeText(AddVideoActivity.this, R.string.input_priority, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void setSelected() {
		checkbox_public.setChecked(currentSelected == 1 ? true : false);
		checkbox_pay.setChecked(currentSelected == 2 ? true : false);
		checkbox_wuji.setChecked(currentSelected == 3 ? true : false);
	}

	public static void intoActivity(Context context, int pageType, VideoItem videoItem){
		Intent intent = new Intent(context, AddVideoActivity.class);
		intent.putExtra("pageType", pageType);
		intent.putExtra("videoItem", videoItem);
		context.startActivity(intent);
	}


	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvLeftTitle.setText(R.string.add_video);
	}

	@Override
	protected void setAttribute() {
		switch (currentPageType) {
		case ADD_VIDEO:
			mTvLeftTitle.setText(R.string.add_video);
			break;
		case EDIT_VIDEO:
			mTvLeftTitle.setText(R.string.edit_video);
			et_video_name.setText(videoItem.getTitle());
			if(!TextUtils.isEmpty(videoItem.getStatus())){
				currentSelected = Integer.parseInt(videoItem.getStatus());
				setSelected();
			}			
			break;
		}	

		tv_word_num.setText(String.format(getString(R.string.name_word_num), et_video_name.getText().toString().trim().length()));
		tv_wuji.setText(Html.fromHtml(getString(R.string.wuji)));
		et_video_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int remain	= s.toString().trim().length();
				if(remain <= 10){
					tv_word_num.setText(String.format(getString(R.string.name_word_num), remain));
				}				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}


	private void doEditVideo(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		showLoading();
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("id", videoItem.getId());
			jsonRequest.put("title", et_video_name.getText().toString().trim());
			jsonRequest.put("status", currentSelected);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.EDIT_VIDEO, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				CommonResponse response2 = JSON.parseObject(response.toString(), CommonResponse.class);
				if ("000000".equals(response2.getCode())) {
					MyToast.makeText(AddVideoActivity.this, R.string.edit_video_success, Toast.LENGTH_LONG).show();
					sendBroadcast(new Intent(Constants.REFRESH_ACTIVITY_ACTION));
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AddVideoActivity.this, response2.getCode(),response2.getMessage());
				}
				dismissLoading();
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
