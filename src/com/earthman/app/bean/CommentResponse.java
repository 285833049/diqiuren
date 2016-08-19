package com.earthman.app.bean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月7日 上午10:52:58
 * @Decription
 */
public class CommentResponse extends BaseResponse {
//	  "articleId": 3426,
//      "articleUserId": 6,
//      "authorId": 4218,
//      "content": "ddss",
//      "parentId": 190,
//      "replyId": 6
	
	private CommentResult result;
	
	/**
	 * result
	 *
	 * @return  the result
	 * @since   1.0.0
	*/
	
	public CommentResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(CommentResult result) {
		this.result = result;
	}

	public class CommentResult {
		private String type;
		private String articleId;
		private String articleUserId;
		private String authorId;
		private String content;
		private String parentId;
		private String replyId;
		private int position;
		
		/**
		 * position
		 *
		 * @return  the position
		 * @since   1.0.0
		*/
		
		public int getPosition() {
			return position;
		}
		/**
		 * @param position the position to set
		 */
		public void setPosition(int position) {
			this.position = position;
		}
		/**
		 * articleId
		 *
		 * @return  the articleId
		 * @since   1.0.0
		*/
		
		public String getArticleId() {
			return articleId;
		}
		/**
		 * type
		 *
		 * @return  the type
		 * @since   1.0.0
		*/
		
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * parentId
		 *
		 * @return  the parentId
		 * @since   1.0.0
		*/
		
		public String getParentId() {
			return parentId;
		}
		/**
		 * @param parentId the parentId to set
		 */
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		/**
		 * replyId
		 *
		 * @return  the replyId
		 * @since   1.0.0
		*/
		
		public String getReplyId() {
			return replyId;
		}
		/**
		 * @param replyId the replyId to set
		 */
		public void setReplyId(String replyId) {
			this.replyId = replyId;
		}
		/**
		 * @param articleId the articleId to set
		 */
		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}
		/**
		 * articleUserId
		 *
		 * @return  the articleUserId
		 * @since   1.0.0
		*/
		
		public String getArticleUserId() {
			return articleUserId;
		}
		/**
		 * @param articleUserId the articleUserId to set
		 */
		public void setArticleUserId(String articleUserId) {
			this.articleUserId = articleUserId;
		}
		/**
		 * authorId
		 *
		 * @return  the authorId
		 * @since   1.0.0
		*/
		
		public String getAuthorId() {
			return authorId;
		}
		/**
		 * @param authorId the authorId to set
		 */
		public void setAuthorId(String authorId) {
			this.authorId = authorId;
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
		
	}
	
}
