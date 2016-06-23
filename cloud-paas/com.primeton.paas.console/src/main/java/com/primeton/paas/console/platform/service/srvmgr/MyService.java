/**
 * 
 */
package com.primeton.paas.console.platform.service.srvmgr;

import java.io.Serializable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MyService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7441074357711604602L;

	private String type;

	public MyService() {
	}

	public MyService(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return "MyService [type=" + type + "]";
	}
	
}
