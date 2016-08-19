package com.earthman.app.bean;

/**
 * 作者：Zhou
 * 日期：2016-3-8 下午5:08:23
 * 描述：（）
 */
public class CountryPhoneItem {

	private int id;
	private String countrycode;
	private String phonecode;

	public CountryPhoneItem(int id, String countrycode, String phonecode) {
		this.countrycode = countrycode;
		this.id = id;
		this.phonecode = phonecode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getPhonecode() {
		return phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}
	
	
}
