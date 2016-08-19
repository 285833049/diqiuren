package com.earthman.app.nim.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.earthman.app.R;
import com.earthman.app.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * 搜索加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class AdvancedTeamSearchActivity extends Activity implements OnClickListener{

    private ClearableEditTextWithIcon searchEditText;

    public static final void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_search_activity);
        findViews();
        initTitlebar();
    }

    private void findViews() {
        searchEditText = (ClearableEditTextWithIcon) findViewById(R.id.team_search_edittext);
        searchEditText.setDeleteImage(R.drawable.nim_grey_delete_icon);
    }

    private void initTitlebar() {
    	findViewById(R.id.btn_back).setOnClickListener(this);
    	((TextView)findViewById(R.id.tv_left)).setText(R.string.search_join_team);
    	Button btnSearch=(Button) findViewById(R.id.btn_right);
    	btnSearch.setText(getString(R.string.search));
    	btnSearch.setOnClickListener(this);
    }

    private void queryTeamById() {
        NIMClient.getService(TeamService.class).searchTeam(searchEditText.getText().toString()).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                updateTeamInfo(team);
            }

            @Override
            public void onFailed(int code) {
                if(code == 803) {
                    Toast.makeText(AdvancedTeamSearchActivity.this, R.string.team_number_not_exist, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdvancedTeamSearchActivity.this,"search team failed: " + code, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(AdvancedTeamSearchActivity.this, "search team exception：" + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 搜索群组成功的回调
     * @param team 群
     */
    private void updateTeamInfo(Team team) {
        if (team.getId().equals(searchEditText.getText().toString())) {
            AdvancedTeamJoinActivity.start(AdvancedTeamSearchActivity.this, team.getId());
        }

    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			 if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                 Toast.makeText(AdvancedTeamSearchActivity.this, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
             } else {
                 queryTeamById();
             }
			break;
		}
	}
}
