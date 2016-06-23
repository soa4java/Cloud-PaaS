package com.primeton.paas.manage.api.listener;

import com.primeton.paas.manage.api.model.ICluster;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public interface ServiceEventListener {
	
	/**
	 * 
	 * @param cluster
	 */
	void doCreate(ICluster cluster);
	
	/**
	 * 
	 * @param cluster
	 */
	void doDestroy(ICluster cluster);
	
	/**
	 * 
	 * @param privious
	 * @param now
	 */
	void doModify(ICluster privious, ICluster now);
	
}
