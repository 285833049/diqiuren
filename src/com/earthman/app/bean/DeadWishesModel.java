package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-25 下午3:06:19
 * @Decription 寄语页面模型
 */
public class DeadWishesModel {

	private String code;
	private String message;
	private DeadWishesInfoModel result;

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

	public DeadWishesInfoModel getResult() {
		return result;
	}

	public void setResult(DeadWishesInfoModel result) {
		this.result = result;
	}

}
