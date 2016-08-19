package com.earthman.app.ui.activity.album;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.enums.CompressType;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.upload_image_switch_compress)
public class ImageCompressActivity extends BaseActivity {
	
	public static final int REQUEST_CODE=0x144;
	public static final String IMAGE_COMPRESS_TYPE_EXTRA="IMAGE_COMPRESS_TYPE_EXTRA";
	public static final String IMAGE_TOTAL_SIZE_EXTRA="IMAGE_TOTAL_SIZE_EXTRA";
	
	private CompressType mCompressType=CompressType.NORMAL;
	
	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;
	@ViewInject(R.id.btn_right)
	private Button mBtnRight;
	
	@ViewInject(R.id.ib_normal)
	private ImageButton mIbNormal;
	@ViewInject(R.id.ib_hd)
	private ImageButton mIbHD;
	@ViewInject(R.id.ib_original)
	private ImageButton mIbOriginal;
	
	@ViewInject(R.id.rl_normal)
	private RelativeLayout mRlNormal;
	@ViewInject(R.id.rl_hd)
	private RelativeLayout mRlHD;
	@ViewInject(R.id.rl_original)
	private RelativeLayout mRlOriginal;

	@ViewInject(R.id.tv_image_size)
	private TextView mTvImageSize;
	private float mTotalSize;
	
	public static void startImageCompressActivity(Context context,float totalSize){
		Intent intent=new Intent(context,ImageCompressActivity.class);
		intent.putExtra("IMAGE_TOTAL_SIZE_EXTRA", totalSize);
		((Activity)context).startActivityForResult(intent,REQUEST_CODE);
	}
	@Override
	protected void initData() {
		if(getIntent()!=null){
			mTotalSize=getIntent().getFloatExtra(IMAGE_TOTAL_SIZE_EXTRA, 0);
		}
	}

	@Override
	protected void initView() {
		mBtnBack.setText(R.string.upload_image_cancel);
		mTvTitle.setText(R.string.picture_quality);
		mBtnRight.setText(R.string.upload_image);
		DecimalFormat format=new DecimalFormat(".00");
		mTvImageSize.setText(getString(R.string.tv_imageSize,format.format(mTotalSize)));
		
		mBtnBack.setOnClickListener(this);
		mBtnRight.setOnClickListener(this);
		mRlNormal.setOnClickListener(this);
		mRlHD.setOnClickListener(this);
		mRlOriginal.setOnClickListener(this);
		ibClickStatus();
	}

	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handclick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			setCompressResult(RESULT_CANCELED);
			break;
		case R.id.btn_right:
			setCompressResult(RESULT_OK);
			break;
		case R.id.rl_normal:
			mCompressType=CompressType.NORMAL;
			ibClickStatus();
			break;
		case R.id.rl_hd:
			mCompressType=CompressType.HD;
			ibClickStatus();
			break;
		case R.id.rl_original:
			mCompressType=CompressType.ORIGINAL;
			ibClickStatus();
			break;

		default:
			break;
		}
	}
	
	private void ibClickStatus(){
		if(mCompressType==CompressType.NORMAL){
			mIbNormal.setImageResource(R.drawable.xuanzhong_small);
			mIbHD.setImageDrawable(null);
			mIbOriginal.setImageDrawable(null);
		}else if(mCompressType==CompressType.HD){
			mIbNormal.setImageDrawable(null);
			mIbHD.setImageResource(R.drawable.xuanzhong_small);
			mIbOriginal.setImageDrawable(null);
		}else if(mCompressType==CompressType.ORIGINAL){
			mIbNormal.setImageDrawable(null);
			mIbHD.setImageDrawable(null);
			mIbOriginal.setImageResource(R.drawable.xuanzhong_small);
		}
	}
	
	
	private void setCompressResult(int resultCode){
		if(resultCode==RESULT_CANCELED){
			setResult(resultCode);
			
		}else{
			Intent intent=new Intent();
			intent.putExtra(IMAGE_COMPRESS_TYPE_EXTRA,mCompressType);
			setResult(resultCode,intent);
		}
		finish();
	}	
}
