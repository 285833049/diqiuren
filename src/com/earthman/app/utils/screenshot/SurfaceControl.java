package com.earthman.app.utils.screenshot;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.earthman.app.MyApplication;

/**
 * Created by xf on 2014/9/14.
 */
public class SurfaceControl {

	public static Bitmap screenshot(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		File file = getCameraPhotoFile();
		try {
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				try {
					MediaScannerConnection.scanFile(MyApplication.getInstance(), new String[] { file.getAbsolutePath() }, new String[] { "image/jpg" },
							new OnScanCompletedListener() {
								@Override
								public void onScanCompleted(String path, Uri uri) {
								}
							});
					Toast.makeText(MyApplication.getInstance(), "截屏文件已保存至 相册", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(MyApplication.getInstance(), "保存失败！", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bmp;
	}

	public static File getCameraPhotoFile() {
		File dir = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Screenshots");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
		return file;
	}

}
