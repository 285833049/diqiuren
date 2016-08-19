package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.CountryPhoneItem;
import com.earthman.app.db.PhoneDBManager;
import com.earthman.app.widget.sortListView.CharacterParser;
import com.earthman.app.widget.sortListView.PinyinComparator;
import com.earthman.app.widget.sortListView.SortAdapter;
import com.earthman.app.widget.sortListView.SortModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: CountryChoiseActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月8日
 */
@ContentView(R.layout.activity_choise_country)
public class CountryChoiseActivity extends BaseActivity {
	@ViewInject(R.id.country_list)
	ListView mLvCountry;
	@ViewInject(R.id.tv_title)
	TextView mTvTitleCenter;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;

	@Override
	protected void handclick(View v) {
		if (v.getId()==R.id.btn_back) {
			finish();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		try {

			PhoneDBManager phoneDBManager = new PhoneDBManager(this);
			countryPhones = phoneDBManager.getCountryPhones();
			// ArrayList<String> countrysList = new ArrayList<String>();
			//
			// for (CountryPhoneItem countryPhoneItem : countryPhones) {
			// countrysList.add(countryPhoneItem.getCountrycode());
			// }
			// InputStream file = getAssets().open("country_name.json");
			// byte[] data = new byte[file.available()];
			// file.read(data);
			// file.close();
			// String jsoncountry = new String(data);
			// Map<String, Object> mapForJson =
			// JsonUtils.getMapForJson(jsoncountry);
			//
			// countrysList.toArray(countrys);
			// LogUtil.i("country", countrys.toString()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mTvTitleCenter.setText(getString(R.string.choise_country));
		mBtnTitleLeft.setOnClickListener(this);
		initViews();
	}

	@Override
	protected void setAttribute() {

	}

	private SortAdapter adapter;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private ArrayList<CountryPhoneItem> countryPhones;

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		// sideBar = (SideBar) findViewById(R.id.sidrbar);
		// dialog = (TextView) findViewById(R.id.dialog);
		// sideBar.setTextView(dialog);

		// // 设置右侧触摸监听
		// sideBar.setOnTouchingLetterChangedListener(new
		// OnTouchingLetterChangedListener() {
		//
		// @Override
		// public void onTouchingLetterChanged(String s) {
		// // 该字母首次出现的位置
		// int position = adapter.getPositionForSection(s.charAt(0));
		// if (position != -1) {
		// country_list.setSelection(position);
		// }
		//
		// }
		// });

		mLvCountry.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Intent intent = new Intent();
				intent.putExtra("phonecode", countryPhones.get(position).getCountrycode() + countryPhones.get(position).getPhonecode());
				setResult(3, intent);
				finish();
			}
		});

		SourceDateList = filledData(countryPhones);

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList, 2);
		mLvCountry.setAdapter(adapter);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private List<SortModel> filledData(ArrayList<CountryPhoneItem> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getCountrycode());
			sortModel.setPahonecode(date.get(i).getPhonecode());

			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date.get(i).getCountrycode());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	@SuppressWarnings("unused")
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
