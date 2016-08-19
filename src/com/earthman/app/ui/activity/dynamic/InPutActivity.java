package com.earthman.app.ui.activity.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommentResponse;
import com.earthman.app.bean.CommentResponse.CommentResult;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月26日 上午9:39:54
 * @Decription
 */
@ContentView(R.layout.activit_input)
public class InPutActivity extends BaseActivity {
	// @ViewInject(R.id.biaoqing)
	// ImageView biaoqing;
	@ViewInject(R.id.input_et)
	EditText mEtInput;
	@ViewInject(R.id.send)
	Button mBtnSend;
	@ViewInject(R.id.rl_input)
	RelativeLayout mRlInput;
	@ViewInject(R.id.rl_input_else)
	LinearLayout rl_input_else;
	private int position1;
	int keyBoardHeight;
	private int screenHeight;
	private String type;
	private String replyId;
	private String parentId;
	private String authorId;
	private String articleId;
	int bottom;

	/*
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.send:
			// post方式提交
			// 包含参数：
			// articleId:动态id
			// replyId:被回复人id
			// content:回复的内容
			// parentId:被回复的回复id（如果是回复发布动态的人，则不传值或传null）
			// authorId:动态发布人id
			String message = mEtInput.getText().toString().trim();
			if (TextUtils.isEmpty(message)) {
				MyToast.makeText(this, R.string.reply_content, Toast.LENGTH_SHORT).show();
				return;
			}
			doPostReply(message);
			break;
		case R.id.rl_input_else:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * doPostReply回复
	 * voidREPLY
	 * 
	 * @exception
	 */
	private void doPostReply(String message) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			if ("2".equals(type)) {
				jsonRequest.put("replyId", replyId);
				jsonRequest.put("parentId", parentId);
			}
			jsonRequest.put("articleId", articleId);
			jsonRequest.put("content", message);
			jsonRequest.put("authorId", authorId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.REPLY, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CommentResponse response2 = JSON.parseObject(response.toString(), CommentResponse.class);
				if ("000000".equals(response2.getCode())) {
					LogUtil.i(TAG, response.toString());
					CommentResult result = response2.getResult();
					// CommentContent commentResult=new CommentContent();
					// commentResult.setArticleId(response2.getResult().getArticleId());
					// commentResult.setArticleUserId(response2.getResult().getArticleUserId());
					// commentResult.setAuthorId(response2.getResult().getAuthorId());
					// commentResult.setContent(response2.getResult().getContent());
					// commentResult.setParentId(response2.getResult().getParentId());
					// commentResult.setReplyId(response2.getResult().getReplyId());
					// commentResult.setType(type);
					result.setType(type);
					result.setPosition(position1);
					EventBus.getDefault().post(result);
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(InPutActivity.this, response2.getCode(), response2.getMessage());
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
	 * intent.putExtra("replyId",
	 * dynamicContent.getArticlesComments().get(position).getReplyUserId());
	 * intent.putExtra("parentId",
	 * dynamicContent.getArticlesComments().get(position).getCommentId());
	 * 
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			type = getIntent().getStringExtra("type");
			articleId = getIntent().getStringExtra("articleId");
			authorId = getIntent().getStringExtra("authorId");
			if ("2".equals(type)) {
				replyId = getIntent().getStringExtra("replyId");
				parentId = getIntent().getStringExtra("parentId");
				position1 = getIntent().getIntExtra("position", 0);
			}
		}
		overridePendingTransition(0, 0);// 取消activity进出场动画
		screenHeight = (int) AndroidUtils.getDeviceHight(this);
		OnGlobalLayoutListener mLayoutChangeListener = new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 判断窗口可见区域大小
				Rect r = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
				int heightDifference = screenHeight - (r.bottom - r.top);
				bottom = r.bottom;
				LogUtil.i(TAG, heightDifference + "heightDifference");
				LogUtil.i(TAG, r.bottom + "bottom");
				LogUtil.i(TAG, r.top + "top");
				mRlInput.layout(0, (int) bottom - mRlInput.getHeight(), (int) AndroidUtils.getDeviceWidth(InPutActivity.this), bottom);
			}
		};
		// 注册布局变化监听
		getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnSend.setOnClickListener(this);
		rl_input_else.setOnClickListener(this);

		mRlInput.layout(0, (int) bottom - mRlInput.getHeight(), (int) AndroidUtils.getDeviceWidth(this), bottom);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mEtInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mEtInput, 0);
			}
		}, 100);
		mEtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			finish();
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
