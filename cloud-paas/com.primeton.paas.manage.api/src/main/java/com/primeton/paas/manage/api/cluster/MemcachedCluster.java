/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.MemcachedService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MemcachedCluster extends AbstractCluster {

	private static final long serialVersionUID = -1762567417200512040L;

	public static final String TYPE = MemcachedService.TYPE;
	
	private static final String MEMCACHEDSIZE = "memcachedSize";

	/**
	 * 
	 */
	public MemcachedCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}
	
	
	/**
	 * Get Memcached Size (MB) <br>
	 * 
	 * @return
	 */
	public int getMemcachedSize(){
		return getValue(MEMCACHEDSIZE, 0);
	}
	
	/**
	 * Set Memcached size (MB) <br>
	 * 
	 * @param memcachedSize
	 */
	public void setMemcachedSize(int memcachedSize) {
		setValue(MEMCACHEDSIZE, memcachedSize);
	}

}
