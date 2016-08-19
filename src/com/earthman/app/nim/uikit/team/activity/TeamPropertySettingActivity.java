package com.earthman.app.nim.uikit.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.common.util.string.StringTextWatcher;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;

/**
 * 群属性
 * Created by hzxuwen on 2015/4/10.
 */
public class TeamPropertySettingActivity extends Activity implements View.OnClickListener {

	private static final String EXTRA_TID = "EXTRA_TID";
	public static final String EXTRA_DATA = "EXTRA_DATA";
	private static final String EXTRA_FIELD = "EXTRA_FIELD";

	private static Handler handler;

	// view
	private EditText editText;
	private TextView mTvLeftTitle;

	// data
	private String teamId;
	private TeamFieldEnum filed;
	private String initialValue;

	/**
	 * 修改群某一个属性公用界面
	 * 
	 * @param activity
	 * @param teamId
	 * @param field
	 * @param initialValue
	 * @param requestCode
	 */
	public static void start(Activity activity, String teamId, TeamFieldEnum field, String initialValue, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, TeamPropertySettingActivity.class);
		intent.putExtra(EXTRA_TID, teamId);
		intent.putExtra(EXTRA_DATA, initialValue);
		intent.putExtra(EXTRA_FIELD, field);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 修改群某一个属性公用界面
	 * 
	 * @param context
	 * @param teamId
	 * @param field
	 * @param initialValue
	 */
	public static void start(Context context, String teamId, TeamFieldEnum field, String initialValue) {
		Intent intent = new Intent();
		intent.setClass(context, TeamPropertySettingActivity.class);
		intent.putExtra(EXTRA_TID, teamId);
		intent.putExtra(EXTRA_DATA, initialValue);
		intent.putExtra(EXTRA_FIELD, field);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nim_team_name_activity);

		findViews();
		parseIntent();

	}

	private void parseIntent() {
		teamId = getIntent().getStringExtra(EXTRA_TID);
		filed = (TeamFieldEnum) getIntent().getSerializableExtra(EXTRA_FIELD);
		initialValue = getIntent().getStringExtra(EXTRA_DATA);
		initTitlebar();
		initData();

	}

	private void initTitlebar() {
		mTvLeftTitle = (TextView) findViewById(R.id.tv_left);
		Button btnRight = (Button) findViewById(R.id.btn_right);
		btnRight.setText(R.string.save);
		findViewById(R.id.btn_back).setOnClickListener(this);
		btnRight.setOnClickListener(this);
	}

	private void initData() {
		int limit = 0;
		switch (filed) {
		case Name:
			mTvLeftTitle.setText(R.string.team_settings_name);
			editText.setHint(R.string.team_settings_set_name);
			limit = 64;
			break;
		case Introduce:
			mTvLeftTitle.setText(R.string.team_introduce);
			editText.setHint(R.string.team_introduce_hint);
			limit = 512;
			break;
		case Extension:
			mTvLeftTitle.setText(R.string.team_extension);
			editText.setHint(R.string.team_extension_hint);
			limit = 65535;
			break;
		}

		if (!TextUtils.isEmpty(initialValue)) {
			editText.setText(initialValue);
			editText.setSelection(initialValue.length());
		}
		editText.addTextChangedListener(new StringTextWatcher(limit, editText));
	}

	private void findViews() {
		editText = (EditText) findViewById(R.id.discussion_name);
		editText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP;
			}

		});
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					complete();
					return true;
				} else {
					return false;
				}
			}
		});
		showKeyboardDelayed(editText);

		LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.background);
		backgroundLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showKeyboard(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_back) {
			HideKeyboard(v);
			onBackPressed();
			finish();
		} else if (i == R.id.btn_right) {
			showKeyboard(false);
			complete();
		}
	}

	// 隐藏虚拟键盘
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		}
	}

	/**
	 * 点击保存
	 */
	private void complete() {
		if (filed == TeamFieldEnum.Name) {
			if (TextUtils.isEmpty(editText.getText().toString())) {
				Toast.makeText(this, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
			} else {
				char[] s = editText.getText().toString().toCharArray();
				int i;
				for (i = 0; i < s.length; i++) {
					if (String.valueOf(s[i]).equals(" ")) {
						Toast.makeText(this, R.string.now_allow_space, Toast.LENGTH_SHORT).show();
						break;
					}
				}
				if (i == s.length) {
					saveTeamProperty();
				}
			}
		} else {
			if (TextUtils.equals(editText.getText().toString(), initialValue)) {
				showKeyboard(false);
				finish();
			} else if (TextUtils.isEmpty(teamId)) {
				saved();
			} else {
				saveTeamProperty();
			}
		}
	}

	private void saved() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATA, editText.getText().toString());
		setResult(Activity.RESULT_OK, intent);
		showKeyboard(false);
		finish();
	}

	/**
	 * 保存设置
	 */
	private void saveTeamProperty() {
		if (teamId == null) { // 讨论组创建时，设置群名称
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DATA, editText.getText().toString());
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else {
			// HashMap<TeamFieldEnum, Serializable> fields = new HashMap<>();
			// fields.put(TeamFieldEnum.Name, "test batch name");
			// fields.put(TeamFieldEnum.Introduce, "test batch introduce");
			// fields.put(TeamFieldEnum.Extension, "test batch extension");
			// NIMClient.getService(TeamService.class).updateTeamFields(teamId,
			// fields);

			NIMClient.getService(TeamService.class).updateTeam(teamId, filed, editText.getText().toString()).setCallback(new RequestCallback<Void>() {
				@Override
				public void onSuccess(Void param) {
					Toast.makeText(TeamPropertySettingActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
					saved();
				}

				@Override
				public void onFailed(int code) {
					Toast.makeText(TeamPropertySettingActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onException(Throwable exception) {
					Toast.makeText(TeamPropertySettingActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	//
	// @Override
	// public void onBackPressed() {
	// showKeyboard(false);
	// super.onBackPressed();
	// }

	protected final Handler getHandler() {
		if (handler == null) {
			handler = new Handler(getMainLooper());
		}
		return handler;
	}

	/**
	 * 延时弹出键盘
	 * 
	 * @param focus
	 *            键盘的焦点项
	 */
	protected void showKeyboardDelayed(View focus) {
		final View viewToFocus = focus;
		if (focus != null) {
			focus.requestFocus();
		}

		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (viewToFocus == null || viewToFocus.isFocused()) {
					showKeyboard(true);
				}
			}
		}, 200);
	}

	protected void showKeyboard(boolean isShow) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			if (getCurrentFocus() == null) {
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			} else {
				imm.showSoftInput(getCurrentFocus(), 0);
			}
		} else {
			if (getCurrentFocus() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	//
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// onBackPressed();
	// }
}
