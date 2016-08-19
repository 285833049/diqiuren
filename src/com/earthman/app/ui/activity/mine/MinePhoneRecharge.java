package com.earthman.app.ui.activity.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnSurePayClickListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: PhoneChongZhiActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月8日
 */
@ContentView(R.layout.prepaid_recharge)
public class MinePhoneRecharge extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	//
	@ViewInject(R.id.chongzhi_huafei)
	ImageView mIvContact;
	@ViewInject(R.id.phonenumber)
	EditText mEtPhoneNumber;
	@ViewInject(R.id.huafei_no1)
	TextView tv_10;
	@ViewInject(R.id.huafei_no2)
	TextView tv_20;
	@ViewInject(R.id.huafei_no3)
	TextView tv_30;
	@ViewInject(R.id.huafei_no4)
	TextView tv_100;
	@ViewInject(R.id.huafei_no5)
	TextView tv_200;
	@ViewInject(R.id.huafei_no6)
	TextView tv_300;
	private PayPwdDialog payPwdDialog;
	private ActivityResultListener listener;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {

		switch (v.getId()) {
		case R.id.chongzhi_huafei:
			// 获取系统联系人
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.huafei_no1:
			final String money1 = "50";
			showPayPwdDialog(money1);
			break;
		case R.id.huafei_no2:
			final String money2 = "100";
			showPayPwdDialog(money2);
			break;
		case R.id.huafei_no3:
			final String money3 = "200";
			showPayPwdDialog(money3);
			break;
		case R.id.huafei_no4:
			final String money4 = "300";
			showPayPwdDialog(money4);
			break;
		case R.id.huafei_no5:
			final String money5 = "500";
			showPayPwdDialog(money5);
			break;
		case R.id.huafei_no6:
			break;
		default:
			break;
		}
	}

	public UserInfoPreferences preferences;

	private void showPayPwdDialog(final String money) {
		preferences = new UserInfoPreferences(MinePhoneRecharge.this);
		if (preferences.getUserPayPsw() == null
				|| preferences.getUserPayPsw().equals("")) {
			showSetPayPwdAlert();
		} else {

			final String phone = mEtPhoneNumber.getText().toString().trim();
			payPwdDialog = new PayPwdDialog(this, money,
					new OnSurePayClickListener() {

						@Override
						public void onSurePayClick(int payType, String pwd) {
							// doGetChongZhi(phone, money2, pageType, pwd);
							new PayFactory(MinePhoneRecharge.this,
									PayFactory.CASE_PAY_FOR_PHONE, payType,
									money).genPayPhoneRequest(phone,
									preferences.getUserPayPsw());

						}

					});
			listener = payPwdDialog;
			payPwdDialog.setCancelable(false);
			payPwdDialog.show();
		}
	}

	/**
	 * {userid}/{token}/{phonenumber}/{cardnum}/{paytype}/{paypasswd}
	 * phonenumber：充值电话号码 cardnum：话费金额【目前支持50/100/300】
	 * paytype：支付方式：（0:地球币:1:微信；2:支付宝:3:银联:4:paypal） paypasswd：支付密码
	 * doGetChongZhi(这里用一句话描述这个方法的作用) void
	 * 
	 * @exception
	 */
	/*
	 * private void doGetChongZhi(String phone, String money, String pageType,
	 * String psw) { myLoadingDialog.show(); ArrayList<Object> list = new
	 * ArrayList<Object>(); list.add(preferences.getUserID());
	 * list.add(preferences.getUserToken()); list.add(phone); list.add(money);
	 * if ("地球人币支付".equals(pageType)) { list.add(0); }else {
	 * MyToast.makeText(PhoneChongZhiActivity.this,"该支付方式暂未完成",
	 * Toast.LENGTH_LONG).show(); return; }
	 * list.add(MD5.getMessageDigest(psw.getBytes())); String loginUrl =
	 * HttpUrlConstants.getUrl(this, HttpUrlConstants.CHONGZHI_HUAFEI, list);
	 * executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl,
	 * BaseResponse.class, new Response.Listener<BaseResponse>() {
	 * 
	 * @Override public void onResponse(BaseResponse response) { if
	 * (myLoadingDialog.isShowing()) { myLoadingDialog.dismiss(); } if
	 * ("000000".equals(response.getCode())) {
	 * MyToast.makeText(PhoneChongZhiActivity.this, R.string.huafeichongzhi1,
	 * Toast.LENGTH_LONG).show(); } else {
	 * MyToast.makeText(PhoneChongZhiActivity.this, response.getMessage(),
	 * Toast.LENGTH_LONG).show(); } } }, getErrorListener())); }
	 */

	@Override
	protected void initData() {
		paySuccessReceiver = new PaySuccessReceiver();
		paySuccessReceiver.setHandler(handler);
		this.registerReceiver(paySuccessReceiver, new IntentFilter(
				Constants.PAY_SUCCESS_ACTION));
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what > 0) {
				MyToast.makeText(MinePhoneRecharge.this,
						R.string.huafeichongzhi1, Toast.LENGTH_SHORT).show();
			}
		};
	};
	private PaySuccessReceiver paySuccessReceiver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleLeft.setOnClickListener(this);
		mIvContact.setOnClickListener(this);
		mTvTitleLeft.setText(getString(R.string.prepaid_recharge));
		mEtPhoneNumber.addTextChangedListener(new EditChangedListener());
		tv_10.setOnClickListener(this);
		tv_20.setOnClickListener(this);
		tv_30.setOnClickListener(this);
		tv_100.setOnClickListener(this);
		tv_200.setOnClickListener(this);
		tv_300.setOnClickListener(this);

	}

	class EditChangedListener implements TextWatcher {
		private CharSequence temp;// 监听前的文本
		private int editStart;// 光标开始位置
		private int editEnd;// 光标结束位置
		private final int charMaxNum = 15;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() > 6) {
				tv_10.setEnabled(true);
				tv_20.setEnabled(true);
				tv_30.setEnabled(true);
				tv_100.setEnabled(true);
				tv_200.setEnabled(true);
				tv_300.setEnabled(true);
			} else if (s.length() < 6) {
				tv_10.setEnabled(false);
				tv_20.setEnabled(false);
				tv_30.setEnabled(false);
				tv_100.setEnabled(false);
				tv_200.setEnabled(false);
				tv_300.setEnabled(false);
			}
			temp = s;
		}

		@Override
		public void afterTextChanged(Editable s) {
			/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
			editStart = mEtPhoneNumber.getSelectionStart();
			editEnd = mEtPhoneNumber.getSelectionEnd();
			if (temp.length() > charMaxNum) {
				MyToast.makeText(getApplicationContext(), R.string.huafei_tips,
						Toast.LENGTH_LONG).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mEtPhoneNumber.setText(s);
				mEtPhoneNumber.setSelection(tempSelection);

			}

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PayPwdDialog.CHANGE_PAYTYPE:
				if (listener != null) {
					listener.onActivityResult(requestCode, resultCode, data);
				}
				break;
			default:
				// ContentProvider展示数据类似一个单个数据库表
				// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
				ContentResolver reContentResolverol = getContentResolver();
				// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				// String username =
				// cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					String usernumber = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					mEtPhoneNumber.setText(usernumber);
				}

				break;
			}

		}
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		unregisterReceiver(paySuccessReceiver);
		super.onDestroy();

	}
}
