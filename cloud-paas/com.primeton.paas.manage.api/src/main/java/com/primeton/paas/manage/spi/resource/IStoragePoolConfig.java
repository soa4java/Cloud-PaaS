/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.model.StoragePoolConfig;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IStoragePoolConfig {

	/**
	 * 
	 * @param config
	 * @return
	 */
	boolean add(StoragePoolConfig config);
	
	/**
	 * 
	 * @param id
	 */
	void delete(String id);
	
	/**
	 * 
	 * @param config
	 * @return
	 */
	boolean update(StoragePoolConfig config);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	StoragePoolConfig get(String id);
	
	/**
	 * 
	 * @param criteria
	 * @param pageCond
	 * @return
	 */
	List<StoragePoolConfig> getAll(Map<String,Object> criteria,IPageCond pageCond);
	
	/**
	 * 
	 * @return
	 */
	List<StoragePoolConfig> getAllEnabled();
	
	/**
	 * 
	 * @return
	 */
	List<StoragePoolConfig> getAllDisabled();
	
}
