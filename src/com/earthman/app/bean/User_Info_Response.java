package com.earthman.app.bean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月20日 上午11:16:23
 * @Decription
 */
public class User_Info_Response extends BaseResponse {
	// {
	// "code": "000000",
	// "result": {
	// "cardId": "AA0000021",
	// "id": 4246,
	// "nice": "hok"
	// }
	// }
	private UerinfoRessult result;

	/**
	 * result
	 *
	 * @return the result
	 * @since 1.0.0
	 */

	public UerinfoRessult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(UerinfoRessult result) {
		this.result = result;
	}

	public class UerinfoRessult {
		private String cardId;
		private String id;
		private String nice;
		private String avatar;

		/**
		 * avatar
		 *
		 * @return  the avatar
		 * @since   1.0.0
		*/
		
		public String getAvatar() {
			return avatar;
		}

		/**
		 * @param avatar the avatar to set
		 */
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		/**
		 * cardId
		 *
		 * @return the cardId
		 * @since 1.0.0
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

		/**
		 * id
		 *
		 * @return the id
		 * @since 1.0.0
		 */

		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
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

		/* 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UerinfoRessult [cardId=" + cardId + ", id=" + id + ", nice=" + nice + ", avatar=" + avatar + "]";
		}

	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User_Info_Response [result=" + result + "]";
	}

}
