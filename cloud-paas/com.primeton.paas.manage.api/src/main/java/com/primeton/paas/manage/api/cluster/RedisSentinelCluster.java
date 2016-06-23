/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.RedisSentinelService;

/**
 * Redis 哨兵服务集群. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisSentinelCluster extends AbstractCluster {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = -5100780807441045893L;
	
	public static final String TYPE = RedisSentinelService.TYPE;

	/**
	 * Default. <br>
	 */
	public RedisSentinelCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}

}
