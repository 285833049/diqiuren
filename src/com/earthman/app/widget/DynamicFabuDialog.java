package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.ui.activity.dynamic.EditDynamic;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月26日 下午3:06:56
 * @Decription
 */
public class DynamicFabuDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;

	/**
	 * 创建一个新的实例 DynamicFabuDialog.
	 *
	 * @param context
	 */
	public DynamicFabuDialog(Context context) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		initView();
	}

	/**
	 * initView(这里用一句话描述这个方法的作用)
	 * void
	 * @exception
	 */
	private void initView() {
		setContentView(R.layout.dynamic_dialog_fabu);
		TextView wenzi = (TextView) findViewById(R.id.wenzi);
		TextView photo = (TextView) findViewById(R.id.pickphoto);
		TextView cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this); 
		wenzi.setOnClickListener(this);
		photo.setOnClickListener(this);
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 80));
	}

	private int  PIC = 1;
	private int WENZI = 2;

	/*
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			dismiss();
			break;
		case R.id.pickphoto:
			Intent intent=new Intent(context,EditDynamic.class);
			intent.putExtra("type", PIC);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.wenzi:
			Intent wenziintent=new Intent(context,EditDynamic.class);
			wenziintent.putExtra("type", WENZI);
			context.startActivity(wenziintent);
			dismiss();
			break;
		default:
			break;
		}
	}

}
