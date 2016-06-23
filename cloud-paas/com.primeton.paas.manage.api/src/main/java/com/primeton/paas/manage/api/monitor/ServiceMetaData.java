/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMetaData extends MetaData {
	
	public static final String CLUSTER_ID = "clusterId";
	
	/**
	 * 
	 * @return
	 */
	public String getClusterId() {
		return get(CLUSTER_ID);
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public void setClusterId(String clusterId) {
		set(CLUSTER_ID, clusterId);
	}
	
}
