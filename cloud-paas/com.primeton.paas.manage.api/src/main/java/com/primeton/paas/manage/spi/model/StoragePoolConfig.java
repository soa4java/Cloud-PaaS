/**
 * 
 */
package com.primeton.paas.manage.spi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StoragePoolConfig implements Serializable {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 1992422812181427630L;
	
	private String id;
	private String name;
	private int storageSize;
	private int minSize = 1;
	private int maxSize = 1;	
	private int increaseSize = 1;
	private int decreaseSize = 1;
	private int retrySize = 50; 
	private long timeInterval = 10L;
	private long waitIncreaseTime = 60L;
	private long waitDecreaseTime = 10L; 
	private long createTimeout = 60L;
	private long destroyTimeout = 10L;
	private int isEnable = DISABLE;
	private String remarks;
	
	
	public static final int ENABLE = 1;
	public static final int DISABLE = 0;
	
	private Map<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public StoragePoolConfig() {
		super();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the storageSize
	 */
	public int getStorageSize() {
		return storageSize;
	}

	/**
	 * @param storageSize the storageSize to set
	 */
	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	/**
	 * @return the minSize
	 */
	public int getMinSize() {
		return minSize;
	}

	/**
	 * @param minSize the minSize to set
	 */
	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @return the increaseSize
	 */
	public int getIncreaseSize() {
		return increaseSize;
	}

	/**
	 * @param increaseSize the increaseSize to set
	 */
	public void setIncreaseSize(int increaseSize) {
		this.increaseSize = increaseSize;
	}

	/**
	 * @return the decreaseSize
	 */
	public int getDecreaseSize() {
		return decreaseSize;
	}

	/**
	 * @param decreaseSize the decreaseSize to set
	 */
	public void setDecreaseSize(int decreaseSize) {
		this.decreaseSize = decreaseSize;
	}


	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @return the waitIncreaseTime
	 */
	public long getWaitIncreaseTime() {
		return waitIncreaseTime;
	}

	/**
	 * @param waitIncreaseTime the waitIncreaseTime to set
	 */
	public void setWaitIncreaseTime(long waitIncreaseTime) {
		this.waitIncreaseTime = waitIncreaseTime;
	}

	/**
	 * @return the waitDecreaseTime
	 */
	public long getWaitDecreaseTime() {
		return waitDecreaseTime;
	}

	/**
	 * @param waitDecreaseTime the waitDecreaseTime to set
	 */
	public void setWaitDecreaseTime(long waitDecreaseTime) {
		this.waitDecreaseTime = waitDecreaseTime;
	}

	/**
	 * @return the createTimeout
	 */
	public long getCreateTimeout() {
		return createTimeout;
	}

	/**
	 * @param createTimeout the createTimeout to set
	 */
	public void setCreateTimeout(long createTimeout) {
		this.createTimeout = createTimeout;
	}

	/**
	 * @return the destroyTimeout
	 */
	public long getDestroyTimeout() {
		return destroyTimeout;
	}

	/**
	 * @param destroyTimeout the destroyTimeout to set
	 */
	public void setDestroyTimeout(long destroyTimeout) {
		this.destroyTimeout = destroyTimeout;
	}


	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	
	public int getRetrySize() {
		return retrySize;
	}

	public void setRetrySize(int retrySize) {
		this.retrySize = retrySize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName() + " id=[" + id + "], "
				+ " name=[" + name + "], " + " minSize=[" + minSize + "], "
				+ " maxSize=[" + maxSize + "], " + " increaseSize=["
				+ increaseSize + "], " + " decreaseSize=[" + decreaseSize
				+ "], " + " retrySize=[" + retrySize + "], "
				+ " timeInterval=[" + timeInterval + "], " + " remarks=["
				+ remarks + "], " + " isEnable=[" + isEnable + "].";
	}

}
