package com.earthman.app.bean;

import com.earthman.app.enums.RankingDateType;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-28 下午2:00:59
 * @Decription 排行信息实体
 */
public class RankingTypeInfo {

	private RankingDateType rankingType;
	private String rankingName;

	public RankingTypeInfo(RankingDateType rankingType, String rankingName) {
		super();
		this.rankingType = rankingType;
		this.rankingName = rankingName;
	}

	public RankingDateType getRankingType() {
		return rankingType;
	}

	public void setRankingType(RankingDateType rankingType) {
		this.rankingType = rankingType;
	}

	public String getRankingName() {
		return rankingName;
	}

	public void setRankingName(String rankingName) {
		this.rankingName = rankingName;
	}

}
