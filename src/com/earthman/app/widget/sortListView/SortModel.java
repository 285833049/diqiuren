package com.earthman.app.widget.sortListView;

import java.util.ArrayList;

public class SortModel {

	private String name; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母
	private String pahonecode; // 电话国家码或者好友头像地址
	private String userid;// 好友的id
	private ArrayList<String> groups;// 加入的圈子
	private String cardId;
	private String phone;
	private String remarks;
	private String nice;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNice() {
		return nice;
	}

	public void setNice(String nice) {
		this.nice = nice;
	}

	public SortModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * cardId
	 * 
	 * @return the cardId
	 * @since 1.0.0
	 */

	public String getCardId() {
		return cardId;
	}

	/**
	 * @param cardId
	 *            the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public SortModel(String name, String sortLetters, String pahonecode, ArrayList<String> groups) {
		super();
		this.name = name;
		this.sortLetters = sortLetters;
		this.pahonecode = pahonecode;
		this.groups = groups;
	}

	public SortModel(String name,String cardId, String sortLetters, String pahonecode, ArrayList<String> groups, String phone) {
		super();
		this.name = name;
		this.cardId=cardId;
		this.sortLetters = sortLetters;
		this.pahonecode = pahonecode;
		this.groups = groups;
		this.phone = phone;
	}

	/**
	 * @return the groups
	 */
	public ArrayList<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}

	/**
	 * @return the pahonecode
	 */
	public String getPahonecode() {
		return pahonecode;
	}

	/**
	 * @param pahonecode
	 *            the pahonecode to set
	 */
	public void setPahonecode(String pahonecode) {
		this.pahonecode = pahonecode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
