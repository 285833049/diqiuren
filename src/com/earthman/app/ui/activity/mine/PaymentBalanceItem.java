package com.earthman.app.ui.activity.mine;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-7-16 上午8:45:28
 * @Decription 支付方式余额项
 */
public class PaymentBalanceItem {

	private int paymentIcon;//支付方式图标
	private String paymentName;//...名称
	private int paymentBalance;//...余额

	public PaymentBalanceItem(int paymentIcon, String paymentName, int paymentBalance) {
		super();
		this.paymentIcon = paymentIcon;
		this.paymentName = paymentName;
		this.paymentBalance = paymentBalance;
	}

	public int getPaymentIcon() {
		return paymentIcon;
	}

	public void setPaymentIcon(int paymentIcon) {
		this.paymentIcon = paymentIcon;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public int getPaymentBalance() {
		return paymentBalance;
	}

	public void setPaymentBalance(int paymentBalance) {
		this.paymentBalance = paymentBalance;
	}

}
