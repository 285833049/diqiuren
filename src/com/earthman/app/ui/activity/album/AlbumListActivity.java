package com.earthman.app.ui.activity.album;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.AlbumListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.GetAlbumListResponse;
import com.earthman.app.bean.GetAlbumListResponse.Album;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-27 下午2:56:21
 * @Decription (相册列表界面)
 */

@ContentView(R.layout.album_list)
public class AlbumListActivity extends BaseActivity{

	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.btn_right)
	private Button btn_right;
	@ViewInject(R.id.gridview)
	private GridView gridview;
	private AlbumListAdapter albumListAdapter;
	public static final int EDIT_ALBUM = 0x99;
	private ArrayList<Album> albums;
	private RefreshReceiver receiver;
	
	public static final int SEE_MINE_ALBUM = 1;//查看我的相册
	public static final int SEE_FRIEND_ALBUM = 2;//查看好友的相册
	private int currentPage;
	private int friendid;
	private PayPwdDialog payPwdDialog;
	private ActivityResultListener listener;
	private Album curAlbum;
	private PaySuccessReceiver receiver2;
	
	private class RefreshReceiver extends BroadcastReceiver{
		/* 
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constants.REFRESH_ACTIVITY_ACTION.equals(intent.getAction())){
				doGetAlbumList();				
			}
			
		}
		
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PayFactory.CASE_SEE_VIDEO_ALBUM:
				if(curAlbum == null){
					return;
				}
				for(Album album : albums){
					if(album.getId() == curAlbum.getId()){
						album.setAlbumsClass(null);
					}
				}
				albumListAdapter.notifyDataSetChanged();						
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
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
		albums = new ArrayList<GetAlbumListResponse.Album>();		
		Intent intent = getIntent();
		if(intent != null){
			currentPage = intent.getIntExtra("pageType", 1);
			friendid = intent.getIntExtra("friendid", 0);
		}
		albumListAdapter = new AlbumListAdapter(this, albums, currentPage);
	}
	
	
	/**
	 * 
	 * intoActivity(跳转到当前页面)
	 * @param context
	 * @param pageType
	 * void
	 * @exception
	 */
	public static void intoActivity(Context context, int pageType, int friendid){
		Intent intent = new Intent(context, AlbumListActivity.class);
		intent.putExtra("pageType", pageType);
		intent.putExtra("friendid", friendid);
        context.startActivity(intent);		
	}

	/* 
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		onBack();
	}
	
	
	
	@Override
	public void onBackPressed() {
		onBack();
		super.onBackPressed();
	}

	private void onBack(){
		setResult(RESULT_OK);
		finish();
	}

	
	@OnClick(R.id.btn_right)
	private void onAddAlbumClick(View v){
		AlbumEditActivity.intoActivity(AlbumListActivity.this, AlbumEditActivity.ADD_ALBUM, null);
	}
	
	@OnItemClick(R.id.gridview)
	private void onItemClick(AdapterView<?> parent, View view, int position, long id){
		switch (currentPage) {
		case SEE_MINE_ALBUM://查看自己的相册跳转
			if(position == 0){
				AlbumEditActivity.intoActivity(AlbumListActivity.this, AlbumEditActivity.ADD_ALBUM, null);
			}else{
				Intent intent = new Intent(AlbumListActivity.this, PhotoListActivity.class);		
				intent.putExtra("album", albums.get(position - 1));
				startActivity(intent);
			}
			break;
		case SEE_FRIEND_ALBUM://查看好友的相册
			if("1".equals(albums.get(position).getAlbumsClass())){
				showPayDialog(albums.get(position));
			}else{
				Intent intent = new Intent(AlbumListActivity.this, PhotoListActivity.class);		
				intent.putExtra("album", albums.get(position));
				intent.putExtra("currentPage", currentPage);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		
		
	}
	
	private void showPayDialog(final Album album){
		final String money = "2";
		curAlbum = album;
		payPwdDialog = new PayPwdDialog(this, money, new OnSurePayClickListener() {

			@Override
			public void onSurePayClick(int payType, String pwd) {
				new PayFactory(AlbumListActivity.this, PayFactory.CASE_SEE_VIDEO_ALBUM, payType, money).genVideoAlbumRequest(0, friendid, album.getId(), pwd);
			}

		});		
		listener = payPwdDialog;
		payPwdDialog.setCancelable(false);
		payPwdDialog.show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PayPwdDialog.CHANGE_PAYTYPE:
				if (listener != null) {
					listener.onActivityResult(requestCode, resultCode, data);
				}
				break;
			default:				
				break;
			}

		}
	}


	/* 
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.album);		
		switch (currentPage) {
		case SEE_MINE_ALBUM:
			doGetAlbumList();
			btn_right.setText(R.string.add_album);
			break;
		case SEE_FRIEND_ALBUM:
			btn_right.setVisibility(View.GONE);
			doGetFriendAlbumList();
			break;
		default:
			break;
		}
		gridview.setAdapter(albumListAdapter);
		receiver = new RefreshReceiver();
		registerReceiver(receiver, new IntentFilter(Constants.REFRESH_ACTIVITY_ACTION));
		receiver2 = new PaySuccessReceiver();
		receiver2.setHandler(mHandler);
		registerReceiver(receiver2, new IntentFilter(Constants.PAY_SUCCESS_ACTION));
		
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent != null){
			String from = intent.getStringExtra("from");
			if(AlbumEditActivity.class.getSimpleName().equals(from)){
				doGetAlbumList();
			}
		}
		super.onNewIntent(intent);
	}

	/**
	 * 
	 * doGetAlbumList(获取自己的相册列表)
	 * void
	 * @exception
	 */
	private void doGetAlbumList(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		showLoading();
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GETMYALBUMS, list);
		executeRequest(new FastJsonRequest<GetAlbumListResponse>(Method.GET, loginUrl, GetAlbumListResponse.class, new Response.Listener<GetAlbumListResponse>() {

			@Override
			public void onResponse(GetAlbumListResponse response) {
				if ("000000".equals(response.getCode())) {
					albums.clear();
					albums.addAll(response.getResult());
					albumListAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AlbumListActivity.this, response.getCode(),response.getMessage());
				}
				dismissLoading();
			}

		}, getErrorListener()));
	}
	
	/**
	 * 
	 * doGetFriendAlbumList(获取好友的相册列表)
	 * void
	 * @exception
	 */
	private void doGetFriendAlbumList(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(friendid);
		showLoading();
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.VISITFRIENDALBUM, list);
		executeRequest(new FastJsonRequest<GetAlbumListResponse>(Method.GET, loginUrl, GetAlbumListResponse.class, new Response.Listener<GetAlbumListResponse>() {

			@Override
			public void onResponse(GetAlbumListResponse response) {
				if ("000000".equals(response.getCode())) {
					albums.clear();
					albums.addAll(response.getResult());
					albumListAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AlbumListActivity.this, response.getCode(),response.getMessage());

				}
				dismissLoading();
			}

		}, getErrorListener()));
	}

	@Override
	protected void onDestroy() {
		if(receiver != null){
			unregisterReceiver(receiver);
			unregisterReceiver(receiver2);
		}
		super.onDestroy();
	}
	
	

}
