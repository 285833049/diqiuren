package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: FriendList
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月10日
 */

public class FriendList extends BaseResponse {
	// "avatar": "/images/avatar/pettyIcon/1601/16012801545880082.jpg",
	// "groups": "地球人,学生,家长",
	// "id": 27,
	// "matter": "关系",
	// "mygroup": "在我的分组",
	// "nice": "阿美",
	// "remarks": "备注"
	private ArrayList<FriendListResult> result;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FriendList [result=" + result + "]";
	}

	/**
	 * @return the result
	 */
	public ArrayList<FriendListResult> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<FriendListResult> result) {
		this.result = result;
	}

	public class FriendListResult {
		private String avatar;
		private String groups;
		private String id;
		private String nice;
		private String mygroup;
		private String remarks;
		private String matter;
		private String cardId;
		private String phone;
		
		private boolean isChecked;

		/**
		 * cardId
		 * 
		 * @return the cardId
		 * @since 1.0.0
		 */

		public String getCardId() {
			return cardId;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		/**
		 * @param cardId
		 *            the cardId to set
		 */
		public void setCardId(String cardId) {
			this.cardId = cardId;
		}

		/**
		 * @return the mygroup
		 */
		public String getMygroup() {
			return mygroup;
		}

		/**
		 * @param mygroup
		 *            the mygroup to set
		 */
		public void setMygroup(String mygroup) {
			this.mygroup = mygroup;
		}

		/**
		 * @return the remarks
		 */
		public String getRemarks() {
			return remarks;
		}

		/**
		 * @param remarks
		 *            the remarks to set
		 */
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		/**
		 * @return the matter
		 */
		public String getMatter() {
			return matter;
		}

		/**
		 * @param matter
		 *            the matter to set
		 */
		public void setMatter(String matter) {
			this.matter = matter;
		}

		public FriendListResult() {
			super();
			// TODO Auto-generated constructor stub
		}

		public FriendListResult(String avatar, String groups, String id, String nice) {
			super();
			this.avatar = avatar;
			this.groups = groups;
			this.id = id;
			this.nice = nice;
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
		 * @return the groups
		 */
		public String getGroups() {
			return groups;
		}

		/**
		 * @param groups
		 *            the groups to set
		 */
		public void setGroups(String groups) {
			this.groups = groups;
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
		
		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "FriendListResult [avatar=" + avatar + ", groups=" + groups + ", id=" + id + ", nice=" + nice + ", mygroup=" + mygroup + ", remarks="
					+ remarks + ", matter=" + matter + "]";
		}

	}
}
