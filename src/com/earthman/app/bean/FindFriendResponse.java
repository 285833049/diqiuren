package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: FindFriendResponse
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月14日
 */

public class FindFriendResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": [
	// {
	// "avatar": "/images/avatar/pettyIcon/1601/16012606183873133.jpg",
	// "id": 34,
	// "nice": "夜幕"
	// }
	// ]
	// }
	private ArrayList<FindFriendResult> result;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FindFriendResponse [result=" + result + "]";
	}

	/**
	 * @return the result
	 */
	public ArrayList<FindFriendResult> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<FindFriendResult> result) {
		this.result = result;
	}

	public class FindFriendResult {
		private String avatar;
		private String id;
		private String nice;
		private String uniqueid;
		private String isFrined;

		public String getIsFrined() {
			return isFrined;
		}

		public void setIsFrined(String isFrined) {
			this.isFrined = isFrined;
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

		public String getUniqueid() {
			return uniqueid;
		}

		public void setUniqueid(String uniqueid) {
			this.uniqueid = uniqueid;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "FindFriendResult [avatar=" + avatar + ", id=" + id + ", nice=" + nice + ", uniqueid=" + uniqueid + ",isFrined=" + isFrined + "]";
		}

	}
}
