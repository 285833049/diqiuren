package com.earthman.app.eventbusbean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月6日 上午10:23:53
 * @Decription
 */
public class DynamicTypeBean {
	private String dynamicType;
	private int position;

	public DynamicTypeBean(String dynamicType,int position) {
		super();
		this.dynamicType = dynamicType;
		this.position = position;
	}

	/**
	 * position
	 *
	 * @return  the position
	 * @since   1.0.0
	*/
	
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * dynamicType
	 *
	 * @return the dynamicType
	 * @since 1.0.0
	 */

	public String getDynamicType() {
		return dynamicType;
	}

	/**
	 * @param dynamicType
	 *            the dynamicType to set
	 */
	public void setDynamicType(String dynamicType) {
		this.dynamicType = dynamicType;
	}
}
