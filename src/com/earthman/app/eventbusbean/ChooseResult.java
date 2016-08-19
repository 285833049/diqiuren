package com.earthman.app.eventbusbean;

import java.io.Serializable;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-22 下午5:08:27
 * @Decription
 */
public class ChooseResult implements Serializable{
	private int chooseType;// 1: 表示不显示位置 2：表示显示城市 3：表示显示具体位置
	private String cityName;
	private String title; 
	private String address;
	private double longitude;
	private double latitude;
		
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getChooseType() {
		return chooseType;
	}

	public void setChooseType(int chooseType) {
		this.chooseType = chooseType;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ChooseResult(int chooseType, String cityName, String title, String address){
		this.chooseType = chooseType;
		this.cityName = cityName;
		this.title = title;
		this.address = address;
	}
	
	
	public ChooseResult setlatlog(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
		return this;
	}

}
