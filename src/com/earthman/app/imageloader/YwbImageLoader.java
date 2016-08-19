package com.earthman.app.imageloader;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

import com.earthman.app.R;
import com.earthman.app.utils.FileUtils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 
 * 作者：Zhou
 * 日期：2016-1-15 下午1:07:20
 * 描述：（）
 */

public class YwbImageLoader {
	private ImageLoader imageLoader = null;//ImageLoader对象
	public YwbImageLoader() { 
		imageLoader = ImageLoader.getInstance();
	}
	
	/**
	 * 初始化ImageLoader <br/>
	 * 
	 * @param [参数1]-[参数1说明] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public static void initImageLoader(Context context) {
		// 获取本地缓存的目录，该目录在SDCard的根目录下
		File cacheDir = new File(FileUtils.getImageCachePath());
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		// 设置线程数量
		// 设定线程等级比普通低一点
		builder.threadPriority(Thread.NORM_PRIORITY - 1);
		builder.tasksProcessingOrder(QueueProcessingType.LIFO);
		//使用软引用，有利于bitmap 的内存回收。
//		builder.memoryCache(new WeakMemoryCache());
		builder.memoryCache(new LruMemoryCache((int)(Runtime.getRuntime().maxMemory() / 8)));//分配程序最大内存为128Mb 6/1作为图片缓存
		builder.threadPoolSize(3);
		builder.denyCacheImageMultipleSizesInMemory();
		// 设定缓存的SDcard目录，UnlimitDiscCache速度最快
		builder.diskCacheSize(100 * 1024 * 1024); 
		// 设定网络连接超时 timeout: 8s 读取网络连接超时read timeout: 15s
		builder.imageDownloader(new BaseImageDownloader(context, 8*1000, 20*1000));
		// 设置ImageLoader的配置参数
		builder.defaultDisplayImageOptions(initDisplayOptions(true,true,
				R.drawable.pictures_no));

		// 初始化ImageLoader
		ImageLoader.getInstance().init(builder.build());
	}
	/**
	 * 返回默认的参数配置
	 * 
	 * @param isDefaultShow
	 *            true：显示默认的加载图片 false：不显示默认的加载图片
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault,
			int resId, int imgOnFail) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		// 设置图片缩放方式
		// EXACTLY: 图像将完全按比例缩小的目标大小
		// EXACTLY_STRETCHED: 图片会缩放到目标大小
		// IN_SAMPLE_INT: 图像将被二次采样的整数倍
		// IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
		// NONE: 图片不会调整
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		if (isShowDefault) {
			//降低图片的质量
			displayImageOptionsBuilder.bitmapConfig(Config.RGB_565);
			// 默认显示的图片
			displayImageOptionsBuilder.showImageOnLoading(resId);
			// 地址为空的默认显示图片
			displayImageOptionsBuilder.showImageForEmptyUri(imgOnFail);
			// 加载失败的显示图片
			displayImageOptionsBuilder.showImageOnFail(imgOnFail);
		}
		// 开启内存缓存
		displayImageOptionsBuilder.cacheInMemory(true);
		// 开启SDCard缓存
		displayImageOptionsBuilder.cacheOnDisc(true);
		displayImageOptionsBuilder.considerExifParams(true);
		return displayImageOptionsBuilder.build();
	}
	
	/**
	 * 返回默认的参数配置
	 * 
	 * @param isDefaultShow
	 *            true：显示默认的加载图片 false：不显示默认的加载图片
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault,boolean isCacheOnDisk,
			int resId) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		// 设置图片缩放方式
		// EXACTLY: 图像将完全按比例缩小的目标大小
		// EXACTLY_STRETCHED: 图片会缩放到目标大小
		// IN_SAMPLE_INT: 图像将被二次采样的整数倍
		// IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
		// NONE: 图片不会调整
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		if (isShowDefault) {
			// 默认显示的图片
			displayImageOptionsBuilder.showImageOnLoading(resId);
			// 地址为空的默认显示图片
			displayImageOptionsBuilder.showImageForEmptyUri(resId);
			// 加载失败的显示图片
			displayImageOptionsBuilder.showImageOnFail(resId);
		}
		// 开启内存缓存
		displayImageOptionsBuilder.bitmapConfig(Config.RGB_565);
		displayImageOptionsBuilder.cacheInMemory(true);
		// 开启SDCard缓存
		displayImageOptionsBuilder.cacheOnDisk(isCacheOnDisk);
		displayImageOptionsBuilder.considerExifParams(true);
		return displayImageOptionsBuilder.build();
	}
	
	/**
	 * @param uri 图片下载地址
	 * @param imageAware 图片容器
	 * @param options 下载的参数配置
	 */
	public void loadImage(String uri, ImageAware imageAware,
			DisplayImageOptions options) {
		
		imageLoader.displayImage(uri, imageAware, options);
	}
	
	/**
	 * 
	 * @param uri 图片下载地址
	 * @param imageAware 图片容器
	 * @param defaultImage 默认图片
	 */
	public void loadImage(String uri, ImageAware imageAware,
			int defaultImage) {
		imageLoader.displayImage(uri, imageAware, initDisplayOptions(true,true, defaultImage));
	}
	
	/**
	 * @param uri 图片下载地址
	 * @param imageView 图片容器
	 * @param options 下载的参数配置
	 */
	public void loadImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
		imageLoader.displayImage(uri, imageView, options);
	}
	
	/**
	 * 
	 * @param uri 图片下载地址
	 * @param imageView 图片容器
	 * @param defaultImage 默认图片
	 */
	public void loadImage(String uri, ImageView imageView,
			int defaultImage) {
		imageLoader.displayImage(uri, imageView, initDisplayOptions(true,true ,defaultImage));
	}
	
	/**
	 * @param uri 图片下载地址
	 * @param imageView 图片容器
	 */
	public void loadImage(String uri,ImageView imageView) {
			if(uri.startsWith("file:")){//本地图片不需要硬盘缓存
        			imageLoader.displayImage(uri,imageView, initDisplayOptions(true,false,
        					R.drawable.pictures_no));
    		}else{
        		imageLoader.displayImage(uri,imageView, initDisplayOptions(true,true,
        				R.drawable.pictures_no));
    		}
			
			
	}

	/**
	 * 
	 * @param uri 图片下载地址
	 * @param imageView 图片容器
	 * @param defaultImage 默认图片
	 */
	public void loadImage(String uri, ImageView imageView,
			int defaultImage,ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, initDisplayOptions(true,true, defaultImage), listener);
	}
	
	/**
	 * 
	 * @param uri 图片下载地址，不显示默认图
	 * @param imageView 图片容器
	 * @param defaultImage 默认图片
	 */
	public void loadImage(String uri, ImageView imageView, boolean defaultImage) {
		imageLoader.displayImage(uri, imageView, initDisplayOptions(defaultImage,true, 0));
	}
	
	/**
	 * 
	 * @param uri 图片下载地址，不显示默认图
	 * @param imageView 图片容器
	 * @param defaultImage 默认图片
	 */
	public void loadImage(String uri, ImageView imageView, int imageOnLoading, int imgOnFail) {
		imageLoader.displayImage(uri, imageView, initDisplayOptions(true, imageOnLoading, imgOnFail));
	}
}