package com.earthman.app.bean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月19日 下午8:19:44
 * @Decription
 */
public class ContactURLBean extends BaseResponse {
	private contactResponse result;

	public class contactResponse {
		String filePath;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
	}

	public contactResponse getResult() {
		return result;
	}

	public void setResult(contactResponse result) {
		this.result = result;
	}

}
