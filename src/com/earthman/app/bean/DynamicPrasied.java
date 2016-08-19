package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年4月6日 下午8:05:18
 * @Decription
 */
public class DynamicPrasied extends BaseResponse {
	// {
	// "id": "1",
	// "value": "http://localhost:8080/images/avatar/avatar.jpg"
	// }
	private ArrayList<UserList> result;

	/**
	 * result
	 *
	 * @return the result
	 * @since 1.0.0
	 */

	public ArrayList<UserList> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<UserList> result) {
		this.result = result;
	}

	public class UserList {
		private String id;
		private String value;

		/**
		 * id
		 *
		 * @return the id
		 * @since 1.0.0
		 */

		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * value
		 *
		 * @return the value
		 * @since 1.0.0
		 */

		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

	}
}
