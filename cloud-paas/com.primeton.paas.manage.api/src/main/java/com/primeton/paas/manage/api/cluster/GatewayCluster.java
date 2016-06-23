/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.GatewayService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GatewayCluster extends AbstractCluster {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4659480394427029459L;
	
	public static final String TYPE = GatewayService.TYPE;

	/**
	 * Default. <br>
	 */
	public GatewayCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}

}
