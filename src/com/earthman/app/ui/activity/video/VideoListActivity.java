package com.earthman.app.ui.activity.video;

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
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.VideoListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.bean.GetVideoListResponse;
import com.earthman.app.bean.GetVideoListResponse.VideoItem;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.ui.activity.video.player.VideoPlayActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.PayPwdDialog;
import com.earthman.app.widget.VideoEditDelDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-1 上午10:18:58
 * @Decription (视频列表界面)
 */
@ContentView(R.layout.video_list)
public class VideoListActivity extends BaseActivity {

	@ViewInject(R.id.tv_left)
	private TextView mTvLeftTitle;
	@ViewInject(R.id.btn_right)
	private Button mBtnNewVideo;
	@ViewInject(R.id.gridview)
	private GridView gridview;
	private VideoListAdapter adapter;
	public static final int SEE_MINE_VIDEO = 1;// 查看我的视频
	public static final int SEE_FRIEND_VIDEO = 2;// 查看好友的视频
	private int currentPage;
	private int friendid;
	private VideoEditDelDialog dialog;
	private ArrayList<VideoItem> videoItems;
	private PayPwdDialog payPwdDialog;
	private ActivityResultListener listener;
	private VideoItem curVideoItem;
	private PaySuccessReceiver receiver2;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			String name = intent.getStringExtra("name");
			String percent = intent.getStringExtra("percent");
			if (Constants.UPLOAD_SUCCESS_ACTION.equals(intent.getAction())) {
				Toast.makeText(VideoListActivity.this, name + getString(R.string.upload_video_success), Toast.LENGTH_SHORT).show();
				doGetVideoList();
			} else if (Constants.UPLOAD_FAIL_ACTION.equals(intent.getAction())) {
				Toast.makeText(VideoListActivity.this, name + getString(R.string.upload_video_fail), Toast.LENGTH_SHORT).show();
			} else if (Constants.UPLOAD_START_ACTION.equals(intent.getAction())) {
				Toast.makeText(VideoListActivity.this, name + getString(R.string.upload_video_start), Toast.LENGTH_SHORT).show();
			} else if (Constants.UPLOAD_LOAD_ACTION.equals(intent.getAction())) {
				Toast.makeText(VideoListActivity.this, name + getString(R.string.has_upload) + percent, Toast.LENGTH_SHORT).show();
			} else if (Constants.REFRESH_ACTIVITY_ACTION.equals(intent.getAction())) {
				doGetVideoList();
			}
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

