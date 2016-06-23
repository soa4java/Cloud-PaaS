/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.io.Serializable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MyCluster implements Serializable {

	private static final long serialVersionUID = 7620310390714223628L;

	private String id;
	private String name;
	private String type;
	private boolean isStandalone;
	private int size;
	private int maxSize;
	private int minSize;
	private String owner;
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isStandalone() {
		return isStandalone;
	}

	public void setStandalone(boolean isStandalone) {
		this.isStandalone = isStandalone;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return "MyCluster [id=" + id + ", name=" + name + ", type=" + type
				+ ", isStandalone=" + isStandalone + ", size=" + size
				+ ", maxSize=" + maxSize + ", minSize=" + minSize + ", owner="
				+ owner + ", desc=" + desc + "]";
	}

}
