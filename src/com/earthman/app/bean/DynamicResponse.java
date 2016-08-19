package com.earthman.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月5日 下午3:06:11
 * @Decription
 */
public class DynamicResponse extends BaseResponse implements Serializable{
	// id:动态id
	// userId:动态发布人id
	// imgs:动态图片
	// ceatedAt:动态发布时间
	// avatar:头像
	// nice：昵称
	// title:动态文字内容
	// priaiseAmount:点赞人数
	// articlesComments：动态回复
	// nid:回复id
	// nLevel：层级：0开始
	// sUserId：回复人昵称
	// sReplyId：被回复人昵称
	// sTitle：回复内容
	private List<DynamicContent> result;

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DynamicResponse [result=" + result + "]";
	}

	public List<DynamicContent> getResult() {
		return result;
	}

	public void setResult(List<DynamicContent> result) {
		this.result = result;
	}

	public class DynamicContent implements Serializable{
		/**
		 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
		 *
		 * @since 1.0.0
		 */
		
		private static final long serialVersionUID = 1L;
		private String id;
		private String userId;
		private String imgs;
		private String createdAt;
		private String avatar;
		private String nice;
		private String title;
		private int praiseAmount;
		private String hasPraised;//是否点赞
		private List<CommentContent> articlesComments;
		private String remarks;
		private String location;
		private double longitude;
		private double latitude;
		private ArticlesBase articlesBase;
					
		public ArticlesBase getArticlesBase() {
			return articlesBase;
		}
		public void setArticlesBase(ArticlesBase articlesBase) {
			this.articlesBase = articlesBase;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		/* 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "DynamicContent [id=" + id + ", userId=" + userId + ", imgs=" + imgs + ", createdAt=" + createdAt + ", avatar=" + avatar + ", nice=" + nice
					+ ", title=" + title + ", praiseAmount=" + praiseAmount + ", hasPraised=" + hasPraised + ", articlesComments=" + articlesComments + "]";
		}
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
		
		public class ArticlesBase implements Serializable{
			private int id;
			private int userId;
			private String imgs;
			private String remarks;
			private String nice;
			private String title;
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public int getUserId() {
				return userId;
			}
			public void setUserId(int userId) {
				this.userId = userId;
			}
			public String getImgs() {
				return imgs;
			}
			public void setImgs(String imgs) {
				this.imgs = imgs;
			}
			public String getRemarks() {
				return remarks;
			}
			public void setRemarks(String remarks) {
				this.remarks = remarks;
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
		}
		
		public class CommentContent implements Serializable{
//			{"nid":58,"nLevel":0,"sCort":"58","sUserId":null,"sReplyId":"芙蓉姐","sTitle":"胡姐真棒！"}
//				articlesComments：动态回复对象
//			  commentId:回复id
//			  level：层级：0开始
//			  replyUserId：回复人id
//			  replyNice：回复人昵称
//			  pReplyNice：被回复人昵称
//			  content：回复内容

			/**
			 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
			 *
			 * @since 1.0.0
			 */
			
			private static final long serialVersionUID = 1L;
			public CommentContent() {
				super();
			}

			/**
			 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
			 *
			 * @since 1.0.0
			 */
			private String commentId;
			private String level;
			private String replyUserId;
			private String replyNice;
			private String pReplyNice;
			private String content;
			private String replyRemarks;
			private String pReplyRemarks;

			public String getReplyRemarks() {
				return replyRemarks;
			}

			public void setReplyRemarks(String replyRemarks) {
				this.replyRemarks = replyRemarks;
			}

			public String getPReplyRemarks() {
				return pReplyRemarks;
			}

			public void setPReplyRemarks(String pReplyRemarks) {
				this.pReplyRemarks = pReplyRemarks;
			}

			/* 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "CommentContent [commentId=" + commentId + ", level=" + level + ", replyUserId=" + replyUserId + ", replyNice=" + replyNice
						+ ", pReplyNice=" + pReplyNice + ", content=" + content + "]";
			}

			/**
			 * commentId
			 *
			 * @return  the commentId
			 * @since   1.0.0
			*/
			
			public String getCommentId() {
				return commentId;
			}

			/**
			 * @param commentId the commentId to set
			 */
			public void setCommentId(String commentId) {
				this.commentId = commentId;
			}

			/**
			 * level
			 *
			 * @return  the level
			 * @since   1.0.0
			*/
			
			public String getLevel() {
				return level;
			}

			/**
			 * @param level the level to set
			 */
			public void setLevel(String level) {
				this.level = level;
			}

			/**
			 * replyUserId
			 *
			 * @return  the replyUserId
			 * @since   1.0.0
			*/
			
			public String getReplyUserId() {
				return replyUserId;
			}

			/**
			 * @param replyUserId the replyUserId to set
			 */
			public void setReplyUserId(String replyUserId) {
				this.replyUserId = replyUserId;
			}

			/**
			 * replyNice
			 *
			 * @return  the replyNice
			 * @since   1.0.0
			*/
			
			public String getReplyNice() {
				return replyNice;
			}

			/**
			 * @param replyNice the replyNice to set
			 */
			public void setReplyNice(String replyNice) {
				this.replyNice = replyNice;
			}

			/**
			 * pReplyNice
			 *
			 * @return  the pReplyNice
			 * @since   1.0.0
			*/
			
			public String getPReplyNice() {
				return pReplyNice;
			}

			/**
			 * @param pReplyNice the pReplyNice to set
			 */
			public void setPReplyNice(String pReplyNice) {
				this.pReplyNice = pReplyNice;
			}

			/**
			 * content
			 *
			 * @return  the content
			 * @since   1.0.0
			*/
			
			public String getContent() {
				return content;
			}

			/**
			 * @param content the content to set
			 */
			public void setContent(String content) {
				this.content = content;
			}

//			/* 
//			 * @see android.os.Parcelable#describeContents()
//			 */
//			@Override
//			public int describeContents() {
//				return 0;
//			}
//
//			/* 
//			 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
//			 */
//			@Override
//			public void writeToParcel(Parcel dest, int flags) {
//				dest.writeString(commentId);
//				dest.writeString(level);
//				dest.writeString(replyUserId);
//				dest.writeString(replyNice);
//				dest.writeString(pReplyNice);
//				dest.writeString(content);
//			}
		}
	}
}
