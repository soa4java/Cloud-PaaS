/**
 * 
 */
package com.primeton.paas.console.common;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Entry {
	
	private String id;
	private String text;
	
	/**
	 * Default. <br>
	 */
	public Entry() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param text
	 */
	public Entry(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Entry [id=" + id + ", text=" + text + "]";
	}

}
