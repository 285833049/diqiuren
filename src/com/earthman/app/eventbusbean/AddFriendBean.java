package com.earthman.app.eventbusbean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月9日 下午3:26:34
 * @Decription
 */
public class AddFriendBean {
	private int addFriend;

	public AddFriendBean(int addFriend) {
		super();
		this.addFriend = addFriend;
	}

	/**
	 * addFriend
	 *
	 * @return the addFriend
	 * @since 1.0.0
	 */

	public int getAddFriend() {
		return addFriend;
	}

	/**
	 * @param addFriend
	 *            the addFriend to set
	 */
	public void setAddFriend(int addFriend) {
		this.addFriend = addFriend;
	}

}
