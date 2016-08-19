package com.earthman.app.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 
 * 作者：Zhou
 * 日期：2015-10-13 上午10:30:15
 * 版权：地球人
 * 描述：（）
 */
		
public class FormatNumberUtil {

	public static final int TYPE_NUMBER = 0;// 普通数字类型
	public static final int TYPE_CURRENCY = 1;// 货币类型
	
	private static final int DEFAULT_FRACTION_DIGITS = 2;
	
	private NumberFormat nf;
	
	private FormatNumberUtil(int type) {
		if (type == TYPE_NUMBER) {
			nf = NumberFormat.getInstance();
		} else {
			nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
		}
		nf.setMinimumFractionDigits(DEFAULT_FRACTION_DIGITS);
		nf.setMaximumFractionDigits(DEFAULT_FRACTION_DIGITS);
	}
	
	public static FormatNumberUtil getInstance(int type) {
		return new FormatNumberUtil(type);
	}
	
	public void setFractionDigits(int value) {
		nf.setMinimumFractionDigits(value);
		nf.setMaximumFractionDigits(value);
	}
	
	/**
	 * 格式化数字，精确到小数点后两位数
	 * @param number
	 * @return
	 */
	public String format(double number) {
		return nf.format(number);
	}
	
}
