package com.earthman.app.eventbusbean;

import java.util.List;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月6日 下午6:41:01
 * @Decription
 */
public class DynamicDetialBean {
	private String id;
	private String userId;
	private String imgs;
	private String createdAt;
	private String avatar;
	private String nice;
	private String title;
	private int praiseAmount;
	private String hasPraised;// 是否点赞
	private List<CommentContent> articlesComments;

	public String getHasPraised() {
		return hasPraised;
	}

	public void setHasPraised(String hasPraised) {
		this.hasPraised = hasPraised;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNice() {
		return nice;
	}

	public void setNice(String nice) {
		this.nice = nice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPraiseAmount() {
		return praiseAmount;
	}

	public void setPraiseAmount(int praiseAmount) {
		this.praiseAmount = praiseAmount;
	}

	public List<CommentContent> getArticlesComments() {
		return articlesComments;
	}

	public void setArticlesComments(List<CommentContent> articlesComments) {
		this.articlesComments = articlesComments;
	}

	public class CommentContent {
		// {"nid":58,"nLevel":0,"sCort":"58","sUserId":null,"sReplyId":"芙蓉姐","sTitle":"胡姐真棒！"}
		// "nLevel": 0,
		// "nid": 48,
		// "sCort": "48",
		// "sReplyId": "芙蓉姐",
		// "sTitle": "辛苦大家了。"

		private String nLevel;
		private String nid;
		private String sCort;
		private String sReplyId;
		private String sTitle;

		public String getnLevel() {
			return nLevel;
		}

		public void setnLevel(String nLevel) {
			this.nLevel = nLevel;
		}

		public String getNid() {
			return nid;
		}

		public void setNid(String nid) {
			this.nid = nid;
		}

		public String getsCort() {
			return sCort;
		}

		public void setsCort(String sCort) {
			this.sCort = sCort;
		}

		public String getsReplyId() {
			return sReplyId;
		}

		public void setsReplyId(String sReplyId) {
			this.sReplyId = sReplyId;
		}

		public String getsTitle() {
			return sTitle;
		}

		public void setsTitle(String sTitle) {
			this.sTitle = sTitle;
		}
	}
}
