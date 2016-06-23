/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.model;

/**
 * 主机套餐模板. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HostTemplate {
	
	public static final String UNIT_BYTE = "B";
	public static final String UNIT_KB = "KB";
	public static final String UNIT_MB = "MB";
	public static final String UNIT_GB = "GB";
	public static final String UNIT_TB = "TB";
	
	public static final String OS_SUSE = "SUSE";
	public static final String OS_REDHAT = "Readhat";
	public static final String OS_UBUNTU = "Ubuntu";
	
	private String templateId;
	private String templateName;
	private String imageId;
	private String profileId;
	private String unit = UNIT_GB;
	private int cpu = 1;
	private int memory = 1;
	private int storage;
	private String osName;
	private String osVersion;
	
	/**
	 * Default. <br>
	 */
	public HostTemplate() {
		super();
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "HostTemplate [templateId=" + templateId + ", templateName="
				+ templateName + ", imageId=" + imageId + ", profileId="
				+ profileId + ", unit=" + unit + ", cpu=" + cpu + ", memory="
				+ memory + ", storage=" + storage + ", osName=" + osName
				+ ", osVersion=" + osVersion + "]";
	}

}
