package com.earthman.app.ui.activity.chongzhi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.SelectPaymentAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.PaymentInfo;
import com.earthman.app.utils.SharePreferenceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ChoiceZhiFuStyleActivity
 * @Description: 选择支付方式
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月4日
 */
@ContentView(R.layout.select_payment_activity)
public class SelectPaymentActivity extends BaseActivity implements OnItemClickListener {
	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvLeft;
	@ViewInject(R.id.btn_right)
	private Button mBtnRight;
	@ViewInject(R.id.lv_content)
	private ListView mLvContent;
	private List<PaymentInfo> mPaymentList;
	private SelectPaymentAdapter mAdapter;
	private boolean isRegister;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

	@Override
	protected void initData() {
		isRegister=getIntent().getBooleanExtra("isRegister", false);
		mPaymentList = new ArrayList<PaymentInfo>();
		TypedArray payTypeIcon;
		String[] payTypeName;
		if (isRegister) {
			payTypeIcon = getResources().obtainTypedArray(R.array.payment_icons);
			payTypeName = getResources().getStringArray(R.array.payment_names);
		} else {
			payTypeIcon = getResources().obtainTypedArray(R.array.not_payment_icons);
			payTypeName = getResources().getStringArray(R.array.not_payment_names);
		}
		for (int i = 0; i < payTypeName.length; i++) {
			mPaymentList.add(new PaymentInfo(payTypeIcon.getResourceId(i, 0), payTypeName[i]));
		}
		payTypeIcon.recycle();
	}

	@Override
	protected void initView() {
		SharePreferenceUtil.init(getApplicationContext());
		ViewUtils.inject(this);
		mBtnBack.setOnClickListener(this);
		mTvLeft.setText(getString(R.string.selectpayment));
		mBtnRight.setVisibility(View.GONE);
		mLvContent.setOnItemClickListener(this);
	}

	@Override
	protected void setAttribute() {
		mLvContent.setAdapter(mAdapter = new SelectPaymentAdapter(mPaymentList));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		mAdapter.setSelection(position);
		mAdapter.notifyDataSetChanged();
		Intent intent=new Intent();
		intent.putExtra("position", position);
		intent.putExtra("payName", mPaymentList.get(position).getBankName());
		intent.putExtra("payTypeNum", mPaymentList.size());
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

}
