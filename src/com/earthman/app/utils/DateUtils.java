package com.earthman.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类 
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-22 下午1:50:11
 * @Decription
 */
public class DateUtils {

	public static String getFormatDate(String date) {
		String[] dateArray = date.split(" ");
		String tempDate = dateArray[0];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(tempDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			return "周 日";
		case 2:
			return "周 一";
		case 3:
			return "周 二";
		case 4:
			return "周 三";
		case 5:
			return "周 四";
		case 6:
			return "周 五";
		default:
			return "周 六";
		}
	}
	
	public static String getMonthAndDay(String date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date tempDate = null;
		try {
			tempDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf=new SimpleDateFormat("MM-dd");
		return sdf.format(tempDate);
	}

}
