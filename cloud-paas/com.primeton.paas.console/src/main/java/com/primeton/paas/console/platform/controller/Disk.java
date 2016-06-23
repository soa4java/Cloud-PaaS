/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.console.platform.controller;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class Disk {
	
	private String filesystem;
	
	private String size;
	
	private String used;
	
	private String avail;
	
	private String use;
	
	private String mounted;

	public Disk() {
		super();
	}

	public String getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(String filesystem) {
		this.filesystem = filesystem;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getAvail() {
		return avail;
	}

	public void setAvail(String avail) {
		this.avail = avail;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getMounted() {
		return mounted;
	}

	public void setMounted(String mounted) {
		this.mounted = mounted;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Disk [filesystem=" + filesystem + ", size=" + size + ", used="
				+ used + ", avail=" + avail + ", use=" + use + ", mounted="
				+ mounted + "]";
	}

}
