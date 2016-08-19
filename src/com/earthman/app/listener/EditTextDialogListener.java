package com.earthman.app.listener;

import android.widget.EditText;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-27 下午2:13:59
 * @Decription 带输入文本框对话窗口回调接口
 */
public interface EditTextDialogListener {
	void onLeftButtonClick(EditText etContent);
	void onRightButtonClick(EditText etContent);
}
