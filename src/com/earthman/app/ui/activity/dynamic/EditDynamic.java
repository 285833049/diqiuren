package com.earthman.app.ui.activity.dynamic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.LocationPreference;
import com.earthman.app.enums.MaxSelectPhotoType;
import com.earthman.app.eventbusbean.ChooseResult;
import com.earthman.app.eventbusbean.OKDynamicSend;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.UpLoadFileUtils;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.selectphoto.SelectPhotoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @param <E>
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月26日 下午3:26:27
 * @Decription
 */
@ContentView(R.layout.dynamic_edit)
public class EditDynamic extends BaseActivity {
	@ViewInject(R.id.btn_back)
	Button back;
	@ViewInject(R.id.tv_left)
	TextView tv_left;
	@ViewInject(R.id.tv_dynamic_size)
	TextView tv_dynamic_size;
	@ViewInject(R.id.btn_right)
	Button btn_right;
	@ViewInject(R.id.dynamic_et)
	EditText dynamic_et;
	@ViewInject(R.id.grideview_img)
	GridView grideview_img;
	private GridAdapter gridAdapter;
	private int width_grid;
	private final int DELETEPIC = 0x002;
	private int clickposition;
	private int type;
	@ViewInject(R.id.tv_current_location)
	private TextView tv_current_location;
    private ChooseResult result;
    
    private YwbImageLoader mImageLoader;
	/*
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			// 发送
			String dynamicContentStr = dynamic_et.getText().toString().trim();
			if (TextUtils.isEmpty(dynamicContentStr)) {
				MyToast.makeText(this, R.string.content_cannot_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			doPostSendDynamic(dynamicContentStr);
			break;
		case R.id.tv_current_location:
			Intent intent = new Intent(EditDynamic.this, LocationChoiceActivity.class);		
			if(result == null){
				intent.putExtra("result", new ChooseResult(1, null, null, null));			
			}else{
				intent.putExtra("result", result);
			}
			startActivity(intent);			
			break;
		default:
			break;
		}
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initData()
	 * 2---->>文字发表，1---->>图片发表
	 */
	@Override
	protected void initData() {
		mImageLoader=new YwbImageLoader();
		width_grid = (int) ((AndroidUtils.getDeviceWidth(this)));
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		tv_current_location.setOnClickListener(this);
		tv_dynamic_size.setText("250/250");
		initMygride();
		dynamic_et.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;// 监听前的文本
			private final int charMaxNum = 250;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				temp = s;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_dynamic_size.setText((charMaxNum - s.length()) + "/250");
				// edittext_num.setText(Html.fromHtml(String.format(getString(R.string.death_jiyu),
				// Constants.MAX_EDITTEXT_NUM,(Constants.MAX_EDITTEXT_NUM-temp.length()))));
			}

