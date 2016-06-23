/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.MailService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailCluster extends AbstractCluster {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = 1793591168651668852L;
	
	public static final String TYPE = MailService.TYPE;
	
	public MailCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
	
}
