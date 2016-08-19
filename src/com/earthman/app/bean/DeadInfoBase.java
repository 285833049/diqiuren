package com.earthman.app.bean;

import com.earthman.app.utils.StringUtils;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-28 上午11:26:54
 * @Decription 逝者信息实例
 */
public class DeadInfoBase {
	
	private int id;
	private String nice;
	private String avatar;
	private String remarks;
	private String matter;
	private String cardId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNice() {
		return StringUtils.getString(nice);
	}
	public void setNice(String nice) {
		this.nice = nice;
	}
	public String getAvatar() {
		return StringUtils.getString(avatar);
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getRemarks() {
		return StringUtils.getString(remarks);
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMatter() {
		return StringUtils.getString(matter);
	}
	public void setMatter(String matter) {
		this.matter = matter;
	}
	public String getCardId() {
		return StringUtils.getString(cardId);
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	
	
}
