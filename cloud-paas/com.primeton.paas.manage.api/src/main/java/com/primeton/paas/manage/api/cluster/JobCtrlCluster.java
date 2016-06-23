/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.JobCtrlService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JobCtrlCluster extends AbstractCluster {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = -4504942220849812183L;
	
	public static final String TYPE = JobCtrlService.TYPE;

	/**
	 * Default. <br>
	 */
	public JobCtrlCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$ 
	}

}
