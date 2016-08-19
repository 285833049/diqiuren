package com.earthman.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-6 下午7:19:37
 * @Decription
 */
public class GetVideoListResponse extends BaseResponse implements Serializable{

	private ArrayList<VideoItem> result;
		
	public ArrayList<VideoItem> getResult() {
		return result;
	}

	public void setResult(ArrayList<VideoItem> result) {
		this.result = result;
	}


	public class VideoItem implements Serializable{
		private int id;
		private int userId;
		private String descript;
		private String img;
		private String type;
		private String status;
		private String deletedAt;
		private String createdAt;
		private String updatedAt;
		private String albumeId;
		private String albumsId;
		private String forward;
		private String smallimg;
		private String title;
		private String videos;
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
		public String getDescript() {
			return descript;
		}
		public void setDescript(String descript) {
			this.descript = descript;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getDeletedAt() {
			return deletedAt;
		}
		public void setDeletedAt(String deletedAt) {
			this.deletedAt = deletedAt;
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
		public String getAlbumeId() {
			return albumeId;
		}
		public void setAlbumeId(String albumeId) {
			this.albumeId = albumeId;
		}
		public String getAlbumsId() {
			return albumsId;
		}
		public void setAlbumsId(String albumsId) {
			this.albumsId = albumsId;
		}
		public String getForward() {
			return forward;
		}
		public void setForward(String forward) {
			this.forward = forward;
		}
		public String getSmallimg() {
			return smallimg;
		}
		public void setSmallimg(String smallimg) {
			this.smallimg = smallimg;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getVideos() {
			return videos;
		}
		public void setVideos(String videos) {
			this.videos = videos;
		}
				
	}
}
