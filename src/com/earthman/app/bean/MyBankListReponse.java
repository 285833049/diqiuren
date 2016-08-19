package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: MyBankListReponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月10日
 */

public class MyBankListReponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": [
	// {
	// "banktype": 0,
	// "cardtype": 1,
	// "destaccnumber": "123456789",
	// "userid": 10
	// }
	// ]
	// }
	// destaccnumber：银行卡号
	// cardtype：银行卡类型0：储蓄卡 1：信用卡
	// banktype：发卡行
	// 银行名称0:招商银行;1:广发银行;2:华夏银行;3:工商银行;4:中信银行;5:中国银行;6:农业银行;7:邮政储蓄;8:平安银行;9:建设银行;10:交通银行;11:广大银行;12:兴业银行;13:浦发银行;14:民生银行
	private ArrayList<MyBankList> result;

	/**
	 * @return the result
	 */
	public ArrayList<MyBankList> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<MyBankList> result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyBankListReponse [result=" + result + "]";
	}

	public class MyBankList {
		private int destaccnumber;
		private int cardtype;
		private int banktype;

		/**
		 * @return the destaccnumber
		 */
		public int getDestaccnumber() {
			return destaccnumber;
		}

		/**
		 * @param destaccnumber
		 *            the destaccnumber to set
		 */
		public void setDestaccnumber(int destaccnumber) {
			this.destaccnumber = destaccnumber;
		}

		/**
		 * @return the cardtype
		 */
		public int getCardtype() {
			return cardtype;
		}

		/**
		 * @param cardtype
		 *            the cardtype to set
		 */
		public void setCardtype(int cardtype) {
			this.cardtype = cardtype;
		}

		/**
		 * @return the banktype
		 */
		public int getBanktype() {
			return banktype;
		}

		/**
		 * @param banktype
		 *            the banktype to set
		 */
		public void setBanktype(int banktype) {
			this.banktype = banktype;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MyBankList [destaccnumber=" + destaccnumber + ", cardtype=" + cardtype + ", banktype=" + banktype
					+ "]";
		}

	}
}
