package com.earthman.app.bean;

import java.util.List;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-10 下午2:37:26
 * @Decription 圈子-单个圈子好友数据模型
 */
public class CircleFirendsModel {
	private String code;
	private String message;
	private List<CircleFriendInfo> result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<CircleFriendInfo> getResult() {
		return result;
	}

	public void setResult(List<CircleFriendInfo> result) {
		this.result = result;
	}

}
