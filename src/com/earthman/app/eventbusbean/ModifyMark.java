package com.earthman.app.eventbusbean;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-5-12 上午11:31:15
 * @Decription
 */
public class ModifyMark {
	
	private String id;
	private String remark;
	
	public ModifyMark(String id, String remark){
		this.id = id;
		this.remark = remark;
	}
	public ModifyMark(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
