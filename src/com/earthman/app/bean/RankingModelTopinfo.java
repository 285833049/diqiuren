package com.earthman.app.bean;

import java.util.ArrayList;

public class RankingModelTopinfo extends BaseResponse {

	private RankingModelTopinfo result;

	public RankingModelTopinfo getResult() {
		return result;
	}

	public void setResult(RankingModelTopinfo result) {
		this.result = result;
	}

	public ArrayList<RankingModelTopinfoEntity> getTopinfo() {
		return topinfo;
	}

	public void setTopinfo(ArrayList<RankingModelTopinfoEntity> topinfo) {
		this.topinfo = topinfo;
	}

	private ArrayList<RankingModelTopinfoEntity> topinfo;

	public class RankingModelTopinfoEntity {
		private int id;
		private String nice;
		private String avatar;
		private String remarks;
		private String matter;
		private String cardId;
		private String uniqueid;
		private String recommendNum;

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

		public String getUniqueid() {
			return uniqueid;
		}

		public void setUniqueid(String uniqueid) {
			this.uniqueid = uniqueid;
		}

		public String getRecommendNum() {
			return recommendNum;
		}

		public void setRecommendNum(String recommendNum) {
			this.recommendNum = recommendNum;
		}

	}
}
