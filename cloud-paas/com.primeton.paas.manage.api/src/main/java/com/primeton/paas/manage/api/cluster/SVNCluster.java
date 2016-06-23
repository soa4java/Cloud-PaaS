/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.SVNService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SVNCluster extends AbstractCluster {

	private static final long serialVersionUID = 7853291699473065511L;

	public static final String TYPE = SVNService.TYPE;
	
	public SVNCluster() {
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}

	
}
