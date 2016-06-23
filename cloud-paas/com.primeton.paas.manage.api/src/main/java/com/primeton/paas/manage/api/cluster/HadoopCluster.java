/**
 *
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.HadoopService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HadoopCluster extends AbstractCluster {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8233595561450819530L;

	public static final String TYPE = HadoopService.TYPE;

	/**
	 * Default. <br>
	 */
	public HadoopCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}
	
}
