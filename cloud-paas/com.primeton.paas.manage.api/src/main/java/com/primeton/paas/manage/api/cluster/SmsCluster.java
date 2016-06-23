/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.SmsService;

/**
 * Marked @Deprecated by ZhongWen.Li
 * 
 * @author liyanping(liyp@primeton.com)
 */
@Deprecated
public class SmsCluster extends AbstractCluster {

	private static final long serialVersionUID = 7068725348164804049L;
	
	public static final String TYPE = SmsService.TYPE;
	
	public SmsCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
	
}