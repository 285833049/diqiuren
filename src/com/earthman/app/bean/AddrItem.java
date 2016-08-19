package com.earthman.app.bean;

public class AddrItem {
	private int addrId;
	private int addrCode;
	private int parentId;
	private String addrName;

	public AddrItem(){
		
	}
	
	public AddrItem(int addrId, int addrCode, String addrName, int parentId) {
		this.addrId = addrId;
		this.addrCode = addrCode;
		this.addrName = addrName;
		this.parentId = parentId;
	}

	public int getAddrId() {
		return addrId;
	}

	public void setAddrId(int addrId) {
		this.addrId = addrId;
	}

	public int getAddrCode() {
		return addrCode;
	}

	public void setAddrCode(int addrCode) {
		this.addrCode = addrCode;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}
	
	@Override
	public String toString() {
		return addrName;
	}
}
