package com.earthman.app.bean;

import java.util.List;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-30 上午10:26:51
 * @Decription 排行内容实体类
 */
public class RankingContentModel {
	
	private List<RankingInfo> videoTop;// 视频排行
	private List<RankingInfo> moodsTop;// 人气排行
	private List<RankingInfo> incomesTop;// 消费排行
	private List<RankingInfo> recommendTop;// 推荐排行

	public List<RankingInfo> getVideoTop() {
		return videoTop;
	}

	public void setVideoTop(List<RankingInfo> videoTop) {
		this.videoTop = videoTop;
	}

	public List<RankingInfo> getMoodsTop() {
		return moodsTop;
	}

	public void setMoodsTop(List<RankingInfo> moodsTop) {
		this.moodsTop = moodsTop;
	}

	public List<RankingInfo> getIncomesTop() {
		return incomesTop;
	}

	public void setIncomesTop(List<RankingInfo> incomesTop) {
		this.incomesTop = incomesTop;
	}

	public List<RankingInfo> getRecommendTop() {
		return recommendTop;
	}

	public void setRecommendTop(List<RankingInfo> recommendTop) {
		this.recommendTop = recommendTop;
	}

}
