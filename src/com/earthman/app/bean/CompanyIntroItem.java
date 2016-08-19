package com.earthman.app.bean;

import com.earthman.app.enums.CompanyIntroType;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-29 下午6:12:10
 * @Decription 公司介绍实体
 */
public class CompanyIntroItem {

	private CompanyIntroType introType;
	private String introName;

	public CompanyIntroItem(CompanyIntroType introType, String introName) {
		super();
		this.introType = introType;
		this.introName = introName;
	}

	public CompanyIntroType getIntroType() {
		return introType;
	}

	public void setIntroType(CompanyIntroType introType) {
		this.introType = introType;
	}

	public String getIntroName() {
		return introName;
	}

	public void setIntroName(String introName) {
		this.introName = introName;
	}

}
