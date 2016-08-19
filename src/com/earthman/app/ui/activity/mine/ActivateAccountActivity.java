package com.earthman.app.ui.activity.mine;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.constant.Constent;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.utils.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 激活帐户
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.activate_account)
public class ActivateAccountActivity extends BaseActivity {
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.activation_btn)
	Button activation_btn;
	@ViewInject(R.id.tv_account)
	private TextView tv_account;
	@ViewInject(R.id.tv_duedate)
	private TextView tv_duedate;
	@ViewInject(R.id.tv_createdat)
	private TextView tv_createdat;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.activation_btn:
			Intent intent = new Intent(this, PayActivity.class);
			if(Constants.UNACTIVE.equals(preferences.getUserStatus())){//未激活
				intent.putExtra("type", Constent.REGIST_TO_SELF);
			}else{//续费
				intent.putExtra("type", Constent.RENEW);				
			}
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void initData() {

	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		activation_btn.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.mine_jihuo);
		tv_account.setText(preferences.getUsercardId());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		tv_createdat.setText(format.format(new Date(preferences.getCreatedAt())));
		if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活状态
			tv_duedate.setText(R.string.unactive);

		} else {// 激活显示有效期
			tv_duedate.setText(preferences.getDueDate());
			if(Constants.ALLLIFE_ACTIVITED.equals(preferences.getDueDate())){
				activation_btn.setVisibility(View.GONE);
			}else{
				activation_btn.setVisibility(View.VISIBLE);
				activation_btn.setText(R.string.continue_pay);
			}



		}

	}

}
