package com.earthman.app.bean;

import com.earthman.app.enums.MapName;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-9 下午5:55:13
 * @Decription 地图应用信息对象
 */
public class MapInfo {
 
	private MapName mapEnum;
	private int mapIcon;
	private String mapName;
	private String mapPageName;
	
	public MapInfo(MapName mapEnum,int mapIcon,String mapName, String mapPageName) {
		super();
		this.mapEnum=mapEnum;
		this.mapIcon=mapIcon;
		this.mapName = mapName;
		this.mapPageName = mapPageName;
	}
	
	public MapName getMapEnum() {
		return mapEnum;
	}

	public void setMapEnum(MapName mapEnum) {
		this.mapEnum = mapEnum;
	}
	
	public int getMapIcon() {
		return mapIcon;
	}

	public void setMapIcon(int mapIcon) {
		this.mapIcon = mapIcon;
	}

	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public String getMapPageName() {
		return mapPageName;
	}
	
	public void setMapPageName(String mapPageName) {
		this.mapPageName = mapPageName;
	}
	
}
