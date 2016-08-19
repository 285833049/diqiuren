package com.earthman.app.ui.activity.dynamic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.eventbusbean.PhotoSaveBean;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.album.AlbumListActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.FileUtils;
import com.earthman.app.utils.ImageUtils;
import com.earthman.app.widget.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月25日 下午4:14:31
 * @Decription
 */
public class ImageViewPage extends Activity implements OnClickListener {
	private static final String TAG="ImageViewPage";
	private YwbImageLoader imageLoader;
	private ArrayList<String> imageList;
	private int position;
	private ViewPager imageviewpage;
	private Button mBtnImgDownload;
	private String currImgURL;
	private int currIndex;
	private Dialog dialog;
	private Button btn_back;
	private Button btn_right;
	private int currentPosition;
	private Matrix matrix;
	private Bitmap bitmap;
	private int myWidth;
	private int myHeight;
	private Bitmap rotatedBitmap;
	private ImageUtils mImageutil;
	private int from;
	private RelativeLayout title;

	private TextView mTvCurrentTotal;//当前图片/总图片数量
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpageimage);
		if (getIntent().getExtras() != null) {
			String pics = (String) getIntent().getExtras().get("pics");
			position = (Integer) getIntent().getExtras().get("position");
			imageList = new ArrayList<String>(Arrays.asList(pics.split(",")));
			from = getIntent().getExtras().getInt("currentPage");
		}
		imageLoader = new YwbImageLoader();
		imageviewpage = (ViewPager) findViewById(R.id.image_view_page);
		imageviewpage.setAdapter(myImgAdapter);
		imageviewpage.setCurrentItem(position);
		currentPosition = position;
		imageviewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPosition = arg0;
				mTvCurrentTotal.setText(currentPosition+1+" / "+imageList.size());				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mBtnImgDownload = (Button) findViewById(R.id.btn_img_download);
		mBtnImgDownload.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_right.setText(R.string.rotate);
		btn_back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		title = (RelativeLayout) findViewById(R.id.title);
		if(from != AlbumListActivity.SEE_MINE_ALBUM){
			title.setVisibility(View.GONE);
		}
		
		mTvCurrentTotal=(TextView) findViewById(R.id.tv_current_total);
		mTvCurrentTotal.setText(currentPosition+1+" / "+imageList.size());
	}

	// viewpage_image_adapter
	private ViewPagerAdapter myImgAdapter = new ViewPagerAdapter();

	private class ViewPagerAdapter extends PagerAdapter{
		private  View view;
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}



		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			view = (View) object;
			
			super.setPrimaryItem(container, position, object);
		}


		public View getPrimaryItem(){
			return view;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			final PhotoView view = new PhotoView(ImageViewPage.this);
			view.enable();
			imageLoader.loadImage(imageList.get(position), view, R.drawable.pictures_no);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			view.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					Toast.makeText(ImageViewPage.this, "long click", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
			container.addView(view);
			view.setId(position);
			return view;
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_img_download:
			if(mImageutil==null){
				mImageutil = new ImageUtils();
			}
			mImageutil.saveNetworkImg(imageList.get(currentPosition));
				
			try {
				Log.d("ImageUtilsxianyong", "xianyong:" + imageList.get(currentPosition));
			} catch (Exception e) {
				Log.d("ImageUtilsxianyong", "xianyong1:" + e.getMessage());
			}

			// new ImageUtils().saveNetworkImg(imageList.get(currIndex ==
			// imageList.size() - 1 ? currIndex : currIndex - 1));//
			// 因ViewPager有自动加载下级图片操作所以，这边当前下表需要单独处理
			break;
		case R.id.btn_cancel://取消
			dialog.dismiss();
			break;
		case R.id.btn_anti_rotate://逆时针旋转
			if(bitmap == null){
				return;
			}
			rotatedBitmap = rotaingImageView(-90);
			break;
		case R.id.btn_rotate://顺时针旋转
			if(bitmap == null){
				return;
			}
			rotatedBitmap = rotaingImageView(90);
			break;
		case R.id.btn_save://保存
			if(rotatedBitmap != null){
                 new Thread(){

					@Override
					public void run() {						
						super.run();
						String fileName = System.currentTimeMillis() + ".jpg";
						String path = FileUtils.getPubImagePath() +"/" + fileName;
						saveFile(rotatedBitmap, path);
						EventBus.getDefault().post(new PhotoSaveBean(currentPosition, path));
						dialog.dismiss();
						finish();
					}
                	 
                 }.start();
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			showRotatePop();
			break;
		default:
			break;
		}
	}
	
	/** 
	 * 保存文件 
	 * @param bm 
	 * @param fileName 
	 * @throws IOException 
	 */  
	public void saveFile(Bitmap bm, String path) { 
		try {     
			File myCaptureFile = new File(path);  
			if(!myCaptureFile.exists()){  
				myCaptureFile.createNewFile();
			}  
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
			bos.flush();  
			bos.close();  
			FileUtils.scanFileAsync(ImageViewPage.this,path);//通知相册显示
		} catch (Exception e) {
		}
		
	}
	
	public void showRotatePop(){
		dialog = new Dialog(this, R.style.upomp_bypay_MyDialog);
		View view = LayoutInflater.from(this).inflate(R.layout.photo_rotate, null);		
		dialog.setContentView(view);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btn_anti_rotate = (Button) view.findViewById(R.id.btn_anti_rotate);
		Button btn_rotate = (Button) view.findViewById(R.id.btn_rotate);
		Button btn_save = (Button) view.findViewById(R.id.btn_save);		
		btn_cancel.setOnClickListener(this);
		btn_rotate.setOnClickListener(this);
		btn_anti_rotate.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		android.view.WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.width = (int) AndroidUtils.getDeviceWidth(this);
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		matrix = new Matrix();
		new Thread(){

			@Override
			public void run() {
				super.run();
				bitmap = getNetBitmap(imageList.get(currentPosition));
				myWidth = bitmap.getWidth();
				myHeight = bitmap.getHeight();
			}

		}.start();					
		dialog.show();

	}

	private Bitmap rotaingImageView(int angle2) {  		
		//旋转图片 动作  
		matrix.postRotate(angle2);  
		System.out.println("angle2=" + angle2);  
		// 创建新的图片  
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
				myWidth, myHeight, matrix, true);  
		//Drawable drawable = ImageDispose.bitmapToDrawable(resizedBitmap);  
		((PhotoView)myImgAdapter.getPrimaryItem()).setImageBitmap(resizedBitmap);
		//((PhotoView)imageviewpage.findViewById(currentPosition)).setImageBitmap(resizedBitmap);
		//imageView.setBackgroundDrawable(drawable);        
		return resizedBitmap;  
	}

	public static Bitmap getNetBitmap(String url)  
	{  
		Bitmap bitmap = null;  
		InputStream in = null;  
		BufferedOutputStream out = null; 
		int buffersize = 2*1024;
		try  
		{  
			in = new BufferedInputStream(new URL(url).openStream(), buffersize);  
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();  
			out = new BufferedOutputStream(dataStream, buffersize);  
			byte[] b = new byte[buffersize];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			} 
			out.flush();  
			byte[] data = dataStream.toByteArray();  
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  
			data = null;  
			return bitmap;  
		}  
		catch (IOException e)  
		{  
			e.printStackTrace();  
			return null;  
		}  
	}  

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageLoader.getInstance().clearMemoryCache();
	}
}
