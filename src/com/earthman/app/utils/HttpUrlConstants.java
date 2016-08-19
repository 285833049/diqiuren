package com.earthman.app.utils;

import java.util.ArrayList;

import android.content.Context;

import com.earthman.app.enums.NetworkType;

/**
 * 作者：Zhou 日期：2015-10-15 上午10:05:49 版权：地球人 描述：（http请求相关数据）
 */
public class HttpUrlConstants {

	public static boolean isDevelopmentMode = false;// 是否为开发模式
	public static NetworkType mNetworkType = NetworkType.OFFICIAL;

	/**
	 * 正式服务器IP地址
	 */
	public final static String SERVER_IP = "www.dqr2558.com:8080";// 请求接口ip地址

	/**
	 * 调试环境IP地址
	 */
	private final static String DEBUG_SERVER_IP = "www.yuwubao.com:8080";// 请求接口ip地址
	// private final static String DEBUG_SERVER_IP = "192.168.100.19:8080";//
	// 请求接口ip地址

	/**
	 * 测试环境IP地址
	 */
	private final static String TEST_SERVER_IP = "25050a37.nat123.net";// 测试ip地址

	public static final String GET_COMPANY_INTRO = "/home/getAboutCompany";// 获取公司介绍
	public static final String GET_PHONE_CODE = "/appuser/mdfphone";// 修改绑定手机号-获取手机验证码
	public static final String GET_MDFPHONE = "/appuser/mdfphone";// 修改绑定手机号－提交
	public static final String GET_FRIEND_INFO = "/appuser/myinfo";// 我的个人资料－查看
	public static final String REGISTER = "/appuser/register";// 获取手机验证码
	public static final String UPLOADPIC = "/appuser/picupload";// 图片上传
	public static final String YOUKE_LOGIN = "/appuser/visitorlogin";// 游客登陆
	public static final String LOGIN = "/appuser/login";// 正常登陆
	public static final String LOST_PSW = "/appuser/lostpwd";// 忘记密码
	public static final String MODIFY_PSW = "/appuser/modifypwd";// 修改密码
	public static final String FRIEND_LIST = "/appuser/friendlist";// 好友列表
	public static final String FIND_FRIEND = "/appuser/selectUsersByWords";// 查找好友
	public static final String MY_ACCOUNT_INFO = "/appuser/selectMyAccountEX";// 我的账户
	public static final String MY_BANK_LIST = "/appuser/selectMyBankCards";// 我的银行卡列表
	public static final String VERIFY_PAYPWD = "/account/verifyPaypwdset";// 验证支付密码
	public static final String MY_LIFE_TOTAL_MONEY = "/appuser/register/queryTotalFee";// 查询我的终生服务费
	public static final String MODIFIED_PAY_PSW = "/account/modifyPaypwd";// 修改支付密码
	public static final String REGIST_PAY = "/appuser/registerPay";// 注册支付接口注册-地球币代付-请求代付
	public static final String HOME_DATA = "/home/getBanners";// 首页轮播数据等接口
	public static final int BANNER_POSITION = 1;// 首页轮播数据
	public static final String SET_PRIVACY = "/appuser/setPrivacy";// 个人资料权限设置
	public static final String SET_USER_MODIFY_INFO = "/appuser/modifyInfo";// 修改用户资料
	public static final String GET_VISITH_HISTORY = "/appuser/visitHistory";
	public static final String ADD_FRIEND = "/appuser/makeFriends";// 添加好友
	public static final String DELETE_FRIEND = "/appuser/deleteFriendYX";// 删除好友
	public static final String FRIEND_INFO = "/appuser/friendinfo";// 查看好友资料
	public static final String FRIENDS_VISIT = "/appuser/visitFriends";// 我看过谁统计接口
	// -------------已废弃--------------
	public static final String LOADIMAGEURL = "http://192.168.100.58:8080/EarthMan";// 加载图片的baseurl
	// -------------已废弃--------------
	public static final String REPLACE_JIHUO_LIST = "/appuser/queryForMePay";// 代替好友激活列表
	public static final String LOGIN_OUT = "/appuser/logout";// 登出
	public static final String CHECK_PAY_PSW_SET = "/account/checkPaypwdset";// 检测是否设置过支付密码
	public static final String GET_USER_ADD_FRIEND = "/appuser/selectUsersAdd";// 查询添加我的人，和我添加的人
	public static final String FAMILY_REGISTER = "/appuser/familyRegister";// 代替好友注册，亲情号注册
	public static final String DELETE_FRIEND_REQUEST = "/appuser/deleteFriendRequest";// 清空我的添加好友记录
	public static final String GET_COMPANY_NEWS = "/home/getCompanyNews";// 获取公司新闻消息
	public static final String GET_EARTH_NEWS = "/home/getEarthManNews";// 获取地球人新闻消息
	public static final String GET_WORLD_NEWS = "/home/getWorldsNews";// 获取世界新闻消息
	public static final String GET_NEWS_DETAILS = "/home/getNewsById";// 获取新闻详情
	public static final String GET_SHARE_LIST = "/appuser/relationships";// 查看我的分享者列表
	public static final String SET_PAYPWD = "/account/setPaypwd";// 设置支付密码
	public static final String QUERY_BILL = "/account/queryBill";// 最近交易记录
	public static final String THIRD_LOGIN = "/appuser/thirdpartylogin";// 第三方登录
	public static final String SETPAYPWD = "/account/setPaypwd";// 设置支付密码
	public static final String ACCOUNT_TRANSFER = "/account/transferEX";// 转账给对方
	public static final String CHONGZHI_HUAFEI = "/utils/payPhoneBill";// 充值话费
	public static final String LIST_TRANS_USERS = "/account/selectLtstTransUsers";// 最近转账记录

