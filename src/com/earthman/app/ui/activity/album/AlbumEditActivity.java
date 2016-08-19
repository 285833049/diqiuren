package com.earthman.app.ui.activity.album;

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
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.bean.GetAlbumListResponse.Album;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogCommon;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-27 下午4:43:20
 * @Decription （相册编辑页面包括新增相册）
 */
@ContentView(R.layout.album_edit)
public class AlbumEditActivity extends BaseActivity {

	private int pageType;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
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
	@ViewInject(R.id.btn_delete)
	private Button btn_delete;
	@ViewInject(R.id.et_album_name)
	private EditText et_album_name;
	@ViewInject(R.id.et_album_content)
	private EditText et_album_content;
	@ViewInject(R.id.tv_word_num)
	private TextView tv_word_num;
	@ViewInject(R.id.tv_remain_num)
	private TextView tv_remain_num;

	public static final int ADD_ALBUM = 0;// 新增相册
	public static final int EDIT_ALBUM = 1;// 编辑相册
	private String currentSelected;// null 公开  1 支付 2 五级防护
	private int albumid;
	private Album album;
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
		if (intent != null) {
			pageType = intent.getIntExtra("pageType", 0);
			album = (Album) intent.getSerializableExtra("album");
		}
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}

	@OnClick(R.id.rl_public)
	private void onPublicClick(View v) {
		currentSelected = null;
		setSelected();
	}

	@OnClick(R.id.rl_pay)
	private void onPayClick(View v) {
		if("1".equals(currentSelected)){
			currentSelected = null;
		}else{
			currentSelected = "1";
		}		
		setSelected();
	}

	@OnClick(R.id.rl_wuji)
	private void onWujiClick(View v) {
		if("2".equals(currentSelected)){
			currentSelected = null;
		}else{
			currentSelected = "2";
		}		
		setSelected();
	}


	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		finish();
	}

	private void setSelected() {
		checkbox_public.setChecked(currentSelected == null ? true : false);
		checkbox_pay.setChecked("1".equals(currentSelected) ? true : false);
		checkbox_wuji.setChecked("2".equals(currentSelected) ? true : false);
	}


	@OnClick(R.id.btn_finish)
	private void onFinishClick(View view){
		if(checkData()){
			switch (pageType) {
			case ADD_ALBUM:
				doCreateAlbum();
				break;
			case EDIT_ALBUM:
				doEditAlbum();
				break;
			default:
				break;
			}
		}
	}

	@OnClick(R.id.btn_delete)
	private void onDeleteClick(View view){

		DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.issure_delete_album),
				getString(R.string.determine), new CommonDialogListener() {
			@Override
			public void onRightButtonClick() {
				doDeleteAlbum();
			}

		});
		commonDialog.show();
	}

	private boolean checkData(){
		String album_name = et_album_name.getText().toString().trim();
		String album_content = et_album_content.getText().toString().trim();
		if(TextUtils.isEmpty(album_name)){
			Toast.makeText(AlbumEditActivity.this, R.string.input_album_name, Toast.LENGTH_SHORT).show();
			return false;
		}else if(TextUtils.isEmpty(album_content)){
			Toast.makeText(AlbumEditActivity.this, R.string.input_albumn_content, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * intoActivity
	 * 
	 * @param context
	 * @param pageType
	 *            页面类型 0 新增相册 1编辑相册
	 *            void
	 * @exception
	 */
	public static void intoActivity(Context context, int pageType, Album album) {
		Intent intent = new Intent(context, AlbumEditActivity.class);
		intent.putExtra("pageType", pageType);	
		intent.putExtra("album", album);
		//Activity activity = (Activity) context;
		//activity.startActivityForResult(intent, AlbumListActivity.EDIT_ALBUM);
		context.startActivity(intent);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		switch (pageType) {
		case ADD_ALBUM:// 新增相册
			tv_left.setText(R.string.add_album);
			break;
		case EDIT_ALBUM:// 编辑相册
			tv_left.setText(R.string.edit_album);
			btn_delete.setVisibility(View.VISIBLE);
			albumid = album.getId();
			et_album_content.setText(album.getDescript());
			et_album_name.setText(album.getName());
			currentSelected = album.getAlbumsClass();
			setSelected();
			break;
		default:
			break;
		}
		tv_word_num.setText(String.format(getString(R.string.name_word_num), et_album_name.getText().toString().trim().length()));
		tv_remain_num.setText(String.format(getString(R.string.resume_album_num), et_album_content.getText().toString().trim().length()));
		tv_wuji.setText(Html.fromHtml(getString(R.string.wuji)));
		et_album_name.addTextChangedListener(new TextWatcher() {

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
		et_album_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int remain	= s.toString().trim().length();
				if(remain <= 100){
					tv_remain_num.setText(String.format(getString(R.string.resume_album_num), remain));
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

	/**
	 * 
	 * doCreateAlbum(新建相册)
	 * void
	 * @exception
	 */
	private void doCreateAlbum(){
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();		
		try {
			jsonRequest.put("name", et_album_name.getText().toString().trim());
			jsonRequest.put("descript", et_album_content.getText().toString().trim());
			jsonRequest.put("albumsClass", currentSelected);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.CREATEALBUM, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				CommonResponse response2 = JSON.parseObject(response.toString(), CommonResponse.class);
				if ("000000".equals(response2.getCode())) {
					Toast.makeText(AlbumEditActivity.this, R.string.create_album_success, Toast.LENGTH_SHORT).show();
					//setResult(RESULT_OK);
					sendBroadcast(new Intent(Constants.REFRESH_ACTIVITY_ACTION));
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AlbumEditActivity.this, response2.getCode(),response2.getMessage());				
				}
				AlbumEditActivity.this.dismissLoading();
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

	/**
	 * 
	 * doEditAlbum(编辑相册)
	 * void
	 * @exception
	 */
	private void doEditAlbum(){
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("id", album.getId());
			jsonRequest.put("name", et_album_name.getText().toString().trim());
			jsonRequest.put("descript", et_album_content.getText().toString().trim());
			jsonRequest.put("albumsClass", currentSelected);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.EDITALBUM, list);
		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				CommonResponse response2 = JSON.parseObject(response.toString(), CommonResponse.class);
				if ("000000".equals(response2.getCode())) {
					Toast.makeText(AlbumEditActivity.this, R.string.edit_album_success, Toast.LENGTH_SHORT).show();
					//setResult(RESULT_OK);
					Intent intent = new Intent(AlbumEditActivity.this, AlbumListActivity.class);
					intent.putExtra("from", AlbumEditActivity.class.getSimpleName());
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AlbumEditActivity.this, response2.getCode(),response2.getMessage());
				}
				AlbumEditActivity.this.dismissLoading();
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


	/**
	 * 
	 * onDeleteAlbum(删除相册)
	 * void
	 * @exception
	 */
	private void doDeleteAlbum(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(albumid);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.DELETEALBUM, list);
		executeRequest(new FastJsonRequest<CommonResponse>(Method.GET, loginUrl, CommonResponse.class, new Response.Listener<CommonResponse>() {

			@Override
			public void onResponse(CommonResponse response) {
				if ("000000".equals(response.getCode())) {
					Toast.makeText(AlbumEditActivity.this, R.string.delete_album_success, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(AlbumEditActivity.this, AlbumListActivity.class);
					intent.putExtra("from", AlbumEditActivity.class.getSimpleName());
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					//					setResult(RESULT_OK);
					//					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AlbumEditActivity.this, response.getCode(),response.getMessage());
				}
			}

		}, getErrorListener()));
	}

}
