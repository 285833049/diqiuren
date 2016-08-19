package com.earthman.app.eventbusbean;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-22 上午11:24:24
 * @Decription
 */
public class SearchBean {
	private int searchType;
	private String key;
	
	public SearchBean(int searchType, String key){
		this.searchType = searchType;
		this.key = key;
	}
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
