//package com.earthman.app.utils.choisepic;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//
//import com.earthman.app.R;
//import com.earthman.app.utils.choisepic.ListImageDirPopupWindow.OnImageDirSelected;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ContentView;
//import com.lidroid.xutils.view.annotation.ViewInject;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow.OnDismissListener;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * @Author：ERIC
// * @Version：地球人
// * @Date：2016年3月28日 下午5:38:53
// * @Decription
// */
//@ContentView(R.layout.activity_all_pictures)
//public class AllPicturesActivity extends Activity implements OnClickListener, OnImageDirSelected {
//	@ViewInject(R.id.btn_back)
//	Button btn_back;
//	@ViewInject(R.id.tv_left)
//	TextView tv_left;
//	@ViewInject(R.id.btn_right)
//	Button btn_ok;
//	@ViewInject(R.id.id_gridView)
//	GridView mGirdView;
//	@ViewInject(R.id.id_bottom_ly)
//	RelativeLayout bottom_ly;
//	@ViewInject(R.id.id_total_count)
//	TextView mImageCount;
//	@ViewInject(R.id.id_choose_dir)
//	TextView mChooseDir;
//	private ProgressDialog mProgressDialog;
//
//	/**
//	 * 存储文件夹中的图片数量
//	 */
//	private int mPicsSize;
//	/**
//	 * 图片数量最多的文件夹
//	 */
//	private File mImgDir;
//	/**
//	 * 所有的图片
//	 */
//	private List<String> mImgs;
//
//	private MyAdapter mAdapter;
//	/**
//	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
//	 */
//	private HashSet<String> mDirPaths = new HashSet<String>();
//
//	/**
//	 * 扫描拿到所有的图片文件夹
//	 */
//	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
//
//
//	int totalCount = 0;
//
//	private int mScreenHeight;
//
//	/*
//	 * @see android.app.Activity#onCreate(android.os.Bundle)
//	 */
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		initView();
//		getImages();
//	}
//	/**
//	 * 初始化展示文件夹的popupWindw
//	 */
//	private void initListDirPopupWindw() {
//		mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7), mImageFloders,
//				LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir, null));
//
//		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				// 设置背景颜色变暗
//				WindowManager.LayoutParams lp = getWindow().getAttributes();
//				lp.alpha = 1.0f;
//				getWindow().setAttributes(lp);
//			}
//		});
//		// 设置选择文件夹的回调
//		mListImageDirPopupWindow.setOnImageDirSelected(this);
//	}
//
//	/**
//	 * InitView(初始化界面)
//	 * void
//	 * @exception
//	 */
//	private void initView() {
//		ViewUtils.inject(this);
//		btn_back.setOnClickListener(this);// 返回finish
//		bottom_ly.setOnClickListener(this);// 弹出底部的 文件选择
//
//	}
//
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			mProgressDialog.dismiss();
//			// 为View绑定数据
//			data2View();
//			// 初始化展示文件夹的popupWindw
//			initListDirPopupWindw();
//		}
//	};
//
//	/**
//	 * 为View绑定数据
//	 */
//	private void data2View() {
//		if (mImgDir == null) {
//			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		mImgs = Arrays.asList(mImgDir.list());
//		/**
//		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
//		 */
//		mAdapter = new MyAdapter(getApplicationContext(), mImgs, R.layout.grid_item_1, mImgDir.getAbsolutePath());
//		mGirdView.setAdapter(mAdapter);
//		mImageCount.setText(totalCount + "张");
//	};
//
//	private ListImageDirPopupWindow mListImageDirPopupWindow;
//
//	/*
//	 * 处理点击事件
//	 */
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.id_bottom_ly:// 弹出底部的 文件选择
//			showpop();
//			break;
//		case R.id.btn_back:// 返回
//			finish();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * showpop(这里用一句话描述这个方法的作用)
//	 * void
//	 * @exception
//	 */
//	private void showpop() {
//		mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
//		mListImageDirPopupWindow.showAsDropDown(bottom_ly, 0, 0);
//		// 设置背景颜色变暗
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.alpha = .3f;
//		getWindow().setAttributes(lp);
//	}
//
//	/* 
//	 * @see com.earthman.app.utils.choisepic.ListImageDirPopupWindow.OnImageDirSelected#selected(com.earthman.app.utils.choisepic.ImageFloder)
//	 */
//	@Override
//	public void selected(ImageFloder floder) {
//		mImgDir = new File(floder.getDir());
//		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
//		{
//			@Override
//			public boolean accept(File dir, String filename)
//			{
//				if (filename.endsWith(".jpg") || filename.endsWith(".png")
//						|| filename.endsWith(".jpeg"))
//					return true;
//				return false;
//			}
//		}));
//		/**
//		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
//		 */
//		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
//				R.layout.grid_item_2, mImgDir.getAbsolutePath());
//		mGirdView.setAdapter(mAdapter);
//		// mAdapter.notifyDataSetChanged();
//		mImageCount.setText(floder.getCount() + "张");
//		mChooseDir.setText(floder.getName());
//		mListImageDirPopupWindow.dismiss();
//	}
//	/**
//	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
//	 */
//	private void getImages()
//	{
//		if (!Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED))
//		{
//			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		// 显示进度条
//		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
//
//		new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//
//				String firstImage = null;
//
//				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//				ContentResolver mContentResolver = AllPicturesActivity.this
//						.getContentResolver();
//
//				// 只查询jpeg和png的图片
//				Cursor mCursor = mContentResolver.query(mImageUri, null,
//						MediaStore.Images.Media.MIME_TYPE + "=? or "
//								+ MediaStore.Images.Media.MIME_TYPE + "=?",
//						new String[] { "image/jpeg", "image/png" },
//						MediaStore.Images.Media.DATE_MODIFIED);
//
//				Log.e("TAG", mCursor.getCount() + "");
//				while (mCursor.moveToNext())
//				{
//					// 获取图片的路径
//					String path = mCursor.getString(mCursor
//							.getColumnIndex(MediaStore.Images.Media.DATA));
//
//					Log.e("TAG", path);
//					// 拿到第一张图片的路径
//					if (firstImage == null)
//						firstImage = path;
//					// 获取该图片的父路径名
//					File parentFile = new File(path).getParentFile();
//					if (parentFile == null)
//						continue;
//					String dirPath = parentFile.getAbsolutePath();
//					ImageFloder imageFloder = null;
//					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
//					if (mDirPaths.contains(dirPath))
//					{
//						continue;
//					} else
//					{
//						mDirPaths.add(dirPath);
//						// 初始化imageFloder
//						imageFloder = new ImageFloder();
//						imageFloder.setDir(dirPath);
//						imageFloder.setFirstImagePath(path);
//					}
//
//					int picSize = parentFile.list(new FilenameFilter()
//					{
//						@Override
//						public boolean accept(File dir, String filename)
//						{
//							if (filename.endsWith(".jpg")
//									|| filename.endsWith(".png")
//									|| filename.endsWith(".jpeg"))
//								return true;
//							return false;
//						}
//					}).length;
//					totalCount += picSize;
//
//					imageFloder.setCount(picSize);
//					mImageFloders.add(imageFloder);
//
//					if (picSize > mPicsSize)
//					{
//						mPicsSize = picSize;
//						mImgDir = parentFile;
//					}
//				}
//				mCursor.close();
//
//				// 扫描完成，辅助的HashSet也就可以释放内存了
//				mDirPaths = null;
//
//				// 通知Handler扫描图片完成
//				mHandler.sendEmptyMessage(0x110);
//
//			}
//		}).start();
//
//	}
//}
