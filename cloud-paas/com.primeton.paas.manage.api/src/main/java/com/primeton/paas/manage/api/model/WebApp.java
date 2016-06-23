/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class WebApp implements Serializable {
	
	private static final long serialVersionUID = 5450193758166656720L;
	
	public static final int STATE_WAIT_OPEN = 0;
	public static final int STATE_OPEND = 1;
	public static final int STATE_WAIT_DESTORY = 2;
	
	public static final String TYPE_FREE = "free";
	public static final String TYPE_CHARGE = "charge";
	
	public static final String DEFAULT_OWNER = "system";
	
	private String name;
	private String displayName;
	private String owner = DEFAULT_OWNER;
	private String desc;
	
	private static final String STATE = "state";
	private static final String TYPE = "type";
	private static final String SECONDARY_DOMAIN = "secondaryDomain";
	private static final String PERSONALIZED_DOMAIN = "personalizedDomain";
	private static final String EOS_APP = "eosApp";

	
	private Map<String, String> attributes = new HashMap<String, String>();
	
	public WebApp() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public int getState() {
		return (int)getValue(STATE, STATE_WAIT_OPEN);
	}

	/**
	 * 
	 * @param state
	 */
	public void setState(int state) {
		addAttribute(STATE, String.valueOf(state));
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return getValue(TYPE, null);
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		addAttribute(TYPE, type);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSecondaryDomain() {
		return getValue(SECONDARY_DOMAIN, null);
	}

	/**
	 * 
	 * @param secondaryDomain
	 */
	public void setSecondaryDomain(String secondaryDomain) {
		addAttribute(SECONDARY_DOMAIN, secondaryDomain);
	}

	/**
	 * 
	 * @return
	 */
	public String getPersonalizedDomain() {
		return getValue(PERSONALIZED_DOMAIN, null);
	}

	/**
	 * 
	 * @param personalizedDomain
	 */
	public void setPersonalizedDomain(String personalizedDomain) {
		addAttribute(PERSONALIZED_DOMAIN, personalizedDomain);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEosApp() {
		return getValue(EOS_APP, false);
	}
	
	public void setEosApp(boolean isEosApp) {
		addAttribute(EOS_APP, isEosApp);
	}

	/**
	 * 
	 * @return
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributes
	 */
	public void setAttributes(Map<String, String> attributes) {
		if(attributes == null) {
			this.attributes.clear();
			return;
		}
		this.attributes = attributes;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addAttribute(String key, String value) {
		if (key == null || key.trim().length() == 0) {
			return;
		}
		this.attributes.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addAttribute(String key, boolean value) {
		addAttribute(key, String.valueOf(value));
	}
	
	/**
	 * 
	 * @param attributes
	 */
	public void addAttributes(Map<String, String> attributes) {
		if (attributes == null || attributes.size() == 0) {
			return;
		}
		this.attributes.putAll(attributes);
	}
	
	/**
	 * 
	 * @param key
	 */
	public void removeAttribute(String key) {
		if (key == null || key.trim().length() == 0) {
			return;
		}
		if (attributes == null || attributes.size() == 0) {
			return;
		}
		this.attributes.remove(key);
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private String getValue(String key, String defaultValue) {
		if (StringUtil.isEmpty(key)) {
			return defaultValue;
		}
		String value = this.attributes.get(key);
		return value == null ? defaultValue : value;
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private long getValue(String key, long defaultValue) {
		if(StringUtil.isEmpty(key)) {
			return defaultValue;
		}
		String value = this.attributes.get(key);
		if (!StringUtil.isEmpty(value)) {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private boolean getValue(String key, boolean defaultValue) {
		if(StringUtil.isEmpty(key)) {
			return defaultValue;
		}
		String value = this.attributes.get(key);
		if (!StringUtil.isEmpty(value)) {
			return Boolean.parseBoolean(value);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("name:").append(name)
			.append(", displayName:").append(displayName)
			.append(", owner:").append(owner)
			.append(", desc:").append(desc)
			.append(", attributes:{").append(attributes + "}");
		
		return stringBuffer.toString();
	}
	
}
