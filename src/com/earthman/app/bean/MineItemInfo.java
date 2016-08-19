package com.earthman.app.bean;

import com.earthman.app.enums.MineItemType;

/**
 * description: 我的：选项实体 company: 地球人 author: Vinci date: 2016-3-15下午6:15:37
 * version: 1.0 remark: 创建页面（Vinci）
 */
public class MineItemInfo {

	private MineItemType itemType;//菜单类型
	private int iconID;
	private String itemName;
	private boolean isTitle;

	public MineItemInfo(MineItemType itemType, int iconID, String itemName,
			boolean isTitle) {
		super();
		this.itemType = itemType;
		this.iconID = iconID;
		this.itemName = itemName;
		this.isTitle = isTitle;
	}

	public MineItemType getItemType() {
		return itemType;
	}

	public void setItemType(MineItemType itemType) {
		this.itemType = itemType;
	}

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

}
