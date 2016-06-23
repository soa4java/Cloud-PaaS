/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ServiceEPSRelDao {
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	boolean add(ServiceEPSRel rel);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	boolean delete(String clusterId);
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	boolean update(ServiceEPSRel rel);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	ServiceEPSRel get(String clusterId);

}
