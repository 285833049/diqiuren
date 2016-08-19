package com.earthman.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-7 下午8:41:45
 * @Decription
 */
public class GetHomeVideoResponse extends BaseResponse implements Serializable{
	private GetHomeVideoResult result;
	
	public GetHomeVideoResult getResult() {
		return result;
	}

	public void setResult(GetHomeVideoResult result) {
		this.result = result;
	}

	public class GetHomeVideoResult implements Serializable{
		private ArrayList<HomeVideo> videos;
		private int total;
		
		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public ArrayList<HomeVideo> getVideos() {
			return videos;
		}

		public void setVideos(ArrayList<HomeVideo> videos) {
			this.videos = videos;
		}	
		
		public class HomeVideo implements Serializable{
			private int id;
			private String link;
			private String name;
			private String createdAt;
			private String updatedAt;
			private String userId;
			private String author;
			private String details;
			private String img;
						
			public String getImg() {
				return img;
			}
			public void setImg(String img) {
				this.img = img;
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

		}
		
		
	}
	
	
	
	

}
