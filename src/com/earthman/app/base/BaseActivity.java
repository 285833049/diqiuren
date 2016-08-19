package com.earthman.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.earthman.app.R;
import com.earthman.app.constant.Constent;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.ui.activity.mine.SetPayPwdActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.SystemBarTintManager;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyLoadingDialog;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-22 上午10:55:42
 * @Decription
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

	public int pageNo = 0;// 当前页码
	public int pageSize = 20;// 每页显示的记录数
	public static final String TAG = "EarthMan";
	public UserInfoPreferences preferences;
	protected MyLoadingDialog myLoadingDialog;// 加载进度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ViewUtils.inject(this);
		preferences = new UserInfoPreferences(this);
		myLoadingDialog = new MyLoadingDialog(this, getString(R.string.loading));
		String simpleName = getClass().getSimpleName();
		if (!(simpleName.equals("LoginActivity") || simpleName.equals("PhonesFamilyActivity") || simpleName.equals("PeronalDetialActivity") || simpleName.equals("DeadActivity") || simpleName
				.equals("AccountFamilyPhone")||simpleName.equals("VoiceCallActivity")||simpleName.equals("VideoCallActivity"))) {// 除登录页面全屏显示外，非全屏显示的界面都设置状态栏颜色与标题栏一致
			initSystemBar(this);
		}
		initData();
		initView();
		setAttribute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(getClass().getName());
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		cancelRequest(this);
		NetStatusHandUtil.getInstance().dimissDialog();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}
		handclick(v);
	}

	public void initSystemBar(Activity activity) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			setTranslucentStatus(activity, true);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);

		tintManager.setStatusBarTintEnabled(true);

		// 使用颜色资源

		tintManager.setStatusBarTintResource(R.color.tab_text_hover);

	}

	private void setTranslucentStatus(Activity activity, boolean on) {

		Window win = activity.getWindow();

		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

		if (on) {

			winParams.flags |= bits;

		} else {

			winParams.flags &= ~bits;

		}

		win.setAttributes(winParams);

	}

	/**
	 * 
	 * checkUserStatus(检查用户状态)
	 * void
	 * 
	 * @exception
	 */
	protected boolean checkUserStatus() {
		UserInfoPreferences preferences = new UserInfoPreferences(this);
		if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活
			DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.active_inform), getString(R.string.go_active),
					new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(BaseActivity.this, PayActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		} else if (Constants.YOUKE.equals(preferences.getUserStatus())) {// 游客身份
			DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.register_inform), getString(R.string.go_register),
					new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(BaseActivity.this, RegistActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * showSetPayPwdAlert(显示是否设置支付密码弹窗)
	 * void
	 * 
	 * @exception
	 */
	public void showSetPayPwdAlert() {
		DialogCommon commonDialog = new DialogCommon(this, getString(R.string.wenxin_inform), getString(R.string.set_paypwd_inform), getString(R.string.go_set),
				new CommonDialogListener() {
					@Override
					public void onRightButtonClick() {
						startActivity(new Intent(BaseActivity.this, SetPayPwdActivity.class));
					}

				});
		commonDialog.show();
	}

	/**
	 * 执行网络请求
	 * 
	 * @param request
	 */
	public void executeRequest(Request<?> request) {
		if(AndroidUtils.isNetworkAvailable(this)){
			RequestManager.getInstance().addRequest(request, this);
		}else{
			MyToast.makeText(this, R.string.net_error, Toast.LENGTH_SHORT).show();
			dismissLoading();
		}
		
		
	}

	/**
	 * 执行带标志的网络请求
	 * 
	 * @param request
	 * @param tag
	 */
	protected void executeRequest(Request<?> request, Object tag) {
		RequestManager.getInstance().addRequest(request, tag);
	}

	/**
	 * 取消指定标志的网络请求
	 * 
	 * @param tag
	 */
	protected void cancelRequest(Object tag) {
		RequestManager.getInstance().cancelAll(tag);
	}

	/**
	 * 处理网络请求失败时的监听器
	 * 
	 * @return
	 */
	protected Response.ErrorListener getErrorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissLoading();
				MyToast.makeText(BaseActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
				LogUtil.e(TAG, error.toString());
			}
		};
	}

	/**
	 * 
	 * showLoading(显示加载进度)
	 * void
	 * 
	 * @exception
	 */
	protected void showLoading() {
		if (myLoadingDialog != null) {
			myLoadingDialog.show();
		}

	}

	/**
	 * 
	 * dismissLoading(取消加载进度)
	 * void
	 * 
	 * @exception
	 */
	protected void dismissLoading() {
		if (myLoadingDialog != null) {
			myLoadingDialog.dismiss();
		}
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 显示或隐藏键盘
	 */
	protected void showOrHideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideKeyboard(v, ev)) {
				hideKeyboard(v);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideKeyboard(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}

	// 初始化数据
	protected abstract void initData();

	// 初始化UI控件
	protected abstract void initView();

	// 设置控件属性
	protected abstract void setAttribute();

	// 点击事件
	protected abstract void handclick(View v);

	// 获取保存的设置
	protected void getSaveState(Bundle savedInstanceState) {

	}

}
