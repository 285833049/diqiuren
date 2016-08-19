package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.widget.passwordview.GridPasswordView;

/**
 * 提现输入密码提示
 * 
 * @author xiexianyong
 * 
 */

public class WithdrawalsPwdDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private int payType;
	private OnSurePayClickListener listener;
	private String pwd;
	private ImageView close_img;
	private GridPasswordView withdrawals_normal;
	private TextView forget_pwd;

	public WithdrawalsPwdDialog(Context context, OnSurePayClickListener listener) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		this.listener = listener;
		initView();
	}

	private void initView() {
		setContentView(R.layout.personal_withdrawal_pwd_alert);
		close_img = (ImageView) findViewById(R.id.close_img);
		withdrawals_normal = (GridPasswordView) findViewById(R.id.withdrawals_normal);
		forget_pwd = (TextView) findViewById(R.id.forget_pwd);

		withdrawals_normal
				.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {

					@Override
					public void onTextChanged(String psw) {

					}

					@Override
					public void onInputFinish(String psw) {
						if (listener != null) {
							listener.onSurePayClick(payType, pwd);
						}
						dismiss();
					}
				});
		android.view.WindowManager.LayoutParams params = getWindow()
				.getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils
				.dip2px(context, 40));
		close_img.setOnClickListener(this);
		forget_pwd.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_img:
			dismiss();
			break;
		case R.id.forget_pwd:

			break;

		default:
			break;
		}
	}

}
