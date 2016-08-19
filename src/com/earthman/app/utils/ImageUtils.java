package com.earthman.app.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.earthman.app.MyApplication;

public class ImageUtils {

	private long lastClick;
	private Map<String, String> mTaskMap;

	public ImageUtils() {
		mTaskMap = new HashMap<String, String>();
	}

	public void saveNetworkImg(String url) {
		String imageName = getImageName(url);
		if (putImgTask(url, imageName))
			return;// 图片任务重复或者图片已经下载 直接返回
		new ImageAsync(url, imageName).execute(url);
		Toast.makeText(MyApplication.getContext(), "图片下载中...",
				Toast.LENGTH_SHORT).show();
	}

	public String getImageName(String url) {

		String paths[] = url.split("/");
		return paths[paths.length - 1];
	}

	// 根据URL判断下载图片任务是否存在，存在返回true 并把任务添加到mTaskMap 中
	// 本地已经存在图片 返回true
	// 其他情况返回false
	private boolean putImgTask(String imgUrl, String imageName) {
		if (mTaskMap.containsKey(imgUrl)) {
			return true;
		} else {
			// 图片已经下载直接返回
			String filePath = FileUtils.isPubImageFileExist(imageName);
			if (!TextUtils.isEmpty(filePath)) {
				
				long currentTime = System.currentTimeMillis();
				if(currentTime - lastClick > 3000){
					Toast.makeText(MyApplication.getContext(),
							"图片已保存至" + filePath, Toast.LENGTH_LONG).show();
				}
	            lastClick = currentTime;
				
				return true;
			}
			mTaskMap.put(imgUrl, imgUrl);
			return false;
		}
	}

	class ImageAsync extends AsyncTask<String, Void, String> {

		private String mImageUrl;
		private String mPath;
		private String mImgName;

		public ImageAsync(String imageUrl, String imgName) {
			mImageUrl = imageUrl;
			mImgName = imgName;
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			try {

				HttpResponse response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					mPath = FileUtils.getPubImagePath();
					// mImgName=new
					// SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new
					// Date()) + ".jpg";
					// 在这个路径下创建一个文件夹
					File f = new File(mPath, mImgName);
					// 如果这个文件夹不存在，就新建一个
					f.createNewFile();
					FileOutputStream fos = null;
					try {
						// 写入到这个文件中
						fos = new FileOutputStream(f);
						byte[] buf = new byte[1024];
						int length;
						while ((length = is.read(buf)) != -1) {
							fos.write(buf, 0, length);
						}
						is.close();
						fos.close();
						return "success";
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "error";
		}

		@Override
		protected void onPostExecute(String result) {
			mTaskMap.remove(mImageUrl);
			if ("success".equals(result)) {
				Toast.makeText(MyApplication.getContext(),
						"图片已保存至" + mPath +File.separator+ mImgName, Toast.LENGTH_LONG).show();
				//Log.d("LYM",  mPath + mImgName);
				//FileUtils.scanDirAsync(MyApplication.getContext(),mPath);
				FileUtils.scanFileAsync(MyApplication.getContext(), mPath +File.separator+ mImgName);
				
			}
		}
	}

}
