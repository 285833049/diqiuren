package com.earthman.app.bean;

import com.earthman.app.enums.MineSettingType;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-12 下午2:52:10
 * @Decription 设置页面实体类
 */
public class MineSettingInfo {
	
	private boolean isTitle;
	private MineSettingType settingType;
	private String name;
	
	public MineSettingInfo(boolean isTitle,MineSettingType settingType, String name) {
		super();
		this.isTitle=isTitle;
		this.settingType = settingType;
		this.name = name;
	}
	
	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	
	public MineSettingType getSettingType() {
		return settingType;
	}

	public void setSettingType(MineSettingType settingType) {
		this.settingType = settingType;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
