package com.earthman.app.utils;

import android.content.Context;
import android.content.Intent;

import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.constant.Constent;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.ui.activity.bankcard.PayActivity;
import com.earthman.app.ui.activity.login.RegistActivity;
import com.earthman.app.widget.DialogCommon;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-6-20 上午10:42:39
 * @Decription 用户工具类
 */
public class UserUtils {
	
	//检测用户登录状态
	public static boolean checkState(final Context context) {
		UserInfoPreferences preferences = new UserInfoPreferences(context);
		if (Constants.UNACTIVE.equals(preferences.getUserStatus())) {// 未激活
			DialogCommon commonDialog = new DialogCommon(context, context.getString(R.string.wenxin_inform), context.getString(R.string.active_inform),
					context.getString(R.string.go_active), new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(context, PayActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							context.startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		} else if (Constants.YOUKE.equals(preferences.getUserStatus())) {// 游客身份
			DialogCommon commonDialog = new DialogCommon(context, context.getString(R.string.wenxin_inform), context.getString(R.string.register_inform),
					context.getString(R.string.go_register), new CommonDialogListener() {
						@Override
						public void onRightButtonClick() {
							Intent intent = new Intent(context, RegistActivity.class);
							intent.putExtra("type", Constent.REGIST_TO_SELF);
							context.startActivity(intent);
						}

					});
			commonDialog.show();
			return false;
		}
		return true;
	}

}
