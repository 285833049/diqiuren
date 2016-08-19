package com.earthman.app.bean;

/**
 * @Title: AccountInfo
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月10日
 */

public class AccountInfo extends BaseResponse {
	// {
	// "code": "000000",
	// "result": {
	// "cardId": "AA0111111",
	// "createdAt": 1453713392000,
	// "money": 32.5,
	// "year": 1
	// }
	// }
	private AccountInfoResult result;

	/**
	 * @return the result
	 */
	public AccountInfoResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(AccountInfoResult result) {
		this.result = result;
	}

	public class AccountInfoResult {
		private String cardId;
		private String duedate;
		private String currencyem;
		private String currencyatv;
		private int year;

		/**
		 * @return the cardId
		 */
		public String getCardId() {
			return cardId;
		}

		/**
		 * @param cardId
		 *            the cardId to set
		 */
		public void setCardId(String cardId) {
			this.cardId = cardId;
		}

		public String getDuedate() {
			return duedate;
		}

		public void setDuedate(String duedate) {
			this.duedate = duedate;
		}

		public String getCurrencyem() {
			return currencyem;
		}

		public void setCurrencyem(String currencyem) {
			this.currencyem = currencyem;
		}

		public String getCurrencyatv() {
			return currencyatv;
		}

		public void setCurrencyatv(String currencyatv) {
			this.currencyatv = currencyatv;
		}

		/**
		 * @return the year
		 */
		public int getYear() {
			return year;
		}

		/**
		 * @param year
		 *            the year to set
		 */
		public void setYear(int year) {
			this.year = year;
		}

	}
}
