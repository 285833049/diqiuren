package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.listener.EditTextDialogListener;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月21日 下午2:25:26
 * @Decription
 */
public class DialogEditText extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;
	private TextView mTvTitle;
	private EditText mEtContent;
	private Button mBtnLeft, mBtnRight;

	private EditTextDialogListener mListener;

	// 兼容早期使用过的代码不报错，下个版本将会删除
	@Deprecated
	public DialogEditText(final Context context) {
		super(context, R.style.upomp_bypay_MyDialog);
//		mContext = context;
//		setContentView(R.layout.dialog_edit_layout);
//		final EditText etContent = (EditText) findViewById(R.id.et_content);
//		// 取消按钮
//		Button btnCancel = (Button) findViewById(R.id.btn_left);
//		btnCancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});
//		// 确定按钮
//		Button btnOk = (Button) findViewById(R.id.btn_right);
//		btnOk.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String editcontent = etContent.getEditableText().toString();
//				ChoiceRelationActivity alert = (ChoiceRelationActivity) context;
//				if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
//
//					return;
//				}
//				alert.adapter.setData(etContent.getText().toString().trim());
//				alert.adapter.notifyDataSetChanged();
//				dismiss();
//			}
//
//		});
//		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
//		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 60));
	}

	// 最近方法
	public DialogEditText(Context context, String titleName, String inputHint, String etContent, EditTextDialogListener listener) {
		super(context, R.style.upomp_bypay_MyDialog);
		setContentView(R.layout.dialog_edittext);
		setCancelable(false);
		mContext = context;
		mListener = listener;
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mEtContent = (EditText) findViewById(R.id.et_content);
		mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		// 为考虑到对话框扩展性以及通用性，操作按钮ID名称以 btn_left、btn_right来命名
		mBtnLeft = (Button) findViewById(R.id.btn_left);
		mBtnRight = (Button) findViewById(R.id.btn_right);
		if (!TextUtils.isEmpty(titleName))
			mTvTitle.setText(titleName);
		if (!TextUtils.isEmpty(inputHint))
			mEtContent.setHint(inputHint);
		if (!TextUtils.isEmpty(etContent)){
			mEtContent.setText(etContent);
			mEtContent.setSelection(etContent.length());
		}
		mBtnLeft.setOnClickListener(this);
		mBtnRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_left:
			dismiss();
			mListener.onLeftButtonClick(mEtContent);
			break;
		case R.id.btn_right:
			if (mEtContent.getText().toString().length() == 0) {
				Toast.makeText(mContext, R.string.content_cannot_empty, Toast.LENGTH_SHORT).show();
				return;
			} else {
				dismiss();
				mListener.onRightButtonClick(mEtContent);
			}
			break;
		}
	}

}
