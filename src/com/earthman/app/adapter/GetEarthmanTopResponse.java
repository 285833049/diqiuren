package com.earthman.app.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.RankingInfo;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-8 上午10:19:40
 * @Decription
 */
public class GetEarthmanTopResponse extends BaseResponse{

	private ArrayList<RankingInfo> result;
	
	public ArrayList<RankingInfo> getResult() {
		return result;
	}

	public void setResult(ArrayList<RankingInfo> result) {
		this.result = result;
	}


	public class EarthManTop implements Serializable{
		/**
		 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
		 *
		 * @since 1.0.0
		 */
		
		private static final long serialVersionUID = 1L;
		private int id;
		private String nice;
		private String recommendNum;
		private String avatar;
		private String cardId;
				
		public String getCardId() {
			return cardId;
		}
		public void setCardId(String cardId) {
			this.cardId = cardId;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNice() {
			return nice;
		}
		public void setNice(String nice) {
			this.nice = nice;
		}
		public String getRecommendNum() {
			return recommendNum;
		}
		public void setRecommendNum(String recommendNum) {
			this.recommendNum = recommendNum;
		}		
	}
}
