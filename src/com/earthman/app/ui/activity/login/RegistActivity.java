package com.earthman.app.ui.activity.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.RegistResponse;
import com.earthman.app.constant.Constent;
import com.earthman.app.eventbusbean.RegisterBean;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PassWordUtils;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.timepickerview.TimePickerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.activity_regist)
public class RegistActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.regist_getcode)
	TextView mTvGetCode;
	@ViewInject(R.id.ll_right_address)
	LinearLayout mLlChoiseAddress;
	@ViewInject(R.id.ll_right_birthday)
	RelativeLayout mRlSetBirthday;
	@ViewInject(R.id.regist_to_friend)
	RelativeLayout mRlRegistFriend;
	@ViewInject(R.id.regist_user_address)
	TextView mTvUserAddress;
	@ViewInject(R.id.regist_friend_id)
	EditText mEtFriendId;
	@ViewInject(R.id.regist_user_nick)
	EditText mEtUserNick;
	@ViewInject(R.id.regist_user_phone)
	EditText mEtUserPhone;
	@ViewInject(R.id.regist_user_code)
	EditText mEtUserCode;
	@ViewInject(R.id.regist_user_psw_1)
	EditText mEtUserPsw1;
	@ViewInject(R.id.regist_user_psw_2)
	EditText mEtUserPsw2;
	@ViewInject(R.id.regist_user_birthday)
	TextView mEtUserBirthday;
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.regist_user_next)
	Button mBtnRegistNext;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.tv_title)
	TextView mTvTitleCenter;
	@ViewInject(R.id.choose_province)
	RelativeLayout mRlChoiseProince;
	private TimeCount time;
	private TimePickerView pvTime;
	private String userNickStr;
	private String userPhoneStr;
	private String usercodeStr;
	private String psw1Str;
	private String psw2Str;
	private String userBirthdayStr;
	private String[] addrCodes;
	private String cardid;
	private String brithday;
	private int type;
	private String relative;
	private String RELATIVEFRIEND = "朋友";

	private void init2() {
		// View vMasker = findViewById(R.id.vMasker); // 时间选择器
		pvTime = new TimePickerView(RegistActivity.this, TimePickerView.Type.YEAR_MONTH_DAY); //
		// 控制时间范围
		Calendar calendar = Calendar.getInstance();
		pvTime.setRange(calendar.get(Calendar.YEAR) - 150, calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(true);
		pvTime.setCancelable(false); // 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date) {
				mEtUserBirthday.setText(getTime(date, "yyyy年MM月dd日"));
				brithday = getTime(date, "yyyy-MM-dd");
				LogUtil.i(TAG, brithday);
			}
		}); // 弹出时间选择器
		mRlSetBirthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pvTime.show();
			}
		});
	}

	public static String getTime(Date date, String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		return format.format(date);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_getcode:
			String phone = mEtUserPhone.getText().toString();
			if (TextUtils.isEmpty(phone)) {
				MyToast.makeText(RegistActivity.this, R.string.phone_num, Toast.LENGTH_SHORT).show();
				return;
			}

			myLoadingDialog.show();
			doGetSmsCode();
			break;

		case R.id.regist_user_next:
			getDataFromUI();
			if (!PassWordUtils.isPassWord(psw1Str)) {
				MyToast.makeText(this, R.string.regist_toast4, Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(userNickStr) || TextUtils.isEmpty(userPhoneStr) || TextUtils.isEmpty(usercodeStr) || TextUtils.isEmpty(psw1Str)
					|| TextUtils.isEmpty(userBirthdayStr) || TextUtils.isEmpty(psw2Str)) {
				MyToast.makeText(this, R.string.regist_toast1, Toast.LENGTH_SHORT).show();
				return;
			}
			if (type == Constent.REGIST_TO_FRIEND && preferences.getUserPhone().equals(userPhoneStr)) {
				MyToast.makeText(this, R.string.regist_toast5, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!psw1Str.equals(psw2Str)) {
				MyToast.makeText(this, R.string.regist_toast2, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!TextUtils.isEmpty(mEtFriendId.getText())) {
				cardid = mEtFriendId.getText().toString().trim();
			} else {
				cardid = preferences.getUsercardId();
			}
			doPostRegist(cardid);// 注册
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.choose_province:
			ProvinceCityCountryActivity.intoActivity(RegistActivity.this, ProvinceCityCountryActivity.PAGETYPE_PROVINCE, 0, "", "", 0);
			break;
		default:
			break;
		}
	}

	/**
	 * 执行注册请求
	 * {telephone}/{authcode}/{birthday}自己注册
	 * {telephone}/{authcode}/{birthday}/{userid}/{token}提好友注册
	 * matter：关系（汉字字符串）
	 * cardId：推荐人地球人代码
	 */
	private void doPostRegist(String cardid) {
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userPhoneStr);
		list.add(usercodeStr);
		// long birthdayLong = Long.parseLong(brithday);// string 的生日转成长整形
		list.add(brithday);
		// 好友注册和自己注册参数不同
		if (type == Constent.REGIST_TO_FRIEND || type == Constent.REGIST_TO_FAMILY) {
			list.add(preferences.getUserID());
			list.add(preferences.getUserToken());
		}
		JSONObject jsonRequest = new JSONObject();
		try {
			// jsonRequest.put("passwd",
			// MD5.getMessageDigest(psw1Str.getBytes()));
			jsonRequest.put("passwd", psw1Str);// 涉及环信的使用明文密码
			jsonRequest.put("registerProvince", addrCodes[0]);
			jsonRequest.put("registerCity", addrCodes[1]);
			jsonRequest.put("registerArea", addrCodes[2]);
			jsonRequest.put("nice", userNickStr);
			if (type == Constent.REGIST_TO_FRIEND || type == Constent.REGIST_TO_SELF) {
				jsonRequest.put("cardId", cardid);
			}
			if (type == Constent.REGIST_TO_FAMILY) {
				jsonRequest.put("matter", relative);
				jsonRequest.put("cardId", cardid);
			}
			LogUtil.i(TAG, "matter" + relative + "cardId" + cardid + "registerProvince" + addrCodes[0] + "registerCity" + addrCodes[1] + "registerArea" + addrCodes[2]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl;
		if (type == Constent.REGIST_TO_SELF) {
			getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.REGISTER, list);
		} else {
			getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.FAMILY_REGISTER, list);
		}
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				RegistResponse response2 = JSON.parseObject(response.toString(), RegistResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				dismissLoading();
				if ("000000".equals(response2.getCode())) {
					MyToast.makeText(RegistActivity.this, R.string.regist_toast3, Toast.LENGTH_LONG).show();
					if (type == Constent.REGIST_TO_SELF) {
						preferences.setUserID(response2.getResult().getUserid());
						preferences.setUserToken(response2.getResult().getToken());
					}
					if (type == Constent.REGIST_TO_FRIEND || type == Constent.REGIST_TO_FAMILY) {
						preferences.setUserpayID(response2.getResult().getUserid());
						preferences.setHeruniqueid(response2.getResult().getUniqueid());
					}
					
					EventBus.getDefault().post(new RegisterBean(type));
					Intent intent = new Intent(RegistActivity.this, PayActivity.class);
					intent.putExtra("type", type);
					startActivityForResult(intent, RESULT_OK);
					finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(RegistActivity.this, response2.getCode(), response2.getMessage());
				}

			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, getUrl, jsonRequest, listener, getErrorListener()) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}
		};
		executeRequest(jsonObjectRequest);
	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == 0x12123) {
			finish();
		}
	}

	/**
	 * 获取输入数据
	 */
	private void getDataFromUI() {
		userNickStr = mEtUserNick.getText().toString().trim();
		userPhoneStr = mEtUserPhone.getText().toString().trim();
		usercodeStr = mEtUserCode.getText().toString().trim();
		psw1Str = mEtUserPsw1.getText().toString().trim();
		psw2Str = mEtUserPsw2.getText().toString().trim();
		userBirthdayStr = mEtUserBirthday.getText().toString().trim();
	}

	/**
	 * 验证码倒计时
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onFinish() {// 计时完毕
			mTvGetCode.setText(getString(R.string.get_code));
			mTvGetCode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao));
			mTvGetCode.setClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			mTvGetCode.setClickable(false);// 防止重复点击
			mTvGetCode.setText(millisUntilFinished / 1000 + getString(R.string.code_second));
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent != null) {
			String region = intent.getStringExtra("region");
			String regionCode = intent.getStringExtra("regionCode");
			mTvUserAddress.setText(region);
			if (!TextUtils.isEmpty(regionCode)) {
				addrCodes = regionCode.split(";");
			}
		}
		super.onNewIntent(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {

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
		if (getIntent().getExtras() != null) {
			type = (Integer) getIntent().getExtras().get("type");
			if (type == Constent.REGIST_TO_FRIEND) {
				mRlRegistFriend.setVisibility(View.VISIBLE);
				mEtFriendId.setHint(getString(R.string.input_earthid));
				mTvTitleLeft.setText(getString(R.string.register2));
				// relative = RELATIVEFRIEND;传入空的，服务器做相关的判断，区分朋友和亲情号
				mEtFriendId.setText(preferences.getUsercardId());
			}
			if (type == Constent.REGIST_TO_SELF) {
				mEtFriendId.setHint(getString(R.string.input_earthid));
				mTvTitleCenter.setText(getResources().getString(R.string.regist_login));
			}
			if (type == Constent.REGIST_TO_FAMILY) {
				mEtFriendId.setHint(getString(R.string.input_earthid));
				mTvTitleLeft.setText(getString(R.string.register3));
				mEtUserPhone.setHint(R.string.input_self_phone);
				relative = getIntent().getExtras().getString("relative");
				mEtFriendId.setText(preferences.getUsercardId());
			}
		}
		init2();
		mBtnTitleLeft.setOnClickListener(this);
		time = new TimeCount(60000, 1000);
		mTvGetCode.setOnClickListener(this);
		mBtnRegistNext.setOnClickListener(this);
		mEtUserBirthday.setOnClickListener(this);
		mRlChoiseProince.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	private void doGetSmsCode() {
		String phone = mEtUserPhone.getText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		String getUrl = null;
		if (type == Constent.REGIST_TO_FAMILY) {
			list.add(8);// 亲情号注册
			getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GENERATE_SMSCODE, list);
		} else {
			getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.REGISTER, list);
		}
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {

			@SuppressLint("NewApi")
			@Override
			public void onResponse(BaseResponse response) {
				if (myLoadingDialog.isShowing()) {
					myLoadingDialog.dismiss();
				}
				if ("000000".equals(response.getCode())) {
					time.start();
					mTvGetCode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao_bg));
					MyToast.makeText(RegistActivity.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
				} else {
					mTvGetCode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao));
					NetStatusHandUtil.getInstance().handStatus(RegistActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	public void onBackPressed() {
		if (myLoadingDialog.isShowing()) {
			return;
		}
		super.onBackPressed();
	}

}
