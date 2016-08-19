package com.earthman.app.nim.uikit.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.common.util.string.StringUtil;

/**
 * Created by hzxuwen on 2015/3/19.
 */
public class AdvancedTeamNicknameActivity extends Activity implements TextWatcher, View.OnClickListener {

	private static Handler handler;

	// constant
	public static final String EXTRA_NAME = "EXTRA_NAME";
	public static final int REQ_CODE_TEAM_NAME = 20;
	private static final int MAX_LENGTH = 32;

	// view
	private EditText regularTeamNickname;

	// data
	private String nickName;

	public static void start(Context context, String name) {
		Intent intent = new Intent();
		intent.setClass(context, AdvancedTeamNicknameActivity.class);
		intent.putExtra(EXTRA_NAME, name);
		((Activity) context).startActivityForResult(intent, REQ_CODE_TEAM_NAME);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nim_advanced_team_nickname_activity);
		setTitle(R.string.team_nickname);

		nickName = getIntent().getStringExtra(EXTRA_NAME);
		if (nickName == null) {
			nickName = "";
		}

		// ActionBarUtil.addRightClickableTextViewOnActionBar(this,
		// R.string.save, this);
		initTitlebar();

		regularTeamNickname = (EditText) findViewById(R.id.regular_team_nickname);
		regularTeamNickname.setText(nickName);
		regularTeamNickname.addTextChangedListener(this);
		regularTeamNickname.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
					// do nothing, just consume ACTION_UP event
					return true;
				}
				return false;
			}

		});
		regularTeamNickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
		showKeyboardDelayed(regularTeamNickname);
		findViewById(R.id.background).setOnClickListener(this);
	}

	private void initTitlebar() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_left)).setText(R.string.team_nickname);
		Button btnSave = (Button) findViewById(R.id.btn_right);
		btnSave.setText(getString(R.string.save));
		btnSave.setOnClickListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		int editEnd = regularTeamNickname.getSelectionEnd();
		regularTeamNickname.removeTextChangedListener(this);
		while (StringUtil.counterChars(s.toString()) > MAX_LENGTH && editEnd > 0) {
			s.delete(editEnd - 1, editEnd);
			editEnd--;
		}
		regularTeamNickname.setSelection(editEnd);
		regularTeamNickname.addTextChangedListener(this);
	}

	private void complete() {
		if (TextUtils.isEmpty(regularTeamNickname.getText().toString())) {
			Toast.makeText(this, R.string.team_name_toast, Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = getIntent();
			intent.putExtra(EXTRA_NAME, regularTeamNickname.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		showKeyboard(false);
		super.onBackPressed();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			finish();
			break;
		case R.id.btn_right:
			showKeyboard(false);
			complete();
			break;
		case R.id.background:
			showKeyboard(false);
			break;
		}
	}
}
