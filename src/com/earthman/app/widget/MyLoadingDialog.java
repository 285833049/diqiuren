package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;

/**
 * @Title: MyLoadingDialog
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月11日
 */

public class MyLoadingDialog {
	public Dialog mDialog;
	private AnimationDrawable animationDrawable = null;
	private TextView mTvHint;

	public MyLoadingDialog(Context context, String message) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.progress_view, null);

		mTvHint = (TextView) view.findViewById(R.id.progress_message);
		mTvHint.setText(message);
		ImageView loadingImage = (ImageView) view
				.findViewById(R.id.progress_view);
		loadingImage.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
		
		mDialog = new Dialog(context, R.style.upomp_bypay_MyDialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);

	}

	public void show() {		
		if(!mDialog.isShowing()){	
			loadAnimation();
			mDialog.show();			
		}
		
	}
	
	private void loadAnimation(){
		if (animationDrawable != null) {
			animationDrawable.setOneShot(false);
			animationDrawable.start();
		}
	}
	
	public void show(String hint){
		mTvHint.setText(hint);
		if(!mDialog.isShowing()){
			loadAnimation();
			mDialog.show();
		}		
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	public void dismiss() {
		if(mDialog == null)
			return;
		if (mDialog.isShowing()) {
			animationDrawable.stop();
			mDialog.dismiss();			
		}
	}

	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}

}
