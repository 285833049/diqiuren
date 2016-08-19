package com.earthman.app.bean;

import android.graphics.Bitmap;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-5 下午6:35:29
 * @Decription
 */
public class LoadedImage {

	Bitmap mBitmap;

	public LoadedImage(Bitmap bitmap) {
		mBitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}
}
