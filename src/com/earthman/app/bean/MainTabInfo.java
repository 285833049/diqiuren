package com.earthman.app.bean;

import com.earthman.app.enums.MainTabEnum;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-4-1 下午3:32:14
 * @Decription 首页标签实体
 */
public class MainTabInfo {

	private int normalImg;
	private int selectedImg;
	private String tabName;
	private int msgNum;
	private MainTabEnum tabEnum;
	
	public MainTabInfo(int normalImg, int selectedImg, String tabName, int msgNum, MainTabEnum tabEnum) {
		super();
		this.normalImg = normalImg;
		this.selectedImg = selectedImg;
		this.tabName = tabName;
		this.msgNum = msgNum;
		this.tabEnum = tabEnum;
	}

	public int getNormalImg() {
		return normalImg;
	}

	public void setNormalImg(int normalImg) {
		this.normalImg = normalImg;
	}

	public int getSelectedImg() {
		return selectedImg;
	}

	public void setSelectedImg(int selectedImg) {
		this.selectedImg = selectedImg;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public int getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}

	public MainTabEnum getTabEnum() {
		return tabEnum;
	}

	public void setTabEnum(MainTabEnum tabEnum) {
		this.tabEnum = tabEnum;
	}

}
