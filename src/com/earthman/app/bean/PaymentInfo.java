package com.earthman.app.bean;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-30 下午3:07:36
 * @Decription 支付类型实体类
 */
public class PaymentInfo {
	
	private int bankIcon;
	private String bankName;

	public PaymentInfo(int bankIcon, String bankName) {
		super();
		this.bankIcon = bankIcon;
		this.bankName = bankName;
	}

	public int getBankIcon() {
		return bankIcon;
	}

	public void setBankIcon(int bankIcon) {
		this.bankIcon = bankIcon;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
