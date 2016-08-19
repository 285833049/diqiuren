package com.earthman.app.bean;

/**
 * 好友列表-新朋友数量的显示
 * 
 * @Author：xiexianyong
 * @Version：地球人
 * @Date：2016-4-7 下午2:59:20
 * @Decription
 */
public class NewFriendNumEvent {
	private int num;

	public NewFriendNumEvent(int msg) {
		num = msg;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
