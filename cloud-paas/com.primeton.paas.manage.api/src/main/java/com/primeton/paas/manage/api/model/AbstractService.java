/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * PAAS Service Abstract Implements. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractService implements IService {
	
	private static final long serialVersionUID = -8054861646408347609L;
	
	private String id;
	private String name;
	private String type;
	private String parentId = "-1";
	private String ip;
	private int port;
	private int state;
	private int pid;
	private String owner;
	private Date startDate;
	private Map<String, String> attributes = new HashMap<String, String>();
	
	private static final String CREATE_BY = "createdBy";
	private static final String CREATE_DATE = "createdDate";
	private static final String STANDALONE = "isStandalone";
	private static final String PACKAGE_ID = "packageId";
	private static final String HA_MODE = "haMode"; // Cluster | Master&Slave
	private static final String MODE = "mode"; // LOGIC | PHYSICAL

	/**
	 * Default. <br>
	 */
	public AbstractService() {
		super();
	}

	/**
	 * 
	 * @param type
	 */
	protected AbstractService(String type) {
		super();
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCreatedBy() {
		return getValue(CREATE_BY);
	}

	public void setCreatedBy(String createdBy) {
		setValue(CREATE_BY, createdBy);
	}

	public Date getCreatedDate() {
		return new Date(getValue(CREATE_DATE, System.currentTimeMillis()));
	}

	public void setCreatedDate(Date createdDate) {
		if (createdDate != null) {
			setValue(CREATE_DATE, createdDate.getTime());
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.model.IService#isStandalone()
	 */
	public boolean isStandalone() {
		return getValue(STANDALONE, false);
	}
	
	public void setStandalone(boolean isStandalone) {
		setValue(STANDALONE, isStandalone);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.model.IService#getPackageId()
	 */
	public String getPackageId() {
		return getValue(PACKAGE_ID);
	}
	
	public void setPackageId(String packageId) {
		setValue(PACKAGE_ID, packageId);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.model.IService#getHaMode()
	 */
	public String getHaMode() {
		return getValue(HA_MODE);
	}
	
	/**
	 * 
	 * @param haMode
	 */
	public void setHaMode(String haMode) {
		setValue(HA_MODE, haMode);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.model.IService#getMode()
	 */
	public String getMode() {
		return getValue(MODE, MODE_PHYSICAL);
	}
	
	protected void setMode(String mode) {
		setValue(MODE, ((MODE_LOGIC.equals(mode) || MODE_PHYSICAL.equals(mode)))
				? mode : MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected String getValue(String key, String defaultValue) {
		if(StringUtil.isEmpty(key)) {
			return defaultValue;
		}
		if(attributes != null) {
			String value = attributes.get(key);
			return value == null ? defaultValue : value;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected String getValue(String key) {
		return getValue(key, null);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, String value) {
		if(StringUtil.isEmpty(key)) {
			return;
		}
		this.attributes.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected byte getValue(String key, byte defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		try {
			if(value != null) {
				return Byte.parseByte(value);
			}
		} catch (NumberFormatException e) {
			// ignore it
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, byte value) {
		setValue(key, String.valueOf(value));
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected int getValue(String key, int defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		try {
			if(value != null) {
				return Integer.parseInt(value);
			}
		} catch (NumberFormatException e) {
			// ignore it
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, int value) {
		setValue(key, String.valueOf(value));
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected long getValue(String key, long defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		try {
			if(value != null) {
				return Long.parseLong(value);
			}
		} catch (NumberFormatException e) {
			// ignore it
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, long value) {
		setValue(key, String.valueOf(value));
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected boolean getValue(String key, boolean defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValue(String key, boolean value) {
		setValue(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(getClass().getSimpleName())
			.append("[id:").append(id)
			.append(", name:").append(name)
			.append(", type:").append(type)
			.append(", parentId:").append(parentId)
			.append(", ip:").append(ip)
			.append(", port:").append(port)
			.append(", state:").append(state)
			.append(", pid:").append(pid)
			.append(", owner:").append(owner)
			.append(", attributes:").append(attributes)
			.append("]").toString();
	}

}
