package com.earthman.app.base;

import android.content.ContentValues;
import android.content.Context;

/**
 * @Title: UserInfoPreferences
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月10日
 */

public class UserInfoPreferences extends BasePreferences {
	// 保存用户信息的表名
	private final static String PERFERENCE_NAME = "user_info";
	/**
	 * shareperfence保存的用户信息
	 */

	public static final String USER_TOKEN = "token";// 用户token
	public static final String USER_ID = "userid";// 用户id
	public static final String USER_PAY_ID = "userpayid";// 用户支付id
	public static final String USER_PHONE = "telephone";// 用户电话号码
	public static final String USER_NAME = "username";// 用户昵称
	public static final String USER_MONEY = "usermoney";// 用户资产
	public static final String USER_CARDID = "usercardid";// 用户地球人id
	public static final String USER_UNIQUEID = "uniqueid";// 唯一标示
	public static final String USER_YOUXIAOQI = "useryear";// 用户有效期
	public static final String USER_CREAD_TIME = "usercreatat";// 用户创建时间
	public static final String USER_ISFIRST_OPEN = "isfirstopen";// 用户是否第一次进入app
	public static final String USER_IS_LOGIN = "islogin";// 用户是否登陆
	public static final String USER_PAY_PSW = "paypsw";// 支付
	private Context context;
	private static final String USER_ICO = "avatar";// 用户头像
	private static final String USER_CARD_ID = "cardId";// 地球人id
	private static final String USER_REAL_NAME = "realName";// 用户姓名
	private static final String USER_GENDER = "gender";// 性别
	private static final String USER_BIRTHDAY = "birthday";// 生日
	private static final String USER_BLOOD = "blood";// 血型
	private static final String USER_HEIGHT = "height";// 身高
	private static final String USER_WEIGHT = "weight";// /体重
	private static final String USER_FEELINGS = "feelings";// 情感
	private static final String USER_DEGREES = "degrees";// 学历
	private static final String USER_PROFESSIONAL = "professional";// 职业
	private static final String USER_EMERGENCYCONTACT = "emergencyContact";// 紧急联系人
	private static final String USER_COMPANY_ADDRESS = "companyAddress";// 工作地点
	private static final String USER_HOME_ADDRESS = "homeAddress";// 籍贯
	private static final String USER_STATUS = "userstatus";// 用户状态
	private static final String USER_LIFE = "life";// 个性签名
	private static final String IS_GOING_ADD_MESSAGE = "Isgoingaddmessage";// 个性签名
	private static final String IS_THIRD_LOGIN = "isThirdLogin";// 第三方登录标示
	private static final String DUE_DATE = "duedate";// 有效期
	private static final String CREATEDAT = "createdAt";// 注册时间
	private static final String ISSET_PAYPWD = "isSetPayPwd";// 是否设置过支付密码 0未设置过
																// 1设置过
	private static final String HER_UNIQUEID = "heruniqueid";// 对方uniqueid

	/**
	 * @param context
	 * @param table
	 */
	public UserInfoPreferences(Context context) {
		super(context, PERFERENCE_NAME);
		this.context = context;
	}

	public void setCreatedAt(long createdAt) {
		putValueLong(createdAt, CREATEDAT);
	}

	public Long getCreatedAt() {
		return getLong(CREATEDAT);
	}

	public void setIsSetPwd(int isSetPwd) {
		putValueint(isSetPwd, ISSET_PAYPWD);
	}

	public int getIsSetPwd() {
		return getInt(ISSET_PAYPWD);
	}

	/**
	 * 
	 * getDueDate(获取有效期)
	 * 
	 * @return String
	 * @exception
	 */
	public String getDueDate() {
		return getString(DUE_DATE);
	}

	public void setUserDueDate(String duedate) {
		putValueStr(duedate, DUE_DATE);
	}

	// public static final String USER_PAY_PSW="paypsw";//支付

	/**
	 * 存放支付密码
	 */
	public void setUserPayPsw(String userpaypsw) {

		putValueStr(userpaypsw, USER_PAY_PSW);
	}

	/**
	 * 存放用户token
	 * 
	 * @param version
	 */
	public void setUserToken(String usertoken) {
		putValueStr(usertoken, USER_TOKEN);
	}

	/**
	 * 存放用户状态
	 * 
	 * @param usertoken
	 */
	public void setUserStatus(String userstatus) {
		putValueStr(userstatus, USER_STATUS);
	}

