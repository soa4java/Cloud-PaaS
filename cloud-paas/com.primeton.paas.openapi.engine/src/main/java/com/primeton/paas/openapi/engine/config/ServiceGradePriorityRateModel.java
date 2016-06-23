/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

import com.primeton.paas.openapi.admin.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceGradePriorityRateModel implements IConfigModel {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2168976411475370089L;
	
	private int priorityRate=-1;

	public int getPriorityRate() {
		return priorityRate;
	}

	public void setPriorityRate(int priorityRate) {
		this.priorityRate = priorityRate;
	}
	
}
