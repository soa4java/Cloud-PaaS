/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.NginxService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class NginxCluster extends AbstractCluster {

	private static final long serialVersionUID = 6383656097866299028L;

	public static final String TYPE = NginxService.TYPE;
	
	public static final String DEFAULT_CLUSTER_NAME = NginxService.TYPE + "-default";
	
	/**
	 * Default. <br>
	 */
	public NginxCluster() {
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}
	
}
