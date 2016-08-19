package com.earthman.app.bean;

import com.earthman.app.enums.HomeNaviType;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-29 下午2:16:42
 * @Decription 首页导航实体类
 */
public class HomeNaviItem {

	private HomeNaviType naviType;
	private int naviIcon;
	private String naviName;

	public HomeNaviItem(HomeNaviType naviType, int naviIcon, String naviName) {
		super();
		this.naviType = naviType;
		this.naviIcon = naviIcon;
		this.naviName = naviName;
	}

	public HomeNaviType getNaviType() {
		return naviType;
	}

	public void setNaviType(HomeNaviType naviType) {
		this.naviType = naviType;
	}

	public int getNaviIcon() {
		return naviIcon;
	}

	public void setNaviIcon(int naviIcon) {
		this.naviIcon = naviIcon;
	}

	public String getNaviName() {
		return naviName;
	}

	public void setNaviName(String naviName) {
		this.naviName = naviName;
	}

}
