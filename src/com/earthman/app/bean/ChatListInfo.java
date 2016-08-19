package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * 会话列表
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-4-1 下午5:50:36
 * @Decription
 */
public class ChatListInfo extends BaseResponse {
	private ArrayList<ChatListEntity> result;

	public ArrayList<ChatListEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<ChatListEntity> result) {
		this.result = result;
	}

	public class ChatListEntity {
		private int id;
		private String nice;
		private String avatar;
		private String remarks;
		private String matter;
		private String cardId;
		private String phone;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
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

		@Override
		public String toString() {
			return "ChatListEntity[id=" + id + ",nice=" + nice + ",avatar=" + avatar + ",remarks=" + remarks + ",matter=" + matter + ",cardId=" + cardId
					+ ",phone=" + phone + "]";
		}

	}

}
