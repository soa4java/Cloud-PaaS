/**
 * 
 */
package com.primeton.paas.mail.server.model;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class AttachmentInfo {

	private String mailId;
	private String attachId;
	private String name;
	private String path;

	public AttachmentInfo() {

	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "AttachmentInfo [mailId=" + mailId + ", attachId=" + attachId
				+ ", name=" + name + ", path=" + path + "]";
	}

}
