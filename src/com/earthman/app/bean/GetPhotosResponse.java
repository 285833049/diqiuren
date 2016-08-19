package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-3 下午12:53:06
 * @Decription
 */
public class GetPhotosResponse extends BaseResponse{

	private ArrayList<Photo> result;
		
	public ArrayList<Photo> getResult() {
		return result;
	}

	public void setResult(ArrayList<Photo> result) {
		this.result = result;
	}

	public class Photo{
		private int id;
		private String img;
		private String imgname;
		private boolean isSelected;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getImgname() {
			return imgname;
		}
		public void setImgname(String imgname) {
			this.imgname = imgname;
		}
		public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}		
		
	}
}
