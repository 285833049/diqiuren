package com.earthman.app.ui.activity.news;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.ui.fragment.news.CompanyNewsFragment;
import com.earthman.app.ui.fragment.news.EarthNewsFragment;
import com.earthman.app.ui.fragment.news.WorldNewsFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-28 上午11:40:32
 * @Decription 企业新闻
 */
@ContentView(R.layout.news_activity)
public class NewsActivity extends BaseActivity {

	@ViewInject(android.R.id.tabhost)
	private FragmentTabHost mTabHost;
	private FragmentManager fragmentManager;
	private String[] tabNames;// Tab文字
	private Class<?>[] tabContents;// Tab内容
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	
	private int mCurrTab;

	@Override
	protected void initData() {
		fragmentManager = getSupportFragmentManager();
		tabNames = getResources().getStringArray(R.array.news_tabnames);
		tabContents = new Class[] { CompanyNewsFragment.class, EarthNewsFragment.class, WorldNewsFragment.class };
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTabHost.setup(this, fragmentManager, R.id.news_content);
		// 初始化标签
		for (int i = 0; i < tabNames.length; i++) {
			TabSpec mTabSpect = mTabHost.newTabSpec(tabNames[i]).setIndicator(getTabView(tabNames[i]));
			mTabHost.addTab(mTabSpect, tabContents[i], null);
		}
		mTabHost.setCurrentTab(mCurrTab=getIntent().getIntExtra("index", 0));
	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.news_dynamic);
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	private View getTabView(String tabName) {
		View view = LayoutInflater.from(this).inflate(R.layout.news_tab_item, null, false);
		TextView tvTabName = (TextView) view.findViewById(R.id.tv_news_tab);
		tvTabName.setText(tabName);
		return view;
	}

	@Override
	protected void handclick(View v) {
		
	}

}
