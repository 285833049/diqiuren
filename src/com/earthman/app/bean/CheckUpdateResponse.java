package com.earthman.app.bean;


/**
 * 作者：Zhou
 * 日期：2015-10-28 下午4:57:15
 * 版权：地球人
 * 描述：（）
 */
public class CheckUpdateResponse extends BaseResponse{

	private CheckUpdateResult result;

	public CheckUpdateResult getResult() {
		return result;
	}

	public void setResult(CheckUpdateResult result) {
		this.result = result;
	}
	
	public class CheckUpdateResult {
		private int hasNew;
		private String name;
		private String content;
		private String size;
		private String downloadUrl;
		private String publishTime;
		private int mustUpdate;
		private String version;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getDownloadUrl() {
			return downloadUrl;
		}

		public void setDownloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}

		public String getPublishTime() {
			return publishTime;
		}

		public void setPublishTime(String publishTime) {
			this.publishTime = publishTime;
		}

		public int getHasNew() {
			return hasNew;
		}

		public void setHasNew(int hasNew) {
			this.hasNew = hasNew;
		}

		public int getMustUpdate() {
			return mustUpdate;
		}

		public void setMustUpdate(int mustUpdate) {
			this.mustUpdate = mustUpdate;
		}
	}
	
}
