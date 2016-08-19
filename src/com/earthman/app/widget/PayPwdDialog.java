package com.earthman.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.ui.activity.chongzhi.SelectPaymentActivity;
import com.earthman.app.ui.activity.mine.ActivationDetailsActivity;
import com.earthman.app.ui.activity.mine.MinePhoneRecharge;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.widget.passwordview.GridPasswordView;

/**
 * 作者：Zhou 日期：2016-1-25 下午5:23:46 描述：（）
 */
public class PayPwdDialog extends Dialog implements android.view.View.OnClickListener, ActivityResultListener {

	private Context context;
	private String[] payType;
	private String pageTypestr;
	private OnSurePayClickListener listener;
	private TextView money_detial;
	private String payMoney;
	private GridPasswordView pswView;
	private TextView money;
	private TextView morenbank;
	private TextView changebank;
	private ImageView close;
	public static final int CHANGE_PAYTYPE = 0x999;
	private int currentPayType;

	public PayPwdDialog(Context context, String payMoney, OnSurePayClickListener listener) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		this.payMoney = payMoney;
		this.listener = listener;
		initData();
		initView();
	}
	
	private void initData(){
		payType = context.getResources().getStringArray((Activity)context instanceof ActivationDetailsActivity?R.array.payment_names:R.array.not_payment_names);
		
//		if((Activity)context instanceof ActivationDetailsActivity){
//			payType = context.getResources().getStringArray(R.array.payment_names);
//		}else{
//			payType = context.getResources().getStringArray(R.array.not_payment_names);
//		}
	
//		payType = context.getResources().getStringArray(R.array.not_payment_names);
//		pageTypestr = payType[0];
		currentPayType = 0;
	}

	private void initView() {
		setContentView(R.layout.chongzhi_detial_dialog);
		money_detial = (TextView) findViewById(R.id.cz_dialog_money);
		money = (TextView) findViewById(R.id.chongzhi_money_dialog);
//		money.setVisibility(View.GONE);
		changebank = (TextView) findViewById(R.id.change_bank);
		morenbank = (TextView) findViewById(R.id.bank_moren);
		morenbank.setVisibility(View.GONE);
		close = (ImageView) findViewById(R.id.dialog_close);
		close.setOnClickListener(this);
		
		money.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.cz_dialog_money), payMoney)));
		changebank.setOnClickListener(this);

		pswView = (GridPasswordView) findViewById(R.id.gpv_normal);
		pswView.setFocusable(true);
		pswView.setFocusableInTouchMode(true);
		pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {

			@Override
			public void onTextChanged(String psw) {

			}

			@Override
			public void onInputFinish(String psw) {
				if (listener != null) {
					listener.onSurePayClick(currentPayType, psw);
				}
				dismiss();
			}
		});
		
	}


	/**
	 * 
	 * setPayWay(设置支付方式)1激活币--2-->地球币，3-->微信，4-->支付宝，5-->银联，6-->信用卡，7-->paypal
	 * void
	 * 
	 * @exception
	 */
	public void setPayWay(int paytype) {
		String payName = null;
		switch(paytype){
		case 1://激活币
			payName="激活币";
			break;
		case 2://地球币
			payName="地球币";
			break;
		case 3://微信支付
			payName="微信支付";
			break;
		case 4://支付宝
			payName="支付宝";
			break;
		}
		
		
		morenbank.setText(payName);
		pageTypestr = payName;
		currentPayType = paytype;
		
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 40));
		money_detial.setText(morenbank.getText().toString());

		morenbank.setText(pageTypestr);
	}

	/**
	 * 
	 * dismissChange(隐藏更换选项)
	 * void
	 * 
	 * @exception
	 */
	public void dismissChange() {
		changebank.setVisibility(View.GONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_bank:
			Intent intent = new Intent(context, SelectPaymentActivity.class);
			intent.putExtra("isRegister", payType.length==3?false:true);
			if(context instanceof Activity){
				Activity activity = (Activity) context;
				activity.startActivityForResult(intent, CHANGE_PAYTYPE);
			}			
			//dismiss();
			break;
		case R.id.dialog_close:
			dismiss();
			break;

		default:
			break;
		}
	}


	/* 
	 * @see android.preference.PreferenceManager.OnActivityResultListener#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case CHANGE_PAYTYPE:	
				if(data == null){
					return;
				}
				int position = data.getIntExtra("position", 0);
				String payName=data.getStringExtra("payName");
				if(payName.equals(context.getString(R.string.currency_activation))){
					position=1;
				}else if(payName.indexOf(context.getString(R.string.currency_earth))!=-1){
					position=2;
				}else if(payName.equals(context.getString(R.string.currency_weixinpay))){
					position=3;
				}else if(payName.equals(context.getString(R.string.currency_alipay))){
					position=4;
				}
				if(context instanceof MinePhoneRecharge){
					setPayWay(position);
					
				}else if(context instanceof PeronalDetialActivity){
					setPayWay(position);
				}else{
					setPayWay(data.getIntExtra("payTypeNum", 0)<4?position+1:position);
				}
				
				
				if(payName.equals(context.getString(R.string.currency_activation))||payName.indexOf(context.getString(R.string.currency_earth))!=-1){
					if (listener != null) {
						listener.onSurePayClick(position, null);
					}
					dismiss();
				}
				//话费充值判断是否是支付宝和微信支付
				if(context instanceof MinePhoneRecharge){
					switch (position) {
					case 3:
					case 4:
						if (listener != null) {
							listener.onSurePayClick(position, null);
						}
						dismiss();
						break;

					default:
						break;
					}
					
					
				}else if(context instanceof PeronalDetialActivity){
					switch (position) {
					case 3:
					case 4:
						if (listener != null) {
							listener.onSurePayClick(position, null);
						}
						dismiss();
						break;

					default:
						break;
					}
					
				}else{
					setPayWay(data.getIntExtra("payTypeNum", 0)<4?position+1:position);
				}
				
				
				
//				switch (position) {
//				case 1:
//				case 2:					
//					if (listener != null) {
//						listener.onSurePayClick(position, null);
//					}
//					dismiss();
//					break;
//				}
//				switch (position) {
//				case 0:
//				case 1:					
//					if (listener != null) {
//						listener.onSurePayClick(position, null);
//					}
//					dismiss();
//					break;
//				default:
//					break;
//				}
				break;
			default:
				break;
			}
		}
		
		return;
	}

	
}