	public static final String GET_USER_INFO = "/appuser/selectUsersByEarthId";// 通过CardID获取用户信息
	public static final String CIRCLE_ALL_MY_GROUPS = "/appuser/selectAllMyGroupsFriends";// 获取我的圈子详情列表
	public static final String CIRCLE_ALL_FRIENDS_GROUPS = "/appuser/selectAllGroupsFriends";// 获取朋友圈子详情列表
	public static final String FAMILY_LIST = "/appuser/selectMyFamily";// 获取的情亲号列表
	public static final String PAY_FOR_FRIENDS_DIQIUBI = "/appuser/payForFriends";// 代付注册地球币
	public static final String ACTIVE_PAYBY_EARTHCOIN = "/account/activePayByEarthCoin";// 地球币支付
	public static final String ACTIVE_PAY = "/account/activePay";// 获取订单信息－账号激活
	public static final String GENERATE_SMSCODE = "/appuser/generAuthCode";// 获取验证码
	public static final String VALIDATE_SMSCODE = "/appuser/validateAuthCode";// 验证验证码
	public static final String UPDATE_ACTION = "/utils/checkversion";// 版本更新
	public static final String MY_GROUPS = "/appuser/myGroups";// 我的圈子列表

	public static final String CIRCLE_CREATE = "/appuser/createGroup";// 创建圈子
	public static final String CIRCLE_DELETE = "/appuser/deleteGroup";// 删除圈子
	public static final String CIRCLE_EDIT = "/appuser/editGroup";// 编辑圈子

	public static final String SET_FRIEND = "/appuser/setFriend";// 设置分组备注关系
	public static final String FRANCHISE_APPLICATION = "/lifeend/specialAplay";// 特许申请

