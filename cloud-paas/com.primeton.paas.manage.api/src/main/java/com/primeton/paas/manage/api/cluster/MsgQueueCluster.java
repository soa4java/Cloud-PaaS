/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.MsgQueueService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgQueueCluster extends AbstractCluster {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = -1497229890791726855L;
	
	public static final String TYPE = MsgQueueService.TYPE;

	/**
	 * Default. <br>
	 */
	public MsgQueueCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}

	
	
}
