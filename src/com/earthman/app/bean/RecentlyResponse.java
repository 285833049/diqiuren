package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月20日 下午3:54:49
 * @Decription
 */
public class RecentlyResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": [
	// {
	// "cardId": "AA0000021",
	// "id": 4246,
	// "nice": "hok"
	// avatar
	// }
	// ]
	// }
	private ArrayList<RecentlyResult> result;

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecentlyResponse [result=" + result + "]";
	}

	/**
	 * result
	 *
	 * @return the result
	 * @since 1.0.0
	 */

	public ArrayList<RecentlyResult> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<RecentlyResult> result) {
		this.result = result;
	}

	public class RecentlyResult {
		private String cardId;
		private String id;
		private String nice;
		private String avatar;
		private String remarks;
		
		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
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

		/*
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RecentlyResult [cardId=" + cardId + ", id=" + id + ", nice=" + nice + ", avatar=" + avatar + "]";
		}

	}
}
