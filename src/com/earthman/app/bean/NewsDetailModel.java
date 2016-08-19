package com.earthman.app.bean;

public class NewsDetailModel {
	
	private String code;
	private String message;
	private NewsInfo result;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NewsInfo getResult() {
		return result;
	}

	public void setResult(NewsInfo result) {
		this.result = result;
	}

	public class NewsInfo{
		private int id;
		private int sort;
		private String img;
		private String title;
		private String content;
		private String descript;
		private long createdAt;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getSort() {
			return sort;
		}
		public void setSort(int sort) {
			this.sort = sort;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getDescript() {
			return descript;
		}
		public void setDescript(String descript) {
			this.descript = descript;
		}
		public long getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(long createdAt) {
			this.createdAt = createdAt;
		}
		
		
	} 

}
