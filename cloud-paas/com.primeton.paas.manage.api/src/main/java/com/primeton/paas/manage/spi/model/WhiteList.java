/**
 * 
 */
package com.primeton.paas.manage.spi.model;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WhiteList {
	
	public static final int STATUS_NOTMOUNT = 0;
	public static final int STATUS_MOUNTED = 1;

	private String id;
	private String ip;
	private String path = "";
	private int status = STATUS_NOTMOUNT;
	
	public WhiteList() {
		super();
	}

	/**
	 * @param id
	 * @param ip
	 */
	public WhiteList(String id, String ip) {
		super();
		this.id = id;
		this.ip = ip;
	}

	/**
	 * @param id
	 * @param ip
	 * @param path
	 * @param status
	 */
	public WhiteList(String id, String ip, String path, int status) {
		super();
		this.id = id;
		this.ip = ip;
		this.path = path;
		this.status = status;
	}

	/**
	 * @return the id
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
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "WhiteList [id=" + id + ", ip=" + ip + ", path=" + path
				+ ", status=" + status + "]";
	}

}
