package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月24日 上午10:39:22
 * @Decription
 */
public class QuanziListResponse extends BaseResponse {
	// "result": [
	// {
	// "id": 101,
	// "name": "亲友圈",
	// "sort": 0,
	// "userId": 0
	// }
	private ArrayList<quanziLiseResult> result;

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuanziListResponse [result=" + result + "]";
	}

	/**
	 * result
	 *
	 * @return the result
	 * @since 1.0.0
	 */

	public ArrayList<quanziLiseResult> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ArrayList<quanziLiseResult> result) {
		this.result = result;
	}

	public  class quanziLiseResult {
		private String id;
		private String name;
		private String sort;
		private String userId;

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
		 * name
		 *
		 * @return the name
		 * @since 1.0.0
		 */

		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * sort
		 *
		 * @return the sort
		 * @since 1.0.0
		 */

		public String getSort() {
			return sort;
		}

		/**
		 * @param sort
		 *            the sort to set
		 */
		public void setSort(String sort) {
			this.sort = sort;
		}

		/**
		 * userId
		 *
		 * @return the userId
		 * @since 1.0.0
		 */

		public String getUserId() {
			return userId;
		}

		/**
		 * @param userId
		 *            the userId to set
		 */
		public void setUserId(String userId) {
			this.userId = userId;
		}

		/*
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "quanziLiseResult [id=" + id + ", name=" + name + ", sort=" + sort + ", userId=" + userId + "]";
		}

	}
}
