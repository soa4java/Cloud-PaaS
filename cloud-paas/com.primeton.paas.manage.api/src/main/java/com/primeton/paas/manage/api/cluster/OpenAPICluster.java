package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.OpenAPIService;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class OpenAPICluster extends AbstractCluster {

	private static final long serialVersionUID = -2932905542480233210L;

	public static final String TYPE = OpenAPIService.TYPE;
	
	public OpenAPICluster() {
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
}
