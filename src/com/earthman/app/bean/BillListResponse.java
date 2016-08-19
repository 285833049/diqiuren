package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月19日 下午3:34:52
 * @Decription
 */
public class BillListResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": [
	// {
	// "createdAt": 1453713407000,
	// "id": 9,
	// "money": -10.00,
	// "otherAccount": "",
	// "payWay": "支付宝",
	// "type": "会员续费"
	// }
	// ]
	// }
	
	private ArrayList<BillResult> result;
	/**
	 * result
	 *
	 * @return  the result
	 * @since   1.0.0
	*/
	
	public ArrayList<BillResult> getResult() {
		return result;
	}



	/**
	 * @param result the result to set
	 */
	public void setResult(ArrayList<BillResult> result) {
		this.result = result;
	}



	public class BillResult {
		
		/**
		 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
		 *
		 * @since 1.0.0
		 */
		
		private String createdAt;
		private String id;
		private String money;
		private String otherAccount;
		private String payWay;
		private String type;
		/**
		 * createdAt
		 *
		 * @return  the createdAt
		 * @since   1.0.0
		*/
		
		public String getCreatedAt() {
			return createdAt;
		}
		/**
		 * @param createdAt the createdAt to set
		 */
		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}
		/**
		 * id
		 *
		 * @return  the id
		 * @since   1.0.0
		*/
		
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * money
		 *
		 * @return  the money
		 * @since   1.0.0
		*/
		
		public String getMoney() {
			return money;
		}
		/**
		 * @param money the money to set
		 */
		public void setMoney(String money) {
			this.money = money;
		}
		/**
		 * otherAccount
		 *
		 * @return  the otherAccount
		 * @since   1.0.0
		*/
		
		public String getOtherAccount() {
			return otherAccount;
		}
		/**
		 * @param otherAccount the otherAccount to set
		 */
		public void setOtherAccount(String otherAccount) {
			this.otherAccount = otherAccount;
		}
		/**
		 * payWay
		 *
		 * @return  the payWay
		 * @since   1.0.0
		*/
		
		public String getPayWay() {
			return payWay;
		}
		/**
		 * @param payWay the payWay to set
		 */
		public void setPayWay(String payWay) {
			this.payWay = payWay;
		}
		/**
		 * type
		 *
		 * @return  the type
		 * @since   1.0.0
		*/
		
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		
		
	}
	
}
