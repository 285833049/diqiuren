package com.earthman.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.MineUpdateInfo;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.PayFactory;

import de.greenrobot.event.EventBus;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-28 下午6:15:22
 * @Decription
 */
public class PaySuccessReceiver extends BroadcastReceiver{

	private Handler handler;
	/* 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent == null){
			return;
		}
		if(intent.getAction().equals(Constants.PAY_SUCCESS_ACTION)){
			int currentCase = Constants.currentPayReason;
			if(handler != null){
				handler.sendEmptyMessage(currentCase);
			}
			switch (currentCase) {
			case PayFactory.CASE_ACTIVE_SELF://激活本账户
				new UserInfoPreferences(context).setUserStatus(Constants.SHARE_ACTIVED);
				EventBus.getDefault().post(new MineUpdateInfo());
				break;
			case PayFactory.CASE_ACTIVE_FRIEND://激活好友
				
				break;
			case PayFactory.CASE_ADDQINQING://注册亲情号				
				break;
			case PayFactory.CASE_JINGXIAN://献花
				
				break;
			case PayFactory.CASE_PAY_FOR_LOOK://付费查看
				break;
			case PayFactory.CASE_PAY_FOR_PHONE://话费充值
				break;
			case PayFactory.CASE_REGISTER_FOR_FRIEND://替好友注册
				break;
			default:
				break;
			}
		}
		
	}
	
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}

}
