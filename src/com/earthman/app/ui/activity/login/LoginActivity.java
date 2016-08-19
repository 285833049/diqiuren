package com.earthman.app.ui.activity.login;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.LoginResponse;
import com.earthman.app.bean.LoginResponse.LoginResult;
import com.earthman.app.constant.Constent;
import com.earthman.app.nim.NimCache;
import com.earthman.app.nim.config.preference.Preferences;
import com.earthman.app.nim.config.preference.UserPreferences;
import com.earthman.app.nim.uikit.cache.DataCacheManager;
import com.earthman.app.nim.uikit.common.ui.dialog.DialogMaker;
import com.earthman.app.nim.uikit.common.util.log.LogUtil;
import com.earthman.app.nim.uikit.common.util.string.MD5;
import com.earthman.app.ui.activity.MainActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {
	@ViewInject(R.id.forget_psw_tv)
	TextView forget_psw_tv;
	@ViewInject(R.id.regist_user_tv)
	TextView regist_user_tv;
	@ViewInject(R.id.rb_user_login)
	RadioButton user_login;
	@ViewInject(R.id.rb_youke_login)
	RadioButton youke_login;
	@ViewInject(R.id.login_change_rg)
	RadioGroup rg_login_change;
	@ViewInject(R.id.login_getcode_tv)
	TextView getcode;
	@ViewInject(R.id.login_et_userid)
	EditText user_id;
	@ViewInject(R.id.login_user_psw)
	EditText user_psw;
	@ViewInject(R.id.Login_button)
	Button Login_button;
	@ViewInject(R.id.ll_login_disanfang)
	LinearLayout ll_login_disanfang;
	@ViewInject(R.id.sl_center)
	ScrollView sl_center;
	@ViewInject(R.id.weixin)
	ImageView weixin;
	static final int YOUKE = 0;
	static final int USER = 1;
	private boolean progressShow;
	private int type = USER;
	private TimeCount time;
	private String youke_phone;
	private String code_psw;
	private String loginUrl;

	private static final String KICK_OUT = "KICK_OUT";
	private AbortableFuture<LoginInfo> mLoginRequest;

	private long lastClick;
	private UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.UMENGLOGIN);
	private OnCheckedChangeListener myCheckChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_user_login:
				forget_psw_tv.setVisibility(View.VISIBLE);
				user_id.setFocusable(true);
				user_id.setFocusableInTouchMode(true);

				ll_login_disanfang.setVisibility(View.GONE);
				user_id.setText("");
				user_psw.setText("");
				getcode.setVisibility(View.GONE);
				type = USER;
				user_psw.setHint(getString(R.string.user_psw));
				user_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				user_id.setHint(getResources().getString(R.string.user_id));
				break;
			case R.id.rb_youke_login:
				forget_psw_tv.setVisibility(View.GONE);
				user_id.setFocusable(true);
				user_id.setFocusableInTouchMode(true);

				user_id.setText("");
				user_psw.setText("");
				user_psw.setHint(getString(R.string.input_smscode1));
				user_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_NORMAL);
				ll_login_disanfang.setVisibility(View.VISIBLE);
				getcode.setVisibility(View.VISIBLE);
				type = YOUKE;
				user_id.setHint(getResources().getString(R.string.login_youke_hint));
				break;
			default:
				break;
			}
		}
	};

	public static void start(Context context) {
		start(context, false);
	}

	public static void start(Context context, boolean kickOut) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(KICK_OUT, kickOut);
		context.startActivity(intent);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 跳转找回密码界面
		case R.id.forget_psw_tv:
			Intent forgetPswIntent = new Intent(LoginActivity.this, FindPswActivity.class);
			startActivity(forgetPswIntent);
			break;
		// 跳转到注册界面
		case R.id.regist_user_tv:
			Intent registIntent = new Intent(LoginActivity.this, RegistActivity.class);
			registIntent.putExtra("type", Constent.REGIST_TO_SELF);
			startActivity(registIntent);
			break;
		// 执行登陆操作
		case R.id.Login_button:
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastClick < 3000) {
				return;
			}
			lastClick = currentTime;

			getDataFromUI();
			if (TextUtils.isEmpty(youke_phone)) {
				MyToast.makeText(LoginActivity.this, R.string.phone_num, Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(code_psw)) {
				MyToast.makeText(LoginActivity.this, R.string.user_psw, Toast.LENGTH_SHORT).show();
				return;
			}
			doGetLogin();// 执行登陆操作

			break;
		// 获取验证码
		case R.id.login_getcode_tv:
			String phone = user_id.getText().toString();
			if (TextUtils.isEmpty(phone)) {
				MyToast.makeText(LoginActivity.this, R.string.phone_num, Toast.LENGTH_SHORT).show();
				return;
			}
			time.start();
			getcode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao_bg));
			doGetSmsCode();
			break;
		case R.id.weixin:// 微信登录
			// weixinLogin();
			login(SHARE_MEDIA.WEIXIN);
			break;
		default:
			break;
		}
	}

	/**
	 * 游客和访客登陆
	 */
	private void doGetLogin() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(youke_phone);
		if (type == USER) {
			// list.add(MD5.getMessageDigest(code_psw.getBytes()));
			list.add(code_psw);// 涉及环信的使用明文密码
			loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.LOGIN, list);
		}

		if (type == YOUKE) {
			list.add(code_psw);
			loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.YOUKE_LOGIN, list);
		}
		executeRequest(new FastJsonRequest<LoginResponse>(Method.GET, loginUrl, LoginResponse.class, new Response.Listener<LoginResponse>() {

			@Override
			public void onResponse(LoginResponse response) {
				if ("000000".equals(response.getCode())) {
					// 登陆成功,保存数据
					LoginResult loginResult = response.getResult();
					// 登录云信
					doLoginNim(loginResult, loginResult.getUniqueid(), MD5.getStringMD5(user_psw.getText().toString()).toLowerCase(), loginResult.getToken());
					// ------------------------------------
					// startActivity(new Intent(LoginActivity.this,
					// MainActivity.class));
					// finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(LoginActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 登录云信
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	private void doLoginNim(final LoginResult loginResult, final String account, final String password, final String token) {
		DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (mLoginRequest != null) {
					Toast.makeText(getApplicationContext(), "地球人登入成功", Toast.LENGTH_LONG).show();
					mLoginRequest.abort();
					onLoginDone();
				}
			}
		}).setCanceledOnTouchOutside(false);

		// 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号 和token同步到云信服务器。
		// 在这里直接使用同步到云信服务器的帐号和token登录。
		// 这里为了简便起见，demo就直接使用了密码的md5作为token。
		// 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
		// 登录
		mLoginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, password));
		mLoginRequest.setCallback(new RequestCallback<LoginInfo>() {
			@Override
			public void onSuccess(LoginInfo param) {
				// ----------------------------------------只有在两个账号都成功登陆的情况下才保存用户信息
				preferences.setUserToken(loginResult.getToken());
				preferences.setUserID(loginResult.getUserid());
				if (type == USER) {
					preferences.setUserPhone(loginResult.getTelephone());
				}
				preferences.setUsercardId(loginResult.getUserid());
				preferences.setUseruniqueid(loginResult.getUniqueid());
				preferences.setUsernice(loginResult.getNice());
				preferences.setUserIco(loginResult.getAvatar());
				preferences.setUserStatus(loginResult.getStatus());
				preferences.setUserPhone(loginResult.getTelephone());
				preferences.setUserID(loginResult.getUserid());
				// try {
				// preferences.setIsSetPwd(Integer.valueOf(loginResult.getIsSetPayPsw()));
				// } catch (Exception e) {// 防止isSetPayPsw修改为1 闪退
				// e.printStackTrace();
				// }
				preferences.setIsLogin(false);
				// ---------------------------------------
				LogUtil.i(TAG, "login success");

				onLoginDone();
				NimCache.setAccount(account);
				saveLoginInfo(account, password);

				// 初始化消息提醒
				NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

				// 初始化免打扰
				if (UserPreferences.getStatusConfig() == null) {
					UserPreferences.setStatusConfig(NimCache.getNotificationConfig());
				}
				NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

				// 构建缓存
				DataCacheManager.buildDataCacheAsync();

				// 进入主界面
				// MainActivity.start(LoginActivity.this, null);
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}

			@Override
			public void onFailed(int code) {
				onLoginDone();
				if (code == 302 || code == 404) {
					Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onException(Throwable exception) {
				onLoginDone();
			}
		});
	}

	private void onLoginDone() {
		mLoginRequest = null;
		DialogMaker.dismissProgressDialog();
	}

	private void saveLoginInfo(final String account, final String token) {
		Preferences.saveUserAccount(account);
		Preferences.saveUserToken(token);
	}

	/**
	 * 游客登陆获取验证码
	 */
	private void doGetSmsCode() {
		String phone = user_id.getText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(phone);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.YOUKE_LOGIN, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(LoginActivity.this, R.string.getcode_success, Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(LoginActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 获取界面输入数据
	 */
	private void getDataFromUI() {
		youke_phone = user_id.getText().toString().trim();
		code_psw = user_psw.getText().toString().trim();
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
			getcode.setText(getString(R.string.get_code));
			getcode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao));
			getcode.setClickable(true);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			getcode.setClickable(false);// 防止重复点击
			// getcode.setBackground(getResources().getDrawable(R.drawable.rect_yuanjiao_bg));
			getcode.setText(millisUntilFinished / 1000 + getString(R.string.code_second));
		}
	}

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		time = new TimeCount(60000, 1000);
		forget_psw_tv.setOnClickListener(this);
		regist_user_tv.setOnClickListener(this);
		Login_button.setOnClickListener(this);
		getcode.setOnClickListener(this);
		rg_login_change.check(R.id.rb_user_login);
		rg_login_change.setOnCheckedChangeListener(myCheckChangeListener);
		user_id.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				changeScrollView();
				return false;
			}
		});
	}

	private void changeScrollView() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				sl_center.scrollTo(0, sl_center.getHeight());
			}
		}, 300);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	@Override
	protected void setAttribute() {
		user_id.setOnFocusChangeListener(this);
		user_psw.setOnFocusChangeListener(this);
		weixin.setOnClickListener(this);
		addWXPlatform();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v instanceof EditText) {
			openInputMethod((EditText) v);
			EditText eText = (EditText) v;
			String text = eText.getText().toString().trim();
			if (TextUtils.isEmpty(text)) {
				if (hasFocus) {
					eText.setHint("");
				} else {
					switch (v.getId()) {
					case R.id.login_et_userid:
						if (type == YOUKE) {
							eText.setHint(R.string.login_youke_hint);
						} else {
							eText.setHint(R.string.user_id);
						}
						break;
					case R.id.login_user_psw:
						eText.setHint(R.string.user_psw);
						break;
					default:
						break;
					}
				}
			}
		}
	}

	public void openInputMethod(final EditText editText) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}, 200);

	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// 添加微信平台
		try {
			UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, Constants.WX_APPID, Constants.WX_APPSECRET);
			wxHandler.addToSocialSDK();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 授权。如果授权成功，则获取用户信息
	 * 
	 * @param platform
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(LoginActivity.this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				/*
				 * Toast.makeText(LoginActivity.this, "授权开始",
				 * Toast.LENGTH_SHORT).show();
				 */
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(LoginActivity.this, getString(R.string.authorization_failed), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				// 获取uid
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					// uid不为空，获取用户信息
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, getString(R.string.authorization_failed), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(LoginActivity.this, getString(R.string.authorization_cancel), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(LoginActivity.this, platform, new UMDataListener() {

			@Override
			public void onStart() {
				Toast.makeText(LoginActivity.this, getString(R.string.get_data), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (info != null) {
					preferences.setUsernice(String.valueOf(info.get("nickname")));
					preferences.setUserIco(String.valueOf("headimgurl"));
					doThirdLogin();
				}
			}
		});
	}

	private void doThirdLogin() {
		ArrayList<Object> list = new ArrayList<Object>();
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.THIRD_LOGIN, list);
		executeRequest(new FastJsonRequest<LoginResponse>(Method.GET, getUrl, LoginResponse.class, new Response.Listener<LoginResponse>() {

			@Override
			public void onResponse(LoginResponse response) {
				if ("000000".equals(response.getCode())) {
					LoginResult loginResult = response.getResult();
					if (loginResult == null) {
						return;
					}
					preferences.setUserToken(loginResult.getToken());
					preferences.setUserID(loginResult.getUserid());
					preferences.setUserStatus(loginResult.getStatus());
					preferences.setIsLogin(false);
					preferences.setIsThirdLogin(false);
					Intent logIntent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(logIntent);
					LoginActivity.this.finish();
				} else {
					NetStatusHandUtil.getInstance().handStatus(LoginActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));

	}

}
