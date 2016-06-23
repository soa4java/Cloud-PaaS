/**
 * 
 */
package com.primeton.upcloud.ws.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VServer {
	
	private static final String ID = "pkVserver";
	
	private static final String IP = "privateNetAddress";
	
	private static final String NAME = "name";

	private Map<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public VServer() {
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
	public String getIp() {
		return attributes.get(IP);
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		attributes.put(IP, ip);
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
		return VServer.class.getName() + ":" + this.attributes;
	}
	
}
