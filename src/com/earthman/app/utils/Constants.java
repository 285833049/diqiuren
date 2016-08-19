package com.earthman.app.utils;

import android.os.Environment;

/**
 * 作者：Zhou 日期：2016-3-8 下午4:58:27 描述：（）
 */
public class Constants {

	public static final String PACKAGE_NAME = "com.earthman.app";
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;
	public static final String ADDR_DB_PATH = DB_PATH + "/" + "addr.db";
	public static final String PHONE_DB_PATH = DB_PATH + "/" + "phone.db";
	/**
	 * shareperfence保存的用户信息
	 */

	public static final String USER_INFO_TABLE = "userinfo";// 用户sp文件名
	public static final String USER_TOKEN = "token";// 用户token
	public static final String USER_ID = "userid";// 用户id
	public static final String USER_PHONE = "telephone";// 用户电话号码
	public static final String USER_NAME = "username";// 用户昵称
	public static final String USER_MONEY = "usermoney";// 用户资产
	public static final String USER_CARDID = "usercardid";// 用户地球人id
	public static final String USER_YOUXIAOQI = "useryear";// 用户有效期
	public static final String USER_CREAD_TIME = "usercreatat";// 用户创建时间
	public static final int ADD_FRIEND_TO_SOMEONE = 0;// 0：用户A单方申请添加B
	public static final int ADD_FRIEND_TO_TONGYI = 1;// 1：用户B确认添加A
	public static final String UMENGLOGIN = "com.umeng.login";
	public static final int SET_PAY_PSW_NO = 0;// result:0未设置过，1已经设置
	public static final int SET_PAY_PSW_OK = 1;// result:0未设置过，1已经设置

	public static final int LIFEPAY = 0;// 缴费方式（0:趸缴，1:年缴）
	public static final int YEARPAY = 1;// 年缴
	public static final int PAYSTYLE_ACTIVATE=1;
	public static final int PAYSTYLE_EARTH = 2;// 支付方式：（1:激活币；2:地球币:3:微信；4:支付宝:5:银联:6:paypal）

	public static final String UNACTIVE = "未激活";
	public static final String SHARE_ACTIVED = "已激活";
	public static final String DEAD = "死亡";
	public static final String YOUKE = "游客";

	public static final int TYPE_FORGET_PAYPWD = 7;

	public static final String WX_APPID = "wx852ca3cedd82a9d1";
	public static final String WX_APPSECRET = "a5f89a0649e62ea0553ecf70cc8eb517";
	public static final String API_KEY = "e10adc3949ba59asde56e057f20f883e";
	// 商户号
	public static final String MCH_ID = "1315545601";

	public static final int APP_SHARE = 1;// 分享APP下载链接
	public static final int INVITE_FRIEND_SHARE = 2;// 邀请好友
	public static final int QRCODE_SHARE = 3;//分享二维码
	public static final String APP_DOWNLOAD_URL = "http://test.yysw2015.com/App.php";	
	public static final String INVITE_REGISTER_URL = "http://test.yysw2015.com/register?id=";

	public static int currentPayReason;// 当前缴费的缘由

	public static final int ACTIVE_PAY = 1;// 账户激活缴费
	
	public static final String PAY_SUCCESS_ACTION = "paysuccess";
	
	public static final String REFRESH_ACTIVITY_ACTION = "refresh_activity";//刷新界面action
	
	public static final String UPLOAD_SUCCESS_ACTION = "upload_success";//上传视频成功
	public static final String UPLOAD_FAIL_ACTION = "upload_fail";//上传失败
	public static final String UPLOAD_START_ACTION = "upload_start";//开始上传
	public static final String UPLOAD_LOAD_ACTION = "upload_load";//正在上传
	
	public static final String MODIFY_REMARK = "modify_remark";//修改备注

	public static final int REQUEST_SETPERSONDETAIL = 0x01;//刷新我的界面
	/**
	 * 其他信息
	 */
	public static final int MAX_AGE = 150;// 最大年龄
	public static final int MAX_EDITTEXT_NUM = 300;// 最大年龄

	public static final int THEIR_ACTIVATIOON = 1;// 自己激活
	public static final int INSTEAD_ACTIVATION = 2;// 代替别人激活
	
	public static final int NOT_SET_PRIORITY = 0;//未设置
	public static final int PUBLIC_PRIORITY = 1;//公开
	public static final int PAY_PRIORITY = 2;//付费
	public static final int PROTECT_PRIORITY = 3;//五级防护
	
	public static final String ALLLIFE_ACTIVITED = "永久";
	
	/**
	 * 个人中心权限
	 */
	public static final int PERSONINFO_NOT_SET = 4;// 个人信息未设置
	public static final int PERSONINFO_PUBLIC = 0;//个人信息公开
	public static final int PERSONINFO_PAY = 1;//个人信息付费
	public static final int PERSONAINFO_PROTECT = 3;//个人信息五级防护
	
	/**
	 * 缓存数据
	 */
	public class DBName{
		public static final String NIM_FILE="nim_file";
		
	}
	public final static int NEAR_SEARCH = 0;//周边检索
	public final static int KEY_SEARCH = 1;//关键字检索

	
}