	/**
	 * 
	 * setIsgoingaddmessage(存放是否有新添加好友信息)
	 * 
	 * @param isgoingadd
	 *            void
	 * @exception
	 */
	public void setIsgoingaddmessage(Boolean isgoingadd) {
		putValueBoolean(isgoingadd, IS_GOING_ADD_MESSAGE);
	}

	/**
	 * 存放用户id
	 * 
	 * @param version
	 */
	public void setUserID(String userid) {
		putValueStr(userid, USER_ID);
	}

	public void setUserpayID(String userid) {
		putValueStr(userid, USER_PAY_ID);
	}

	public String getUserpayID() {
		return getString(USER_PAY_ID);
	}

	/**
	 * 存放用户手机号
	 * 
	 * @param phone
	 */
	public void setUserPhone(String phone) {
		putValueStr(phone, USER_PHONE);
	}

	/**
	 * 是否第一次打开app
	 * 
	 * @param isfirst
	 */
	public void setIsfirstLogin(Boolean isfirst) {
		putValueBoolean(isfirst, USER_ISFIRST_OPEN);
	}

	/**
	 * 是否登录
	 * 
	 * @param isLogin
	 */
	public void setIsLogin(boolean isLogin) {
		putValueBoolean(isLogin, USER_IS_LOGIN);
	}

	/**
	 * 
	 * setIsThirdLogin(设置是否第三方登录)
	 * 
	 * @param isThirdLogin
	 *            void
	 * @exception
	 */
	public void setIsThirdLogin(boolean isThirdLogin) {
		putValueBoolean(isThirdLogin, IS_THIRD_LOGIN);
	}

	/**
	 * 存放用户昵称
	 */
	public void setUsernice(String username) {
		putValueStr(username, USER_NAME);
	}

	/**
	 * 存放用户头像
	 */
	public void setUserIco(String avatar) {
		putValueStr(avatar, USER_ICO);
	}

	/**
	 * 存放用户地球人id
	 */
	public void setUsercardId(String cardId) {
		putValueStr(cardId, USER_CARD_ID);

	}

	public void setUseruniqueid(String uniqueid) {
		putValueStr(uniqueid, USER_UNIQUEID);
	}

	/**
	 * 存放他人的uniqueid
	 */
	public void setHeruniqueid(String heruniqueid) {
		putValueStr(heruniqueid, HER_UNIQUEID);

	}

	/**
	 * 得到他人的uniqueid
	 */
	public String getHeruniqueid() {
		return getString(HER_UNIQUEID);
	}

	/**
	 * 存放用户姓名
	 */
	public void setUserrealName(String realName) {
		putValueStr(realName, USER_REAL_NAME);
	}

	/**
	 * 存放用户性别
	 */
	public void setUserGender(String gender) {
		putValueStr(gender, USER_GENDER);
	}

	/**
	 * 存放用户生日
	 */
	public void setUserBirthday(String birthday) {
		putValueStr(birthday, USER_BIRTHDAY);
	}

	/**
	 * 存放用户血型
	 */
	public void setUserBlood(String blood) {
		putValueStr(blood, USER_BLOOD);
	}

	/**
	 * 存放用户身高
	 */
	public void setUserHeight(String height) {
		putValueStr(height, USER_HEIGHT);
	}

	/**
	 * 存放用户体重
	 */
	public void setUserWeight(String weight) {
		putValueStr(weight, USER_WEIGHT);
	}

	/**
	 * 存放用户情感
	 */
	public void setUserFeelings(String feelings) {
		putValueStr(feelings, USER_FEELINGS);
	}

	/**
	 * 存放用户学历
	 */
	public void setUserDegrees(String degrees) {
		putValueStr(degrees, USER_DEGREES);
	}

	/**
	 * 存放用户职业
	 */
	public void setUserProfessional(String professional) {
		putValueStr(professional, USER_PROFESSIONAL);
	}

	/**
	 * 存放用户紧急联系人
	 */
	public void setUseremergencyContact(String emergencyContact) {
		putValueStr(emergencyContact, USER_EMERGENCYCONTACT);
	}

	/**
	 * 存放用户工作地点
	 */

	public void setUserCompanyAddress(String companyAddress) {
		putValueStr(companyAddress, USER_COMPANY_ADDRESS);
	}

	/**
	 * 存放用户籍贯
	 */
	public void setUserHomeAddress(String homeAddress) {
		putValueStr(homeAddress, USER_HOME_ADDRESS);
	}

