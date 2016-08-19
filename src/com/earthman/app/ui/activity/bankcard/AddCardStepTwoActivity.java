package com.earthman.app.ui.activity.bankcard;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者：Zhou
 * 日期：2016-3-3 下午8:35:55
 * 描述：（）
 */
@ContentView(R.layout.add_bankcard_steptwo)
public class AddCardStepTwoActivity extends BaseActivity{

	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.card_num)
	private TextView card_num;
	@ViewInject(R.id.card_name)
	private TextView card_name;
	@ViewInject(R.id.phone)
	private TextView phone;
	private String bankcardNum;
	private String idcardNum;
	
	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {
		
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		this.finish();
	}
	
	@OnClick(R.id.next)
	private void onNextClick(View v){
		Intent intent = new Intent(AddCardStepTwoActivity.this, VerifyMobileActivity.class);
		startActivity(intent);
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.add_bankcard);
		Intent intent = getIntent();
		if(intent != null){
			bankcardNum = intent.getStringExtra("bankcardNum");
			idcardNum = intent.getStringExtra("idcardNum");
		}
		card_num.setText(bankcardNum);
		
	}
	
	
	private void doGetCardType(){
		
	}

}
