package com.earthman.app.nim.session.activity;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.VinciBaseActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.OpenFileUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
@ContentView(R.layout.message_download_file_activity)
public class MessageDownloadFileActivity extends VinciBaseActivity {

	@ViewInject(R.id.progressBar)
	private ProgressBar mProgressBar;
	private IMMessage mIMMessage;
	private FileAttachment fileAttachment;
	private DbUtils mDbUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initArguments() {
		mIMMessage = (IMMessage) getIntent().getSerializableExtra("message");
		mDbUtils = DbUtils.create(this, Constants.DBName.NIM_FILE);
		fileAttachment = (FileAttachment) mIMMessage.getAttachment();
		FileAttachment tempFileAttachment;
		try {
			tempFileAttachment = mDbUtils.findFirst(Selector.from(FileAttachment.class).where("md5", "=", fileAttachment.getMd5()));
			File file = new File(FileUtils.getFilePath(fileAttachment.getDisplayName()));
			// 已经存在
			if (tempFileAttachment.getMd5().equals(fileAttachment.getMd5()) && file.exists()) {
				Intent intent = OpenFileUtils.open(this, file.getAbsolutePath());
				if (intent != null) {
					Toast.makeText(MessageDownloadFileActivity.this, "正在打开...", Toast.LENGTH_SHORT).show();
					startActivity(intent);
				}
				finish();
			} else {
				downloadAttachment();
			}
		} catch (Exception e) {
			e.printStackTrace();
			downloadAttachment();
		}
	}

	@Override
	public void initSubviews() {
		mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	public void initData() {

	}

	@Override
	public void onClickListener(View v) {

	}

	/**
	 * 下载附件
	 */
	private void downloadAttachment() {
		String fileURL = fileAttachment.getUrl();
		final File file = new File(FileUtils.getFilePath(fileAttachment.getDisplayName()));
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new HttpUtils().download(fileURL, file.getAbsolutePath(), true, false, new RequestCallBack<File>() {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				int percentage = (int) ((double) current / (double) total * 100);
				mProgressBar.setProgress(percentage);
				String result = percentage + "%";
//				mTvDownloadPercentage.setText("100%".equals(result) ? "下载完成" : result);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				try {// 保存到本地数据库并未实体id赋值
					mDbUtils.saveBindingId(mIMMessage);
				} catch (DbException e) {
					e.printStackTrace();
				}
				Toast.makeText(MessageDownloadFileActivity.this, "正在打开...", Toast.LENGTH_SHORT).show();
				Intent intent = OpenFileUtils.open(MessageDownloadFileActivity.this, arg0.result.getAbsolutePath());
				if (intent != null) {
					startActivity(intent);
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(MessageDownloadFileActivity.this, "下载失败:"+arg1, Toast.LENGTH_SHORT).show();
				System.out.println("error-->"+arg1);
			}
		});
		
		
//		new HttpUtils().download(fileURL, file.getAbsolutePath(), true, false, new RequestCallBack<File>() {
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//				super.onLoading(total, current, isUploading);
//				int percentage = (int) ((double) current / (double) total * 100);
//				mProgressBar.setProgress(percentage);
//				// String result = percentage + "%";
//				// mTvDownloadPercentage.setText("100%".equals(result) ? "下载完成"
//				// : result);
//			}
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				file.delete();
//				Toast.makeText(MessageDownloadFileActivity.this, "下载失败...", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<File> arg0) {
//				try {
//					mDbUtils.save(mIMMessage);
//				} catch (DbException e) {
//					e.printStackTrace();
//				}
//				Toast.makeText(MessageDownloadFileActivity.this, "正在打开...", Toast.LENGTH_SHORT).show();
//				Intent intent = OpenFileUtils.open(MessageDownloadFileActivity.this, arg0.result.getAbsolutePath());
//				if (intent != null) {
//					startActivity(intent);
//				}
//				finish();
//			}
//		});
	}
}
