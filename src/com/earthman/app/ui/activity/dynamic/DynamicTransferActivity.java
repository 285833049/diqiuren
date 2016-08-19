package com.earthman.app.ui.activity.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.earthman.app.bean.DynamicResponse.DynamicContent;
import com.earthman.app.eventbusbean.OKDynamicSend;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-24 上午10:00:21
 * @Decription 动态转发
 */
@ContentView(R.layout.dynamic_transfer)
public class DynamicTransferActivity extends BaseActivity{
	@ViewInject(R.id.et_reason)
    private EditText et_reason;
	private DynamicContent dynamicContent;
	@ViewInject(R.id.iv_pic)
	private ImageView iv_pic;
	@ViewInject(R.id.tv_text)
	private TextView tv_text;
	
	private String mRetransUserId;
	 
	
	/* 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		Intent intent = getIntent();
		if(intent != null){
			dynamicContent = (DynamicContent) intent.getSerializableExtra("dynamicContent");
		}
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		
	}
	
	@OnClick(R.id.btn_back)
	private void onCancelClick(View view){
		finish();
	}
	
	@OnClick(R.id.btn_right)
	private void onPublishClick(View view){
		doRetransTopics();
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		ArrayList<String> imageList=null;
		
		String imges;
		String content;
		
		if(dynamicContent.getArticlesBase()!=null){//此条动态是转发的，则应转发被转发的动态
			imges=dynamicContent.getArticlesBase().getImgs();
			content=dynamicContent.getArticlesBase().getTitle();
			mRetransUserId=String.valueOf(dynamicContent.getArticlesBase().getId());
		}else{
			imges=dynamicContent.getImgs();
			content=dynamicContent.getTitle();
			mRetransUserId=dynamicContent.getId();
		}
		
		if (imges.length() != 0 && imges.contains(",")) {
			imageList = new ArrayList<String>(Arrays.asList(imges.split(",")));
		} else if (imges.length() != 0 && !imges.contains(",")) {
			imageList = new ArrayList<String>();
			imageList.add(imges);
		} 
		if(imageList!=null){
			iv_pic.setVisibility(View.VISIBLE);
			new YwbImageLoader().loadImage(imageList.get(0), iv_pic);
		}
		tv_text.setText(content);
		
		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}
	
	
	public void doRetransTopics(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(mRetransUserId);
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("title", et_reason.getText().toString()); 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String postUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.RETRANSTOPIC, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				dismissLoading();
				if ("000000".equals(response2.getCode())) {
					MyToast.makeText(DynamicTransferActivity.this, R.string.transfer_success, Toast.LENGTH_SHORT).show();
					EventBus.getDefault().post(new OKDynamicSend(1));
					DynamicTransferActivity.this.finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(DynamicTransferActivity.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, postUrl, jsonRequest, listener, getErrorListener()) {
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
