package com.earthman.app.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.earthman.app.R;
import com.earthman.app.constant.Constent;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyLoadingDialog;
import com.earthman.app.widget.MyToast;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 作者：Zhou
 * 日期：2015-10-10 上午10:38:50
 * 版权：地球人
 * 描述：（碎片基类）
 */

public abstract class BaseFragment extends Fragment implements OnClickListener {

	protected View convertView;
	protected Activity activity;
	protected ViewGroup container;
	protected static final String TAG = "EarthMan";
	protected UserInfoPreferences preferences;
	protected YwbImageLoader ywbImageLoader;
	protected MyLoadingDialog myLoadingDialog;// 加载进度
	protected int pageNo;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		myLoadingDialog = new MyLoadingDialog(getActivity(), getActivity().getString(R.string.loading));
		super.onAttach(activity);
		preferences = new UserInfoPreferences(activity);
		ywbImageLoader = new YwbImageLoader();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.container = container;
		if (convertView == null) {
			convertView = createView();
			initData();
			initView(convertView);
			setAttribute();
		} else {
			ViewGroup viewGroup = (ViewGroup) convertView.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(convertView);
			}
		}
		return convertView;
	}

	/**
	 * 
	 * checkUserStatus(这里用一句话描述这个方法的作用)
	 * 
	 * @return
	 * @exception
	 */
	protected boolean checkUserStatus() {
		UserInfoPreferences preferences = new UserInfoPreferences(activity);
		if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活
			DialogCommon commonDialog = new DialogCommon(activity, getString(R.string.wenxin_inform), getString(R.string.active_inform),
					getString(R.string.go_active), new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(activity, PayActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		} else if (Constants.YOUKE.equals(preferences.getUserStatus())) {// 游客身份
			DialogCommon commonDialog = new DialogCommon(activity, getString(R.string.wenxin_inform), getString(R.string.register_inform),
					getString(R.string.go_register), new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(activity, RegistActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}
		handclick(v);
	}

	@Override
	public void onDestroy() {
		cancelRequest(this);
		NetStatusHandUtil.getInstance().dimissDialog();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * 执行网络请求
	 * 
	 * @param request
	 */
	public void executeRequest(Request<?> request) {
		if (AndroidUtils.isNetworkAvailable(activity)) {
			RequestManager.getInstance().addRequest(request, this);
		} else {
			MyToast.makeText(activity, R.string.net_error, Toast.LENGTH_SHORT).show();
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
				LogUtil.e("网络请求错误", error.toString());
				dismissLoading();
				MyToast.makeText(activity, R.string.server_error, Toast.LENGTH_LONG).show();

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

	protected abstract View createView();

	protected abstract void initData();

	protected abstract void initView(View convertView);

	protected abstract void setAttribute();

	protected abstract void handclick(View v);

	/**
	 * Fragment返回事件 <br/>
	 * 
	 * @param [参数1]-[参数1说明] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public boolean onBackPress() {
		return false;
	};
}
