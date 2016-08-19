package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.MineSettingAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.MineSettingInfo;
import com.earthman.app.enums.MineSettingType;
import com.earthman.app.utils.CheckUpdatUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 设置
 * 
 * @author xiexianyong
 * 
 */

@ContentView(R.layout.set_activity)
public class MineSetting extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	// ------------
	List<MineSettingInfo> mSettingList;
	@ViewInject(R.id.lv_content)
	ListView mLvContent;

	@Override
	protected void handclick(View v) {
	
	}

	@Override
	protected void initData() {
        mSettingList = new ArrayList<MineSettingInfo>();
		mSettingList.add(new MineSettingInfo(true, MineSettingType.ACCOUNT_PASSWORD_CHANGE, getString(R.string.account_password_modification)));
		mSettingList.add(new MineSettingInfo(false, MineSettingType.PAY_PASSWORD_CHANGE, getString(R.string.payment_password_modification)));
		mSettingList.add(new MineSettingInfo(true, MineSettingType.SPECIAL_APPLY, getString(R.string.franchise_application)));
		mSettingList.add(new MineSettingInfo(false, MineSettingType.CHECK_UPDATE, getString(R.string.check_update)));
		mSettingList.add(new MineSettingInfo(true, MineSettingType.ABOUT_US, getString(R.string.about_us)));
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mLvContent.setAdapter(new MineSettingAdapter(mSettingList));
		mLvContent.setOnItemClickListener(this);

	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.mine_setting);

	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MineSettingInfo info = mSettingList.get(arg2);
		switch (info.getSettingType()) {
		case ACCOUNT_PASSWORD_CHANGE:
			startActivity(new Intent(this, SettingAccountPwd.class));// 账户密码修改
			break;
		case PAY_PASSWORD_CHANGE:
			startActivity(new Intent(this, SettingPayPsw.class));// 支付密码修改
			break;
		case SPECIAL_APPLY:
			startActivity(new Intent(this, SetingApplyDie.class));
			break;
		case CHECK_UPDATE:
			new CheckUpdatUtils(MineSetting.this).doCheckNewVersionTask(true);// 更新
			break;
		case ABOUT_US:
			startActivity(new Intent(this,AboutUsActivity.class));//关于我们
			break;
		default:
			break;
		}
	}

}
