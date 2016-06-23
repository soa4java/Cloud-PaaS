/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.List;

import com.primeton.paas.manage.spi.model.WhiteList;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface WhiteListDao {
	
	/**
	 * 
	 * @param whiteList
	 * @return
	 */
	boolean insert(WhiteList whiteList);
	
	/**
	 * 
	 * @param whiteList
	 */
	void update(WhiteList whiteList);
	
	/**
	 * 
	 * @param storageId
	 * @param ip
	 */
	void delete(String storageId, String ip);
	
	/**
	 * 
	 * @param storageId
	 * @param ip
	 * @return
	 */
	WhiteList getWhiteList(String storageId, String ip);
	
	/**
	 * 
	 * @param storageId
	 * @return
	 */
	List<WhiteList> getWhiteListsByStorage(String storageId);
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	List<WhiteList> getWhiteListsByIp(String ip);
	
	/**
	 * 
	 * @return
	 */
	List<WhiteList> getAll();
	
	/**
	 * 
	 * @param ip
	 * @param mountPoint
	 * @return
	 */
	WhiteList getWhiteListByMountPoint(String mountPoint,String ip);
}
