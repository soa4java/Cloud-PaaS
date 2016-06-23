/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.CollectorService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class CollectorCluster extends AbstractCluster {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -4364449693474339539L;

	public static final String TYPE = CollectorService.TYPE;
	
	/**
	 * Default. <br>
	 */
	public CollectorCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}
	
}
