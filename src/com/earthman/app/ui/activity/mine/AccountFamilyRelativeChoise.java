package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.adapter.MyGridAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.listener.EditTextDialogListener;
import com.earthman.app.widget.DialogEditText;
import com.earthman.app.widget.MyGridViewView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 亲情号－>选择关系
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * 				@Date：2016-3-17 下午5:01:32
 * @Decription 修改 Eric
 */
@ContentView(R.layout.choice_relation_activity)
public class AccountFamilyRelativeChoise extends BaseActivity {

	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;

	@ViewInject(R.id.gridview)
	MyGridViewView mRelativeGv;
	public MyGridAdapter mRelativeGvAdapter;
	Dialog dialog;
	private List<String> relativeList;

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
		String[] relativeArrays = getResources().getStringArray(R.array.relative_family);
		relativeList=new ArrayList<String>(Arrays.asList(relativeArrays));//防止抛出java.lang.UnsupportedOperationException异常
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mRelativeGvAdapter = new MyGridAdapter(this,relativeList);
		mRelativeGv.setAdapter(mRelativeGvAdapter);
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.choice_relation);

	}

	public void showPopupWindow() {
		DialogEditText myDialogEditText = new DialogEditText(this, getResources().getString(R.string.new_relative_title),
				getResources().getString(R.string.new_relative_hint), "", new EditTextDialogListener() {
					@Override
					public void onRightButtonClick(EditText etContent) {
						relativeList.add(relativeList.size()-1,etContent.getText().toString().trim());
						mRelativeGvAdapter.notifyDataSetChanged();
					}
					@Override
					public void onLeftButtonClick(EditText etContent) {

					}
				});
		myDialogEditText.setCancelable(false);
		myDialogEditText.show();
	}

}