	public static final String GET_DEAD_INFO = "/lifeend/selectFallenHomeInfo";// 获取逝者信息
	public static final String GET_DEAD_RECORD_LIST = "/lifeend/selectPresentedList";// 获取逝者敬献记录、寄语列表
	public static final String ADD_WISHES = "/lifeend/wishes";// 添加寄语
	public static final String ADD_DEAD_FLOWERS = "/lifeend/presented";// 对逝者献花
	public static final String CONTACT_BACKUP = "/utils/contactsBackup";// 通讯录上传
	public static final String DYNAMIC_PICS = "/albums/addTopics";// 动态相片上传
	public static final String USERINFO_PAY = "/account/userInfoPay";// 付费查看
	public static final String PRESENTED = "/lifeend/presented";// 献花
	public static final String THIRDPAYFORFRIEND = "/appuser/thirdPayForFriends";// 代替别人激活第三方支付
	public static final String GET_LATESCONTACTS = "/appuser/getLatestContacts";// 获取最近7天得联系人列表
	public static final String GETMYALBUMS = "/albums/getMyAlbums";// 查看我的相册列表
	public static final String GETPHOTOS = "/albums/getPhotosFromOneAlbum";// 根据相册id查看全部照片
	public static final String DELETEALBUM = "/albums/deleteAlbum";// 删除相册
	public static final String EDITALBUM = "/albums/editAlbum/";// 编辑相册
	public static final String CREATEALBUM = "/albums/createAlbum";// 创建相册
	public static final String ADDPHOTOS = "/albums/addPhotos";// 在某个相册中添加照片
	public static final String DELETEPHOTOS = "/albums/deletePhotos";// 删除某个相册中的照片
	public static final String VISITFRIENDALBUM = "/albums/visitFriendAlbums";// 访问好友相册
	public static final String GET_MY_DYNAMIC = "/albums/myTopics";// 获取我的动态
	public static final String VISITFRFRIENDDYNAMIC = "/albums/visitFriendTopics";// 访问单个好友动态
	public static final String GET_FRIEND_DYNAMIC = "/albums/friendTopics";// 获取我朋友的动态/albums/friendTopics
	public static final String DELETE_DYNAMIC = "/albums/deleteTopics/";// 删除我的动态
	public static final String PRAISE_DYNAMIC = "/albums/praiseTopic/";// 点赞
	public static final String DYNAMIC_PRAISED = "/albums/praiseList/";// 点赞列表
	public static final String REPLY = "/albums/replyTopic/";// 回复
	public static final String UPLOAD_VIDEO = "/videos/uploadVideos";// 上传视频
	public static final String GET_VIDEO_LIST = "/videos/listVideos";// 获取自己的视频列表
	public static final String DELETE_VIDEO = "/videos/deleteVideos";// 删除视频
	public static final String VISIT_FRIEND_VIDEO = "/videos/visitFriendVideos";// 访问他人视频
	public static final String EDIT_VIDEO = "/videos/editVideos";// 编辑视频
	public static final String GET_HOME_VIDEO = "/home/getVideos";// 获取首页视频专区
	public static final String GET_VIDEO_DETAIL = "/home/getVideosDetail";// 获取视频详情
	public static final String GET_EARTHMANFC = "/home/earthManFc";// 获取地球人风采
	public static final String GET_EARTHMANTOP = "/home/earthManTop";// 获取首页排行榜
	public static final String GET_AVATARS = "/appuser/getAvatars";// 获取会话列表
	public static final String PAYFORZONE = "/albums/payForZone";// 付费查看朋友相册
	public static final String CONTACTBACKUP = "/utils/contactsRecover";// 通讯录备份
	// 排行
	public static final String GET_RANKING_LIST = "/home/earthManTop";// 获取排行列表

	public static final String VIDEO_REGION_URL = "http://test.yysw2015.com/h5videos";

