package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * 我看过谁
 * 
 * @author xiexianyong
 * 
 */
public class VisitHistoryResponse extends BaseResponse {

	private VisitHistoryResult result;

	public VisitHistoryResult getResult() {
		return result;
	}

	public void setResult(VisitHistoryResult result) {
		this.result = result;
	}

	public class VisitHistoryResult {
		private int payMembers;
		private int visitNumbers;
		private ArrayList<VisitorsListEntity> visitors;

		public int getPayMembers() {
			return payMembers;
		}

		public void setPayMembers(int payMembers) {
			this.payMembers = payMembers;
		}

		public int getVisitNumbers() {
			return visitNumbers;
		}

		public void setVisitNumbers(int visitNumbers) {
			this.visitNumbers = visitNumbers;
		}

		public ArrayList<VisitorsListEntity> getVisitors() {
			return visitors;
		}

		public void setVisitors(ArrayList<VisitorsListEntity> visitors) {
			this.visitors = visitors;
		}

		@Override
		public String toString() {
			return "VisitHistoryResult[payMembers=" + payMembers
					+ ",visitNumbers=" + visitNumbers + ",visitors=" + visitors
					+ "]";
		}

		public class VisitorsListEntity {
			private String avatar;
			private String id;
			private String createdAt;
			private String groups;
			private int isFrined;
			private String nice;
			private String cardId;
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

			public String getAvatar() {
				return avatar;
			}

			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getCreatedAt() {
				return createdAt;
			}

			public void setCreatedAt(String createdAt) {
				this.createdAt = createdAt;
			}

			public String getGroups() {
				return groups;
			}

			public void setGroups(String groups) {
				this.groups = groups;
			}

			public int getIsFrined() {
				return isFrined;
			}

			public void setIsFrined(int isFrined) {
				this.isFrined = isFrined;
			}

			public String getNice() {
				return nice;
			}

			public void setNice(String nice) {
				this.nice = nice;
			}

			@Override
			public String toString() {
				return "VisitorsListEntity[avatar=" + avatar + ",id=" + id
						+ ",createdAt=" + createdAt + ",groups=" + groups
						+ ",isFrined=" + isFrined + ",nice=" + nice + "]";
			}

		}
	}

}
