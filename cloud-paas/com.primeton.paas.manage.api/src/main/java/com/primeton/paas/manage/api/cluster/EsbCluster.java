/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbCluster extends AbstractCluster {

	/**
	 * serialVersionUID. <br>
	 */
	private static final long serialVersionUID = -1489498611036484129L;

	public static final String TYPE = "Esb";

	/**
	 * Default. <br>
	 */
	public EsbCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}
	
}
