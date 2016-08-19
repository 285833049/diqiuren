package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Title: HomeDataResult
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月11日
 */

public class HomeDataResult extends BaseResponse {

	private HomeData result;

	/**
	 * @return the result
	 */
	public HomeData getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(HomeData result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HomeDataResult [result=" + result + "]";
	}
	
	public class HomeData {
		private AboutUs aboutus;
		private ArrayList<Banner> banners;

		public AboutUs getAboutus() {
			return aboutus;
		}

		public void setAboutus(AboutUs aboutus) {
			this.aboutus = aboutus;
		}

		/**
		 * @return the banners
		 */
		public ArrayList<Banner> getBanners() {
			return banners;
		}

		/**
		 * @param banners
		 *            the banners to set
		 */
		public void setBanners(ArrayList<Banner> banners) {
			this.banners = banners;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HomeData [aboutus=" + aboutus + ", banners=" + banners + "]";
		}
		
		public class AboutUs{
			private String img;
			private String content;
			public String getImg() {
				return img;
			}
			public void setImg(String img) {
				this.img = img;
			}
			public String getContent() {
				return content;
			}
			public void setContent(String content) {
				this.content = content;
			}
			
		}
		
		public  class Banner {
			// "banners": [
			// {
			// "addressid": 1,
			// "areaid": 1,
			// "color": "",
			// "hide": 0,
			// "id": 1,
			// "img": "http://www.baidu.com/",
			// "status": "0",
			// "title": "广告1",
			// "url": "http://www.baidu.com/"
			// }
			// ]

			private String addressid;
			private String areaid;
			private String color;
			private String hide;
			private String img;
			private String smallImg;
			private String id;
			private String status;
			private String title;
			private String url;
			private String descript;
			private String createdAt;
			private String updateAt;
			
			public String getSmallImg() {
				return smallImg;
			}

			public void setSmallImg(String smallImg) {
				this.smallImg = smallImg;
			}

			public String getDescript() {
				return descript;
			}

			public void setDescript(String descript) {
				this.descript = descript;
			}

			public String getCreatedAt() {
				return createdAt;
			}

			public void setCreatedAt(String createdAt) {
				this.createdAt = createdAt;
			}

			public String getUpdateAt() {
				return updateAt;
			}

			public void setUpdateAt(String updateAt) {
				this.updateAt = updateAt;
			}

			/**
			 * @return the addressid
			 */
			public String getAddressid() {
				return addressid;
			}

			/**
			 * @param addressid
			 *            the addressid to set
			 */
			public void setAddressid(String addressid) {
				this.addressid = addressid;
			}

			/**
			 * @return the areaid
			 */
			public String getAreaid() {
				return areaid;
			}

			/**
			 * @param areaid
			 *            the areaid to set
			 */
			public void setAreaid(String areaid) {
				this.areaid = areaid;
			}

			/**
			 * @return the color
			 */
			public String getColor() {
				return color;
			}

			/**
			 * @param color
			 *            the color to set
			 */
			public void setColor(String color) {
				this.color = color;
			}

			/**
			 * @return the hide
			 */
			public String getHide() {
				return hide;
			}

			/**
			 * @param hide
			 *            the hide to set
			 */
			public void setHide(String hide) {
				this.hide = hide;
			}

			/**
			 * @return the img
			 */
			public String getImg() {
				return img;
			}

			/**
			 * @param img
			 *            the img to set
			 */
			public void setImg(String img) {
				this.img = img;
			}

			/**
			 * @return the id
			 */
			public String getId() {
				return id;
			}

			/**
			 * @param id
			 *            the id to set
			 */
			public void setId(String id) {
				this.id = id;
			}

			/**
			 * @return the status
			 */
			public String getStatus() {
				return status;
			}

			/**
			 * @param status
			 *            the status to set
			 */
			public void setStatus(String status) {
				this.status = status;
			}

			/**
			 * @return the title
			 */
			public String getTitle() {
				return title;
			}

			/**
			 * @param title
			 *            the title to set
			 */
			public void setTitle(String title) {
				this.title = title;
			}

			/**
			 * @return the url
			 */
			public String getUrl() {
				return url;
			}

			/**
			 * @param url
			 *            the url to set
			 */
			public void setUrl(String url) {
				this.url = url;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "Banner [addressid=" + addressid + ", areaid=" + areaid + ", color=" + color + ", hide=" + hide
						+ ", img=" + img + ", id=" + id + ", status=" + status + ", title=" + title + ", url=" + url + "]";
			}

		}
	}


}
