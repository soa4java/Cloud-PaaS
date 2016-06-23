/**
 * 
 */
package com.primeton.paas.mail.model;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Attachment {

	private String attachId;
	private String name;
	private byte[] context;

	/**
	 * Default. <br>
	 */
	public Attachment() {
		super();
	}

	/**
	 * 
	 * @param name
	 * @param context
	 */
	public Attachment(String name, byte[] context) {
		super();
		this.name = name;
		this.context = context;
	}
	
	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContext() {
		return context;
	}

	public void setContext(byte[] context) {
		this.context = context;
	}

}
