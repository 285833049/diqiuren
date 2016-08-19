package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: MakeFriendResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月17日
 */

public class MakeFriendResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": {
	// "hasMake": [],
	// "pendingMake": [
	// {
	// "avatar": "/images/avatar/pettyIcon/1601/16012505465887967.jpg",
	// "id": 10,
	// "nice": "亮总"
	// },
	// {
	// "avatar": "",
	// "id": 4198,
	// "nice": "花花"
	// }
	// ]
	// }
	// }
	private MakeFriendResult result;

	@Override
	public String toString() {
		return "MakeFriendResponse [result=" + result + "]";
	}

	/**
	 * @return the result
	 */
	public MakeFriendResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(MakeFriendResult result) {
		this.result = result;
	}

	public class MakeFriendResult {
		private ArrayList<MakeFriend> hasMake;
		private ArrayList<MakeFriend> pendingMake;

		/**
		 * @return the hasMake
		 */
		public ArrayList<MakeFriend> getHasMake() {
			return hasMake;
		}

		/**
		 * @param hasMake
		 *            the hasMake to set
		 */
		public void setHasMake(ArrayList<MakeFriend> hasMake) {
			this.hasMake = hasMake;
		}

		/**
		 * @return the pendingMake
		 */
		public ArrayList<MakeFriend> getPendingMake() {
			return pendingMake;
		}

		/**
		 * @param pendingMake
		 *            the pendingMake to set
		 */
		public void setPendingMake(ArrayList<MakeFriend> pendingMake) {
			this.pendingMake = pendingMake;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MakeFriendResult [hasMake=" + hasMake + ", pendingMake=" + pendingMake + "]";
		}

		public class MakeFriend {
			private String avatar;
			private String id;
			private String nice;
			private String cardId;
			private int hasmake;
			private String remarks;
			
			public String getRemarks() {
				return remarks;
			}

			public void setRemarks(String remarks) {
				this.remarks = remarks;
			}

			public String getCardId() {
				return cardId;
			}

			public void setCardId(String cardId) {
				this.cardId = cardId;
			}

			/**
			 * @return the hasmake
			 */
			public int getHasmake() {
				return hasmake;
			}

			/**
			 * @param hasmake
			 *            the hasmake to set
			 */
			public void setHasmake(int hasmake) {
				this.hasmake = hasmake;
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
			 * @return the nice
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
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "MakeFriend [avatar=" + avatar + ", id=" + id + ", nice=" + nice + ",card_Id=" + cardId + "]";
			}

		}
	}

}
