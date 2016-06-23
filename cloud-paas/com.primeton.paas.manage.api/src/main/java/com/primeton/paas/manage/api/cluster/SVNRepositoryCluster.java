/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.SVNRepositoryService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SVNRepositoryCluster extends AbstractCluster {

	private static final long serialVersionUID = 5144459096357050966L;

	public static final String TYPE = SVNRepositoryService.TYPE;

	public SVNRepositoryCluster() {
		super();
		setType(TYPE);
	}
	
	
}
