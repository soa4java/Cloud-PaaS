/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.IService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IServiceQuery {
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	IService get(String id);
	
	/**
	 * 
	 * @param id
	 * @param clazz
	 * @return
	 */
	<O extends IService> O get(String id, Class<O> clazz);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	IService[] getByType(String type);
	
	/**
	 * 
	 * @param type
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByType(String type, Class<O> clazz);
	
	/**
	 * 
	 * @param type
	 * @param pageCond
	 * @return
	 */
	IService[] getByType(String type, IPageCond pageCond);
	
	/**
	 * 
	 * @param type
	 * @param pageCond
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByType(String type, IPageCond pageCond, Class<O> clazz);
	
	/**
	 * 
	 * @param type
	 * @param ip
	 * @return
	 */
	IService[] getByType(String type, String ip);
	
	/**
	 * 
	 * @param type
	 * @param ip
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByType(String type, String ip, Class<O> clazz);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @return
	 */
	IService[] getByOwner(String owner, String type);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByOwner(String owner, String type, Class<O> clazz);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @param pageCond
	 * @return
	 */
	IService[] getByOwner(String owner, String type, IPageCond pageCond);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @param pageCond
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByOwner(String owner, String type, IPageCond pageCond, Class<O> clazz);
	
	/**
	 * 
	 * @param clusterId
	 * @return
	 */
	IService[] getByCluster(String clusterId);
	
	/**
	 * 
	 * @param clusterId
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByCluster(String clusterId, Class<O> clazz);
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	IService[] getByIp(String ip);
	
	/**
	 * 
	 * @param ip
	 * @param type
	 * @return
	 */
	IService[] getByIp(String ip, String type);
	
	/**
	 * 
	 * @param ip
	 * @param type
	 * @param clazz
	 * @return
	 */
	<O extends IService> List<O> getByIp(String ip, String type, Class<O> clazz);
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	IService getByIp(String ip, int port);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	IService[] getChildren(String id);
	
	/**
	 * 
	 * @return
	 */
	String[] getTypes();
	
	/**
	 * 
	 * @return
	 */
	String[] getPhysicalTypes();
	
	/**
	 * 
	 * @return
	 */
	String[] getLogicalTypes();

}
