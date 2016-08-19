package com.earthman.app.bean;

/**
 * 获取支付订单号
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-3-19 下午1:50:28
 * @Decription
 */
public class PaymentOrderInfo extends BaseResponse {
	private PaymentOrderResult result;

	public PaymentOrderResult getResult() {
		return result;
	}

	public void setResult(PaymentOrderResult result) {
		this.result = result;
	}

	public class PaymentOrderResult {
		private String body;
		private String total_fee;
		private String subject;
		private String out_trade_no;

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getOut_trade_no() {
			return out_trade_no;
		}

		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}

		@Override
		public String toString() {
			return "PaymentOrderResult[body=" + body + ",total_fee=" + total_fee + ",subject=" + subject + ",out_trade_no=" + out_trade_no + "]";
		}

	}

}
