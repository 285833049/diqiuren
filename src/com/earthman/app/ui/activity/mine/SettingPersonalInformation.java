package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.AccountInfo;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.PersonalInfo;
import com.earthman.app.bean.PersonalInfo.ProfessionalStatus;
import com.earthman.app.eventbusbean.OKDynamicSend;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.ui.activity.login.ProvinceCityCountryActivity;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyListDialog;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.RoundCornerImageView;
import com.earthman.app.widget.UploadImgDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 设置个人信息
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-7-14 下午3:39:32
 * @Decription
 */
@ContentView(R.layout.set_personal_detail)
public class SettingPersonalInformation extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.personal_detail_layout)
	LinearLayout test_pop_layout;
	@ViewInject(R.id.btn_back)
	Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;
	@ViewInject(R.id.btn_right)
	Button btn_right;
	@ViewInject(R.id.personal_detail_earthid)
	TextView personal_detail_earthid;

	@ViewInject(R.id.personal_detail_name)
	EditText personal_detail_name;
	@ViewInject(R.id.personal_detail_name_btn)
	TextView personal_detail_name_btn;

	@ViewInject(R.id.personal_detail_nice)
	TextView personal_detail_nice;
	@ViewInject(R.id.personal_detail_nice_btn)
	TextView personal_detail_nice_btn;

	@ViewInject(R.id.personal_detail_gender)
	TextView personal_detail_gender;
	@ViewInject(R.id.personal_detail_gender_btn)
	TextView personal_detail_gender_btn;

	@ViewInject(R.id.personal_detail_birthday)
	TextView personal_detail_birthday;
	@ViewInject(R.id.personal_detail_birthday_btn)
	TextView personal_detail_birthday_btn;

	@ViewInject(R.id.personal_detail_blood)
	TextView personal_detail_blood;
	@ViewInject(R.id.personal_detail_blood_btn)
	TextView personal_detail_blood_btn;

	@ViewInject(R.id.personal_detail_height)
	EditText personal_detail_height;
	@ViewInject(R.id.personal_detail_height_btn)
	TextView personal_detail_height_btn;

	@ViewInject(R.id.personal_detail_weight)
	EditText personal_detail_weight;
	@ViewInject(R.id.personal_detail_weight_btn)
	TextView personal_detail_weight_btn;

	@ViewInject(R.id.personal_detail_contact)
	TextView personal_detail_contact;
	@ViewInject(R.id.personal_detail_contact_btn)
	TextView personal_detail_contact_btn;

	@ViewInject(R.id.personal_detail_emotion)
	TextView personal_detail_emotion;
	@ViewInject(R.id.personal_detail_emotion_btn)
	TextView personal_detail_emotion_btn;

	@ViewInject(R.id.personal_detail_education)
	EditText personal_detail_education;
	@ViewInject(R.id.personal_detail_education_btn)
	TextView personal_detail_education_btn;

	@ViewInject(R.id.personal_detail_occupation)
	TextView personal_detail_occupation;
	@ViewInject(R.id.personal_detail_occupation_btn)
	TextView personal_detail_occupation_btn;

	@ViewInject(R.id.personal_detail_emergency)
	TextView personal_detail_emergency;
	@ViewInject(R.id.personal_detail_emergency_btn)
	TextView personal_detail_emergency_btn;

	@ViewInject(R.id.personal_detail_work_place)
	TextView personal_detail_work_place;
	@ViewInject(R.id.personal_detail_work_place_btn)
	TextView personal_detail_work_place_btn;

	@ViewInject(R.id.personal_detail_hometowns)
	TextView personal_detail_hometowns;
	@ViewInject(R.id.personal_detail_hometowns_btn)
	TextView personal_detail_hometowns_btn;

	@ViewInject(R.id.personal_detail_autograph)
	TextView personal_detail_autograph;
	@ViewInject(R.id.personal_detail_autograph_btn)
	TextView personal_detail_autograph_btn;
	@ViewInject(R.id.set_personal_sex)
	RelativeLayout set_personal_sex;
	@ViewInject(R.id.rl_head)
	private RelativeLayout rl_head;
	@ViewInject(R.id.set_imageview)
	private RoundCornerImageView imageView;
	private ActivityResultListener listener;
	private UploadImgDialog uploadImgDialog;
	private UserInfoPreferences userInfoPreferences;
	MyListDialog myListDialog;
	ListView listView;
	String columnname;
	int addressCode;
	String address;
	final ArrayList<String> arrayList = new ArrayList<String>();
	private YwbImageLoader imageLoader;
	String account;
	ProfessionalStatus result;

	@Override
	protected void initData() {
		getuserinfo();// 获取用户信息
		account = getIntent().getStringExtra("account");
		myListDialog = new MyListDialog(this, arrayList);
		String[] sexs = getResources().getStringArray(R.array.modify_persional_sex);
		String[] booldtypes = getResources().getStringArray(R.array.modify_persional_bloodtype);
		String[] emotionStatus = getResources().getStringArray(R.array.modify_persional_emotion_status);
		String[] educationStatus = getResources().getStringArray(R.array.modify_persional_education_status);
		String[] jobStatus = getResources().getStringArray(R.array.modify_persional_job_status);
		sexList = Arrays.asList(sexs);
		boolList = Arrays.asList(booldtypes);
		emotionList = Arrays.asList(emotionStatus);
		educationList = Arrays.asList(educationStatus);
		jobList = Arrays.asList(jobStatus);

	}

	/**
	 * 获取个人信息
	 */
	private void getuserinfo() {
		userInfoPreferences = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		showLoading();
		String getUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_FRIEND_INFO, list);
		executeRequest(new FastJsonRequest<PersonalInfo>(Method.GET, getUrl, PersonalInfo.class, new Response.Listener<PersonalInfo>() {

			@Override
			public void onResponse(PersonalInfo response) {
				if ("000000".equals(response.getCode())) {
					result = response.getResult();
					personal_detail_name.setText(result.getRealName());
					personal_detail_earthid.setText(result.getCardId());// 地球人代码
					personal_detail_name.setText(result.getRealName());// 姓名
					personal_detail_contact.setText(result.getContact());// 手机
					personal_detail_nice.setText(result.getNice());// 昵称
					personal_detail_gender.setText(result.getGender());// 性别
					personal_detail_birthday.setText(result.getBirthday());// 生日
					personal_detail_blood.setText(result.getBlood());// 血型
					personal_detail_height.setText(result.getHeight());// 身高
					personal_detail_weight.setText(result.getWeight());// 体重
					personal_detail_emotion.setText(result.getFeelings());// 情感
					personal_detail_education.setText(result.getDegrees());// 学历
					personal_detail_occupation.setText(result.getProfessional());// 职业
					personal_detail_emergency.setText(result.getEmergencyContact());// 紧急联系人
					personal_detail_work_place.setText(result.getCompanyAddress());// 工作地点
					personal_detail_hometowns.setText(result.getHomeAddress());// 籍贯
					personal_detail_autograph.setText(result.getLife());// 个性签名

				} else {
					NetStatusHandUtil.getInstance().handStatus(SettingPersonalInformation.this, response.getCode(), response.getMessage());
				}

				dismissLoading();
			}
		}, getErrorListener()));
	}

	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.set_personal_sex:
			arrayList.clear();
			arrayList.addAll(sexList);
			myListDialog.setTextView(personal_detail_gender);
			myListDialog.show();
			break;

		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.personal_detail_name_btn:
			columnname = "realName";
			showPopupWindow(personal_detail_name_btn);
			break;
		case R.id.personal_detail_nice_btn:
			columnname = "nice";
			showPopupWindow(personal_detail_nice_btn);
			break;
		case R.id.personal_detail_gender_btn:
			columnname = "gender";
			showPopupWindow(personal_detail_gender_btn);
			break;
		case R.id.personal_detail_birthday_btn:
			columnname = "birthday";
			showPopupWindow(personal_detail_birthday_btn);
			break;
		case R.id.personal_detail_blood_btn:
			columnname = "blood";
			showPopupWindow(personal_detail_blood_btn);
			break;
		case R.id.personal_detail_height_btn:
			columnname = "height";
			showPopupWindow(personal_detail_height_btn);
			break;
		case R.id.personal_detail_weight_btn:
			columnname = "weight";
			showPopupWindow(personal_detail_weight_btn);
			break;
		case R.id.personal_detail_contact_btn:
			columnname = "contact";
			showPopupWindow(personal_detail_contact_btn);
			break;
		case R.id.personal_detail_emotion_btn:
			columnname = "feelings";
			showPopupWindow(personal_detail_emotion_btn);
			break;
		case R.id.personal_detail_education_btn:
			columnname = "degrees";
			showPopupWindow(personal_detail_education_btn);
			break;
		case R.id.personal_detail_occupation_btn:
			columnname = "professional";
			showPopupWindow(personal_detail_occupation_btn);
			break;
		case R.id.personal_detail_emergency_btn:
			columnname = "emergencyContact";
			showPopupWindow(personal_detail_emergency_btn);
			break;
		case R.id.personal_detail_work_place_btn:
			columnname = "companyAddress";
			showPopupWindow(personal_detail_work_place_btn);
			break;
		case R.id.personal_detail_hometowns_btn:
			columnname = "homeAddress";
			showPopupWindow(personal_detail_hometowns_btn);
			break;
		case R.id.personal_detail_autograph_btn:
			columnname = "life";
			showPopupWindow(personal_detail_autograph_btn);
			break;
		case R.id.rl_head:
			if (uploadImgDialog == null) {
				uploadImgDialog = new UploadImgDialog(SettingPersonalInformation.this, UploadImgDialog.UPLOAD_HEAD, imageView, null);
				listener = uploadImgDialog;
			}
			if (!uploadImgDialog.isShowing()) {
				uploadImgDialog.show();
			}
			break;

		case R.id.personal_detail_blood:// 血型
			arrayList.clear();
			arrayList.addAll(boolList);
			myListDialog.setTextView(personal_detail_blood);
			myListDialog.show();

			break;

		case R.id.personal_detail_emotion:// 情感
			arrayList.clear();
			arrayList.addAll(emotionList);
			myListDialog.setTextView(personal_detail_emotion);
			myListDialog.show();
			break;

		case R.id.personal_detail_education:// 学历
			arrayList.clear();
			arrayList.addAll(educationList);
			myListDialog.setTextView(personal_detail_education);
			myListDialog.show();
			break;
		case R.id.personal_detail_occupation:// 职业
			arrayList.clear();
			arrayList.addAll(jobList);
			MyListDialog ListDialog = new MyListDialog(this, arrayList, 212);
			ListDialog.setTextView(personal_detail_occupation);
			ListDialog.show();
			break;
		case R.id.btn_right:// 保存
			myLoadingDialog.show();
			commituserinfo();
			break;

		case R.id.personal_detail_work_place:// 选择地址
			ProvinceCityCountryActivity.intoActivity(SettingPersonalInformation.this, ProvinceCityCountryActivity.PAGETYPE_PROVINCE, 0, "", "", 1);

			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		this.finish();
		super.onBackPressed();
	}

	/**
	 * 修改用户资料
	 */
	private void commituserinfo() {

		String realName = personal_detail_name.getEditableText().toString();// 真实姓名
		final String nice = personal_detail_nice.getEditableText().toString();// 昵称
		String cardId = personal_detail_earthid.getText().toString();// 地球人id
		String gender = personal_detail_gender.getText().toString();// 性别
		String blood = personal_detail_blood.getText().toString();// 血型
		String height = TextUtils.isEmpty(personal_detail_height.getEditableText().toString()) ? "0" : personal_detail_height.getEditableText().toString();// 身高
		String weight = TextUtils.isEmpty(personal_detail_weight.getEditableText().toString()) ? "0" : personal_detail_weight.getEditableText().toString();// 体重
		String contact = personal_detail_contact.getEditableText().toString(); // 联系人
		String feelings = personal_detail_emotion.getText().toString(); // 情感状态
		String degrees = personal_detail_education.getEditableText().toString();// 学历
		String professional = personal_detail_occupation.getText().toString();// 职业
		String emergencyContact = personal_detail_emergency.getText().toString();// 紧急联系人
		String companyAddress = personal_detail_work_place.getText().toString();// 工作地址
		String homeAddress = personal_detail_hometowns.getText().toString();// 籍贯
		String life = personal_detail_autograph.getEditableText().toString();// 个性签名

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(userInfoPreferences.getUserToken());
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("userId", userInfoPreferences.getUserID());
			jsonRequest.put("realName", realName);
			jsonRequest.put("nice", nice);
			jsonRequest.put("cardId", cardId);
			jsonRequest.put("gender", gender);
			jsonRequest.put("blood", blood);
			jsonRequest.put("height", height);
			jsonRequest.put("weight", weight);
			jsonRequest.put("contact", contact);
			jsonRequest.put("feelings", feelings);
			jsonRequest.put("degrees", degrees);
			jsonRequest.put("professional", professional);
			jsonRequest.put("emergencyContact", emergencyContact);
			jsonRequest.put("companyAddress", companyAddress);
			jsonRequest.put("homeAddress", homeAddress);
			jsonRequest.put("life", life);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String getUrl = HttpUrlConstants.getPostUrl(this, HttpUrlConstants.SET_USER_MODIFY_INFO, list);
		LogUtil.i(TAG, getUrl);
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("修改用户资料返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					myLoadingDialog.dismiss();
					MyToast.makeText(SettingPersonalInformation.this, getString(R.string.edit_success), Toast.LENGTH_LONG).show();
					preferences.setUsernice(nice);
					EventBus.getDefault().post(new OKDynamicSend(1));// 发送通知通知动态界面更改用户头像和昵称
				} else {
					myLoadingDialog.dismiss();
					NetStatusHandUtil.getInstance().handStatus(SettingPersonalInformation.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, getUrl, jsonRequest, listener, getErrorListener()) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}

		};
		executeRequest(jsonObjectRequest);
	}

	int privacystatus;
	Dialog dialog;
	private List<String> sexList;
	private List<String> boolList;
	private List<String> emotionList;
	private List<String> educationList;
	private List<String> jobList;
	private String userStatusStr;

	// 设置权限弹出提示框
	@SuppressLint("InflateParams")
	private void showPopupWindow(final TextView tvStatus) {

		LayoutInflater inflaterDl = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_layout, null);
		// 对话框
		dialog = new AlertDialog.Builder(SettingPersonalInformation.this).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		RadioGroup radio_group = (RadioGroup) layout.findViewById(R.id.radio_group);
		final RadioButton cb1 = (RadioButton) layout.findViewById(R.id.cb1);
		final RadioButton cb2 = (RadioButton) layout.findViewById(R.id.cb2);
		final RadioButton cb3 = (RadioButton) layout.findViewById(R.id.cb3);
		radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == cb1.getId()) {
					privacystatus = Constants.PERSONINFO_PUBLIC;
					userStatusStr = getString(R.string.public_information);
				} else if (checkedId == cb2.getId()) {
					privacystatus = Constants.PERSONINFO_PAY;
					userStatusStr = getString(R.string.payment_information);
				} else if (checkedId == cb3.getId()) {
					privacystatus = Constants.PERSONAINFO_PROTECT;
					userStatusStr = getString(R.string.five_information);
				}
			}
		});

		// 取消按钮
		TextView btnCancel = (TextView) layout.findViewById(R.id.personal_detail_set_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 确定按钮
		TextView btnOk = (TextView) layout.findViewById(R.id.personal_detail_set_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// tvStatus.setText(userStatusStr);
				myLoadingDialog.show();
				setPrivacy(privacystatus);
			}

		});
	}

	/**
	 * 获取数据
	 * 
	 * @param privacystatus
	 */
	private void setPrivacy(int privacystatus) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(columnname);
		list.add(privacystatus);
		list.add(userInfoPreferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(SettingPersonalInformation.this, HttpUrlConstants.SET_PRIVACY, list);
		Log.d("xianyong", "getUrl:" + getUrl);
		executeRequest(new FastJsonRequest<AccountInfo>(Method.GET, getUrl, AccountInfo.class, new Response.Listener<AccountInfo>() {

			@Override
			public void onResponse(AccountInfo response) {
				if ("000000".equals(response.getCode())) {
					myLoadingDialog.dismiss();
					MyToast.makeText(SettingPersonalInformation.this, getString(R.string.set_friend), Toast.LENGTH_LONG).show();
					dialog.dismiss();
					hideSetedBtn();
				} else {
					myLoadingDialog.dismiss();
					NetStatusHandUtil.getInstance().handStatus(SettingPersonalInformation.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (listener != null) {
			listener.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		imageLoader = new YwbImageLoader();

		btn_right.setText(getString(R.string.set_groups4));
		userInfoPreferences = new UserInfoPreferences(this);
		/**
		 * imageLoader.loadImage(userInfoPreferences.getUserIco(), imageView,
		 * R.drawable.user_avatar);
		 * personal_detail_earthid.setText(userInfoPreferences.getUsercardId());
		 * // 地球人id
		 * personal_detail_name.setText(userInfoPreferences.getUserrealName());
		 * personal_detail_contact.setText(userInfoPreferences.getUserPhone());/
		 * / 手机号码
		 * personal_detail_nice.setText(userInfoPreferences.getUsernice());// 昵称
		 * personal_detail_gender.setText(userInfoPreferences.getUserGender());/
		 * / 性别
		 * 
		 * // 生日
		 * personal_detail_birthday.setText(userInfoPreferences.getUserBirthday(
		 * ));
		 * personal_detail_blood.setText(userInfoPreferences.getUserBlood());
		 * String userHeight = userInfoPreferences.getUserHeight();
		 * if (0 == Integer.parseInt(userHeight)) {
		 * personal_detail_height.setText("");
		 * } else {
		 * personal_detail_height.setText(userInfoPreferences.getUserHeight());/
		 * / 身高
		 * }
		 * String userWeight = userInfoPreferences.getUserWeight();
		 * if (0 == Integer.parseInt(userWeight)) {
		 * personal_detail_weight.setText("");
		 * } else {
		 * personal_detail_weight.setText(userInfoPreferences.getUserWeight());/
		 * / 体重
		 * }
		 * personal_detail_emotion.setText(userInfoPreferences.getUserFeelings()
		 * );// 情感
		 * personal_detail_education.setText(userInfoPreferences.getUserDegrees(
		 * ));// 学历
		 * personal_detail_occupation.setText(userInfoPreferences.
		 * getUserProfessional());// 职业
		 * personal_detail_emergency.setText(userInfoPreferences.
		 * getUseremergencyContact());// 紧急联系人
		 * 
		 * personal_detail_hometowns.setText(userInfoPreferences.
		 * getUserHomeAddress());// 籍贯
		 * personal_detail_autograph.setText(userInfoPreferences.getUserLife());
		 * // 个性签名
		 * personal_detail_work_place.setText(userInfoPreferences.
		 * getUserCompanyAddress());// 工作地点
		 * 
		 */
		/*
		 * if (address == null || address.equals("")) {
		 * personal_detail_work_place.setText(userInfoPreferences.
		 * getUserCompanyAddress());// 工作地点
		 * } else {
		 * personal_detail_work_place.setText(address);// 选择后的新地址
		 * }
		 */
		personal_detail_name_btn.setOnClickListener(this);
		personal_detail_nice_btn.setOnClickListener(this);
		personal_detail_gender_btn.setOnClickListener(this);
		personal_detail_birthday_btn.setOnClickListener(this);
		personal_detail_blood_btn.setOnClickListener(this);
		personal_detail_height_btn.setOnClickListener(this);
		personal_detail_weight_btn.setOnClickListener(this);
		personal_detail_contact_btn.setOnClickListener(this);
		personal_detail_emotion_btn.setOnClickListener(this);
		personal_detail_education_btn.setOnClickListener(this);
		personal_detail_occupation_btn.setOnClickListener(this);
		personal_detail_emergency_btn.setOnClickListener(this);
		personal_detail_work_place_btn.setOnClickListener(this);
		personal_detail_hometowns_btn.setOnClickListener(this);
		personal_detail_autograph_btn.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		rl_head.setOnClickListener(this);
		set_personal_sex.setOnClickListener(this);
		personal_detail_blood.setOnClickListener(this);
		personal_detail_emotion.setOnClickListener(this);
		personal_detail_education.setOnClickListener(this);
		personal_detail_occupation.setOnClickListener(this);
		personal_detail_work_place.setOnClickListener(this);
		setAllPrivacy();

	}

	@Override
	protected void setAttribute() {
		tv_left.setText(userInfoPreferences.getUsernice());
	}

	private void setAllPrivacy() {
		// if (result == null) {
		// return;
		// }
		// personal_detail_name_btn.setText(getPrivacyText(result.getRealNameStatus()));
		// personal_detail_contact_btn.setText(getPrivacyText(result.getContactStatus()));
		// personal_detail_nice_btn.setText(getPrivacyText(result.getNiceStatus()));
		// personal_detail_gender_btn.setText(getPrivacyText(result.getGenderStatus()));
		// personal_detail_birthday_btn.setText(getPrivacyText(result.getBirthdayStatus()));
		// personal_detail_blood_btn.setText(getPrivacyText(result.getBloodStatus()));
		// personal_detail_height_btn.setText(getPrivacyText(result.getHeightStatus()));
		// personal_detail_weight_btn.setText(getPrivacyText(result.getWeightStatus()));
		// personal_detail_emotion_btn.setText(getPrivacyText(result.getFeelingsStatus()));
		// personal_detail_education_btn.setText(getPrivacyText(result.getDegreesStatus()));
		// personal_detail_occupation_btn.setText(getPrivacyText(result.getProfessionalStatus()));
		// personal_detail_emergency_btn.setText(getPrivacyText(result.getEmergencyContactStatus()));
		// personal_detail_work_place_btn.setText(getPrivacyText(result.getCompanyAddressStatus()));
		// personal_detail_hometowns_btn.setText(getPrivacyText(result.getHomeAddressStatus()));
		// personal_detail_autograph_btn.setText(getPrivacyText(result.getLifeStatus()));
	}

	private void hideSetedBtn() {
		if ("realName".equals(columnname)) {
			personal_detail_name_btn.setVisibility(View.INVISIBLE);
		} else if ("nice".equals(columnname)) {
			personal_detail_nice_btn.setVisibility(View.INVISIBLE);
		} else if ("gender".equals(columnname)) {
			personal_detail_gender_btn.setVisibility(View.INVISIBLE);
		} else if ("birthday".equals(columnname)) {
			personal_detail_birthday_btn.setVisibility(View.INVISIBLE);
		} else if ("blood".equals(columnname)) {
			personal_detail_blood_btn.setVisibility(View.INVISIBLE);
		} else if ("height".equals(columnname)) {
			personal_detail_height_btn.setVisibility(View.INVISIBLE);
		} else if ("weight".equals(columnname)) {
			personal_detail_weight_btn.setVisibility(View.INVISIBLE);
		} else if ("contact".equals(columnname)) {
			personal_detail_contact_btn.setVisibility(View.INVISIBLE);
		} else if ("feelings".equals(columnname)) {
			personal_detail_emotion_btn.setVisibility(View.INVISIBLE);
		} else if ("degrees".equals(columnname)) {
			personal_detail_education_btn.setVisibility(View.INVISIBLE);
		} else if ("professional".equals(columnname)) {
			personal_detail_occupation_btn.setVisibility(View.INVISIBLE);
		} else if ("emergencyContact".equals(columnname)) {
			personal_detail_emergency_btn.setVisibility(View.INVISIBLE);
		} else if ("companyAddress".equals(columnname)) {
			personal_detail_work_place_btn.setVisibility(View.INVISIBLE);
		} else if ("homeAddress".equals(columnname)) {
			personal_detail_hometowns_btn.setVisibility(View.INVISIBLE);
		} else if ("life".equals(columnname)) {
			personal_detail_autograph_btn.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 * showPrivacy(这里用一句话描述这个方法的作用)
	 * 
	 * @param status
	 *            void
	 * @exception
	 */
	private String getPrivacyText(String status) {
		String string = getString(R.string.permission_settings);
		if (TextUtils.isEmpty(status) || Integer.parseInt(status) == Constants.PERSONINFO_NOT_SET) {
			return string;
		}
		switch (Integer.parseInt(status)) {
		case Constants.PERSONINFO_PUBLIC:
			string = getString(R.string.public_information);
			break;
		case Constants.PERSONINFO_PAY:
			string = getString(R.string.payment_information);
			break;
		case Constants.PERSONAINFO_PROTECT:
			string = getString(R.string.five_protect);
		default:
			break;
		}
		return string;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null)
			return;
		addressCode = intent.getIntExtra("addressCode", addressCode);
		address = intent.getStringExtra("address");
		personal_detail_work_place.setText(address);// 选择后的新地址
		super.onNewIntent(intent);
	}

}
