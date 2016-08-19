package com.earthman.app.ui.activity.album;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.PhotoAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CommonResponse;
import com.earthman.app.bean.GetAlbumListResponse.Album;
import com.earthman.app.bean.GetPhotosResponse;
import com.earthman.app.bean.GetPhotosResponse.Photo;
import com.earthman.app.bean.PhotoUploadProgressItem;
import com.earthman.app.enums.CompressType;
import com.earthman.app.enums.MaxSelectPhotoType;
import com.earthman.app.eventbusbean.PhotoSaveBean;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.ImageCompressUtils;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.HeaderGridView;
import com.earthman.app.widget.selectphoto.SelectPhotoActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-30 下午5:47:06
 * @Decription （相册中照片列表界面）
 */
@ContentView(R.layout.photo_list)
public class PhotoListActivity extends BaseActivity {
	@ViewInject(R.id.gridview)
	private HeaderGridView gridview;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.btn_right)
	private Button btn_right;
	@ViewInject(R.id.btn_cancel)
	private Button btn_cancel;
	@ViewInject(R.id.btn_all_choose)
	private Button btn_all_choose;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_delete)
	private TextView tv_delete;// 删除按钮
	private PhotoAdapter adapter;
	public boolean isDeleteStatus;// 是否处于选择删除状态
	private Album album;
	private ArrayList<Photo> photos;
	private int currentPage;

	@ViewInject(R.id.rlt_upload_view)
	private RelativeLayout mRltUploadView;
	@ViewInject(R.id.tv_unupload_number)
	private TextView mTvUnuploadumber;

	private TextView mTvIntroduction;// 简介
	private ImageView mIvPhoto_upload;// 点击图片

	private Timer mTimer;// 上传图片计时器
	private ChangeUnupload mUnupload;

	public static SparseArray<PhotoUploadProgressItem> uploadState = new SparseArray<PhotoUploadProgressItem>();

	private String[] mUploadImagePath; 
	public static final int UPLOAD_IMAGES_MSG=0x56;
	private static final String UPLOAD_PROGRESS_ITEM="UPLOAD_PROGRESS_ITEM";
	
	@SuppressLint("HandlerLeak") Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==UPLOAD_IMAGES_MSG){
				doUploadImg((InputStream[])msg.obj,(PhotoUploadProgressItem)msg.getData().get(UPLOAD_PROGRESS_ITEM),msg.arg1);
			}
		};
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
		photos = new ArrayList<GetPhotosResponse.Photo>();
		Intent intent = getIntent();
		if (intent != null) {
			album = (Album) intent.getSerializableExtra("album");
			currentPage = intent.getIntExtra("currentPage", 1);
		}

		adapter = new PhotoAdapter(this, photos, currentPage);
		doGetPhotos();
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		EventBus.getDefault().register(this);

		View headView = getLayoutInflater().inflate(
				R.layout.photolist_head_view, null);
		mTvIntroduction = (TextView) headView
				.findViewById(R.id.tv_introduction);
		mIvPhoto_upload = (ImageView) headView
				.findViewById(R.id.iv_photo_upload);
		mIvPhoto_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 跳转到上传照片页面
				Intent intent = SelectPhotoActivity.getIntent(PhotoListActivity.this,MaxSelectPhotoType.TripleNum,0);
				startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE);

			}
		});
		gridview.addHeaderView(headView);
		gridview.setOnScrollListener(new PauseOnScrollListener((ImageLoader
				.getInstance()), false, true));
	}

	@OnClick(R.id.btn_right)
	private void onEditClick(View v) {
		AlbumEditActivity.intoActivity(PhotoListActivity.this,
				AlbumEditActivity.EDIT_ALBUM, album);
	}

	/**
	 * setEditStatus(设置编辑状态) void
	 * 
	 * @exception
	 */
	public void setEditStatus() {
		isDeleteStatus = true;
		btn_back.setVisibility(View.GONE);
		tv_left.setVisibility(View.GONE);
		btn_right.setVisibility(View.GONE);
		btn_all_choose.setVisibility(View.VISIBLE);
		btn_cancel.setVisibility(View.VISIBLE);
		tv_delete.setVisibility(View.VISIBLE);
	}

	/**
	 * setUnEditStatus(设置非编辑状态) void
	 * 
	 * @exception
	 */
	private void setUnEditStatus() {
		isDeleteStatus = false;
		btn_back.setVisibility(View.VISIBLE);
		tv_left.setVisibility(View.VISIBLE);
		btn_right.setVisibility(View.VISIBLE);
		btn_all_choose.setVisibility(View.GONE);
		btn_cancel.setVisibility(View.GONE);
		tv_delete.setVisibility(View.GONE);
		tv_title.setVisibility(View.GONE);

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@OnClick(R.id.btn_cancel)
	private void onCancelClick(View v) {
		setUnEditStatus();

		adapter.setSelectStatus(PhotoAdapter.ALL_UNSELECT_STATUS);
		adapter.notifyDataSetChanged();
	}

	@OnClick(R.id.btn_all_choose)
	private void onAllChooseClick(View v) {
		adapter.setSelectStatus(PhotoAdapter.SELECT_ALL_STATUS);
		adapter.notifyDataSetChanged();
		setSelect(adapter.getCount());
	}

	@OnClick(R.id.tv_delete)
	private void onDeleteClick(View v) {
		if (TextUtils.isEmpty(adapter.getSelectIds())) {
			Toast.makeText(PhotoListActivity.this,
					R.string.choose_delete_photo, Toast.LENGTH_SHORT).show();
			return;
		}
		doDeletePhotos();
	}

	@OnClick(R.id.rlt_upload_view)
	private void onStartPhotoUploadClick(View v) {
		PhotoUploadProGressActivity.startActivity(PhotoListActivity.this,
				PhotoUploadProGressActivity.REQUEST_CODE);
	}

	public void setSelect(int num) {
		tv_title.setVisibility(View.VISIBLE);
		tv_title.setText(String.format(getString(R.string.has_choose), num));
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {

		mTvIntroduction.setText(getString(R.string.tv_introduction,
				album.getDescript()));
		tv_left.setText(album.getName());
		if (currentPage == AlbumListActivity.SEE_FRIEND_ALBUM) {// 查看好友
			btn_right.setVisibility(View.GONE);
			mIvPhoto_upload.setVisibility(View.GONE);
		} else {
			btn_right.setText(R.string.edit);
			mIvPhoto_upload.setVisibility(View.VISIBLE);
		}

		gridview.setAdapter(adapter);

	}

	/**
	 * 
	 * doGetPhotos(获取相册列表) void
	 * 
	 * @exception
	 */
	private void doGetPhotos() {

		// 防止选择上传照片后 返回相册列表，相片上传成功时 报错直接跳入首页
		// Activity finish后不再需要更新相片列表
		if (!isFinishing()) {
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(preferences.getUserID());
			list.add(preferences.getUserToken());
			list.add(album.getId());
			showLoading();
			String loginUrl = HttpUrlConstants.getUrl(this,
					HttpUrlConstants.GETPHOTOS, list);
			executeRequest(new FastJsonRequest<GetPhotosResponse>(Method.GET,
					loginUrl, GetPhotosResponse.class,
					new Response.Listener<GetPhotosResponse>() {

						@Override
						public void onResponse(GetPhotosResponse response) {
							if ("000000".equals(response.getCode())) {
								photos.clear();
								photos.addAll(response.getResult());
								if (currentPage == AlbumListActivity.SEE_MINE_ALBUM) {
									adapter.setSelectStatus(PhotoAdapter.ALL_UNSELECT_STATUS);
								}
								adapter.notifyDataSetChanged();
							} else {
								NetStatusHandUtil.getInstance().handStatus(
										PhotoListActivity.this,
										response.getCode(),
										response.getMessage());

							}
							dismissLoading();
						}

					}, getErrorListener()));
		}
	}

	/**
	 * 
	 * doDeletePhotos(删除相册中的相片) void
	 * 
	 * @exception
	 */
	public void doDeletePhotos() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(adapter.getSelectIds());
		showLoading();
		String loginUrl = HttpUrlConstants.getUrl(this,
				HttpUrlConstants.DELETEPHOTOS, list);
		executeRequest(new FastJsonRequest<CommonResponse>(Method.GET,
				loginUrl, CommonResponse.class,
				new Response.Listener<CommonResponse>() {

					@Override
					public void onResponse(CommonResponse response) {
						if ("000000".equals(response.getCode())) {
							Toast.makeText(PhotoListActivity.this,
									R.string.delete_photo_success,
									Toast.LENGTH_SHORT).show();
							setUnEditStatus();
							sendBroadcast(new Intent(
									Constants.REFRESH_ACTIVITY_ACTION));
							doGetPhotos();
						} else {
							NetStatusHandUtil.getInstance().handStatus(
									PhotoListActivity.this, response.getCode(),
									response.getMessage());
						}
						dismissLoading();
					}

				}, getErrorListener()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case SelectPhotoActivity.REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					mUploadImagePath = data.getStringArrayExtra("Bigimage");
					//设置选择相片原图大小
					float totalSize=FileUtils.getFilseSize(mUploadImagePath);
					ImageCompressActivity
							.startImageCompressActivity(PhotoListActivity.this,totalSize);
					// doUploadImg1(array);
					// doUploadImg(array);
				}
			}
			break;
		// 由任务列表返回时刷新图片列表
		case PhotoUploadProGressActivity.REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				doGetPhotos();
			}
			break;
		// 选择图片质量返回
		case ImageCompressActivity.REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				final CompressType cpType=(CompressType) data.getSerializableExtra(ImageCompressActivity.IMAGE_COMPRESS_TYPE_EXTRA);
				// 添加进度数据
				 final PhotoUploadProgressItem uploadProgressItem = new PhotoUploadProgressItem();
					uploadProgressItem.setName(album.getName());
					uploadProgressItem.setImg(mUploadImagePath[0]);
					uploadProgressItem
							.setDescript(getString(R.string.photo_progress_descript));
					final int key=uploadState.size()+1;
					uploadState.put(key, uploadProgressItem);//此处+1作为Key是为了后面绑定对象时做处理
					startUploadProgress();//开始进度查询任务
					
					if(cpType==CompressType.ORIGINAL){//原图直接上传
						doUploadImg(mUploadImagePath,uploadProgressItem,key);
					}else{
						//开启线程 进行图片压缩,压缩完成后进行上传操作
						new Thread(new Runnable() {
							@Override
							public void run() {
								ImageCompressUtils imgFactory = new ImageCompressUtils();
								InputStream [] fis=imgFactory.Compressed(mUploadImagePath, cpType);
								Message msg=new Message();
								//msg.obj=cpPath;
								msg.obj=fis;
								msg.what=UPLOAD_IMAGES_MSG;
								msg.arg1=key;
								Bundle bundle=new Bundle();
								bundle.putSerializable(UPLOAD_PROGRESS_ITEM, uploadProgressItem);
								msg.setData(bundle);
								mHandler.sendMessage(msg);
								imgFactory=null;
							}
						}).start();
					}
			}else{
				mUploadImagePath=null;
			}
			
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	/**
	 * 压缩文件上传设置
	 * @param array
	 * @param uploadProgressItem
	 * @param keyThreadId
	 */
	public void doUploadImg(InputStream [] array,final PhotoUploadProgressItem uploadProgressItem,final int keyThreadId){
		if (array == null || album == null) {
			return;
		}
		// 实例化RequestParams对象
		RequestParams requestParams = new RequestParams();
		for (int i = 0; i < array.length; i++) {
			requestParams.addBodyParameter("agent", "android");
			// imageFile是File格式的对象， 将此File传递给RequestParams
			//requestParams.addBodyParameter("file" + i, new File(array[i]));
			try {
				requestParams.addBodyParameter("file" + i,array[i],array[i].available(),System.currentTimeMillis()+".jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		doUploadImg(uploadProgressItem,keyThreadId,requestParams);
	}
	
	
	/**
	 * 原图上传参数设置
	 * @param array
	 * @param uploadProgressItem
	 * @param keyThreadId
	 */
	public void doUploadImg(String [] array,final PhotoUploadProgressItem uploadProgressItem,final int keyThreadId){
		if (array == null || album == null) {
			return;
		}
		// 实例化RequestParams对象
		RequestParams requestParams = new RequestParams();
		for (int i = 0; i < array.length; i++) {
			requestParams.addBodyParameter("agent", "android");
			// imageFile是File格式的对象， 将此File传递给RequestParams
			requestParams.addBodyParameter("file" + i, new File(array[i]));
		}
		doUploadImg(uploadProgressItem,keyThreadId,requestParams);
	}
	
	/*
	 * 上传多张图片带进度条方法
	 */
	private void doUploadImg(final PhotoUploadProgressItem uploadProgressItem,final int keyThreadId,RequestParams requestParams) {
		
		// 实例化HttpUtils对象， 参数设置链接超时
		HttpUtils httpUtils = new HttpUtils(60 * 1000);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(album.getId());
		String url = HttpUrlConstants.getUrl(PhotoListActivity.this,
				HttpUrlConstants.ADDPHOTOS, list);

		// 通过HTTP_UTILS来发送post请求， 并书写回调函数
		httpUtils.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.d("PhotoListActivity","onFailure");
						refreshUIWithUnuploadNum(keyThreadId);
					}

					@Override
					public void onLoading(long total, final long current,
							boolean isUploading) {
						int progress = (int) ((current * 1.0) / total * 100);
						 Log.d("PhotoListActivity", "progress:"+progress+"	current:"+current+" total:"+total+" isUploading:"+isUploading);
						uploadProgressItem.setProgress(progress);
						uploadProgressItem.setDescript(progress + "%");
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Log.d("PhotoListActivity","onSuccess");
						refreshUIWithUnuploadNum(keyThreadId);

					}
				});

	}

	// 图片全部上传成功后处理
	private void refreshUIWithUnuploadNum(int key) {
		Log.d("PhotoListActivity",""+key);
		uploadState.remove(key);
		if (uploadState.size() == 0) {
			sendBroadcast(new Intent(Constants.REFRESH_ACTIVITY_ACTION));
			doGetPhotos();
			uploadState.clear();
			Toast.makeText(PhotoListActivity.this, R.string.upload_img_success,
					Toast.LENGTH_SHORT).show();
		}

	}

	// 未上传图片数量刷新任务
	class ChangeUnupload extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					updateTvUnuploadumber(""+uploadState.size());
					if (uploadState.size() == 0) {
						stopUploadProgress();
						updateTvUnuploadumber(null);
					}
				}
			});
		}
	}

	private void updateTvUnuploadumber(String num) {
		mTvUnuploadumber.setText("" + num);
	}

	@Override
	protected void onResume() {
		super.onResume();
		startUploadProgress();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopUploadProgress();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	public void onEventMainThread(PhotoSaveBean bean) {
		
	}
	
	

	
	/**
	 * 开启进度查询任务
	 */
	private void startUploadProgress(){
		// 当上传任务队列不为空时,开启刷新进度任务
		if (uploadState.size() != 0) {
			mRltUploadView.setVisibility(View.VISIBLE);
			if(mTimer==null&&mUnupload==null){
				 mTimer = new Timer();
				 mUnupload = new ChangeUnupload();
				 mTimer.purge();
				 mTimer.schedule(mUnupload, 50, 50);
			}
			
		}
	}
	
	/**
	 * 关闭上传进度任务
	 */
	private void stopUploadProgress(){
		mRltUploadView.setVisibility(View.GONE);
		if (mTimer != null) {
			mTimer = null;
		}
		if (mUnupload != null) {
			mUnupload.cancel();
			mUnupload = null;
		}
	}
}
