package com.earthman.app.bean;

/**
 * @Title: RegistResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月16日
 */

public class RegistResponse extends BaseResponse {
	private RegistResult result;

	/**
	 * @return the result
	 */
	public RegistResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(RegistResult result) {
		this.result = result;
	}

	public class RegistResult {
		private String userid;
		private String token;
		private String uniqueid;

		public String getUniqueid() {
			return uniqueid;
		}

		public void setUniqueid(String uniqueid) {
			this.uniqueid = uniqueid;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		/**
		 * @return the userid
		 */
		public String getUserid() {
			return userid;
		}

		/**
		 * @param userid
		 *            the userid to set
		 */
		public void setUserid(String userid) {
			this.userid = userid;
		}
	}
}
