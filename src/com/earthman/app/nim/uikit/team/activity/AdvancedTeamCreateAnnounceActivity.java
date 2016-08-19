package com.earthman.app.nim.uikit.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.cache.SimpleCallback;
import com.earthman.app.nim.uikit.cache.TeamDataCache;
import com.earthman.app.nim.uikit.common.ui.dialog.DialogMaker;
import com.earthman.app.nim.uikit.common.util.sys.NetworkUtil;
import com.earthman.app.nim.uikit.team.helper.AnnouncementHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * 创建群公告界面
 * Created by hzxuwen on 2015/3/18.
 */
public class AdvancedTeamCreateAnnounceActivity extends Activity implements OnClickListener {

    // constant
    private final static String EXTRA_TID = "EXTRA_TID";
    
    private Button mBtnSave;

    // data
    private String teamId;
    private String announce;

    // view
    private EditText teamAnnounceTitle;
    private EditText teamAnnounceContent;

    public static void startActivityForResult(Activity activity, String teamId, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, AdvancedTeamCreateAnnounceActivity.class);
        intent.putExtra(EXTRA_TID, teamId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_create_announce);
        setTitle(R.string.team_annourcement);

        parseIntentData();
        findViews();
        initTitlebar();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_TID);
    }

    private void findViews() {
        teamAnnounceTitle = (EditText) findViewById(R.id.team_announce_title);
        teamAnnounceContent = (EditText) findViewById(R.id.team_announce_content);
        teamAnnounceTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(64)});
        teamAnnounceContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1024)});
    }

    private void initTitlebar() {
    	findViewById(R.id.btn_back).setOnClickListener(this);
    	((TextView)findViewById(R.id.tv_left)).setText(R.string.save);
    	mBtnSave=(Button) findViewById(R.id.btn_right);
    	mBtnSave.setText(getString(R.string.save));
    	mBtnSave.setOnClickListener(this);
    }

    private void requestAnnounceData() {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(teamAnnounceTitle.getText().toString())) {
            Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.team_announce_notice, Toast.LENGTH_SHORT).show();
            return;
        }

        mBtnSave.setEnabled(false);
        // 请求群信息
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamData(t);
            updateAnnounce();
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamData(result);
                        updateAnnounce();
                    } else {
                    	mBtnSave.setEnabled(true);
                    }
                }
            });
        }
    }

    /**
     * 获得最新公告内容
     *
     * @param team 群
     */
    private void updateTeamData(Team team) {
        if (team == null) {
            Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            showKeyboard(false);
            finish();
        } else {
            announce = team.getAnnouncement();
        }
    }

    /**
     * 创建公告更新到服务器
     */
    private void updateAnnounce() {
        String announcement = AnnouncementHelper.makeAnnounceJson(announce, teamAnnounceTitle.getText().toString(),
                teamAnnounceContent.getText().toString());
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Announcement, announcement).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                setResult(Activity.RESULT_OK);
                showKeyboard(false);
                finish();
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                mBtnSave.setEnabled(true);
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                mBtnSave.setEnabled(true);
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
            }
        });
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			 requestAnnounceData();
			break;

		default:
			break;
		}
	}
}
