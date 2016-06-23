/**
 * 
 */
package com.primeton.paas.collect.server;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.collect.common.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServerConfig implements Constants {
	
	private Map<String, Object> metadata = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	public ServerConfig() {
		super();
	}

	/**
	 * @param metadata
	 */
	public ServerConfig(Map<String, Object> metadata) {
		super();
		if (metadata != null) {
			this.metadata = metadata;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T get(String key) {
		return (T) get(key, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(String key, T defaultValue) {
		T value = (T) metadata.get(key);
		return (value == null) ? defaultValue : value;
	}
	
	public void set(String key, Object value) {
		if (StringUtil.isNotEmpty(key)) {
			metadata.put(key, value);
		}
	}

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, Object> metadata) {
		if (metadata == null) {
			this.metadata.clear();
		} else {
			this.metadata = metadata;
		}
	}
	
}
