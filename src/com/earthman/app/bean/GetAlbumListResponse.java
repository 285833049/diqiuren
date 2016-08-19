package com.earthman.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-1 下午5:44:12
 * @Decription
 */
public class GetAlbumListResponse extends BaseResponse implements Serializable{

	private ArrayList<Album> result;
		
	public ArrayList<Album> getResult() {
		return result;
	}

	public void setResult(ArrayList<Album> result) {
		this.result = result;
	}

	public class Album implements Serializable{
		private int id;
		private long createdAt;
		private String img;
		private String name;
		private String descript;
		private String albumsClass;
		private int amount;
								
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public long getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(long createdAt) {
			this.createdAt = createdAt;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescript() {
			return descript;
		}
		public void setDescript(String descript) {
			this.descript = descript;
		}
		public String getAlbumsClass() {
			return albumsClass;
		}
		public void setAlbumsClass(String albumsClass) {
			this.albumsClass = albumsClass;
		}
						
	}
}
