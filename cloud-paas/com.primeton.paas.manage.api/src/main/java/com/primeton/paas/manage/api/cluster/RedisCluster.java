/**
 *
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.RedisService;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RedisCluster extends AbstractCluster {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5498253348014201718L;

	public static final String TYPE = RedisService.TYPE;
	
	/**
	 * Alias name, as sentinel unique id
	 */
	private static final String ALIAS_NAME = "aliasName";

	/**
	 * Default. <br>
	 */
	public RedisCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAliasName() {
		return getValue(ALIAS_NAME);
	}
	
	/**
	 * 
	 * @param aliasName
	 */
	public void setAliasName(String aliasName) {
		setValue(ALIAS_NAME, aliasName);
	}
	
}
