/**
 * 
 */
package com.primeton.paas.manage.spi.model;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StorageVO extends Storage {

	public static final int STATUS_NOTMOUNT = 0;
	public static final int STATUS_MOUNTED = 1;

	private String ip;
	private String mount = "undefined";
	private int status = STATUS_NOTMOUNT;

	public StorageVO() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
