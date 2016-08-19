package com.earthman.app.ui.activity.mine;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.widget.WithdrawalsPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 提现
 * 
 * @author xiexianyong
 * 
 */

@ContentView(R.layout.personal_withdrawals_activity)
public class AccountWithdraw extends BaseActivity implements OnClickListener {
	// -----------title--------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//----------提现部分----------------
	@ViewInject(R.id.personal_withdrawal_btn)
	Button mBtnSubmit;
	@ViewInject(R.id.personal_withdrawal_moneynum)
	EditText mEtMoney;
	@ViewInject(R.id.personal_withdrawal_moneys)
	TextView mTvAccountMoney;
	@ViewInject(R.id.personal_withdrawal_card)
	TextView mTvSelectBankCard;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			finish();

			break;
		case R.id.personal_withdrawal_btn:
			showMyDialog();
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
		mTvTitleLeft.setText(R.string.withdrawals);
		mBtnSubmit.setOnClickListener(this);
		mBtnTitleLeft.setOnClickListener(this);

	}

	/**
	 * 提现dialog
	 */
	private void showMyDialog() {
		WithdrawalsPwdDialog payPwdDialog = new WithdrawalsPwdDialog(this, new OnSurePayClickListener() {
			@Override
			public void onSurePayClick(int pageType, String pwd) {
				
			}
		});
		payPwdDialog.setCanceledOnTouchOutside(false);
		payPwdDialog.show();

	}

	@Override
	protected void setAttribute() {
		
	}

}