		Intent intent = getIntent();
		if (intent != null) {
			currentPage = intent.getIntExtra("pageType", 1);
			friendid = intent.getIntExtra("friendid", 0);
		}
		videoItems = new ArrayList<VideoItem>();
		adapter = new VideoListAdapter(this, videoItems, currentPage);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.UPLOAD_SUCCESS_ACTION);
		intentFilter.addAction(Constants.UPLOAD_FAIL_ACTION);
		intentFilter.addAction(Constants.UPLOAD_START_ACTION);
		intentFilter.addAction(Constants.UPLOAD_LOAD_ACTION);
		intentFilter.addAction(Constants.REFRESH_ACTIVITY_ACTION);
		registerReceiver(receiver, intentFilter);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PayFactory.CASE_SEE_VIDEO_ALBUM:
				if (curVideoItem == null) {
					return;
				}
				for (VideoItem videoItem : videoItems) {
					if (videoItem.getId() == curVideoItem.getId()) {
						videoItem.setStatus("1");
					}
				}
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * 
	 * intoActivity(跳转到当前页面)
	 * 
	 * @param context
	 * @param pageType
	 *            void
	 * @exception
	 */
	public static void intoActivity(Context context, int pageType, int friendid) {
		Intent intent = new Intent(context, VideoListActivity.class);
		intent.putExtra("pageType", pageType);
		intent.putExtra("friendid", friendid);
		context.startActivity(intent);

	}

	@OnClick(value = { R.id.btn_back, R.id.btn_right })
	private void onClickListener(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			onBack();
			break;
		case R.id.btn_right:
			startActivity(new Intent(VideoListActivity.this, AddVideoActivity.class));
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	/**
	 * 
	 * doGetVideoList(获取自己的视频列表)
	 * void
	 * 
	 * @exception
	 */
	private void doGetVideoList() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		showLoading();
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_VIDEO_LIST, list);
		executeRequest(new FastJsonRequest<GetVideoListResponse>(Method.GET, getUrl, GetVideoListResponse.class, new Response.Listener<GetVideoListResponse>() {

			@Override
			public void onResponse(GetVideoListResponse response) {
				if ("000000".equals(response.getCode())) {
					videoItems.clear();
					videoItems.addAll(response.getResult());
					adapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(VideoListActivity.this, response.getCode(), response.getMessage());
				}
				dismissLoading();
			}

		}, getErrorListener()));
	}

	/**
	 * 
	 * doVisitFriend(访问好友视频列表)
	 * void
	 * 
	 * @exception
	 */
	private void doVisitFriend() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(friendid);
		showLoading();
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.VISIT_FRIEND_VIDEO, list);
		executeRequest(new FastJsonRequest<GetVideoListResponse>(Method.GET, getUrl, GetVideoListResponse.class, new Response.Listener<GetVideoListResponse>() {

			@Override
			public void onResponse(GetVideoListResponse response) {
				if ("000000".equals(response.getCode())) {
					videoItems.clear();
					videoItems.addAll(response.getResult());
					adapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(VideoListActivity.this, response.getCode(), response.getMessage());
				}
				dismissLoading();
			}

		}, getErrorListener()));
	}

	private void doDeleteVideos(int videoId) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(videoId);
		showLoading();
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.DELETE_VIDEO, list);
		executeRequest(new FastJsonRequest<CommonResponse>(Method.GET, loginUrl, CommonResponse.class, new Response.Listener<CommonResponse>() {

			@Override
			public void onResponse(CommonResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(VideoListActivity.this, R.string.delete_video_success, Toast.LENGTH_LONG).show();
					doGetVideoList();
				} else {
					NetStatusHandUtil.getInstance().handStatus(VideoListActivity.this, response.getCode(), response.getMessage());

				}
				dismissLoading();
			}

		}, getErrorListener()));
	}

	@Override
	public void onBackPressed() {
		onBack();
		super.onBackPressed();
	}

	private void onBack() {
		setResult(RESULT_OK);
		finish();
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		mTvLeftTitle.setText(R.string.video);
		mBtnNewVideo.setText(R.string.add_video);
		switch (currentPage) {
		case SEE_MINE_VIDEO:
			doGetVideoList();
			break;
		case SEE_FRIEND_VIDEO:
			doVisitFriend();
			break;
		default:
			break;
		}
		receiver2 = new PaySuccessReceiver();
		receiver2.setHandler(mHandler);
		registerReceiver(receiver2, new IntentFilter(Constants.PAY_SUCCESS_ACTION));
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (currentPage) {
				case SEE_MINE_VIDEO:
					if (position == 0) {// 查看自己的视频跳转
						AddVideoActivity.intoActivity(VideoListActivity.this, AddVideoActivity.ADD_VIDEO, null);
					} else {
						LogUtil.d("mine video url", videoItems.get(position-1).getVideos());
						/*Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse(videoItems.get(position - 1).getVideos()), "video/mp4");
						startActivity(intent);*/
						startActivity(VideoPlayActivity.newIntent(VideoListActivity.this, videoItems.get(position - 1).getVideos(),videoItems.get(position-1).getTitle()));
					}
					break;
				case SEE_FRIEND_VIDEO:// 查看好友的视频
					if ("2".equals(videoItems.get(position).getStatus())) {
						curVideoItem = videoItems.get(position);
						showPayDialog(videoItems.get(position));
					} else {
						//LogUtil.d("mine video url", videoItems.get(position-1).getVideos());
						/*Intent intent = new Intent(Intent.ACTION_VIEW);
						String urlString = videoItems.get(position).getVideos();
						if (!TextUtils.isEmpty(urlString)) {
							intent.setDataAndType(Uri.parse(urlString), "video/mp4");
						}
						startActivity(intent);*/
						
						startActivity(VideoPlayActivity.newIntent(VideoListActivity.this, videoItems.get(position).getVideos(),videoItems.get(position).getTitle()));
					}
					break;
				default:
					break;
				}

			}
		});

		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				switch (currentPage) {
				case SEE_MINE_VIDEO:
					if (position == 0) {// 查看自己的视频跳转
						return true;
					}
					showDialg(videoItems.get(position - 1));
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	private void showPayDialog(final VideoItem videoItem) {
		final String money = "2";
		payPwdDialog = new PayPwdDialog(this, money, new OnSurePayClickListener() {

			@Override
			public void onSurePayClick(int payType, String pwd) {
				new PayFactory(VideoListActivity.this, PayFactory.CASE_SEE_VIDEO_ALBUM, payType, money).genVideoAlbumRequest(1, friendid, videoItem.getId(), pwd);
			}

		});
		listener = payPwdDialog;
		payPwdDialog.setCancelable(false);
		payPwdDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UserInfoPreferences sInfoPreferences;
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

	private void showDialg(final VideoItem videoItem) {
		dialog = new VideoEditDelDialog(VideoListActivity.this, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_edit:
					AddVideoActivity.intoActivity(VideoListActivity.this, AddVideoActivity.EDIT_VIDEO, videoItem);
					break;
				case R.id.btn_delete:
					DialogCommon commonDialog = new DialogCommon(VideoListActivity.this, getString(R.string.hint), getString(R.string.sure_delete_video), getString(R.string.ok),
							new CommonDialogListener() {
								@Override
								public void onRightButtonClick() {
									doDeleteVideos(videoItem.getId());
								}

							});
					commonDialog.show();
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	@Override
	protected void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
			unregisterReceiver(receiver2);
		}
		super.onDestroy();
	}

}
