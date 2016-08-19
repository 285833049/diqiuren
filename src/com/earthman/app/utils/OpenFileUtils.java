package com.earthman.app.utils;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.widget.DialogCommon;

/**
 * @Description：打开文件工具类
 * @author：Vinci
 * @date：2015年3月16日 上午9:08:57
 */
public class OpenFileUtils {
	public static Intent open(Context context, String filePath) {

		File file = new File(filePath);
		if (!file.exists())
			return null;
		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if ("m4a".equals(end) || "mp3".equals(end) || "mid".equals(end) || "xmf".equals(end) || "ogg".equals(end) || "wav".equals(end)) {
			return getAudioFileIntent(filePath);
		} else if ("3gp".equals(end) || "mp4".equals(end) || "avi".equals("end") || "rmvb".equals(end) || "rm".equals(end) || "wmv".equals(end) || "asf".equals(end)) {
			return getAudioFileIntent(filePath);
		} else if ("jpg".equals(end) || "jpeg".equals(end) || "gif".equals(end) || "png".equals(end) || "bmp".equals(end)) {
			return getImageFileIntent(filePath);
		} else if ("apk".equals(end)) {
			return getApkFileIntent(filePath);
		} else if ("ppt".equals(end) || "pptx".equals(end) || "xls".equals(end) || "xlsx".equals(end) || "doc".equals(end) || "docx".equals(end) || "pdf".equals(end)) {
			if (isInstallOfficeCheck(context)) {
				return getOfficeFileIntent(filePath);
			}else{
				return null;
			}
		} else if ("chm".equals(end)) {
			return getChmFileIntent(filePath);
		} else if ("txt".equals(end)) {
			return getTextFileIntent(filePath, false);
		} else {
			return getAllIntent(filePath);
		}
		// switch (end) {
		// case "m4a":
		// case "mp3":
		// case "mid":
		// case "xmf":
		// case "ogg":
		// case "wav":
		// return getAudioFileIntent(filePath);
		// case "3gp":
		// case "mp4":
		// return getAudioFileIntent(filePath);
		// case "jpg":
		// case "gif":
		// case "png":
		// case "jpeg":
		// case "bmp":
		// return getImageFileIntent(filePath);
		// case "apk":
		// return getApkFileIntent(filePath);
		// case "ppt":
		// case "pptx":
		// case "xls":
		// case "xlsx":
		// case "doc":
		// case "docx":
		// case "pdf":
		// if (isInstallOfficeCheck(activity)) {
		// return getOfficeFileIntent(filePath);
		// }
		// return null;
		// case "chm":
		// return getChmFileIntent(filePath);
		// case "txt":
		// return getTextFileIntent(filePath, false);
		// default:
		// return getAllIntent(filePath);
		// }
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	public static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// 是否安装应用检测
	private static DialogCommon dialog;

	private static boolean isInstallOfficeCheck(final Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("cn.wps.moffice_eng".equals(info.packageName)) {
				return true;
			}
		}
		dialog = new DialogCommon(context, "提示", "该附件因未安装查看软件无法打开，是否跳转至app市场进行下载？", "前往下载", new CommonDialogListener() {

			@Override
			public void onRightButtonClick() {
				dialog.hide();
				Intent installIntent = new Intent("android.intent.action.VIEW");
				installIntent.setData(Uri.parse("market://details?id=cn.wps.moffice_eng"));
				context.startActivity(installIntent);
			}
		});
		
		if (!((Activity)context).isFinishing())
			dialog.show();

		return false;
	}

	// Android获取一个用于打开Word文件的intent
	public static Intent getOfficeFileIntent(String param) {

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		// bundle.putBoolean(CLEAR_FILE, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
		Uri uri = Uri.fromFile(new File(param));
		intent.setData(uri);
		intent.putExtras(bundle);
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}
}
