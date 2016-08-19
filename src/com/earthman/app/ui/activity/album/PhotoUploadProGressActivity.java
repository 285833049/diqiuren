package com.earthman.app.ui.activity.album;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.PhotoUploadAdapter;
import com.earthman.app.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.photo_upload_progress_activity)
public class PhotoUploadProGressActivity extends BaseActivity {

	public static final int REQUEST_CODE = 0x133;// 启动Activity时的requestCode
	private static final String TAG = "PhotoUploadProGressActivity";
	private static final int NOTIFYDATA_CHANGED = 0x01;
	// 标题栏控件
	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.btn_right)
	private Button mBtnRight;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;

	@ViewInject(R.id.lv_upload_progress_list)
	private ListView mLvUploadProgress;
	private PhotoUploadAdapter mAdapter;

	private Timer timer;// 记时器
	private ChangeListViewTask task;// 进度轮询任务

	public static void startActivity(Context context, int requestCode) {
		Intent intent = new Intent(context, PhotoUploadProGressActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mBtnRight.setVisibility(View.GONE);
		mTvLeft.setVisibility(View.GONE);
		mTvTitle.setText(R.string.photo_progress_title);
		//refreshMap2List();
		mAdapter = new PhotoUploadAdapter(PhotoUploadProGressActivity.this,PhotoListActivity.uploadState);
		mLvUploadProgress.setAdapter(mAdapter);

		timer = new Timer();// 初始化计时器
		setResult(Activity.RESULT_OK);
	}

	/*private void refreshMap2List() {
		if (mList != null) {
			mList.clear();
			mList.addAll(PhotoListActivity.uploadState.values());
		} else {
			mList = new ArrayList<PhotoUploadProgressItem>(
					PhotoListActivity.uploadState.values());
		}
		
	}*/

	// 定时刷新上传任务显示数据
	private class ChangeListViewTask extends TimerTask {
		@Override
		public void run() {
			//refreshMap2List();
			//Log.d(TAG, "" + PhotoListActivity.uploadState.size());
			mHandler.obtainMessage(NOTIFYDATA_CHANGED).sendToTarget();
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NOTIFYDATA_CHANGED:
				if (PhotoListActivity.uploadState.size() == 0) {
					setResult(Activity.RESULT_CANCELED);// 图片全部上传成功后,直接由上一层自己刷新图片
					finish();
				}
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onResume() {
		if (task == null) {
			task = new ChangeListViewTask();
			timer.schedule(task, 1000, 1000);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		super.onPause();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.btn_back)
	private void onBtnBackClick(View view) {
		this.finish();
	}

	@Override
	protected void handclick(View v) {
		// TODO Auto-generated method stub

	};
}
