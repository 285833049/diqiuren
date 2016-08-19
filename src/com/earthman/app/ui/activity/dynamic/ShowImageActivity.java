package com.earthman.app.ui.activity.dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.widget.selectphoto.ImageLoader;
import com.earthman.app.widget.selectphoto.ImageLoader.Type;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月29日 下午3:30:40
 * @Decription
 */
@ContentView(R.layout.showimageactivity)
public class ShowImageActivity extends Activity {
	@ViewInject(R.id.iv)
	ImageView iv;
	@ViewInject (R.id.delete)
	TextView delete;

	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		if (getIntent().getExtras()!=null) {
			ImageLoader.getInstance(3, Type.LIFO).loadImage(getIntent().getExtras().getString("imagestr"), iv);
		}
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
	}
	
}
