package com.earthman.app.bean;

import java.io.Serializable;



public class PhotoUploadProgressItem implements Serializable {

	private String name;
	private String img;
	private int progress;
	private String descript;
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}

}
