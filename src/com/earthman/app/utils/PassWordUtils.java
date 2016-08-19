package com.earthman.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @Title: TextUtils
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年2月29日
 */

public class PassWordUtils {
	/**
	 * 判断手机号码是否正确！
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断密码是否为密码必须是6-16位，必须同时有字母和数字。
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isPassWord(String password) {
		if (TextUtils.isEmpty(password)) {
			return false;
		}								
		Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[\\dA-Za-z]{6,16}$");
		Matcher m = p.matcher(password);
		LogUtil.i("输入密码校验", password + "------>" + m.matches());
		return m.matches();

	}

	/**
	 * 判断邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
