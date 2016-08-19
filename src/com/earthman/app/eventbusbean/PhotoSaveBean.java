package com.earthman.app.eventbusbean;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-6-15 下午4:23:58
 * @Decription
 */
public class PhotoSaveBean {
	private int position;
	private String path;
	
	public PhotoSaveBean(int position, String path){
		this.position = position;
		this.path = path;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	

}