	/**
	 * 存放用户个性签名
	 */
	public void setUserLife(String life) {
		putValueStr(life, USER_LIFE);
	}

	/**
	 * 获取用户iD
	 * 
	 * @return
	 */
	public String getUserID() {
		return getString(USER_ID);
	}

	/**
	 * 获取支付密码
	 */
	public String getUserPayPsw() {
		return getString(USER_PAY_PSW);
	}

	/**
	 * 获取用户token
	 * 
	 * @return
	 */
	public String getUserToken() {
		return getString(USER_TOKEN);
	}

	/**
	 * 获取用户电话
	 * 
	 * @return
	 */
	public String getUserPhone() {
		return getString(USER_PHONE);
	}

	/**
	 * 获取用户头像
	 */
	public String getUserIco() {
		return getString(USER_ICO);
	}

	/**
	 * 获取地球人id
	 * 
	 * @return
	 */
	public String getUsercardId() {
		return getString(USER_CARD_ID);
	}

	public String getUseruniqueId() {
		return getString(USER_UNIQUEID);
	}

	/**
	 * 获取用户姓名
	 * 
	 * @return
	 */
	public String getUserrealName() {
		return getString(USER_REAL_NAME);
	}

	/**
	 * 获取用户性别
	 * 
	 * @return
	 */
	public String getUserGender() {
		return getString(USER_GENDER);
	}

	/**
	 * 获取用户生日
	 * 
	 * @return
	 */
	public String getUserBirthday() {
		return getString(USER_BIRTHDAY);
	}

	/**
	 * 获取用户血型
	 * 
	 * @return
	 */
	public String getUserBlood() {
		return getString(USER_BLOOD);
	}

	/**
	 * 存放用户状态
	 * 
	 * @param usertoken
	 */
	public String getUserStatus() {
		return getString(USER_STATUS);
	}

	/**
	 * 获取用户身高
	 * 
	 * @return
	 */
	public String getUserHeight() {
		return getString(USER_HEIGHT);
	}

	/**
	 * 获取用户体重
	 * 
	 * @return
	 */
	public String getUserWeight() {
		return getString(USER_WEIGHT);
	}

	/**
	 * 获取用户情感
	 * 
	 * @return
	 */
	public String getUserFeelings() {
		return getString(USER_FEELINGS);
	}

	/**
	 * 获取用户学历
	 * 
	 * @return
	 */
	public String getUserDegrees() {
		return getString(USER_DEGREES);
	}

	/**
	 * 获取用户职业
	 * 
	 * @return
	 */
	public String getUserProfessional() {
		return getString(USER_PROFESSIONAL);
	}

	/**
	 * 获取紧急联系人
	 * 
	 * @return
	 */
	public String getUseremergencyContact() {
		return getString(USER_EMERGENCYCONTACT);
	}

	/**
	 * 获取用户工作地点
	 * 
	 * @return
	 */
	public String getUserCompanyAddress() {
		return getString(USER_COMPANY_ADDRESS);
	}

	/**
	 * 用户籍贯
	 * 
	 * @return
	 */
	public String getUserHomeAddress() {
		return getString(USER_HOME_ADDRESS);
	}

	/**
	 * 获取用户个性签名
	 */
	public String getUserLife() {
		return getString(USER_LIFE);
	}

	/**
	 * 获取用户昵称
	 */
	public String getUsernice() {
		return getString(USER_NAME);
	}

	/**
	 * 获取是否第一次打开app
	 * 
	 * @return
	 */
	public boolean getIsFirstOpen() {
		return getBoolean(USER_ISFIRST_OPEN);
	}

	public boolean getIsLogin() {
		return getBoolean(USER_IS_LOGIN);
	}

	public boolean getIsThirdLogin() {
		return getBoolean(IS_THIRD_LOGIN);
	}

	/**
	 * 存放单个数据Str
	 * 
	 * @param value
	 * @param key
	 */
	private void putValueStr(String value, String key) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(key, value);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存放单个数据boolean
	 * 
	 * @param value
	 * @param key
	 */
	private void putValueBoolean(Boolean value, String key) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(key, value);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存放int
	 * 
	 * @param value
	 * @param key
	 */
	private void putValueint(int value, String key) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(key, value);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存放Long
	 * 
	 * @param value
	 * @param key
	 */
	private void putValueLong(Long value, String key) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(key, value);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
