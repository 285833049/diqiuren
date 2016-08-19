package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-18 上午10:48:26
 * @Decription
 */
public class GetHomeNewsResponse extends BaseResponse{

	private GetHomeNewsResult result;

	public GetHomeNewsResult getResult() {
		return result;
	}

	public void setResult(GetHomeNewsResult result) {
		this.result = result;
	}

	public class GetHomeNewsResult {
		private ArrayList<News> companyNews;
		private ArrayList<News> earthManNews;
		private ArrayList<News> worldsNews;
		private int total;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public ArrayList<News> getCompanyNews() {
			return companyNews;
		}

		public void setCompanyNews(ArrayList<News> companyNews) {
			this.companyNews = companyNews;
		}

		public ArrayList<News> getEarthManNews() {
			return earthManNews;
		}

		public void setEarthManNews(ArrayList<News> earthManNews) {
			this.earthManNews = earthManNews;
		}

		public ArrayList<News> getWorldsNews() {
			return worldsNews;
		}

		public void setWorldsNews(ArrayList<News> worldsNews) {
			this.worldsNews = worldsNews;
		}

		public class News{
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

}
