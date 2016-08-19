package com.earthman.app.bean;


/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-23 下午7:17:55
 * @Decription
 */
public class GetVideoDetailResponse extends BaseResponse{
	
	private VideoDetail result;

	public VideoDetail getResult() {
		return result;
	}

	public void setResult(VideoDetail result) {
		this.result = result;
	}
	
	
	public static class VideoDetail{
		private int id;
		private String link;
		private String name;
		private String createdAt;
		private String updatedAt;
		private String userId;
		private String author;
		private String details;
		private String img;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}
		public String getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getDetails() {
			return details;
		}
		public void setDetails(String details) {
			this.details = details;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		
	}
	
}
