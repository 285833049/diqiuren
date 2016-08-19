package com.earthman.app.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.charlie.lee.androidcommon.update.OnUpdateListener;
import com.charlie.lee.androidcommon.update.UpdateError;
import com.charlie.lee.androidcommon.update.UpdateManager;
import com.earthman.app.R;
import com.earthman.app.bean.CheckUpdateResponse;
import com.earthman.app.bean.CheckUpdateResponse.CheckUpdateResult;
import com.earthman.app.widget.MyToast;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-21 下午3:42:22
 * @Decription
 */
public class CheckUpdatUtils {

	private Context context;
	private boolean isManualCheck = false;// 是否手动去更新
	private UpdateManager updateManager;
	private Activity activity;

	public CheckUpdatUtils(Context context) {
		this.context = context;
		if (context instanceof Activity) {
			activity = (Activity) context;
		}
		updateManager = new UpdateManager(context);
		updateManager.setOnUpdateListener(new OnUpdateListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onError(UpdateError error) {
			}

			@Override
			public void onCompleted(String result) {
				if (doHandleCheckNewVersionResponse(result) && CheckUpdatUtils.this.isManualCheck) {
					MyToast.makeText(activity, R.string.latest_version, Toast.LENGTH_LONG).show();
					CheckUpdatUtils.this.isManualCheck = false;
				}
			}
		});
	}

	/**
	 * 检查是否有新版本
	 */
	public void doCheckNewVersionTask(boolean isManualCheck) {
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add("android");
		objects.add("user");
		objects.add(ChannelUtil.getChannel(context));
		objects.add(AndroidUtils.getVersionName(context));
		String getUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.UPDATE_ACTION, objects);
		this.isManualCheck = isManualCheck;
		updateManager.checkVersion(getUrl);
	}

	/**
	 * @param result
	 * @return 返回是否是最新版本,true表示是最新版本，否则为false
	 */
	protected boolean doHandleCheckNewVersionResponse(String result) {
		boolean flag = true;
		try {
			CheckUpdateResponse bean = JSON.parseObject(result, CheckUpdateResponse.class);
			CheckUpdateResult checkUpdateResult = bean.getResult();
			int hasNew = checkUpdateResult.getHasNew();
			if (hasNew == 1) {
				// 有更新
				String size = checkUpdateResult.getSize();
				String apkURL = checkUpdateResult.getDownloadUrl();
				int mustUpdate = checkUpdateResult.getMustUpdate();
				String version = checkUpdateResult.getVersion();
				String content = checkUpdateResult.getContent();
				if (mustUpdate == 1) {// 强制更新
					// showUpdateDialog(true,
					// context.getString(R.string.soft_update_title),
					// String.format(context.getString(R.string.soft_update_force_content),
					// version, size, content),
					// context.getString(R.string.soft_update),
					// context.getString(R.string.cancel), apkURL);
					showUpdateDialog(true, context.getString(R.string.soft_update_title),
							String.format(context.getString(R.string.soft_update_force_content), version, size, content), context.getString(R.string.soft_update),
							context.getString(R.string.exit_group), apkURL);
				} else {// 普通更新
					// showUpdateDialog(false,
					// context.getString(R.string.soft_update_title),
					// String.format(context.getString(R.string.soft_update_default_content),
					// size),
					// context.getString(R.string.soft_update_updatebtn),
					// context.getString(R.string.soft_update_later), apkURL);
					showUpdateDialog(false, context.getString(R.string.soft_update_title),
							String.format(context.getString(R.string.soft_update_force_content), version, size, content), context.getString(R.string.soft_update),
							context.getString(R.string.soft_update_later), apkURL);
				}
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	protected void doHandleCheckNewVersionError(UpdateError error) {
		int errorCode = error.getCode();
		switch (errorCode) {
		case UpdateError.INVALID_URL:
			MyToast.makeText(activity, "URL地址错误", Toast.LENGTH_LONG).show();
			break;
		case UpdateError.NO_SDCARD:
			MyToast.makeText(activity, "无SDScard存储器", Toast.LENGTH_LONG).show();
			break;
		case UpdateError.NO_CONNECTOR:
			MyToast.makeText(activity, "网络连接异常，请检查网络连接", Toast.LENGTH_LONG).show();
			break;
		default:
			MyToast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
			break;
		}
	}

	protected void showUpdateDialog(final boolean isForce, String title, String content, String positive, String nagetive, final String apkURL) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(nagetive, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (isForce) {
					activity.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}
		});
		builder.setNegativeButton(positive, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (isForce) {
					downloadApkByOther(apkURL);
					activity.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
				} else {
					// updateManager.startUpdate(apkURL);
					downloadApkByOther(apkURL);
				}
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 使用第三方浏览器下载
	 */
	private void downloadApkByOther(String apkURL) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse(apkURL));
		context.startActivity(intent);
	}

}
