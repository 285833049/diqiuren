package com.earthman.app.bean;

import java.util.List;
/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午4:35:42
 * @Decription 圈子列表模型
 */
public class CircleListModel {

	private String code;
	private String message;
	private List<CircleListInfo> result;

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

	public List<CircleListInfo> getResult() {
		return result;
	}

	public void setResult(List<CircleListInfo> result) {
		this.result = result;
	}

}
