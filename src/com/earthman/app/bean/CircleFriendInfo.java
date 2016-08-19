package com.earthman.app.bean;

import java.io.Serializable;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午5:35:14
 * @Decription 圈子：朋友信息
 */
public class CircleFriendInfo implements Serializable{

	/**
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nice;
	private String avatar;
	private String remarks;
	private String matter;
	private String cardId;
	private String uniqueid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNice() {
		return nice;
	}
	public void setNice(String nice) {
		this.nice = nice;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMatter() {
		return matter;
	}
	public void setMatter(String matter) {
		this.matter = matter;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	
}
