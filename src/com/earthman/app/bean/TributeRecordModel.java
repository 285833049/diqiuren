package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 下午2:48:01
 * @Decription 敬献记录页面对象模型
 */
public class TributeRecordModel {
	
	private String code;
	private String message;
	private TributeRecordInfoModel result;
	
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
	public TributeRecordInfoModel getResult() {
		return result;
	}
	public void setResult(TributeRecordInfoModel result) {
		this.result = result;
	}
	
}
