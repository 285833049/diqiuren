package com.earthman.app.nim.session.action;

import java.io.File;

import android.content.Intent;

import com.earthman.app.R;
import com.earthman.app.nim.file.browser.FileBrowserActivity;
import com.earthman.app.nim.uikit.session.actions.BaseAction;
import com.earthman.app.nim.uikit.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class FileAction extends BaseAction {

    public FileAction() {
        super(R.drawable.message_plus_file_selector, R.string.input_panel_file);
    }

    /**
     * **********************文件************************
     */
    private void chooseFile() {
        FileBrowserActivity.startActivityForResult(getActivity(), makeRequestCode(RequestCode.GET_LOCAL_FILE));
    }

    @Override
    public void onClick() {
        chooseFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.GET_LOCAL_FILE) {
            String path = data.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
            File file = new File(path);
            IMMessage message = MessageBuilder.createFileMessage(getAccount(), getSessionType(), file, file.getName());
            sendMessage(message);
        }
    }
}
