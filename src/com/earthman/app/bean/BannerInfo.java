package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-8-11 下午2:17:39
 * @Decription Banner实体
 */
public class BannerInfo {

	private String imgUrl;
	private int actionId;
	private String title;

	public BannerInfo(String imgUrl, int actionId, String title) {
		super();
		this.imgUrl = imgUrl;
		this.actionId = actionId;
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int id) {
		this.actionId = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
