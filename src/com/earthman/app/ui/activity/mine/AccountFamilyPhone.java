package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyFamilyAdapter;
import com.earthman.app.adapter.RelativeGrideAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.FamilyListResponse;
import com.earthman.app.bean.FamilyListResponse.FamilyResult.MyFamily;
import com.earthman.app.bean.RelativeItem;
import com.earthman.app.constant.Constent;
import com.earthman.app.eventbusbean.RegisterBean;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyGridView;
import com.earthman.app.widget.RoundCornerImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * @Title: PhonesFamilyActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月13日
 */
@ContentView(R.layout.family_phones)
public class AccountFamilyPhone extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.title_back_iv)
	ImageView mIvTitleLeft;
	// -------------亲情号-----------
	@ViewInject(R.id.lv_familys)
	ListView mLvFamilyPhone;
	@ViewInject(R.id.relative_grideview)
	MyGridView mGvRelative;
	@ViewInject(R.id.title_layout)
	RelativeLayout mRlTitle;
	private ArrayList<RelativeItem> mItemsData;
	@ViewInject(R.id.user_icon)
	RoundCornerImageView mUserIcon;
	@ViewInject(R.id.lin_1)
	View VerticalLine;
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.title_back_iv:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		String[] names = getResources().getStringArray(R.array.relative_friend);
		int lins[] = { R.drawable.dashed_shape1, R.drawable.dashed_shape2, R.drawable.dashed_shape3, R.drawable.dashed_shape4, R.drawable.dashed_shape5,
				R.drawable.dashed_shape6 };
		int tvcolors[] = { R.color.relative_tips1, R.color.relative_tips2, R.color.relative_tips3, R.color.relative_tips4, R.color.relative_tips5,
				R.color.relative_tips6 };
		int adds[] = { R.drawable.addy, R.drawable.addg, R.drawable.addr, R.drawable.addo, R.drawable.addp, R.drawable.addc };
		ArrayList<RelativeItem> data = new ArrayList<RelativeItem>();
		mItemsData = getData(names, adds, lins, data, tvcolors);
	}

	/**
	 * initData(初始化数据)
	 * void
	 * @exception
	 */
	private ArrayList<RelativeItem> getData(String[] names, int[] adds, int[] lins, ArrayList<RelativeItem> data, int[] tvcolors) {
		for (int i = 0; i < names.length; i++) {
			RelativeItem relativeItem = new RelativeItem(names[i], adds[i], lins[i], tvcolors[i]);
			data.add(relativeItem);
		}
		return data;
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mRlTitle.getBackground().setAlpha(10);
		mIvTitleLeft.setOnClickListener(this);
		mUserIcon.setOnClickListener(this);
		mTvTitleLeft.setText(R.string.myaccount_qinqing);
		mFamilyAdapter = new MyFamilyAdapter(this, myFamilies);
		mRelativeGrideAdapter = new RelativeGrideAdapter(this, mItemsData);
		// --------设置数据集------------
		mGvRelative.setAdapter(mRelativeGrideAdapter);
		mLvFamilyPhone.setAdapter(mFamilyAdapter);
		mGvRelative.setOnItemClickListener(myGVItemListener);
		new YwbImageLoader().loadImage(preferences.getUserIco(), mUserIcon,R.drawable.user_avatar);
		doGetFamilyList();// 获取亲情号列表
		EventBus.getDefault().register(this);
	}

	/**
	 * doGetFamilyList(获取亲友列表)/{userid}/{token}
	 * void
	 * @exception
	 */
	private void doGetFamilyList() {
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.FAMILY_LIST, list);
		executeRequest(new FastJsonRequest<FamilyListResponse>(Method.GET, loginUrl, FamilyListResponse.class, new Response.Listener<FamilyListResponse>() {
			@Override
			public void onResponse(FamilyListResponse response) {
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				if ("000000".equals(response.getCode())) {
					if (response.getResult().getMyFamily().size() > 0) {
						VerticalLine.setVisibility(View.VISIBLE);
					} else {
						VerticalLine.setVisibility(View.GONE);
					}
					myFamilies.clear();
					myFamilies.addAll(response.getResult().getMyFamily());
					mFamilyAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(AccountFamilyPhone.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void setAttribute() {
	}

	// gridview 监听
	private OnItemClickListener myGVItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (mItemsData.size() > 1) {
				if (position < mItemsData.size() - 1) {
					mRelativeItem = mItemsData.get(position);
					Intent intent = new Intent(AccountFamilyPhone.this, RegistActivity.class);
					intent.putExtra("type", Constent.REGIST_TO_FAMILY);
					intent.putExtra("relative", mRelativeItem.getName());
					startActivityForResult(intent, RESULT_OK);
				}
				if (position == mItemsData.size() - 1) {
					startActivity(new Intent(AccountFamilyPhone.this, AccountFamilyRelativeChoise.class));
				}
			}
		}
	};
	private RelativeItem mRelativeItem;
	private RelativeGrideAdapter mRelativeGrideAdapter;
	private MyFamilyAdapter mFamilyAdapter;
	ArrayList<MyFamily> myFamilies = new ArrayList<MyFamily>();

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == RESULT_OK) {
			mItemsData.remove(mRelativeItem);
			mRelativeGrideAdapter.notifyDataSetChanged();
			doGetFamilyList();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	};
	
	
	public void onEventMainThread(RegisterBean bean){
		if(bean.getType() == Constent.REGIST_TO_FAMILY){
			doGetFamilyList();
		}
	}
	
	
}
