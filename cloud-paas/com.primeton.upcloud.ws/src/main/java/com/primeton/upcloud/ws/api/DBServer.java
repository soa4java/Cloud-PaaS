/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liming(mailto:li-ming@primeton.com)
 *
 */
public class DBServer {
	
	private static final String ID = "id";
	private static final String URL = "connectUrl";
	private static final String NAME = "name";

	private Map<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public DBServer() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return attributes.get(ID);
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		attributes.put(ID, id);
	}

	/**
	 * @return the ip
	 */
	public String getUrl() {
		return attributes.get(URL);
	}

	/**
	 * @param ip the ip to set
	 */
	public void setUrl(String url) {
		attributes.put(URL, url);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return attributes.get(NAME);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		attributes.put(NAME, name);
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		if (attributes == null) {
			this.attributes.clear();
		} else {
			this.attributes = attributes;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return DBServer.class.getName() + ":" + this.attributes;
	}
	
}
