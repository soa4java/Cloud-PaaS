/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractCluster implements ICluster {

	private static final long serialVersionUID = -3337231411774683779L;
	
	// extends attributes keys
	public static final String MAX_SIZE = "maxSize";
	public static final String MIN_SIZE = "minSize";
	
	private String id;
	private String name;
	private String type;
	private String owner;
	private String desc;
	private Map<String, String> attributes = new HashMap<String, String>();
	

	/**
	 * Default Constructor<br>
	 */
	public AbstractCluster() {
		super();
		setMaxSize(1);
		setMinSize(1);
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	protected void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return getValue(MAX_SIZE, 1);
	}


	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		setValue(MAX_SIZE, maxSize);
	}


	/**
	 * @return the minSize
	 */
	public int getMinSize() {
		return getValue(MIN_SIZE, 1);
	}


	/**
	 * @param minSize the minSize to set
	 */
	public void setMinSize(int minSize) {
		setValue(MIN_SIZE, minSize);
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}


	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}


	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.model.ICluster#getAttributes()
	 */
	public Map<String, String> getAttributes() {
		return this.attributes;
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
			.append(", owner:").append(owner)
			.append(", desc:").append(desc)
			.append(", attributes:").append(attributes)
			.append("]").toString();
	}

	
}
