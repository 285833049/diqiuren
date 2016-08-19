package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.utils.SharePreferenceUtil;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: MineChongZhiActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月3日
 */
@ContentView(R.layout.activity_chongzhi_mine)
public class AccountReCharge extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------充值------------
	@ViewInject(R.id.chongzhi_commit)
	Button mBtnSubmit;
	@ViewInject(R.id.chongzhi_no1)
	TextView mTvChageNum1;
	@ViewInject(R.id.chongzhi_no2)
	TextView mTvChageNum2;
	@ViewInject(R.id.chongzhi_no3)
	TextView mTvChageNum3;
	@ViewInject(R.id.chongzhi_no4)
	TextView mTvChageNum4;
	@ViewInject(R.id.chongzhi_no5)
	TextView mTvChageNum5;
	@ViewInject(R.id.chongzhi_no6)
	EditText mEtChageNum6;
	@ViewInject(R.id.chongzhi_tv_1)
	TextView mTvChagePayMoney;
	private int mMoneyChoiseindex;
	private ArrayList<TextView> mTvlist;
	String[] moneys = { "2", "10", "50", "100", "200", "0" };// 充值金额
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */

	@Override
	protected void handclick(View v) {

		switch (v.getId()) {
		case R.id.chongzhi_no1:
			switchtext(0);
			break;
		case R.id.chongzhi_no2:
			switchtext(1);
			break;

		case R.id.chongzhi_no3:
			switchtext(2);
			break;
		case R.id.chongzhi_no4:
			switchtext(3);
			break;
		case R.id.chongzhi_no5:
			switchtext(4);
			break;
		case R.id.chongzhi_no6:
			switchtext(5);
			break;
		// 提交充值信息
		case R.id.chongzhi_commit:
			// 弹出一个dialog
			if (mMoneyChoiseindex == 5) {
				money = mEtChageNum6.getText().toString().trim();
			} else {
				money = moneys[mMoneyChoiseindex];
			}
			SharePreferenceUtil.init(this);
			SharePreferenceUtil.putString("MONEY", money);
			showMyDialog();
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	private String money;

	/**
	 * 充值dialog
	 */
	private void showMyDialog() {

		PayPwdDialog payPwdDialog = new PayPwdDialog(this, money, new OnSurePayClickListener() {

			@Override
			public void onSurePayClick(int payType, String pwd) {
			}
		});
		payPwdDialog.setCanceledOnTouchOutside(false);
		payPwdDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleRight.setVisibility(View.GONE);
		mBtnSubmit.setOnClickListener(this);
		mBtnTitleLeft.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.chongzhi_title);

		mTvlist = new ArrayList<TextView>();
		mTvlist.add(mTvChageNum1);
		mTvlist.add(mTvChageNum2);
		mTvlist.add(mTvChageNum3);
		mTvlist.add(mTvChageNum4);
		mTvlist.add(mTvChageNum5);
		mTvlist.add(mEtChageNum6);

		mTvChageNum1.setOnClickListener(this);
		mTvChageNum2.setOnClickListener(this);
		mTvChageNum3.setOnClickListener(this);
		mTvChageNum4.setOnClickListener(this);
		mTvChageNum5.setOnClickListener(this);
		mEtChageNum6.setOnClickListener(this);
		mEtChageNum6.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mTvChagePayMoney.setText(Html.fromHtml(String.format(getResources().getString(R.string.chongzhi_totle), s)));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		switchtext(0);
	}

	/**
	 * 切换充值金额
	 * 
	 * @param index
	 */
	private void switchtext(int index) {
		mMoneyChoiseindex = index;
		for (int i = 0; i <= 5; i++) {
			TextView textView = mTvlist.get(i);
			if (index == i) {
				if (index == 5) {
					mEtChageNum6.setText("");
					mEtChageNum6.setCursorVisible(true);
				} else {
					mEtChageNum6.setCursorVisible(false);
				}
				textView.setTextColor(getResources().getColor(R.color.title_background));
				String maney = moneys[mMoneyChoiseindex];
				mTvChagePayMoney.setText(Html.fromHtml(String.format(getString(R.string.chongzhi_totle), maney)));
				textView.setBackgroundResource(R.drawable.text_shape_pressed);
			} else {
				mEtChageNum6.setText(getString(R.string.chongzhi_other_money));
				textView.setTextColor(getResources().getColor(R.color.black1));
				String maney = moneys[mMoneyChoiseindex];
				mTvChagePayMoney.setText(Html.fromHtml(String.format(getString(R.string.chongzhi_totle), maney)));
				textView.setBackgroundResource(R.drawable.text_shape_normal);

			}

		}
	}
}
