package com.earthman.app.bean;

/**
 * 作者：Zhou
 * 日期：2015-11-10 下午1:04:54
 * 版权：地球人
 * 描述：（）
 */
public class UploadImgResponse extends BaseResponse {

	private UploadImgResult result;

	public UploadImgResult getResult() {
		return result;
	}

	public void setResult(UploadImgResult result) {
		this.result = result;
	}

	public class UploadImgResult {

		private String piclink;
		private String filelink;

		public String getFilelink() {
			return filelink;
		}

		public void setFilelink(String filelink) {
			this.filelink = filelink;
		}

		public String getPiclink() {
			return piclink;
		}

		public void setPiclink(String piclink) {
			this.piclink = piclink;
		}

	}

}
