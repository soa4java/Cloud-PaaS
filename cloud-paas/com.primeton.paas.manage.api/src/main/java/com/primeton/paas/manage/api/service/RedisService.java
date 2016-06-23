/**
 *
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6767512771943979795L;

	public static final String TYPE = "Redis";
	
	public static final String RUN_AS_MASTER = "master";
	public static final String RUN_AS_SLAVE = "slave";
	
	private static final String RUN_MODE = "runMode";

	/**
	 * Default. <br>
	 */
	public RedisService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRunMode() {
		return getValue(RUN_MODE, RUN_AS_MASTER);
	}
	
	/**
	 * 
	 * @param runMode
	 */
	public void setRunMode(String runMode) {
		if (RUN_AS_MASTER.equalsIgnoreCase(runMode)) {
			setValue(RUN_MODE, RUN_AS_MASTER);
		} else if (RUN_AS_SLAVE.equalsIgnoreCase(runMode)) {
			setValue(RUN_MODE, RUN_AS_SLAVE);
		}
	}
	
}
