package com.earthman.app.bean;

/**
 * @Title: LoginResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月9日
 */

public class LoginResponse extends BaseResponse {
	private LoginResult result;

	/**
	 * @return the result
	 */
	public LoginResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(LoginResult result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoginResponse [result=" + result + "]";
	}

	public class LoginResult {

		private String telephone;
		private String token;
		private String userid;
		private String status;
		private String avatar;
		private String nice;
		private String cardId;
		private String uniqueid;
		private String isSetPayPsw;

		/**
		 * 
		 * getCardId(地球人ID)
		 * 
		 * @return
		 *         String
		 * @exception
		 */
		public String getCardId() {
			return cardId;
		}

		public void setCardId(String cardId) {
			this.cardId = cardId;
		}

		/**
		 * nice
		 * 
		 * @return the nice
		 * @since 1.0.0
		 */

		public String getNice() {
			return nice;
		}

		/**
		 * @param nice
		 *            the nice to set
		 */
		public void setNice(String nice) {
			this.nice = nice;
		}

		/**
		 * avatar
		 * 
		 * @return the avatar
		 * @since 1.0.0
		 */

		public String getAvatar() {
			return avatar;
		}

		/**
		 * @param avatar
		 *            the avatar to set
		 */
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		/**
		 * @return the telephone
		 */
		public String getTelephone() {
			return telephone;
		}

		/**
		 * @param telephone
		 *            the telephone to set
		 */
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		/**
		 * @return the token
		 */
		public String getToken() {
			return token;
		}

		/**
		 * @param token
		 *            the token to set
		 */
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

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status
		 *            the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		

		public String getUniqueid() {
			return uniqueid;
		}

		public void setUniqueid(String uniqueid) {
			this.uniqueid = uniqueid;
		}
		
		public String getIsSetPayPsw() {
			return isSetPayPsw;
		}

		public void setIsSetPayPsw(String isSetPayPsw) {
			this.isSetPayPsw = isSetPayPsw;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "LoginResult [telephone=" + telephone + ", token=" + token + ", userid=" + userid + ", status=" + status + ",cardId=" + cardId + "]";
		}
		
	}
}
