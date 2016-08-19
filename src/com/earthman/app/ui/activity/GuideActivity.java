package com.earthman.app.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.earthman.app.R;
import com.earthman.app.ui.activity.login.LoginActivity;

/**
 * 欢迎页面图片滚动
 * 
 * @author Administrator
 * 
 */
public class GuideActivity extends Activity {

	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		initView();
	}

	private void initView() {

		List<View> lists = new ArrayList<View>();
		ImageView imageView1 = new ImageView(this);
		imageView1.setBackgroundResource(R.drawable.guide_1);

		ImageView imageView2 = new ImageView(this);
		imageView2.setBackgroundResource(R.drawable.guide_2);

		ImageView imageView3 = new ImageView(this);
		imageView3.setBackgroundResource(R.drawable.guide_3);

		lists.add(imageView1);
		lists.add(imageView2);
		lists.add(imageView3);

		ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter(lists);
		view_pager.setAdapter(adapter);

		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this, LoginActivity.class));
				finish();
			}
		});
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0 == 2) {
					button.setVisibility(View.VISIBLE);
				} else {
					button.setVisibility(View.GONE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> pages;

		public ViewPagerAdapter(List<View> lists) {
			this.pages = lists;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(pages.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(pages.get(position), 0);
			return pages.get(position);
		}

		@Override
		public int getCount() {
			return pages.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
