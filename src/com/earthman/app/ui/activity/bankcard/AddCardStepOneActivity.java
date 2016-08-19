package com.earthman.app.ui.activity.bankcard;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.IDCard;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者：Zhou
 * 日期：2016-3-3 下午7:49:57
 * 描述：（）
 */
@ContentView(R.layout.add_bankcard_stepone)
public class AddCardStepOneActivity extends BaseActivity{

	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.et_bankcardnum)
	private EditText et_bankcardnum;
	@ViewInject(R.id.et_idnum)
	private EditText et_idnum;
	@ViewInject(R.id.next)
	private Button next;
	
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

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		
	}
	
	@OnClick(R.id.btn_back)
	private void onBackClick(View v){
		this.finish();
	}
	
	@OnClick(R.id.next)
	private void onNextClick(View v){
		if(checkBankcard()){
			Intent intent = new Intent(AddCardStepOneActivity.this, AddCardStepTwoActivity.class);
			intent.putExtra("bankcardNum", et_bankcardnum.getText().toString());
			intent.putExtra("idNum", et_idnum.getText().toString());
			startActivity(intent);
		}		
	}
	
	private boolean checkBankcard(){
		String bankcardNum = et_bankcardnum.getText().toString();
		String idNum = et_idnum.getText().toString();
		if(TextUtils.isEmpty(bankcardNum)){
			MyToast.makeText(AddCardStepOneActivity.this, R.string.bankcard_cannot_empty, Toast.LENGTH_SHORT).show();
			return false;
		}else if(TextUtils.isEmpty(idNum)){
			MyToast.makeText(AddCardStepOneActivity.this, R.string.idcard_cannot_empty, Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(!AndroidUtils.checkBankCard(bankcardNum)){
			MyToast.makeText(AddCardStepOneActivity.this, R.string.bankcard_error, Toast.LENGTH_SHORT).show();
			return false;
		}else if(!TextUtils.isEmpty(IDCard.IDCardValidate(idNum))){
			MyToast.makeText(AddCardStepOneActivity.this, R.string.idcard_error, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/* 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.add_bankcard);
	}

}
