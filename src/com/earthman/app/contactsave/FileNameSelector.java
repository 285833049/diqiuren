package com.earthman.app.contactsave;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.earthman.app.utils.LogUtil;
import com.earthman.app.widget.MyToast;

public class FileNameSelector implements FilenameFilter {
	String extension = ".";
	private static String ROOT_PATH;
	public final static String FILE_DIR_NAME = "/EarthMan_Contact_BackUp";

	public FileNameSelector(String fileExtensionNoDot) {
		extension += fileExtensionNoDot;
	}

	public boolean accept(File dir, String name) {
		return name.endsWith(extension);
	}

	public static String getRootPath(Context context){
		String externalStorageState = Environment.getExternalStorageState();
		if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			ROOT_PATH = Environment.getExternalStorageDirectory() + FILE_DIR_NAME;
			LogUtil.i("EarthMan", ROOT_PATH);
		}else {
			ROOT_PATH="";
			MyToast.makeText(context, "请检查内存卡是否安装！", Toast.LENGTH_SHORT).show();
		}
		return ROOT_PATH;
	}
	
}
