package com.earthman.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * 作者：Zhou 日期：2015-10-13 上午10:30:22 版权：地球人 描述：（）
 */

public class FileUtils {
	// SD卡文件根目录名称
	public final static String ROOT_DIRECTORY = "earthman";
	// 图片缓存目录
	public final static String IMAGECACHE = "imagecache";
	// 录音文件目录
	public static final String SPEEX_PATH = "speex";
	// 异常文件目录
	public static final String CRASH_PATH = "crash";
	// Apk的更新目录
	public static final String APK_UPDATE_PATH = "apk_update";
	// 图片目录
	public static final String IMAGE_PATH = "image";
	// 文件目录
	public static final String FILE_PATH = "file";
	// 小区文件目录
	public static final String VILLAGE = "village";
	// 价格目录
	public static final String PRICELIST = "pricelist";
	
	private static java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
	private static String APK_ENABLE_ARRAY = "bind_bankList";

	private static final int MB = 1024 * 1024;
	
	 //扫描指定文件
    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }
     
    //扫描指定目录
    public static void scanDirAsync(Context ctx, String dir) {
           Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
           scanIntent.setData(Uri.fromFile(new File(dir)));
           ctx.sendBroadcast(scanIntent);
    }

	// 图片缓存的SD卡路径
	public static String getImageCachePath() {
		return Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + IMAGECACHE;

	}

	// 图片下载的SD卡路径
	public static String getImagePath() {
		getStructureDirs(IMAGE_PATH);
		return Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + IMAGE_PATH;
	}
	// 图片下载的SD卡路径
	public static String getPubImagePath() {
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + ROOT_DIRECTORY ;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		//Log.d("LYM", path);
		return path;
	}
	
	
	// 判断图片是否已经下载 如果存在返回路径
	public static String isPubImageFileExist(String imgName) {
		if (isFileExist(getPubImagePath(), imgName)) {
			return getPubImagePath() + File.separator + imgName;
		}
		return "";
	}

	// 文件缓存的SD卡路径
	public static String getFilePath() {
		File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + FILE_PATH);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return fileDir.getAbsolutePath();
	}

	// 文件缓存的SD卡路径
	public static String getFilePath(String fileName) {
		File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + FILE_PATH);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return fileDir.getAbsolutePath() + File.separator + fileName;
	}

	// 获取地球人目录
	public static String getEarthmanPath() {
		return Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY;
	}

	// 判断图片是否已经下载 如果存在返回路径
	public static String isImageFileExist(String imgName) {
		if (isFileExist(Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + IMAGE_PATH, imgName)) {
			return Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + IMAGE_PATH + File.separator + imgName;
		}
		return "";
	}

	
	/**
	 * 获取sdcard文件路径,不存在创建
	 * 
	 * @param directoryPath
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(String directoryPath, String fileName) {
		String sdPath = getSdcardPath();
		if (sdPath == null) {
			return null;
		}
		String path=getStructureDirs(directoryPath);
		File file=new File(path,fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file.getAbsolutePath();
	}

	
	/**
	 * 获取sdcard目录或者文件路径
	 * 
	 * @param directoryPath
	 * @param fileName
	 * @return
	 */
	public static String getDirectoryFilePath(String directoryPath, String fileName) {
		String sdPath = getSdcardPath();
		if (sdPath == null) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer(sdPath).append(File.separator).append(ROOT_DIRECTORY);
		if (directoryPath != null) {
			stringBuffer.append(File.separator).append(directoryPath);
		}
		if (fileName != null) {
			stringBuffer.append(File.separator).append(fileName);
		}
		return stringBuffer.toString();
	}

	// 获取缓存图片文件夹的大小
	public static String getImageCacheSize() {
		String sizeString = "0M";
		long dirSize = 0;
		String path = Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + IMAGECACHE;
		File dir = new File(path);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				return sizeString;
			}
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					dirSize += file.length();
				} else if (file.isDirectory()) {
					dirSize += file.length();
				}
			}
		}
		return df.format(dirSize / 1024f / 1024f) + "M";
	}

	/**
	 * 创建文件路径 <br/>
	 * 
	 * @param [dir]-[路径名] <br/>
	 */
	public static String getStructureDirs(String dir) {
		String path = Environment.getExternalStorageDirectory() + File.separator + ROOT_DIRECTORY + File.separator + dir;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath();
	}

	/**
	 * 判定SD卡是否挂载
	 */
	public static boolean checkSdcardMounted() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡跟目录
	 */
	public static String getSdcardPath() {
		if (checkSdcardMounted()) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String path, String fileName) {
		File file = new File(path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static File write2SDFromInput(String path, String fileName, InputStream input) {
		if (input == null) {
			return null;
		}
		File file = null;
		OutputStream output = null;
		try {
			file = new File(path, fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			/*
			 * while ((input.read(buffer)) != -1) { output.write(buffer); }
			 * output.flush();
			 */
			int lenght = 0;
			int total = 0;
			if (output != null && input != null) {
				while ((lenght = input.read(buffer)) > 0) {
					output.write(buffer, 0, lenght);
					total = total + lenght;
				}
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return 单位MB
	 */
	public static int freeSpaceOnSd() {

		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;

		return (int) sdFreeMB;

	}

	/**
	 * 存储数组到sharedperfence
	 * 
	 */
	public static void saveApkEnalbleArray(Context context, String[] booleanArray) {
		SharedPreferences prefs = context.getSharedPreferences(APK_ENABLE_ARRAY, Context.MODE_PRIVATE);
		JSONArray jsonArray = new JSONArray();
		for (String b : booleanArray) {
			jsonArray.put(b);
		}
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(APK_ENABLE_ARRAY, jsonArray.toString());
		editor.commit();
	}

	/**
	 * 读取数组从sharedperfence
	 * 
	 * @param context
	 * @param arrayLength
	 * @return
	 */
	public static String[] getBankListArray(Context context, int arrayLength) {
		SharedPreferences prefs = context.getSharedPreferences(APK_ENABLE_ARRAY, Context.MODE_PRIVATE);
		String[] resArray = new String[arrayLength];
		Arrays.fill(resArray, true);
		try {
			JSONArray jsonArray = new JSONArray(prefs.getString(APK_ENABLE_ARRAY, "[]"));
			for (int i = 0; i < jsonArray.length(); i++) {
				resArray[i] = jsonArray.getString(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resArray;
	}
	 
	
	// 获取缓存图片文件夹的大小
		public static float getFilseSize(String [] fileNames) {
			long dirSize = 0;
				for (String fileName : fileNames) {
					File file=new File(fileName);
					if (file.isFile()) {
						dirSize += file.length();
					} else if (file.isDirectory()) {
						dirSize += file.length();
					}
				}
			
			return dirSize / 1024f / 1024f;
		}
}
