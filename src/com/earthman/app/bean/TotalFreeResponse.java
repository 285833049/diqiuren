package com.earthman.app.bean;

/**
 * @Title: TotalFreeResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月10日
 */

public class TotalFreeResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": {
	// "totalFee": 300.0
	// }
	// }
	private TotalFree result;

	/**
	 * @return the result
	 */
	public TotalFree getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(TotalFree result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TotalFreeResponse [result=" + result + "]";
	}

	public class TotalFree {
		private String totalFee;

		/**
		 * @return the totalFee
		 */
		public String getTotalFee() {
			return totalFee;
		}

		/**
		 * @param totalFee
		 *            the totalFee to set
		 */
		public void setTotalFee(String totalFee) {
			this.totalFee = totalFee;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "TotalFree [totalFee=" + totalFee + "]";
		}
		
	}
}
