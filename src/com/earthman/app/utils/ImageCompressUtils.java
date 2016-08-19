package com.earthman.app.utils;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.earthman.app.enums.CompressType;

public class ImageCompressUtils {

	private static final String TAG="ImageCompressUtils";
	/**
	 * Get bitmap from specified image path
	 * 
	 * @param imgPath
	 * @return
	 */
	public Bitmap getBitmap(String imgPath) {
		// Get bitmap through image path
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = false;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		// Do not compress
		newOpts.inSampleSize = 1;
		newOpts.inPreferredConfig = Config.RGB_565;
		return BitmapFactory.decodeFile(imgPath, newOpts);
	}

	/**
	 * Store bitmap into specified image path
	 * 
	 * @param bitmap
	 * @param outPath
	 * @throws FileNotFoundException
	 */
	public void storeImage(Bitmap bitmap, String outPath, int quality)
			throws FileNotFoundException {
		FileOutputStream os = new FileOutputStream(outPath);
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
		bitmap.recycle();
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Compress image by pixel, this will modify image width/height. Used to get
	 * thumbnail
	 * 
	 * @param imgPath
	 *            image path
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @return
	 */
	public Bitmap ratio(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Config.RGB_565;
		// Get bitmap info, but notice that bitmap is null now
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int  width= newOpts.outWidth;
		int height = newOpts.outHeight;
		// 想要缩放的目标尺寸
		float reqHeight  = pixelH;
		float reqWidth  = pixelW;
		// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
        // 一定都会大于等于目标的宽和高。
		int be = 1;// be=1表示不缩放
		if (width > height && width > reqWidth) {// 如果宽度大的话根据宽度固定大小缩放
			be =  Math.round((float) width / (float) reqWidth);  
		} else if (height>width && height > reqHeight) {// 如果高度高的话根据宽度固定大小缩放
			be =  Math.round((float) height / (float) reqHeight);  
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		Log.d("ratio", "width:"+width+"	height:"+height+"	reqWidth:"+reqWidth+"	reqHeight:"+reqHeight+"	scale:"+be);
		return bitmap;
	}


	/**
	 * 图片质量压缩,返回输入流
	 * @param image 压缩的图片
	 * @param percent 压缩量
	 * @return
	 */
	@SuppressLint("NewApi") public InputStream output2InputCompressed(Bitmap image, int percent) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream2Is os = null;
		os = new ByteArrayOutputStream2Is();
		if(image.getByteCount()/1024<100){//如果图片小于100kb不做质量压缩
			percent=100;
		}
		image.compress(Bitmap.CompressFormat.JPEG, percent, os);
		image.recycle();
		bis = new ByteArrayInputStream(os.getBuf()); // 创建字节输入流
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bis;
	}

	/**
	 * 图片压缩，返回输入流
	 * 
	 * @param imgPath
	 *            原图完整路径
	 * @param compressType
	 *            压缩类型
	 * @return 返回压缩图片完整路径
	 */
	public InputStream[] Compressed(String[] imgPath,
			CompressType compressType) {
		int quality = 60;
		int pixelW=720;
		int pixelH=1280;
		switch (compressType) {
		case NORMAL:
			quality = 60;
			 pixelW=720;
			 pixelH=1280;
			break;
		case HD:
			quality = 60;
			 pixelW=1080;
			 pixelH=1920;
			break;
		case ORIGINAL:
			return null;
		}
		ArrayList<InputStream> dInputFiles = new ArrayList<InputStream>();
		for (String srcPath : imgPath) {
			InputStream in = output2InputCompressed(ratio(srcPath,pixelW,pixelH), quality);
			dInputFiles.add(in);
		}

		return dInputFiles.toArray(new InputStream[dInputFiles.size()]);
	}



	/**
	 * Ratio and generate thumb to the path specified
	 * 
	 * @param image
	 * @param outPath
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @param needsDelete
	 *            Whether delete original file after compress
	 * @throws FileNotFoundException
	 */
	public void ratioAndGenThumb(String imgPath, String outPath, float pixelW,
			float pixelH, int quality) throws FileNotFoundException {
		Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
		storeImage(bitmap, outPath, quality);
	}

	
}
