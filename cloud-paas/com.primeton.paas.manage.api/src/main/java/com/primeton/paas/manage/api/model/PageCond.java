/**
 * 
 */
package com.primeton.paas.manage.api.model;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class PageCond implements IPageCond {

	private static final long serialVersionUID = 2908731778275043309L;
	
	private int begin = 0;
	private int length = 10;
	private int currentPage;
	private int totalPage;
	private int count;
	
	/**
	 * 
	 */
	public PageCond() {
		super();
	}
	
	/**
	 * 
	 * @param begin
	 * @param length
	 * @param count
	 */
	public PageCond(int begin, int length, int count) {
		super();
		if(begin >= 0) this.begin = begin;
		if(length > 0) this.length = length;
		if(count >= 0) this.count = count;
	}

	public int getBegin() {
		return this.begin;
	}

	public void setBegin(int begin) {
		if (begin >= 0) {
			this.begin = begin;
		}
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		if (length > 0)
			this.length = length;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		if(count >= 0) {
			this.count = count;
		}
	}

	public int getCurrentPage() {
		if (this.count == 0) {
			this.currentPage = 0;
		} else {
			this.currentPage = begin / length + 1;
		}
		return this.currentPage;
	}

	public int getTotalPage() {
		if (this.count % this.length == 0) {
			this.totalPage = this.count / this.length;
		} else {
			this.totalPage = this.count / this.length + 1;
		}
		return this.totalPage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(super.toString())
			.append(" [")
			.append("begin=" + getBegin())
			.append(", length=" + getLength())
			.append(", count=" + getCount())
			.append(", currentPage=" + getCurrentPage())
			.append(", totalPage=" + getTotalPage())
			.append("]")
			.toString();
	}

}
