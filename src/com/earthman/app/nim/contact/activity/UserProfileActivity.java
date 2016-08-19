package com.earthman.app.nim.contact.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.PersonalInfo;
import com.earthman.app.bean.PersonalInfo.ProfessionalStatus;
import com.earthman.app.manager.FriendManager;
import com.earthman.app.nim.NimCache;
import com.earthman.app.nim.contact.constant.UserConstant;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.uikit.cache.FriendDataCache;
import com.earthman.app.nim.uikit.cache.NimUserInfoCache;
import com.earthman.app.nim.uikit.common.ui.dialog.DialogMaker;
import com.earthman.app.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.earthman.app.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.earthman.app.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.earthman.app.nim.uikit.common.ui.imageview.HeadImageView;
import com.earthman.app.nim.uikit.common.ui.widget.SwitchButton;
import com.earthman.app.nim.uikit.common.util.log.LogUtil;
import com.earthman.app.nim.uikit.common.util.sys.NetworkUtil;
import com.earthman.app.nim.uikit.session.constant.Extras;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.ui.activity.mine.SettingPersonalInformation;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.SharePreferenceUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = UserProfileActivity.class.getSimpleName();

	private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
	private final String KEY_BLACK_LIST = "black_list";
	private final String KEY_MSG_NOTICE = "msg_notice";

	private String account;

	// 基本信息
	private HeadImageView headImageView;
	private TextView nameText;
	private ImageView genderImage;
	private TextView accountText;
	private TextView mTvArrows;
	private TextView birthdayText;
	private TextView mobileText;
	private TextView emailText;
	private TextView signatureText;
	private RelativeLayout birthdayLayout;
	private RelativeLayout phoneLayout;
	private RelativeLayout emailLayout;
	private RelativeLayout signatureLayout;
	private RelativeLayout aliasLayout;
	private TextView nickText;
	private RelativeLayout mUserDetailLayout;

	// 开关
	private ViewGroup toggleLayout;
	private Button addFriendBtn;
	private Button removeFriendBtn;
	private Button chatBtn;
	private SwitchButton blackSwitch;
	private SwitchButton noticeSwitch;
	private Map<String, Boolean> toggleStateMap;
	private UserInfoPreferences userInfoPreferences;
	private ProfessionalStatus professionalStatus;

	public static void start(Context context, String account) {
		Intent intent = new Intent();
		intent.setClass(context, UserProfileActivity.class);
		intent.putExtra(Extras.EXTRA_ACCOUNT, account);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile_activity);
		account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
		initTitlebar();
		findViews();
		registerObserver(true);
		doGetFriend(account);
	}

	@Override
	protected void onResume() {
		super.onResume();

		updateUserInfo();
		updateToggleView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		registerObserver(false);
	}

	private void registerObserver(boolean register) {
		FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
	}

	FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> account) {
			updateUserOperatorView();
		}

		@Override
		public void onDeletedFriends(List<String> account) {
			updateUserOperatorView();
		}

		@Override
		public void onAddUserToBlackList(List<String> account) {
			updateUserOperatorView();
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> account) {
			updateUserOperatorView();
		}
	};

	private void findViews() {
		mUserDetailLayout = findView(R.id.user_detail_layout);
		headImageView = findView(R.id.user_head_image);
		nameText = findView(R.id.user_name);
		genderImage = findView(R.id.gender_img);
		accountText = findView(R.id.user_account);
		mTvArrows = findView(R.id.tv_arrows);
		mTvArrows.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf"));
		toggleLayout = findView(R.id.toggle_layout);
		addFriendBtn = findView(R.id.add_buddy);
		chatBtn = findView(R.id.begin_chat);
		removeFriendBtn = findView(R.id.remove_buddy);
		birthdayLayout = findView(R.id.birthday);
		nickText = findView(R.id.user_nick);
		birthdayText = (TextView) birthdayLayout.findViewById(R.id.value);
		phoneLayout = findView(R.id.phone);
		mobileText = (TextView) phoneLayout.findViewById(R.id.value);
		emailLayout = findView(R.id.email);
		emailText = (TextView) emailLayout.findViewById(R.id.value);
		signatureLayout = findView(R.id.signature);
		signatureText = (TextView) signatureLayout.findViewById(R.id.value);
		aliasLayout = findView(R.id.alias);
		((TextView) birthdayLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
		((TextView) phoneLayout.findViewById(R.id.attribute)).setText(R.string.phone);
		((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
		((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);
		((TextView) aliasLayout.findViewById(R.id.attribute)).setText(R.string.alias);

		headImageView.setOnClickListener(this);// 图像放大
		mUserDetailLayout.setOnClickListener(this);// 跳转到个人资料

		addFriendBtn.setOnClickListener(onClickListener);
		chatBtn.setOnClickListener(onClickListener);
		removeFriendBtn.setOnClickListener(onClickListener);
		aliasLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserProfileEditItemActivity.startActivity(UserProfileActivity.this, UserConstant.KEY_ALIAS, account);
			}
		});
	}

	private void initTitlebar() {
		TextView tvLeftTitle = findView(R.id.tv_left);
		tvLeftTitle.setText(R.string.user_profile);
		findView(R.id.btn_back).setOnClickListener(this);
		Button btnEdit = findView(R.id.btn_right);
		btnEdit.setText(R.string.edit);
		btnEdit.setOnClickListener(this);
		btnEdit.setVisibility(NimCache.getAccount().equals(account) ? View.VISIBLE : View.GONE);
	}

	private void addToggleBtn(boolean black, boolean notice) {
		blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
		noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
	}

	private void setToggleBtn(SwitchButton btn, boolean isChecked) {
		btn.setCheck(isChecked);
	}

	private void updateUserInfo() {
		if (NimUserInfoCache.getInstance().hasUser(account)) {
			updateUserInfoView();
			return;
		}

		NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallbackWrapper<NimUserInfo>() {
			@Override
			public void onResult(int code, NimUserInfo result, Throwable exception) {
				updateUserInfoView();
			}
		});
	}

	private void updateUserInfoView() {
		// accountText.setText("帐号：" + account.toUpperCase());
		headImageView.loadBuddyAvatar(account);

		if (NimCache.getAccount().equals(account)) {
			nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
		}

		final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(account);
		if (userInfo == null) {
			LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
			return;
		}

		if (userInfo.getGenderEnum() == GenderEnum.MALE) {
			genderImage.setVisibility(View.VISIBLE);
			genderImage.setBackgroundResource(R.drawable.nim_male);
		} else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
			genderImage.setVisibility(View.VISIBLE);
			genderImage.setBackgroundResource(R.drawable.nim_female);
		} else {
			genderImage.setVisibility(View.GONE);
		}
		// ------------------隐藏--------------------------
		if (!TextUtils.isEmpty(userInfo.getBirthday())) {
			// birthdayLayout.setVisibility(View.VISIBLE);
			birthdayLayout.setVisibility(View.GONE);
			birthdayText.setText(userInfo.getBirthday());
		} else {
			birthdayLayout.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(userInfo.getMobile())) {
			// phoneLayout.setVisibility(View.VISIBLE);
			phoneLayout.setVisibility(View.GONE);
			mobileText.setText(userInfo.getMobile());
		} else {
			phoneLayout.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(userInfo.getEmail())) {
			// emailLayout.setVisibility(View.VISIBLE);
			emailLayout.setVisibility(View.GONE);
			emailText.setText(userInfo.getEmail());
		} else {
			emailLayout.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(userInfo.getSignature())) {
			// signatureLayout.setVisibility(View.VISIBLE);
			signatureLayout.setVisibility(View.GONE);
			signatureText.setText(userInfo.getSignature());
		} else {
			signatureLayout.setVisibility(View.GONE);
		}
		// --------------------------------------------
	}

	private void updateUserOperatorView() {
		chatBtn.setVisibility(View.VISIBLE);
		if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
			removeFriendBtn.setVisibility(View.VISIBLE);
			addFriendBtn.setVisibility(View.GONE);
			updateAlias(true);
		} else {
			addFriendBtn.setVisibility(View.VISIBLE);
			removeFriendBtn.setVisibility(View.GONE);
			updateAlias(false);
		}
	}

	private void updateToggleView() {
		if (NimCache.getAccount() != null && !NimCache.getAccount().equals(account)) {
			boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
			boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
			if (blackSwitch == null || noticeSwitch == null) {
				addToggleBtn(black, notice);
			} else {
				setToggleBtn(blackSwitch, black);
				setToggleBtn(noticeSwitch, notice);
			}
			Log.i(TAG, "black=" + black + ", notice=" + notice);
			updateUserOperatorView();
		}
	}

	private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
		ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(
				R.dimen.isetting_item_height));
		vp.setLayoutParams(vlp);

		TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
		titleText.setText(titleResId);

		SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
		switchButton.setCheck(initState);
		switchButton.setOnChangedListener(onChangedListener);
		switchButton.setTag(key);

		toggleLayout.addView(vp);

		if (toggleStateMap == null) {
			toggleStateMap = new HashMap<String, Boolean>();
		}
		toggleStateMap.put(key, initState);
		return switchButton;
	}

	private void updateAlias(boolean isFriend) {
		if (isFriend) {
			aliasLayout.setVisibility(View.VISIBLE);
			aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.VISIBLE);
			Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
			if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
				nickText.setVisibility(View.VISIBLE);
				nameText.setText(friend.getAlias());
				nickText.setText("昵称：" + NimUserInfoCache.getInstance().getUserName(account));
			} else {
				nickText.setVisibility(View.GONE);
				nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
			}
		} else {
			aliasLayout.setVisibility(View.GONE);
			aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
			nickText.setVisibility(View.GONE);
			nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
		}
	}

	private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
		@Override
		public void OnChanged(View v, final boolean checkState) {
			final String key = (String) v.getTag();
			if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
				Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
				if (key.equals(KEY_BLACK_LIST)) {
					blackSwitch.setCheck(!checkState);
				} else if (key.equals(KEY_MSG_NOTICE)) {
					noticeSwitch.setCheck(!checkState);
				}
				return;
			}

			updateStateMap(checkState, key);

			if (key.equals(KEY_BLACK_LIST)) {
				if (checkState) {
					NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
						@Override
						public void onSuccess(Void param) {
							Toast.makeText(UserProfileActivity.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailed(int code) {
							if (code == 408) {
								Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(UserProfileActivity.this, "on failed：" + code, Toast.LENGTH_SHORT).show();
							}
							updateStateMap(!checkState, key);
							blackSwitch.setCheck(!checkState);
						}

						@Override
						public void onException(Throwable exception) {

						}
					});
				} else {
					NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
						@Override
						public void onSuccess(Void param) {
							Toast.makeText(UserProfileActivity.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailed(int code) {
							if (code == 408) {
								Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
							}
							updateStateMap(!checkState, key);
							blackSwitch.setCheck(!checkState);
						}

						@Override
						public void onException(Throwable exception) {

						}
					});
				}
			} else if (key.equals(KEY_MSG_NOTICE)) {
				NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
					@Override
					public void onSuccess(Void param) {
						if (checkState) {
							Toast.makeText(UserProfileActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(UserProfileActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailed(int code) {
						if (code == 408) {
							Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
						}
						updateStateMap(!checkState, key);
						noticeSwitch.setCheck(!checkState);
					}

					@Override
					public void onException(Throwable exception) {

					}
				});
			}
		}
	};

	private void updateStateMap(boolean checkState, String key) {
		if (toggleStateMap.containsKey(key)) {
			toggleStateMap.put(key, checkState); // update state
			Log.i(TAG, "toggle " + key + "to " + checkState);
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == addFriendBtn) {
				if (FLAG_ADD_FRIEND_DIRECTLY) {
					doAddFriend(null, true); // 直接加为好友
				} else {
					onAddFriendByVerify(); // 发起好友验证请求
				}
			} else if (v == removeFriendBtn) {
				onRemoveFriend();
			} else if (v == chatBtn) {
				SessionHelper.startP2PSession(UserProfileActivity.this, account);
			}
		}
	};

	/**
	 * 通过验证方式添加好友
	 */
	private void onAddFriendByVerify() {
		final EasyEditDialog requestDialog = new EasyEditDialog(this);
		requestDialog.setEditTextMaxLength(32);
		requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
		requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestDialog.dismiss();
			}
		});
		requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestDialog.dismiss();
				String msg = requestDialog.getEditMessage();
				doAddFriend(msg, false);
			}
		});
		requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		requestDialog.show();
	}

	private void doAddFriend(String msg, boolean addDirectly) {
		if (!NetworkUtil.isNetAvailable(this)) {
			Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(account) && account.equals(NimCache.getAccount())) {
			Toast.makeText(UserProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
			return;
		}
		// final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD :
		// VerifyType.VERIFY_REQUEST;
		FriendManager.addFriend(this, account);
		// DialogMaker.showProgressDialog(this, "", true);
		// NIMClient.getService(FriendService.class).addFriend(new
		// AddFriendData(account, verifyType, msg))
		// .setCallback(new RequestCallback<Void>() {
		// @Override
		// public void onSuccess(Void param) {
		// DialogMaker.dismissProgressDialog();
		// updateUserOperatorView();
		// if (VerifyType.DIRECT_ADD == verifyType) {
		// Toast.makeText(UserProfileActivity.this, "添加好友成功",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(UserProfileActivity.this, "添加好友请求发送成功",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		//
		// @Override
		// public void onFailed(int code) {
		// DialogMaker.dismissProgressDialog();
		// if (code == 408) {
		// Toast.makeText(UserProfileActivity.this,
		// R.string.network_is_not_available, Toast
		// .LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast
		// .LENGTH_SHORT).show();
		// }
		// }
		//
		// @Override
		// public void onException(Throwable exception) {
		// DialogMaker.dismissProgressDialog();
		// }
		// });
		//
		// Log.i(TAG, "onAddFriendByVerify");
	}

	private void onRemoveFriend() {
		Log.i(TAG, "onRemoveFriend");
		if (!NetworkUtil.isNetAvailable(this)) {
			Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
			return;
		}
		EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend), getString(R.string.remove_friend_tip),
				true, new EasyAlertDialogHelper.OnDialogActionListener() {

					@Override
					public void doCancelAction() {

					}

					@Override
					public void doOkAction() {
						DialogMaker.showProgressDialog(UserProfileActivity.this, "", true);
						UserInfoPreferences userInfoPreferences = new UserInfoPreferences(UserProfileActivity.this);
						ArrayList<Object> list = new ArrayList<Object>();
						list.add(userInfoPreferences.getUserID());
						list.add(account);
						list.add(userInfoPreferences.getUserToken());
						String loginUrl = HttpUrlConstants.getUrl(UserProfileActivity.this, HttpUrlConstants.DELETE_FRIEND, list);
						executeRequest(new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
							@Override
							public void onResponse(BaseResponse response) {
								DialogMaker.dismissProgressDialog();
								if ("000000".equals(response.getCode())) {
									// 删除成功,保存数据
									Toast.makeText(UserProfileActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
									finish();
								} else {
									NetStatusHandUtil.getInstance().handStatus(UserProfileActivity.this, response.getCode(), response.getMessage());
								}
							}
						}, getErrorListener()));

						// NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new
						// RequestCallback<Void>() {
						// @Override
						// public void onSuccess(Void param) {
						// DialogMaker.dismissProgressDialog();
						// Toast.makeText(UserProfileActivity.this,
						// R.string.remove_friend_success,
						// Toast.LENGTH_SHORT).show();
						// finish();
						// }
						//
						// @Override
						// public void onFailed(int code) {
						// DialogMaker.dismissProgressDialog();
						// if (code == 408) {
						// Toast.makeText(UserProfileActivity.this,
						// R.string.network_is_not_available,
						// Toast.LENGTH_SHORT).show();
						// } else {
						// Toast.makeText(UserProfileActivity.this, "on failed:"
						// + code, Toast.LENGTH_SHORT).show();
						// }
						// }
						//
						// @Override
						// public void onException(Throwable exception) {
						// DialogMaker.dismissProgressDialog();
						// }
						// });
					}
				});
		if (!isFinishing()) {
			dialog.show();
		}
	}

	protected <T extends View> T findView(int resId) {
		return (T) (findViewById(resId));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:// 编辑
			// UserProfileSettingActivity.start(UserProfileActivity.this,
			// account);
			Intent intent1 = new Intent(this, SettingPersonalInformation.class);
			intent1.putExtra("account", account);
			startActivity(intent1);
			break;
		case R.id.user_head_image:// 点击用户头像放大
			Intent intent = new Intent(this, UserImageShower.class);
			intent.putExtra("account", account);
			startActivity(intent);

			break;

		case R.id.user_detail_layout:
			Intent userIntent = new Intent(this, PeronalDetialActivity.class);
			userIntent.putExtra("friendsuserid", professionalStatus.getUniqueid());
			userIntent.putExtra("professionalStatus", (Serializable) professionalStatus);
			startActivity(userIntent);
			break;
		}

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void setAttribute() {

	}

	@Override
	protected void handclick(View v) {

	}

	private void doGetFriend(String account) {
		userInfoPreferences = new UserInfoPreferences(this);
		myLoadingDialog.show();
		ArrayList<Object> list = new ArrayList<Object>();
		SharePreferenceUtil.init(UserProfileActivity.this, Constants.USER_INFO_TABLE);
		list.add(userInfoPreferences.getUserID());
		list.add(account);
		list.add(userInfoPreferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(UserProfileActivity.this, HttpUrlConstants.FRIEND_INFO, list);
		LogUtil.i(TAG, getUrl);
		executeRequest(new FastJsonRequest<PersonalInfo>(Method.GET, getUrl, PersonalInfo.class, new Response.Listener<PersonalInfo>() {
			@Override
			public void onResponse(PersonalInfo response) {
				myLoadingDialog.dismiss();
				if ("000000".equals(response.getCode())) {
					professionalStatus = response.getResult();
					if (professionalStatus == null) {
						return;
					}
					accountText.setText("帐号：" + professionalStatus.getCardId());
				} else {
					NetStatusHandUtil.getInstance().handStatus(UserProfileActivity.this, response.getCode(), response.getMessage());

				}
			}
		}, getErrorListener()));
	}
}
