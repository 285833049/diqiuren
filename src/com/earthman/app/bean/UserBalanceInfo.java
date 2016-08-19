package com.earthman.app.bean;

/**
 * 用户余额
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-7-22 下午3:43:52
 * @Decription
 */
public class UserBalanceInfo extends BaseResponse {
	private UserBalanceEtity result;

	public UserBalanceEtity getResult() {
		return result;
	}

	public void setResult(UserBalanceEtity result) {
		this.result = result;
	}

	public class UserBalanceEtity {
		private int currencyem;
		private int currencyatv;
		public int getCurrencyem() {
			return currencyem;
		}
		public void setCurrencyem(int currencyem) {
			this.currencyem = currencyem;
		}
		public int getCurrencyatv() {
			return currencyatv;
		}
		public void setCurrencyatv(int currencyatv) {
			this.currencyatv = currencyatv;
		}

	}

}
