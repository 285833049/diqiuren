package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 特许申请
 * 
 * @author xiexianyong
 * 
 */
@ContentView(R.layout.franchise_application_activity)
public class SetingApplyDie extends BaseActivity {
	@ViewInject(R.id.franchise_application_btn)
	Button mBtnNext;
	@ViewInject(R.id.btn_back)
	Button mBtnLeft;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	@ViewInject(R.id.personal_detail_earthid)
	EditText mEtApplyID;

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.franchise_application_btn:

			if (TextUtils.isEmpty(mEtApplyID.getEditableText().toString())) {
				MyToast.makeText(SetingApplyDie.this, R.string.input_earth_id, Toast.LENGTH_LONG).show();
				return;
			}
			getApplicationDeath();
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * getApplicationDeath(申请死亡)
	 * void
	 * 
	 * @exception
	 */
	private void getApplicationDeath() {
		showLoading();
		String cardId = mEtApplyID.getEditableText().toString();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(cardId);
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.FRANCHISE_APPLICATION, list);
		executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, getUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				dismissLoading();
				if ("000000".equals(response.getCode())) {
					showMyDialog();
				} else {
					NetStatusHandUtil.getInstance().handStatus(SetingApplyDie.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	private void showMyDialog() {
		DialogOK myDialog = new DialogOK(this, getString(R.string.dialog_title), getString(R.string.die_commit_tips));
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
		myDialog.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SetingApplyDie.this.finish();
			}
		});
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnNext.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.franchise_application);
	}
}
