package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-30 上午10:28:13
 * @Decription 排行页面数据模型
 */
public class RankingModel {
	
	private String code;
	private String message;
	private RankingContentModel result;

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

	public RankingContentModel getResult() {
		return result;
	}

	public void setResult(RankingContentModel result) {
		this.result = result;
	}

}
