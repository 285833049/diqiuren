package com.earthman.app.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 作者：Zhou
 * 日期：2015-10-12 下午1:07:34
 * 版权：地球人
 * 描述：（首页tab滑动切换适配器）
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter{

	private ArrayList<Fragment> fragments;
	
	private FragmentManager fm;
	
	/**
	 * 创建一个新的实例 TabFragmentPagerAdapter.
	 *
	 * @param fm
	 */
	public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	/* 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	/* 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
