package com.earthman.app.widget;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.QRCodeUtil;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-24 下午5:24:17
 * @Decription
 */
public class QrCodeDialog extends Dialog{

	private Context context;
	private RoundCornerImageView head_img; 
	private TextView tv_nickname;
	private TextView tv_id;
	private ImageView img_qrcode;
	private YwbImageLoader ywbImageLoader;
	private UserInfoPreferences preferences;
	private Button btn_share;
	private InviteFriendDialog inviteFriendDialog;
	
	/**
	 * 创建一个新的实例 QrCodeDialog.
	 *
	 * @param context
	 */
	public QrCodeDialog(Context context) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		ywbImageLoader = new YwbImageLoader();
		preferences = new UserInfoPreferences(context);
		initView();
		setData();
	}
	
	private void initView(){
		setContentView(R.layout.qrcode_dialog);
		head_img = (RoundCornerImageView) findViewById(R.id.head_img);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_id = (TextView) findViewById(R.id.tv_id);
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		btn_share = (Button) findViewById(R.id.btn_share);
		int dialogWith = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 80));
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.width = dialogWith;
	}
	
	private void setData(){
		ywbImageLoader.loadImage(preferences.getUserIco(), head_img, R.drawable.user_avatar);
		tv_nickname.setText(preferences.getUsernice());
		tv_id.setText(preferences.getUsercardId());
		int width = AndroidUtils.dip2px(context, 200);
		String filePath = FileUtils.getStructureDirs(FileUtils.IMAGECACHE) + File.separator + "QRCode_" + preferences.getUsercardId() +".jpg";
		boolean isCreate = QRCodeUtil.createQRImage(preferences.getUsercardId(), width, width, null, filePath);
		if(isCreate){
			img_qrcode.setImageBitmap(BitmapFactory.decodeFile(filePath));
		}
		btn_share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (inviteFriendDialog == null) {
					inviteFriendDialog = new InviteFriendDialog(context, Constants.QRCODE_SHARE);
				}
				inviteFriendDialog.showDialog();
				
			}
		});
	}
	
	public void showDialog(){
		if(!isShowing()){
			this.show();
		}
	}

}
