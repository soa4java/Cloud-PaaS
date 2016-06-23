/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.primeton.paas.manage.api.exception.StorageException;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.model.Storage;
import com.primeton.paas.manage.spi.model.StorageVO;
import com.primeton.paas.manage.spi.model.WhiteList;

/**
 * 存储管理器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IStorageManager {
	
	/**
	 * 
	 * @param storage
	 * @throws StorageException
	 */
	void add(Storage storage) throws StorageException;

	/**
	 * 
	 * @param size
	 * @param whiteLists
	 * @param timeout
	 * @return
	 * @throws StorageException
	 * @throws TimeoutException
	 */
	Storage create(int size, List<String> whiteLists, long timeout)
			throws StorageException, TimeoutException;
	
	/**
	 * 
	 * @param size
	 * @param whiteLists
	 * @param storageName
	 * @param timeout
	 * @return
	 * @throws StorageException
	 * @throws TimeoutException
	 */
	Storage create(int size, List<String> whiteLists, String storageName,
			long timeout) throws StorageException, TimeoutException;
	
	/**
	 * 
	 * @param size
	 * @param whiteLists
	 * @return
	 * @throws StorageException
	 */
	Storage apply(int size, List<String> whiteLists) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @throws StorageException
	 */
	void release(String id) throws StorageException;

	/**
	 * 
	 * @param id
	 * @throws StorageException
	 */
	void destroy(String id) throws StorageException;
	
	/**
	 * 
	 * @param size
	 * @param num
	 * @throws StorageException
	 */
	void destroy(int size,int num) throws StorageException;
	
	/**
	 * 
	 * @param storage
	 * @throws StorageException
	 */
	void update(Storage storage) throws StorageException;
	
	/**
	 * 
	 * @param storageId
	 * @param size
	 * @return
	 */
	public boolean modifyStorageSize(String storageId , int size);
	
	/**
	 * 
	 * @param ip
	 * @param id
	 * @param mountPoint
	 * @throws StorageException
	 */
	void mount(String ip, String id, String mountPoint) throws StorageException;
	
	/**
	 * 
	 * @param ip
	 * @param id
	 * @throws StorageException
	 */
	void unmount(String ip, String id) throws StorageException;
	
	/**
	 * 
	 * @param ip
	 * @throws StorageException
	 */
	void unmount(String ip) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @param ip
	 * @return
	 * @throws StorageException
	 */
	boolean addWhiteList(String id, String ip) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @param hosts
	 * @throws StorageException
	 */
	void addWhiteLists(String id, String[] hosts) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @param ip
	 * @return
	 * @throws StorageException
	 */
	boolean removeWhiteList(String id, String ip) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws StorageException
	 */
	boolean removeWhiteLists(String id) throws StorageException;
	
	/**
	 * 
	 * @param ip
	 * @return
	 * @throws StorageException
	 */
	boolean removeWhiteListsByIp(String ip) throws StorageException;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Storage get(String id);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	Storage getByName(String name);
	
	/**
	 * 
	 * @param criteria
	 * @param pageCond
	 * @return
	 */
	List<Storage> getAll(Map<String, Object> criteria,IPageCond pageCond);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	List<Storage> getAll(IPageCond pageCond);
	
	/**
	 * 
	 * @return
	 */
	List<Storage> getAll();
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	List<Storage> getByHost(String ip);
	
	/**
	 * 
	 * @param size
	 * @return
	 */
	List<Storage> getBySize(int size);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<String> getIpsById(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<String> getWhiteLists(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<WhiteList> getWLists(String id);
	
	/**
	 * 
	 * @param id
	 * @param ip
	 * @return
	 */
	boolean enableMount(String id, String ip);
	
	/**
	 * 
	 * @param id
	 * @param ip
	 * @return
	 */
	String getMountPoint(String id, String ip);
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	List<String> getMountPoints(String ip);
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	List<StorageVO> getStoragesByIp(String ip);
	
}
