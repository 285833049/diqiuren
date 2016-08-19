package com.earthman.app.eventbusbean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月6日 下午5:48:55
 * @Decription
 */
public class OKDynamicSend {
	private int message ;

	/**
	 * message
	 *
	 * @return  the message
	 * @since   1.0.0
	*/
	
	public int getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(int message) {
		this.message = message;
	}

	public OKDynamicSend(int message) {
		super();
		this.message = message;
	}
	
}
