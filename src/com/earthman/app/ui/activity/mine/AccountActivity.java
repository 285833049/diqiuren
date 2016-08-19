package com.earthman.app.ui.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Title: MyAccountActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月2日
 */
@ContentView(R.layout.activity_my_account)
public class AccountActivity extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------account list-------------------
	@ViewInject(R.id.account_info)
	RelativeLayout mRlAccountInfo;
	@ViewInject(R.id.card_list)
	RelativeLayout mRlAccountBcardList;
	@ViewInject(R.id.chongzhi_myaccount)
	RelativeLayout mRlAccountRechage;
	@ViewInject(R.id.personal_withdrawals_layout)
	RelativeLayout mRlAccountMoneyToBank;
	@ViewInject(R.id.account_change_phoen)
	RelativeLayout mRlAccountChangePhoenNum;
	@ViewInject(R.id.account_qinqing_phone)
	RelativeLayout mRlAccountFamilyAccount;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.account_info:
			startActivity(new Intent(this, AccountInfoActivity.class));// 个人账户信息
			break;
		case R.id.card_list:
			Toast.makeText(AccountActivity.this, R.string.develop_inform, Toast.LENGTH_SHORT).show();
			//startActivity(new Intent(this, AccountBankCardList.class));//
			// 银行卡列表信息
			break;
		case R.id.personal_withdrawals_layout:
			Toast.makeText(AccountActivity.this, R.string.develop_inform, Toast.LENGTH_SHORT).show();
			// startActivity(new Intent(this, AccountWithdraw.class));// 提现
			break;
		case R.id.chongzhi_myaccount:
			Toast.makeText(AccountActivity.this, R.string.develop_inform, Toast.LENGTH_SHORT).show();
			// startActivity(new Intent(this, AccountReCharge.class));// 充值地币
			break;
		case R.id.account_change_phoen:
			startActivity(new Intent(this, AccountChangePhoneStep1.class));// 更换电话号码
			break;
		case R.id.account_qinqing_phone:
			// startActivity(new Intent(this, AccountFamilyPhone.class));//
			// 亲情号注册
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleRight.setVisibility(View.GONE);
		mRlAccountInfo.setOnClickListener(this);
		mRlAccountBcardList.setOnClickListener(this);
		mRlAccountRechage.setOnClickListener(this);
		mRlAccountMoneyToBank.setOnClickListener(this);
		mRlAccountChangePhoenNum.setOnClickListener(this);
		mRlAccountFamilyAccount.setOnClickListener(this);
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.myaccount);
	}

}
