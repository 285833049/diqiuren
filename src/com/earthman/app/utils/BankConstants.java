package com.earthman.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.earthman.app.R;

/**
 * 作者：Zhou
 * 日期：2016-3-3 下午2:54:32
 * 描述：（）
 */
public class BankConstants {
	
	public static final int BANKTYPE_ZHAOSHANG = 0;//招商银行卡
	public static final int BANKTYPE_GUANGFA = 1;//广发银行
	public static final int BANKTYPE_HUAXIA = 2;//华夏银行
	public static final int BANKTYPE_GONGSHANG = 3;//工商银行
	public static final int BANKTYPE_ZHONGXING = 4;//中信银行
	public static final int BANKTYPE_ZHONGGUO = 5;//中国银行
	public static final int BANKTYPE_NONGYE = 6;//农业银行
	public static final int BANKTYPE_YOUZHENG = 7;//邮政储蓄
	public static final int BANKTYPE_PINGAN = 8;//平安银行
	public static final int BANKTYPE_JIANSE = 9;//建设银行
	public static final int BANKTYPE_JIAOTONG = 10;//交通银行
	public static final int BANKTYPE_GUANGDA = 11;//光大银行
	public static final int BANKTYPE_XINGYE = 12;//兴业银行
	public static final int BANKTYPE_PUFA = 13;//浦发银行
	public static final int BANKTYPE_MINSHENG = 14;//民生银行
	
	public static final int PAY_WAY_ATY=1;//激活币支付
	public static final int PAY_WAY_DIQIUBI = 2;//地球币支付
	public static final int PAY_WAY_WEIXIN = 3;//微信支付支付
	public static final int PAY_WAY_ZHIFUBAO = 4;//支付宝币支付
	public static final int PAY_WAY_YINLIAN = 5;//银联支付
	
	public static final int CARDTYPE_CUXU = 0;//储蓄卡
	public static final int CARDTYPE_XINYONG = 1;//信用卡
	
	private TypedArray typedArray;
	private String[] bankNames;
	private String[] cardNames; 
	private Resources resources;
	
	public BankConstants(Context context){
		this.resources = context.getResources();
		typedArray =  resources.obtainTypedArray(R.array.bank_icons);
		bankNames = resources.getStringArray(R.array.bank_names);
		cardNames = resources.getStringArray(R.array.card_names);
	}
	
	
	
	/**
	 * 
	 * getBankImgRes(获取银行卡的logo图标)
	 * @param bankType
	 * @return
	 * @exception
	 */
	public Drawable getBankImgDrawable(int bankType){
		return resources.getDrawable(typedArray.getResourceId(bankType, 0));
	}
	
	/**
	 * 
	 * getBankBg(获取银行卡的背景)
	 * @param bankType
	 * @return
	 * @exception
	 */
	public int getBankDrawable(int bankType){
		switch (bankType) {
		case BANKTYPE_ZHAOSHANG:
		case BANKTYPE_GUANGFA:
		case BANKTYPE_HUAXIA:
		case BANKTYPE_GONGSHANG:
		case BANKTYPE_ZHONGXING:
		case BANKTYPE_ZHONGGUO:
			return R.drawable.red_bg_selector;
		case BANKTYPE_NONGYE:
		case BANKTYPE_YOUZHENG:
		case BANKTYPE_PINGAN:
			return R.drawable.green_bg_selector;
		case BANKTYPE_JIANSE:
		case BANKTYPE_JIAOTONG:
		case BANKTYPE_GUANGDA:
		case BANKTYPE_XINGYE:
		case BANKTYPE_PUFA:
		case BANKTYPE_MINSHENG:
			return R.drawable.blue_bg_selector;
		default:
			break;
		}
		return 0;
	}
	
	/**
	 * 
	 * getBankName(根据银行卡类型得到银行名)
	 * @param bankType
	 * @return
	 * String
	 * @exception
	 */
	public String getBankName(int bankType){
		return bankNames[bankType];
	}
	
     /**
      * 
      * getCardName(根据卡片类型显示卡片名)
      * @param cardType
      * @return
      * String
      * @exception
      */
	
	public String getCardName(int cardType){
		return cardNames[cardType];
	}
}
