package com.earthman.app.widget;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.mime.MultipartRequestParams;
import com.charlie.lee.androidcommon.http.request.MultipartRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.UploadImgResponse;
import com.earthman.app.bean.UploadImgResponse.UploadImgResult;
import com.earthman.app.eventbusbean.OKDynamicSend;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.HttpUrlConstants;

import de.greenrobot.event.EventBus;

/**
 * 作者：Zhou
 * 日期：2016-3-10 上午11:25:53
 * 描述：（）
 */
public class UploadImgDialog extends Dialog implements ActivityResultListener{

	private Context context;
	private Activity activity;
	private TextView takephoto;
	private TextView pickphoto;
	private TextView cancel;
	private ViewClickListener viewClickListener;
	private int uploadType;
	public final static int UPLOAD_HEAD = 0;//上传头像
	public final static int UPLOAD_PERSONAL_ALBUM = 1;//上传个人相册
	private String path;
	private String userid;
	private ImageView imageView;
	private Handler mHandler;
	private UserInfoPreferences preferences;
	
	/**
	 * 创建一个新的实例 UploadImgDialog.
	 *
	 * @param context
	 * @param theme
	 */
	public UploadImgDialog(Context context, int uploadType, ImageView imageView, Handler mHandler) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		this.uploadType = uploadType;
		this.imageView = imageView;
		this.mHandler = mHandler;
		preferences = new UserInfoPreferences(context);
		userid = preferences.getUserID();
		if(context instanceof Activity){
			activity = (Activity) context;
		}
		initView();
		initPath();
	}
	
	
	public void setUploadType(int uploadType){
		this.uploadType = uploadType;
	}
	
		
	private void initView(){
		setContentView(R.layout.uploadimg);
		takephoto = (TextView) findViewById(R.id.takephoto);
		pickphoto = (TextView) findViewById(R.id.pickphoto);
		cancel = (TextView) findViewById(R.id.cancel);
		viewClickListener = new ViewClickListener();
		takephoto.setOnClickListener(viewClickListener);
		pickphoto.setOnClickListener(viewClickListener);
		cancel.setOnClickListener(viewClickListener);
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context));
		params.gravity = Gravity.BOTTOM;
	}
	
	private void initPath(){
		switch (uploadType) {
		case UPLOAD_HEAD:
			path = FileUtils.getStructureDirs(FileUtils.IMAGE_PATH) + File.separator + userid + ".jpg";
			break;
		case UPLOAD_PERSONAL_ALBUM://上传相册
			path = FileUtils.getStructureDirs(FileUtils.IMAGE_PATH) + File.separator + userid + "_" + System.currentTimeMillis() + ".jpg";
			break;
		default:
			break;
		}
	}
	
	private static final int PICKREPORT = 3;// 从相册选择图片
	private static final int TAKEREPORT = 4;// 相机拍照
	private static final int ZOOMPORT = 5;// 裁剪图片返回
		
	private class ViewClickListener implements View.OnClickListener{

		/* 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.takephoto:
				takePhoto();
				break;
			case R.id.pickphoto:
				selectPhoto();				
				break;
			case R.id.cancel:								
			default:
				break;
			}
			dismiss();			
		}
		
	}

	// 拍照
	private void takePhoto() {
		Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(path)));
		activity.startActivityForResult(takeIntent, TAKEREPORT);
	}

	// 从相册选择图片
	private void selectPhoto() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(pickIntent, PICKREPORT);
	}

	public void imagePath(String path) {
		File temp = new File(path);
		Uri imageUri = Uri.parse("file://" + path);
		startPhotoZoom(Uri.fromFile(temp), imageUri);
	}

	/**
	 * 图片裁剪大图方法 裁剪图片宽高获得新图片〉 图片------>>>调用系统的功能
	 */
	public void startPhotoZoom(Uri uri, Uri imageUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, ZOOMPORT);
	}


	/*
	 * 上传图片方法
	 */
	private void doUploadImg() {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		MultipartRequestParams params = new MultipartRequestParams();
		params.put("picfile", new File(path));
		ArrayList<Object> list = new ArrayList<Object>();
        list.add(userid);
        list.add(preferences.getUserToken());
        list.add(uploadType);
		String url = HttpUrlConstants.getUrl(context, HttpUrlConstants.UPLOADPIC,
				list);
		RequestManager.getInstance().addRequest(
				new MultipartRequest(url, params,
						new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject object) {
						UploadImgResponse bean = JSON.parseObject(
								object.toString(),
								UploadImgResponse.class);
						if ("000000".equals(bean.getCode())) {
							Toast.makeText(activity,
									context.getResources().getString(R.string.upload_success),
									Toast.LENGTH_SHORT).show();
							UploadImgResult result = bean.getResult();
							if (result != null) {
								if(mHandler != null){
									Message msg = new Message();
									msg.obj = result.getPiclink();
									mHandler.sendMessage(msg);
								}
								new UserInfoPreferences(context).setUserIco(result.getPiclink());
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.outWidth = imageView.getWidth();
								options.outHeight = imageView.getHeight();
								Bitmap bit = BitmapFactory.decodeFile(path, options);
								imageView.setImageBitmap(bit);
								if (UPLOAD_HEAD==uploadType) {
									EventBus.getDefault().post(new OKDynamicSend(1));//发送通知通知动态界面更改用户头像和昵称
								}
							}
						} else {
							Toast.makeText(activity,
									bean.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}, getErrorListener()), "uploadImg");

	}
	
	/**
	 * 处理网络请求失败时的监听器
	 * 
	 * @return
	 */
	protected Response.ErrorListener getErrorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();
				
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKEREPORT:// 拍照返回
				imagePath(path);
				break;
			case PICKREPORT :// 选择图片返回
				Uri imageUri = Uri.parse("file://" + path);
				startPhotoZoom(data.getData(), imageUri);
				break;
			case ZOOMPORT:// 裁剪图片返回
				doUploadImg();
				break;
			default:
				break;
			}
		}
	}
}
