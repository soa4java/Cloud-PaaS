/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StorageNasVolume {

	private static String VOLUME_ID = "pkStorageNasVolume";

	private static String VOLUME_NAME = "volumeName";

	private static String VOLUME_PATH = "volumePath";

	private static String VOLUME_SIZE = "volumeSize";

	private static String VOLUME_WHITELIST = "volumeWhiteList";

	private Map<String, String> attributes = new HashMap<String, String>();

	/**
	 *
	 */
	public StorageNasVolume() {
		super();
	}

	public String getVolumeID() {
		return attributes.get(VOLUME_ID);
	}

	public void setVolumeID(String volumeID) {
		attributes.put(VOLUME_ID, volumeID);
	}

	public String getVolumePath() {
		return attributes.get(VOLUME_PATH);
	}

	public void setVolumePath(String volumePath) {
		attributes.put(VOLUME_PATH, volumePath);
	}

	public String getVolumeSize() {
		return attributes.get(VOLUME_SIZE);
	}

	public void setVolumeSize(String volumeSize) {
		attributes.put(VOLUME_SIZE, volumeSize);
	}

	public String getVolumeName() {
		return attributes.get(VOLUME_NAME);
	}

	public void setVolumeName(String volumeSize) {
		attributes.put(VOLUME_NAME, volumeSize);
	}

	public String getVolumeWhiteList() {
		return attributes.get(VOLUME_WHITELIST);
	}

	public void setVolumeWhiteList(String volumeWhiteList) {
		attributes.put(VOLUME_WHITELIST, volumeWhiteList);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		if (attributes == null) {
			this.attributes.clear();
		} else {
			this.attributes = attributes;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return StorageNasVolume.class.getName() + ":" + this.attributes;
	}

}
