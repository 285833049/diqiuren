package com.earthman.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.DeadCategoryInfo;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnFlowerPayListener;
import com.earthman.app.ui.activity.chongzhi.SelectPaymentActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.passwordview.GridPasswordView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Title: MyDialogXianHuaPay
 * @Description: 鲜花支付
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月16日
 */

public class DialogPayFlower extends Dialog implements android.view.View.OnClickListener, ActivityResultListener {

	private Context mContext;
	private ImageView mIvClose;
	private ImageView mIvFlower;// 鲜花图片
	private TextView mTvIndate;// 有效期
	private TextView mTvFlowerName;// 鲜花名称
	private TextView mTvPrice;// 单价

	private ImageView mIvAdd, mIvSubtract;// 加减
	private TextView mTvPayNum;// 购买数量
	private TextView mTvPayAmount;// 支付总金额
	private GridPasswordView mPasswordView;// 密码输入框

	private DeadCategoryInfo mFlowerInfo;

	private TextView mTvPayMode, mTvChangePayMode;// 支付方式、改变支付方式

	// ---------------------
	private String[] payType;// 所有的支付类型
	private String pageTypestr;
	private TextView money_detial;// 支付详情
	private String payMoney;// 最终支付金额
	public static final int CHANGE_PAYTYPE = 0x999;
	private int currentPayType;// 当前支付类型

	private OnFlowerPayListener mListener;

	/**
	 * @param mContext
	 */
	public DialogPayFlower(Context mContext, DeadCategoryInfo flowerInfo, OnFlowerPayListener listener) {
		super(mContext, R.style.upomp_bypay_MyDialog);
		this.mContext = mContext;
		this.mListener = listener;
		this.mFlowerInfo = flowerInfo;
		initData();
		initView();
	}

	private void initData() {
		payType = mContext.getResources().getStringArray(R.array.payment_names);
		pageTypestr = payType[0];
		currentPayType = 0;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.dialog_pay_flower);
		// --------------------->
		mIvClose = (ImageView) findViewById(R.id.iv_close);
		mIvFlower = (ImageView) findViewById(R.id.iv_flower);
		mTvFlowerName = (TextView) findViewById(R.id.tv_flower_name);
		mTvIndate = (TextView) findViewById(R.id.tv_indate);
		mTvPrice = (TextView) findViewById(R.id.tv_price);
		mIvSubtract = (ImageView) findViewById(R.id.iv_subtract);
		mTvPayNum = (TextView) findViewById(R.id.tv_pay_num);
		mTvPayAmount = (TextView) findViewById(R.id.tv_pay_amount);
		mIvAdd = (ImageView) findViewById(R.id.iv_add);
		mPasswordView = (GridPasswordView) findViewById(R.id.gpv_normal);
		mTvPayMode = (TextView) findViewById(R.id.tv_pay_mode);
		mTvChangePayMode = (TextView) findViewById(R.id.tv_change_pay_mode);

		// ------------------------------>初始化界面
		mIvFlower.setScaleType(ScaleType.FIT_CENTER);
		ImageLoader.getInstance().displayImage(mFlowerInfo.getPicture(), mIvFlower);// 设置鲜花图片
		mTvFlowerName.setText(mFlowerInfo.getName());// 鲜花名称
		payMoney = String.valueOf(mFlowerInfo.getPrice());

		mTvPrice.setText(mFlowerInfo.getPrice() + " " + mContext.getString(R.string.xianhua_dialog2));
		mTvPayAmount.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.cz_dialog_money2), 2)));
		// ---------------------------->设置点击事件
		mIvClose.setOnClickListener(this);
		mIvSubtract.setOnClickListener(this);
		mIvAdd.setOnClickListener(this);
		mTvChangePayMode.setOnClickListener(this);
		// ---------------------------->
		mPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {

			@Override
			public void onTextChanged(String psw) {

			}

			@Override
			public void onInputFinish(String psw) {
				if (mListener != null) {
					mListener.onSurePayClick(currentPayType, psw, Integer.valueOf(mTvPayNum.getText().toString()), payMoney);
				}
				dismiss();
			}
		});
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(mContext) - AndroidUtils.dip2px(mContext, 40));
	}

	/**
	 * 
	 * setPayWay(设置支付方式)0-->地球币，1-->微信，2-->支付宝，3-->银联，4-->信用卡，5-->paypal
	 * void
	 * 
	 * @exception
	 */
	public void setPayWay(int paytype) {
		mTvPayMode.setText(payType[paytype]);
		pageTypestr = payType[paytype];
		currentPayType = paytype;
	}

	/*
	 * @see android.preference.PreferenceManager.OnActivityResultListener#
	 * onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CHANGE_PAYTYPE:
				if (data == null) {
					return;
				}
				int position = data.getIntExtra("position", 0);
				setPayWay(position);
				switch (position) {
				case PayFactory.PAYTYPE_WEIXIN:
				case PayFactory.PAYTYPE_ALIPAY:
					if (mListener != null) {
						// mListener.onSurePayClick(position, null);
						mListener.onSurePayClick(currentPayType, "", Integer.valueOf(mTvPayNum.getText().toString()), payMoney);
					}
					dismiss();
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}

		return;
	}

	@Override
	public void onClick(View v) {
		int num = Integer.valueOf(mTvPayNum.getText().toString());
		switch (v.getId()) {

		case R.id.iv_close:
			dismiss();
			break;
		case R.id.iv_subtract:// 减数量
			if (num > 1) {
				mTvPayNum.setText(String.valueOf(--num));
				mTvPayAmount.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.cz_dialog_money2), mFlowerInfo.getPrice() * num)));
				payMoney = String.valueOf(mFlowerInfo.getPrice() * num);
			}
			break;
		case R.id.iv_add:// 加数量
			mTvPayNum.setText(String.valueOf(++num));
			mTvPayAmount.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.cz_dialog_money2), mFlowerInfo.getPrice() * num)));
			payMoney = String.valueOf(mFlowerInfo.getPrice() * num);
			break;
		case R.id.tv_change_pay_mode:
			Intent intent = new Intent(mContext, SelectPaymentActivity.class);
			if (mContext instanceof Activity) {
				Activity activity = (Activity) mContext;
				activity.startActivityForResult(intent, CHANGE_PAYTYPE);
			}
			// dismiss();
			break;
		default:
			break;
		}
	}
}
