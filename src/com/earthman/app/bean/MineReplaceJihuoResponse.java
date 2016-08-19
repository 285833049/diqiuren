package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: MineReplaceJihuoResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月16日
 */

public class MineReplaceJihuoResponse extends BaseResponse {

	// {
	// "code": "000000",
	// "result": [
	// {
	// "avatar": "",
	// "cardId": "AA0000010",
	// "fee": 0.0,
	// "id": 29601,
	// "message": "帮忙帮忙",
	// "userId": 4206
	// },
	// {
	// "avatar": "",
	// "cardId": "AA0000010",
	// "fee": 500.0,
	// "id": 29602,
	// "message": "帮忙帮忙",
	// "userId": 4211
	// }
	// ]
	// }
	private ArrayList<ReplaceJihuoResult> result;

	/**
	 * @return the result
	 */
	public ArrayList<ReplaceJihuoResult> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<ReplaceJihuoResult> result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MineReplaceJihuoResponse [result=" + result + "]";
	}

	public class ReplaceJihuoResult {
		private String avatar;
		private String cardId;
		private String fee;
		private String id;
		private String message;
		private String userId;
		private String createdAt;

		/**
		 * createdAt
		 *
		 * @return  the createdAt
		 * @since   1.0.0
		*/
		
		public String getCreatedAt() {
			return createdAt;
		}

		/**
		 * @param createdAt the createdAt to set
		 */
		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		/**
		 * @return the avatar
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

		/**
		 * @return the fee
		 */
		public String getFee() {
			return fee;
		}

		/**
		 * @param fee
		 *            the fee to set
		 */
		public void setFee(String fee) {
			this.fee = fee;
		}

		/**
		 * @return the id
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
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}

		/**
		 * @param userId
		 *            the userId to set
		 */
		public void setUserId(String userId) {
			this.userId = userId;
		}

		/* 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ReplaceJihuoResult [avatar=" + avatar + ", cardId=" + cardId + ", fee=" + fee + ", id=" + id + ", message=" + message + ", userId=" + userId
					+ ", createdAt=" + createdAt + "]";
		}

	}
}
