package com.earthman.app.eventbusbean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月7日 上午10:36:33
 * @Decription
 */
public class FlushReply {
	private int type;

	/**
	 * type
	 *
	 * @return  the type
	 * @since   1.0.0
	*/
	
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public FlushReply(int type) {
		super();
		this.type = type;
	}
	
}
