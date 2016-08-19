package com.earthman.app.bean;

import java.io.Serializable;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-30 上午11:10:41
 * @Decription 排行信息实体
 */
public class RankingInfo implements Serializable{
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String nice;
	private String avatar;
	private String remarks;
	private String matter;
	private String cardId;
	private String uniqueid;
	private String recommendNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(String recommendNum) {
		this.recommendNum = recommendNum;
	}

}
