package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.listener.CommonDialogListener;

/**
 * @Title: MyDialogStyle1
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月15日
 */

public class DialogCommon extends Dialog implements OnClickListener {
	// private String content;
	// private String title;
	// private Context context;
	// private TextView commit;
	// private TextView cancel;
	private Context mContext;
	private TextView mTvTitle;
	private TextView mTvMessage;
	private Button mBtnLeft, mBtnRight;

	private CommonDialogListener mListener;

	@Deprecated
	public DialogCommon(Context context, String title, String content) {
		super(context, R.style.upomp_bypay_MyDialog);
		// this.context=context;
		// this.title=title;
		// this.content=content;
		// setContentView(R.layout.mydialogstyle1);
		// commit = (TextView) findViewById(R.id.personal_detail_set_ok);
		// cancel = (TextView) findViewById(R.id.personal_detail_set_cancel);
		// TextView dialog_content = (TextView)
		// findViewById(R.id.dialog_content);
		// TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
		// dialog_content.setText((CharSequence) content);
		// dialog_title.setText((CharSequence) title);
		// cancel.setOnClickListener(this);
		// android.view.WindowManager.LayoutParams params =
		// getWindow().getAttributes();
		// params.width = (int) (AndroidUtils.getDeviceWidth(context) -
		// AndroidUtils.dip2px(context, 80));

	}

	// 最近方法
	public DialogCommon(Context context, String title, String message, String rightButtonName, CommonDialogListener listener) {
		super(context, R.style.upomp_bypay_MyDialog);
		setContentView(R.layout.dialog_common);
		setCancelable(false);
		mContext = context;
		mListener = listener;
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvMessage = (TextView) findViewById(R.id.tv_message);
		mBtnLeft = (Button) findViewById(R.id.btn_left);
		mBtnRight = (Button) findViewById(R.id.btn_right);
		if (!TextUtils.isEmpty(title))
			mTvTitle.setText(title);
		if (!TextUtils.isEmpty(message))
			mTvMessage.setText(message);
		if (!TextUtils.isEmpty(rightButtonName))
			mBtnRight.setText(rightButtonName);
		mBtnLeft.setOnClickListener(this);
		mBtnRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
//		case R.id.btn_left:
//			mListener.onLeftButtonClick();
//			break;
		case R.id.btn_right:
			mListener.onRightButtonClick();
			break;
		}
	}

}
