/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.List;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.model.VmPoolConfig;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IVmPoolConfig {

	/**
	 * 
	 * @param cfg
	 * @return
	 */
	boolean add(VmPoolConfig cfg);
	
	/**
	 * 
	 * @param cfg
	 * @return
	 */
	boolean update(VmPoolConfig cfg);
	
	/**
	 * 
	 * @param id
	 */
	void delete(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	VmPoolConfig get(String id);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	List<VmPoolConfig> getAll(IPageCond pageCond);
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param pageCond
	 * @return
	 */
	List<VmPoolConfig> getAll(String id,String name,IPageCond pageCond);
	
	/**
	 * 
	 * @return
	 */
	List<VmPoolConfig> getAllEnabled();
	
	/**
	 * 
	 * @return
	 */
	List<VmPoolConfig> getAllDisabled();
	
}
