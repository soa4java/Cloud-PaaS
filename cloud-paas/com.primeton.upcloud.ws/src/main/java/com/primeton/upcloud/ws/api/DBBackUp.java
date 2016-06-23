/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class DBBackUp {
	
	private static final String ID = "uniqueId";
	
	private Map<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public DBBackUp() {
		super();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Integer.parseInt(attributes.get(ID));
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		attributes.put(ID, String.valueOf(id));
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
		return DBBackUp.class.getName() + ":" + this.attributes;
	}
	
}
