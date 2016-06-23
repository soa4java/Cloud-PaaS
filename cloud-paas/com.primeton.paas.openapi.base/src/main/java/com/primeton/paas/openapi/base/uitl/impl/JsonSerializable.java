/**
 * 
 */
package com.primeton.paas.openapi.base.uitl.impl;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JsonSerializable {

	private Object data;

	private String type;

	public JsonSerializable(Object data) {
		this.data = data;
		this.type = data.getClass().getName();
	}

	public String getType() {
		return type;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object obj) {
		this.data = obj;
	}
	
}