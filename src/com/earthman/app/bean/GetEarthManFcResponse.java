package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-7 下午9:52:52
 * @Decription
 */
public class GetEarthManFcResponse extends BaseResponse {

	private ArrayList<EarthManFcInfo> result;

	public ArrayList<EarthManFcInfo> getResult() {
		return result;
	}

	public void setResult(ArrayList<EarthManFcInfo> result) {
		this.result = result;
	}

	public class EarthManFcInfo {
		private int id;
		private String nice;
		private String avatar;
		private String remarks;
		private String cardid;
		private String uniqueid;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNice() {
			return nice;
		}

		public void setNice(String nice) {
			this.nice = nice;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getCardid() {
			return cardid;
		}

		public void setCardid(String cardid) {
			this.cardid = cardid;
		}

		public String getUniqueid() {
			return uniqueid;
		}

		public void setUniqueid(String uniqueid) {
			this.uniqueid = uniqueid;
		}

	}
}
