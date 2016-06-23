/**
 * 
 */
package com.primeton.paas.manage.api.model;

import java.io.Serializable;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class Variable implements Serializable {

	private static final long serialVersionUID = -8828041454853182241L;
	
	private String varKey;
	private String varValue;
	private String description;
	
	/**
	 * 
	 */
	public Variable() {
		super();
	}

	/**
	 * @param key
	 * @param value
	 */
	public Variable(String key, String value) {
		super();
		this.varKey = key;
		this.varValue = value;
	}

	/**
	 * @param key
	 * @param value
	 * @param desc
	 */
	public Variable(String key, String value, String desc) {
		super();
		this.varKey = key;
		this.varValue = value;
		this.description = desc;
	}

	/**
	 * @return the key
	 */
	public String getVarKey() {
		return varKey;
	}

	/**
	 * @param key the key to set
	 */
	public void setVarKey(String key) {
		this.varKey = key;
	}

	/**
	 * @return the value
	 */
	public String getVarValue() {
		return varValue;
	}

	/**
	 * @param value the value to set
	 */
	public void setVarValue(String value) {
		this.varValue = value;
	}

	/**
	 * @return the desc
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "{key=" + varKey
			 + " value=" + varValue
			 + " desc= " + description
			 + "}";
	}
	
}
