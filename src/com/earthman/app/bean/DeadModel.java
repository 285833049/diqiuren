package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-24 下午6:58:59
 * @Decription 祭祀模型
 */
public class DeadModel {
	
	private String code;
	private String message;
	private DeadInfoModel result;

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

	public DeadInfoModel getResult() {
		return result;
	}

	public void setResult(DeadInfoModel result) {
		this.result = result;
	}

}
