package com.earthman.app.bean;

import java.util.List;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-24 下午6:58:38
 * @Decription 祭祀信息模型
 */
public class DeadInfoModel {

	private DeadInfoBase deadInfoBase;
	private List<DeadCategoryInfo> categoryList;// 贡品种类列表
	private List<DeadSurroundInfo> surroundList;// 贡品列表
	private List<DeadFewPresentedInfo> fewPresented;// 敬献记录
	private List<DeadFewWishesInfo> fewWishes;// 寄语记录
	
	public DeadInfoBase getDeadInfoBase() {
		return deadInfoBase;
	}

	public void setDeadInfoBase(DeadInfoBase deadInfoBase) {
		this.deadInfoBase = deadInfoBase;
	}

	public List<DeadCategoryInfo> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<DeadCategoryInfo> categoryList) {
		this.categoryList = categoryList;
	}

	public List<DeadSurroundInfo> getSurroundList() {
		return surroundList;
	}

	public void setSurroundList(List<DeadSurroundInfo> surroundList) {
		this.surroundList = surroundList;
	}

	public List<DeadFewWishesInfo> getFewWishes() {
		return fewWishes;
	}

	public void setFewWishes(List<DeadFewWishesInfo> fewWishes) {
		this.fewWishes = fewWishes;
	}

	public List<DeadFewPresentedInfo> getFewPresented() {
		return fewPresented;
	}

	public void setFewPresented(List<DeadFewPresentedInfo> fewPresented) {
		this.fewPresented = fewPresented;
	}

}
