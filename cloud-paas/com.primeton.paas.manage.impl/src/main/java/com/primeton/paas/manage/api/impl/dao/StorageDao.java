/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.List;
import java.util.Map;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.model.Storage;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface StorageDao {

	/**
	 * 
	 * @param storage
	 * @return
	 */
	boolean insert(Storage storage);
	
	/**
	 * 
	 * @param storage
	 */
	void update(Storage storage);
	
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
	Storage get(String id);
	
	/**
	 * 
	 * @return
	 */
	List<Storage> getAll();
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	List<Storage> getAll(Map<String, Object> criteria);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	List<Storage> getAll(IPageCond pageCond);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	List<Storage> getAll(Map<String, Object> criteria,IPageCond pageCond);
	
}
