package com.earthman.app.bean;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月18日 上午11:48:20
 * @Decription
 */
public class RelativeItem {
		private String name;
		private int add;
		private int line;
		private int color;

		public RelativeItem(String name, int add, int line,int color) {
			super();
			this.name = name;
			this.add = add;
			this.line = line;
			this.color = color;
			
		}

		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAdd() {
			return add;
		}

		public void setAdd(int add) {
			this.add = add;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}
}
