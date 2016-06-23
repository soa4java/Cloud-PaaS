/**
 * 
 */
package com.primeton.paas.console.app.service.util;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LogModel {

	private String type;
	private String level;

	public LogModel() {
		super();
	}

	public LogModel(String type, String level) {
		super();
		this.type = type;
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String toString() {
		return "LogModel [type=" + type + ", level=" + level + "]";
	}

}