	public static final String EMAIL_URL = "https://mail.dqr2015.cn/";// 邮箱地址
	public static final String RETRANSTOPIC = "/albums/retransTopics";// 转发动态
	public static final String BIND_CARD = "/account/bindCard";// 绑定银行卡
	public static final String UNBIND_CARD = "/account/unbindCard";// 对银行卡解绑
	public static final String ADD_CIRCLE_PHOTO = "/albums/addCirclePhotos";// 相册中添加旋转照片
	// 激活币相关
	public static final String ACTIVATE_CURRENCY_INDENT = "/account/activePayByActiveCoin";// 激活币订单生成
	public static final String ACTIVATE_CURRENCY_DONATION = "/account/transferAtvCurrency";// 激活币转赠
	public static final String QUERY_BALANCE = "/account/queryBalance";// 查询余额
	public static final String QUERY_TRANSFER_ACCOUNTS = "/account/queryTansferHistory";// 查询转账记录
	public static final String QUERY_FOR_MEPAY = "/appuser/payForFriendsAtv";// 激活币待支付

	/**
	 * 获取接口对应的GET带参数URL
	 * 
	 * @param context
	 *            上下文环境
	 * @param pathStr
	 *            接口路径
	 * @param params
	 *            // * 请求参数
	 * @return 返回完整的接口路径
	 */
	public static String getUrl(Context context, String pathStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer("http://");
		switch (mNetworkType) {
		case OFFICIAL:// 正式环境
			sb.append(SERVER_IP);
			break;
		case DEBUG:// 调试环境
			sb.append(DEBUG_SERVER_IP);
			break;
		case TEST:// 测试环境
			sb.append(TEST_SERVER_IP);
		default:
			break;
		}
		sb.append("/EarthMan");
		sb.append(pathStr);
		String prefixUrl = sb.toString();
		String url = NetworkUtil.getRequestUrl(context, prefixUrl, list);
		System.out.println("------RequestURL：" + url);
		return url;
	}
	
	/**
	 * 
	 * getRequestURL(这里用一句话描述这个方法的作用)
	 * @return
	 * String
	 * @exception
	 */
	public static String getImgURL(String imgURL){
		StringBuffer sb = new StringBuffer("http://");
		switch (mNetworkType) {
		case OFFICIAL:// 正式环境
			sb.append(SERVER_IP);
			break;
		case DEBUG:// 调试环境
			sb.append(DEBUG_SERVER_IP);
			break;
		case TEST:// 测试环境
			sb.append(TEST_SERVER_IP);
		default:
			break;
		}
		sb.append(imgURL);
		return sb.toString();
	}

	/**
	 * 
	 * getNotifyUrl(获取notifyurl)
	 * 
	 * @param payType
	 * @return
	 *         String
	 * @exception
	 */
	public static String getNotifyUrl(int payType) {
		StringBuffer sb = new StringBuffer("http://");
		switch (mNetworkType) {
		case OFFICIAL:// 正式环境
			sb.append(SERVER_IP);
			break;
		case DEBUG:// 调试环境
			sb.append(TEST_SERVER_IP);
			break;
		case TEST:// 测试环境
			sb.append(TEST_SERVER_IP);
		default:
			break;
		}
		switch (payType) {
		case PayFactory.PAYTYPE_WEIXIN:// 微信
			sb.append("/EarthMan/account/wxpay/notify");
			break;
		case PayFactory.PAYTYPE_ALIPAY:// 支付宝
			sb.append("/EarthMan/account/alipay/notify");
			break;
		default:
			break;
		}
		return sb.toString();

	}

	/**
	 * 
	 * getPostUrl(这里用一句话描述这个方法的作用)
	 * 
	 * @param context
	 * @param pathStr
	 * @return String
	 * @exception
	 */
	public static String getPostUrl(Context context, String pathStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer("http://");
		switch (mNetworkType) {
		case OFFICIAL:// 正式环境
			sb.append(SERVER_IP);
			break;
		case DEBUG:// 调试环境
			sb.append(DEBUG_SERVER_IP);
			break;
		case TEST:// 测试环境
			sb.append(TEST_SERVER_IP);
		default:
			break;
		}
		sb.append("/EarthMan");
		sb.append(pathStr);
		String prefixUrl = sb.toString();
		String url = NetworkUtil.getRequestUrl(context, prefixUrl, list);
		return url;
	}

}
