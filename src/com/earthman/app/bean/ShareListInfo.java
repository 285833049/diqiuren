package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * 分享着列表
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-3-19 下午5:00:20
 * @Decription
 */
public class ShareListInfo extends BaseResponse {
	private ShareListresult result;

	public ShareListresult getResult() {
		return result;
	}

	public void setResult(ShareListresult result) {
		this.result = result;
	}

	public class ShareListresult {

		private ArrayList<ShareListEntity> myUpline;

		private ArrayList<ShareListEntity> myOffline;

		public ArrayList<ShareListEntity> getMyUpline() {
			return myUpline;
		}

		public void setMyUpline(ArrayList<ShareListEntity> myUpline) {
			this.myUpline = myUpline;
		}

		public ArrayList<ShareListEntity> getMyOffline() {
			return myOffline;
		}

		public void setMyOffline(ArrayList<ShareListEntity> myOffline) {
			this.myOffline = myOffline;
		}

		@Override
		public String toString() {
			return "ShareListresult[myUpline=" + myUpline + ",myOffline=" + myOffline + "]";
		}

		public class ShareListEntity {
			private String id;
			private String nice;
			private String avatar;
			private String remarks;
			private String matter;
			private String cardId;
			private String uniqueid;
			private String mygroup;
			private String groups;
			private String createdAt;
			private String isFrined;
			private String status;

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getNice() {
				return nice;
			}

			public void setNice(String nice) {
				this.nice = nice;
			}

			public String getAvatar() {
				return avatar;
			}

			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			public String getRemarks() {
				return remarks;
			}

			public void setRemarks(String remarks) {
				this.remarks = remarks;
			}

			public String getMatter() {
				return matter;
			}

			public void setMatter(String matter) {
				this.matter = matter;
			}

			public String getCardId() {
				return cardId;
			}

			public void setCardId(String cardId) {
				this.cardId = cardId;
			}
			
			public String getUniqueid() {
				return uniqueid;
			}

			public void setUniqueid(String uniqueid) {
				this.uniqueid = uniqueid;
			}

			public String getMygroup() {
				return mygroup;
			}

			public void setMygroup(String mygroup) {
				this.mygroup = mygroup;
			}

			public String getGroups() {
				return groups;
			}

			public void setGroups(String groups) {
				this.groups = groups;
			}

			public String getCreatedAt() {
				return createdAt;
			}

			public void setCreatedAt(String createdAt) {
				this.createdAt = createdAt;
			}

			public String getIsFrined() {
				return isFrined;
			}

			public void setIsFrined(String isFrined) {
				this.isFrined = isFrined;
			}

			@Override
			public String toString() {
				return "ShareListEntity[id=" + id + ",nice=" + nice + ",avatar=" + avatar + ",remarks=" + remarks + ",matter=" + matter + ",cardId=" + cardId
						+ ",mygroup=" + mygroup + ",groups=" + groups + ",createdAt=" + createdAt + ",isFrined=" + isFrined + "]";
			}

		}

	}

}
