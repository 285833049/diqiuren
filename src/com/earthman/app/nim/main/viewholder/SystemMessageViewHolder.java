package com.earthman.app.nim.main.viewholder;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.nim.main.helper.MessageHelper;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.uikit.cache.NimUserInfoCache;
import com.earthman.app.nim.uikit.common.adapter.TViewHolder;
import com.earthman.app.nim.uikit.common.ui.dialog.DialogMaker;
import com.earthman.app.nim.uikit.common.ui.imageview.HeadImageView;
import com.earthman.app.nim.uikit.common.util.sys.TimeUtil;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyToast;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

/**
 * Created by huangjun on 2015/3/18.
 */
public class SystemMessageViewHolder extends TViewHolder {

	private SystemMessage message;
	private HeadImageView headImageView;
	private TextView fromAccountText;
	private TextView timeText;
	private TextView contentText;
	private View operatorLayout;
	private Button agreeButton;
	private Button rejectButton;
	private TextView operatorResultText;
	private SystemMessageListener listener;
	private UserInfoPreferences userInfoPreferences;

	public interface SystemMessageListener {
		void onAgree(SystemMessage message);

		void onReject(SystemMessage message);

		void onLongPressed(SystemMessage message);
	}

	@Override
	protected int getResId() {
		return R.layout.message_system_notification_view_item;
	}

	@Override
	protected void inflate() {
		headImageView = (HeadImageView) view.findViewById(R.id.from_account_head_image);
		fromAccountText = (TextView) view.findViewById(R.id.from_account_text);
		contentText = (TextView) view.findViewById(R.id.content_text);
		timeText = (TextView) view.findViewById(R.id.notification_time);
		operatorLayout = view.findViewById(R.id.operator_layout);
		agreeButton = (Button) view.findViewById(R.id.agree);
		rejectButton = (Button) view.findViewById(R.id.reject);
		operatorResultText = (TextView) view.findViewById(R.id.operator_result);
		view.setBackgroundResource(R.drawable.list_item_bg_selecter);
		
	}

	@Override
	protected void refresh(Object item) {
		message = (SystemMessage) item;
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (listener != null) {
					listener.onLongPressed(message);
				}
				return true;
			}
		});
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent(SystemMessageViewHolder.this.context,PeronalDetialActivity.class);
				intent.putExtra("friendsuserid", message.getFromAccount());
				start(SystemMessageViewHolder.this.context, intent);				
			}
		});
		headImageView.loadBuddyAvatar(message.getFromAccount());
		fromAccountText.setText(NimUserInfoCache.getInstance().getUserDisplayNameEx(message.getFromAccount()));
		contentText.setText(MessageHelper.getVerifyNotificationText(message));
		timeText.setText(TimeUtil.getTimeShowString(message.getTime(), false));
		if (!MessageHelper.isVerifyMessageNeedDeal(message)) {
			operatorLayout.setVisibility(View.GONE);
		} else {
			if (message.getStatus() == SystemMessageStatus.init) {
				// 未处理
				operatorResultText.setVisibility(View.GONE);
				agreeButton.setVisibility(View.VISIBLE);
				rejectButton.setVisibility(View.VISIBLE);
			} else {
				// 处理结果
				agreeButton.setVisibility(View.GONE);
				rejectButton.setVisibility(View.GONE);
				operatorResultText.setVisibility(View.VISIBLE);
				operatorResultText.setText(MessageHelper.getVerifyNotificationDealResult(message));
			}
		}
	}

	public void refreshDirectly(final SystemMessage message) {
		if (message != null) {
			refresh(message);
		}
	}

	public void setListener(final SystemMessageListener l) {
		if (l == null) {
			return;
		}
	
		this.listener = l;
		agreeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doGetAddFriend(message.getFromAccount());
				setReplySending();
				listener.onAgree(message);
			}
		});
		rejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setReplySending();
				listener.onReject(message);
			}
		});
	}


	private void start(Context context, Intent intent) {
		context.startActivity(intent);		
	}

	/**
	 * 等待服务器返回状态设置
	 */
	protected void setReplySending() {
		agreeButton.setVisibility(View.GONE);
		rejectButton.setVisibility(View.GONE);
		operatorResultText.setVisibility(View.VISIBLE);
		operatorResultText.setText(R.string.team_apply_sending);
	}
	
//	public void setContext(Context context){
//		this.this.context=MyApplication.getInstance().getApplicationContext();
//	}

	/**
	 * 添加好友请求
	 * 
	 * @param romAccount
	 */
	private void doGetAddFriend(final String fromAccount) {
		DialogMaker.showProgressDialog(this.context, null, false);
		userInfoPreferences = new UserInfoPreferences(this.context);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(fromAccount);
		list.add(userInfoPreferences.getUserToken());
		list.add(Constants.ADD_FRIEND_TO_TONGYI);// 直接加好友
		String loginUrl = HttpUrlConstants.getUrl(this.context, HttpUrlConstants.ADD_FRIEND, list);

		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class,
				new Response.Listener<BaseResponse>() {

					@Override
					public void onResponse(BaseResponse response) {
						DialogMaker.dismissProgressDialog();
						if ("000000".equals(response.getCode())) {
							MyToast.makeText(context, R.string.add_friend_success, Toast.LENGTH_LONG).show();
							SessionHelper.startP2PSession(context, fromAccount);
							setReplySending();
						}else if(response.getMessage().equals("你们已经是好友")){
							MyToast.makeText(context,R.string.team_join_successed, Toast.LENGTH_LONG).show();
						} else {
							NetStatusHandUtil.getInstance().handStatus(context, response.getCode(), response.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						DialogMaker.dismissProgressDialog();
						MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
					}
				});

		executeRequest(fastJsonRequest);
	}

	/**
	 * 执行网络请求
	 * 
	 * @param request
	 */
	public void executeRequest(Request<?> request) {
		if (AndroidUtils.isNetworkAvailable(this.context)) {
			RequestManager.getInstance().addRequest(request, this);
		} else {
			MyToast.makeText(this.context, R.string.net_error, Toast.LENGTH_SHORT).show();
		}

	}
}
