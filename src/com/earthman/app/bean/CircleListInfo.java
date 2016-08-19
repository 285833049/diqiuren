package com.earthman.app.bean;

import java.util.List;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-19 下午4:35:58
 * @Decription 圈子列表对象
 */
public class CircleListInfo {
	private int groupId;
	private String name;
	private int total;
	private List<CircleFriendInfo> friends;
	
	public CircleListInfo() {
		super();
	}

	public CircleListInfo(String name) {
		super();
		this.name = name;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CircleFriendInfo> getFriends() {
		return friends;
	}

	public void setFriends(List<CircleFriendInfo> friends) {
		this.friends = friends;
	}

}
