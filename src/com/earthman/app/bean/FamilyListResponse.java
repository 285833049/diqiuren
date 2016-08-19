package com.earthman.app.bean;

import java.util.ArrayList;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月21日 上午9:34:36
 * @Decription
 */
public class FamilyListResponse extends BaseResponse {
	// {
	// "code": "000000",
	// "result": {
	// "myFamily": [
	// {
	// "avatar":
	// "http://localhost:8080/images/avatar/pettyIcon/1601/16012603530558998.jpg",
	// "cardId": "AA9999990",
	// "id": 172,
	// "nice": "玲玲",
	// "phone": "18673841847"
//	matter
	// }
	// ]
	// }
	// }
	private FamilyResult result;

	/**
	 * result
	 *
	 * @return the result
	 * @since 1.0.0
	 */

	public FamilyResult getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(FamilyResult result) {
		this.result = result;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FamilyListResponse [result=" + result + "]";
	}

	public class FamilyResult {
		private ArrayList<MyFamily> myFamily;

		/*
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "FamilyResult [myFamily=" + myFamily + "]";
		}

		/**
		 * myFamily
		 *
		 * @return the myFamily
		 * @since 1.0.0
		 */

		public ArrayList<MyFamily> getMyFamily() {
			return myFamily;
		}

		/**
		 * @param myFamily
		 *            the myFamily to set
		 */
		public void setMyFamily(ArrayList<MyFamily> myFamily) {
			this.myFamily = myFamily;
		}

		public class MyFamily {
			private String avatar;
			private String cardId;
			private String id;
			private String nice;
			private String matter;
			private String phone;

			/**
			 * avatar
			 *
			 * @return the avatar
			 * @since 1.0.0
			 */

			public String getAvatar() {
				return avatar;
			}

			/**
			 * matter
			 *
			 * @return  the matter
			 * @since   1.0.0
			*/
			
			public String getMatter() {
				return matter;
			}

			/**
			 * @param matter the matter to set
			 */
			public void setMatter(String matter) {
				this.matter = matter;
			}

			/**
			 * @param avatar
			 *            the avatar to set
			 */
			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			/**
			 * cardId
			 *
			 * @return the cardId
			 * @since 1.0.0
			 */

			public String getCardId() {
				return cardId;
			}

			/**
			 * @param cardId
			 *            the cardId to set
			 */
			public void setCardId(String cardId) {
				this.cardId = cardId;
			}

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
			 * nice
			 *
			 * @return the nice
			 * @since 1.0.0
			 */

			public String getNice() {
				return nice;
			}

			/**
			 * @param nice
			 *            the nice to set
			 */
			public void setNice(String nice) {
				this.nice = nice;
			}

			/**
			 * phone
			 *
			 * @return the phone
			 * @since 1.0.0
			 */

			public String getPhone() {
				return phone;
			}

			/**
			 * @param phone
			 *            the phone to set
			 */
			public void setPhone(String phone) {
				this.phone = phone;
			}

			/* 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return "MyFamily [avatar=" + avatar + ", cardId=" + cardId + ", id=" + id + ", nice=" + nice + ", matter=" + matter + ", phone=" + phone + "]";
			}

		}
	}
}
