package com.earthman.app.ui.activity.login;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.RegionAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.AddrItem;
import com.earthman.app.db.AddrDBManager;
import com.earthman.app.ui.activity.mine.ModifyPersonalInfo;

/**
 * 作者：Zhou
 * 日期：2015-10-21 下午5:05:52
 * 版权：地球人
 * 描述：（）
 */
public class ProvinceCityCountryActivity extends BaseActivity implements OnClickListener {

	private TextView title_tv;
	private Button back_btn;
	private int pageType;
	public static final int PAGETYPE_PROVINCE = 1;// 省
	public static final int PAGETYPE_CITY = 2;// 市
	public static final int PAGETYPE_REGION = 3;// 区
	private String regionStr;
	private int parentId;
	private ListView lv_region;
	private RegionAdapter adapter;
	private ArrayList<AddrItem> list;
	private TextView tv_address;
	private String regionCodeStr;
	static int type;

	/*
	 * @see com.ywb.user.ui.BaseActivity#initData()
	 */
	@Override
	protected void initData() {

	}

	/*
	 * @see com.ywb.user.ui.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		setContentView(R.layout.provincecitycountry);
		title_tv = (TextView) findViewById(R.id.tv_title);
		back_btn = (Button) findViewById(R.id.btn_back);
		lv_region = (ListView) findViewById(R.id.lv_region);
		tv_address = (TextView) findViewById(R.id.tv_address);

	}
	/**
	 * intoActivity(这里用一句话描述这个方法的作用)
	 * @param context
	 * @param pageType
	 * @param parentId
	 * @param regionStr
	 * @param regionCodeStr
  	 * @param types 0：注册选择修改地址  1：个人详情修改地址
	 * void
	 * @exception
	 */
	public static void intoActivity(Context context, int pageType, int parentId, String regionStr, String regionCodeStr, int types) {
		type = types;
		Intent intent = new Intent(context, ProvinceCityCountryActivity.class);
		intent.putExtra("pageType", pageType);
		intent.putExtra("parentId", parentId);
		intent.putExtra("regionStr", regionStr);
		intent.putExtra("regionCodeStr", regionCodeStr);
		context.startActivity(intent);
	}

	/*
	 * @see com.ywb.user.ui.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		back_btn.setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			pageType = intent.getIntExtra("pageType", -1);
			parentId = intent.getIntExtra("parentId", -1);
			regionStr = intent.getStringExtra("regionStr");
			regionCodeStr = intent.getStringExtra("regionCodeStr");
		}

		switch (pageType) {
		case PAGETYPE_PROVINCE:
			title_tv.setText(R.string.choose_province);
			break;
		case PAGETYPE_CITY:
			title_tv.setText(R.string.choose_city);
			break;
		case PAGETYPE_REGION:
			title_tv.setText(R.string.choose_country);
			break;
		default:
			break;
		}
		if (!TextUtils.isEmpty(regionStr)) {
			tv_address.setVisibility(View.VISIBLE);
			tv_address.setText(regionStr);

		}
		list = new AddrDBManager(this).getAddrs(parentId);
		filterCity();
		adapter = new RegionAdapter(this, list);
		lv_region.setAdapter(adapter);
		lv_region.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int toPageType = 0;
				if (pageType == PAGETYPE_PROVINCE) {
					toPageType = PAGETYPE_CITY;
					ProvinceCityCountryActivity.intoActivity(ProvinceCityCountryActivity.this, toPageType, list.get(position).getAddrId(), list.get(position).getAddrName(),
							String.valueOf(list.get(position).getAddrCode()), type);
				} else if (pageType == PAGETYPE_CITY) {
					toPageType = PAGETYPE_REGION;
					if(list.get(position).getAddrId()==2198){//东莞
						String region = regionStr.replace("+", "") + " " + list.get(position).getAddrName();
						Intent intent = new Intent(ProvinceCityCountryActivity.this, RegistActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("region", region);
						intent.putExtra("regionCode", regionCodeStr + ";" + String.valueOf(list.get(position).getAddrCode())+";441900");
						startActivity(intent);
					}else{
						ProvinceCityCountryActivity.intoActivity(ProvinceCityCountryActivity.this, toPageType, list.get(position).getAddrId(), regionStr + " + "
								+ list.get(position).getAddrName(), regionCodeStr + ";" + String.valueOf(list.get(position).getAddrCode()), type);
					}
				} else if (pageType == PAGETYPE_REGION) {
					if (type == 1) {
						String address = regionStr.replace("+", "") + " " + list.get(position).getAddrName();
						Intent intentuser = new Intent(ProvinceCityCountryActivity.this, ModifyPersonalInfo.class);
						intentuser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentuser.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intentuser.putExtra("addressCode", list.get(position).getAddrCode());
						intentuser.putExtra("address", address);
						startActivity(intentuser);
					} else {
						String region = regionStr.replace("+", "") + " " + list.get(position).getAddrName();
						Intent intent = new Intent(ProvinceCityCountryActivity.this, RegistActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("region", region);
						intent.putExtra("regionCode", regionCodeStr + ";" + String.valueOf(list.get(position).getAddrCode()));
						startActivity(intent);
					}
				}
			}
		});
	}

	/**
	 * 
	 * filterCity(对城市过滤，过滤掉香港、澳门、台湾)
	 * void
	 * 
	 * @exception
	 */
	private void filterCity() {
		for (Iterator<AddrItem> iterator = list.iterator(); iterator.hasNext();) {
			AddrItem addrItem = iterator.next();
			if (getString(R.string.hongkong).equals(addrItem.getAddrName()) || getString(R.string.taiwan).equals(addrItem.getAddrName())
					|| getString(R.string.macau).equals(addrItem.getAddrName())) {
				iterator.remove();
			}
		}

	}

	/*
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}

	}

}