			@Override
			public void afterTextChanged(Editable s) {
				/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
				if (temp.length() > charMaxNum) {
					Toast.makeText(getApplicationContext(), getString(R.string.huafei_tips), Toast.LENGTH_LONG).show();
				}
			}
		});
		tv_left.setText(R.string.dynamic_2);
		btn_right.setText(R.string.send);
		EventBus.getDefault().register(this);
	}

	/**
	 * doPostSendDynamic发布动态到服务器
	 * void
	 * @exception
	 */
	private void doPostSendDynamic(String dynamicContentStr) {
		showLoading();
		String userid = preferences.getUserID();
		String token = preferences.getUserToken();
		/*ArrayList<File> files = new ArrayList<File>();
		if (type == 1) {			
			for (int i = 0; i < piclist.size(); i++) {
				files.add(new File(piclist.get(i)));
			} // 上传图片文件发布动态			
		}*/
		ArrayList<String> files = new ArrayList<String>();
		if (type == 1) {			
			for (int i = 0; i < piclist.size(); i++) {
				files.add(piclist.get(i));
			} // 上传图片文件发布动态			
		}
		if(result == null || result.getChooseType() == 1){//不显示位置
			UpLoadFileUtils.doUploadfiles(EditDynamic.this, files, userid, token, dynamicContentStr, mHandler, 0,0, null);
		}else if(result.getChooseType() == 2){//显示当前城市
			UpLoadFileUtils.doUploadfiles(EditDynamic.this, files, userid, token, dynamicContentStr, mHandler, 0,0, result.getCityName());
		}else if(result.getChooseType() == 3){//显示当前的具体位置
			UpLoadFileUtils.doUploadfiles(EditDynamic.this, files, userid, token, dynamicContentStr, mHandler, result.getLongitude(), result.getLatitude(), new LocationPreference(this).getCityName() + "." + result.getTitle());
		}
	    		
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x99:
				dismissLoading();
				EventBus.getDefault().post(new OKDynamicSend(1));
				finish();
				MyToast.makeText(EditDynamic.this, R.string.zuploa_pic_tips, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * initMygride(初始化gridview)
	 * void
	 * @exception
	 */
	private void initMygride() {
		if (getIntent().getExtras() != null) {
			type = (Integer) getIntent().getExtras().get("type");
			if (type == 1) {
				grideview_img.setVisibility(View.VISIBLE);
				Intent intent =SelectPhotoActivity.getIntent(EditDynamic.this, MaxSelectPhotoType.BaseNum, 0);
				startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE);
			} else if (type == 2) {
				grideview_img.setVisibility(View.GONE);
			}
		}
		gridAdapter = new GridAdapter();
		grideview_img.setAdapter(gridAdapter);
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	List<String> piclist = new ArrayList<String>();
	private String[] bigImages;

	/*
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		if (Activity.RESULT_OK == result) {
			switch (request) {
			case SelectPhotoActivity.REQUEST_CODE:
				if (data != null) {
					bigImages = (String[]) data.getExtras().get("Bigimage");
					if (piclist.size() + bigImages.length > 9) {
						MyToast.makeText(this, getString(R.string.choise_max_tips), Toast.LENGTH_SHORT).show();
						return;
					}
					piclist.addAll(piclist.size(), Arrays.asList(bigImages));
					gridAdapter.notifyDataSetChanged();
				}

				break;
			case DELETEPIC:
				piclist.remove(clickposition);
				gridAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	// 适配器
	public class GridAdapter extends BaseAdapter {
		
		public int getCount() {
			return piclist.size() + 1;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			LogUtil.i(TAG, "times----------->" + position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(EditDynamic.this).inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width_grid / 4 - 50, width_grid / 4 - 50);
				holder.image.setLayoutParams(layoutParams);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (parent.getChildCount() == position) {
				if (position == piclist.size()) {// 添加末尾
					holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
					holder.image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=SelectPhotoActivity.getIntent(EditDynamic.this, MaxSelectPhotoType.BaseNum, piclist.size());
							//Intent intent = new Intent(EditDynamic.this, SelectPhotoActivity.class);
							startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE);
						}
					});
					holder.image.setVisibility(piclist.size() == 9 ? View.GONE : View.VISIBLE);
				} else {// 添加图片
					holder.image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							clickposition = position;
							Intent intent = new Intent(EditDynamic.this, ShowImageActivity.class);
							intent.putExtra("imagestr", piclist.get(position));
							startActivityForResult(intent, DELETEPIC);
						}
					});
					mImageLoader.loadImage("file://"+piclist.get(position), holder.image);
					//ImageLoader.getInstance(3, Type.LIFO).loadImage(piclist.get(position), holder.image);
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}
	
	
	public void onEventMainThread(ChooseResult result){
		this.result = result;
		switch (result.getChooseType()) {
		case 1:
			tv_current_location.setText(R.string.current_location);
			break;
		case 2:
			tv_current_location.setText(result.getCityName());
			break;
		case 3:
			tv_current_location.setText(new LocationPreference(this).getCityName() + "." + result.getTitle());
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	
	

}
