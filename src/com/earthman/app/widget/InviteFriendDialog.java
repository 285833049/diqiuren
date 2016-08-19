package com.earthman.app.widget;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.QRCodeUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 作者：Zhou
 * 日期：2016-3-16 上午11:27:32
 * 描述：（）
 */
public class InviteFriendDialog extends Dialog{

	public final static int SHARE_SUCCESS = 0;//分享成功
	public final static int SHARE_CANCEL = 1;//分享取消
	private TextView weixin;
	private TextView weixin_circle;
	private TextView sms;
	private TextView facebook;
	private ViewListener listener;
	private TextView cancel_invite;
	private Context mContext;
	private UMSocialService mController;// 友盟分享
	private UMImage shareImage;// 友盟分享图片
	// 分享结果回调
	private ShareListener shareListener;
	// 分享的标题
	private String shareTitle = "";
	// 分享的内容
	private String shareContent = "";
	// 分享的连接
	private String shareUrl;
	private TextView tv_title;
	private int type;
	private UserInfoPreferences preferences;

	public interface ShareListener {
		public void shareCallBack(SHARE_MEDIA platform, int result);
	}

	/**
	 * 创建一个新的实例 InviteFriendDialog.
	 *
	 * @param context
	 */
	public InviteFriendDialog(Context context, int type) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.mContext = context;
		this.type = type;
		initView();
		initShareData(type);
		
	}

	private void initView(){
		setContentView(R.layout.invite_friend);
		weixin = (TextView) findViewById(R.id.weixin);
		weixin_circle = (TextView) findViewById(R.id.weixin_circle);
		sms = (TextView) findViewById(R.id.sms);
		facebook = (TextView) findViewById(R.id.facebook);
		cancel_invite = (TextView) findViewById(R.id.cancel_invite);
		tv_title = (TextView) findViewById(R.id.tv_title);
		listener = new ViewListener();
		weixin.setOnClickListener(listener);
		weixin_circle.setOnClickListener(listener);
		sms.setOnClickListener(listener);
		facebook.setOnClickListener(listener);
		cancel_invite.setOnClickListener(listener);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = (int) AndroidUtils.getDeviceWidth(mContext);
		preferences = new UserInfoPreferences(mContext);
		
	}
	
	
	private class ViewListener implements View.OnClickListener{

		/* 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.weixin://微信
				// 判断网络是否连接
				if (!AndroidUtils.isNetworkAvailable(mContext)) {
					Toast.makeText(mContext, R.string.no_connector, Toast.LENGTH_SHORT).show();
					return;
				}
				shareWx();				
				break;
			case R.id.weixin_circle://朋友圈
				// 判断网络是否连接
				if (!AndroidUtils.isNetworkAvailable(mContext)) {
					Toast.makeText(mContext, R.string.no_connector, Toast.LENGTH_SHORT).show();
					return;
				}
				shareWxCircle();
				break;
			case R.id.sms://短信
				if(type == Constants.QRCODE_SHARE){
					shareContent = mContext.getResources().getString(R.string.myqrcode) + ":" + preferences.getUsercardId();
					shareUrl="";
				}
				shareSms();
				break;
			case R.id.facebook://facebook
				Toast.makeText(mContext, R.string.develop_inform, Toast.LENGTH_SHORT).show();
				break;
			case R.id.cancel_invite://取消邀请
				break;
			default:
				break;
			}
			dismiss();
		}
	}

	// 初始化数据
	public void initShareData(int type) {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().closeToast();
		shareImage = new UMImage(mContext, R.drawable.ic_launcher);
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext, Constants.WX_APPID, Constants.WX_APPSECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constants.WX_APPID, Constants.WX_APPSECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		
		getShareContent(type);
		
	}


	// 设置微信平台的分享内容
	private void shareWx() {
		// 微信平台的分享内容, 多媒体内容设置为图片, 且只能设置为一种类型
		WeiXinShareContent weixinContent = new WeiXinShareContent(shareImage);
		weixinContent.setShareContent(shareContent);
		weixinContent.setTitle(shareTitle);
		int width = AndroidUtils.dip2px(mContext, 200);
		String filePath = FileUtils.getStructureDirs(FileUtils.IMAGECACHE) + File.separator + "QRCode_" + preferences.getUsercardId() +".jpg";
		boolean isCreate = QRCodeUtil.createQRImage(shareUrl, width, width, null, filePath);
		if(isCreate){
			Toast.makeText(mContext, "创建成功", Toast.LENGTH_LONG).show();
			shareImage = new UMImage(mContext, BitmapFactory.decodeFile(filePath));				
			weixinContent.setShareImage(shareImage);
		}

		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(shareUrl);

		mController.setShareMedia(weixinContent);
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
				if (stCode == 200) {
					Toast.makeText(mContext, R.string.share_success_string, Toast.LENGTH_SHORT).show();
					shareCallBack(SHARE_MEDIA.WEIXIN, SHARE_SUCCESS);
				} else {
					Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
					shareCallBack(SHARE_MEDIA.WEIXIN, SHARE_CANCEL);
				}
				mController.unregisterListener(this);
			}

			@Override
			public void onStart() {
				// Toast.makeText(mContext, "开始分享", Toast.LENGTH_SHORT)
				// .show();
			}
		});
	}

	// 设置朋友圈的分享内容
	private void shareWxCircle() {
		int width = AndroidUtils.dip2px(mContext, 200);
		String filePath = FileUtils.getStructureDirs(FileUtils.IMAGECACHE) + File.separator + "QRCode_" + preferences.getUsercardId() +".jpg";
		boolean isCreate = QRCodeUtil.createQRImage(shareUrl, width, width, null, filePath);
		if(isCreate){
			Toast.makeText(mContext, "创建成功", Toast.LENGTH_LONG).show();
			shareImage = new UMImage(mContext, BitmapFactory.decodeFile(filePath));				
		}
		//-----------------------------------
		// 微信平台的分享内容, 多媒体内容设置为图片, 且只能设置为一种类型
		CircleShareContent content = new CircleShareContent(shareImage);
		content.setShareContent(shareContent);
		content.setTitle(shareTitle);
//		content.setShareImage(arg0)
		// 设置分享内容跳转URL
		content.setTargetUrl(shareUrl);
		mController.setShareMedia(content);
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
				if (stCode == 200) {
					Toast.makeText(mContext, R.string.share_success_string, Toast.LENGTH_SHORT).show();
					shareCallBack(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_SUCCESS);
				} else {
					Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
					shareCallBack(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_CANCEL);
				}
				mController.unregisterListener(this);
			}

			@Override
			public void onStart() {
				// Toast.makeText(mContext, "开始分享", Toast.LENGTH_SHORT)
				// .show();
			}
		});
	}

	// 设置短信的分享内容
	private void shareSms() {
		// 短信分享内容, 多媒体内容设置为图片, 且只能设置为一种类型
		SmsShareContent content = new SmsShareContent();
		content.setShareContent(shareContent + shareUrl);
		mController.setShareMedia(content);
		mController.postShare(mContext, SHARE_MEDIA.SMS, new SnsPostListener() {
			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
				if (stCode == 200) {
					// Toast.makeText(mContext, R.string.share_success_string,
					// Toast.LENGTH_SHORT).show();
					shareCallBack(SHARE_MEDIA.SMS, SHARE_SUCCESS);
				} else {
					Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
				}
				mController.unregisterListener(this);
			}

			@Override
			public void onStart() {
				// Toast.makeText(mContext, "开始分享", Toast.LENGTH_SHORT).show();
			}
		});
	}


	// 获取分享的内容
	private void getShareContent(int type) {
		switch (type) {
		case Constants.INVITE_FRIEND_SHARE://邀请朋友注册分享
			shareContent = mContext.getResources().getString(R.string.invitefriend_share_content);
			shareUrl = Constants.INVITE_REGISTER_URL + preferences.getUserID();
			tv_title.setText(R.string.invite_friend);
			cancel_invite.setText(R.string.cancel_invite);
			break;
		case Constants.APP_SHARE:// APP下载链接分享
			shareContent = mContext.getResources().getString(R.string.app_share_content);
			shareUrl = Constants.APP_DOWNLOAD_URL;
			tv_title.setText(R.string.share_app_download);
			cancel_invite.setText(R.string.cancel_share);
			break;
		case Constants.QRCODE_SHARE://分享二维码
			tv_title.setText(R.string.share_qrcode);
			cancel_invite.setText(R.string.cancel_share);
			int width = AndroidUtils.dip2px(mContext, 200);
			String filePath = FileUtils.getStructureDirs(FileUtils.IMAGECACHE) + File.separator + "QRCode_" + preferences.getUsercardId() +".jpg";
			boolean isCreate = QRCodeUtil.createQRImage(preferences.getUsercardId(), width, width, null, filePath);
			if(isCreate){
				shareImage = new UMImage(mContext, BitmapFactory.decodeFile(filePath));				
			}
			shareImage = new UMImage(mContext, R.drawable.ic_launcher);
		default:
			break;
		}
	}

	/**
	 * 设置分享的内容和图片
	 * @param titleStr 标题
	 * @param content 内容
	 * @param shareUrl 分享点击后的链接
	 * @param 
	 */
	public void setShareContent(String titleStr, String content, String shareUrl, String imgPath) {
		this.shareTitle = titleStr;
		this.shareContent = content;
		if (!TextUtils.isEmpty(imgPath)) {
			shareImage = new UMImage(mContext, imgPath);
		}
		if (!TextUtils.isEmpty(shareUrl)) {
			this.shareUrl = shareUrl;
		}
	}


	public void initShareContent(){
		// 初始化分享的内容
	}

	/**
	 * 分享结果回调
	 */
	private void shareCallBack(SHARE_MEDIA platform, int result) {
		if (shareListener == null) {
			return;
		}
		shareListener.shareCallBack(platform, result);
	}

	/**
	 * 设置分享回调监听
	 * @param shareListener
	 */
	public void setShareListener(ShareListener shareListener) {
		this.shareListener = shareListener;
	}


	public void showDialog(){
		if(!isShowing()){
			this.show();
		}
	}

}
