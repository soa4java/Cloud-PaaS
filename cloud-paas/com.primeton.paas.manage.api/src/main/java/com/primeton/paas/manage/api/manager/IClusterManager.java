/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.IService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface IClusterManager {
	
	String DEFAULT_TYPE = "Default";
	
	/**
	 * Type <br>
	 * 
	 * @return Type
	 */
	String getType();

	/**
	 * 
	 * @param cluster
	 * @return
	 * @throws ClusterException
	 */
	ICluster create(ICluster cluster) throws ClusterException;
	
	/**
	 * 
	 * @param cluster
	 * @param clazz
	 * @return
	 * @throws ClusterException
	 */
	<T extends ICluster> T create(T cluster, Class<T> clazz) throws ClusterException;
	
	/**
	 * 
	 * @param id
	 * @throws ClusterException
	 */
	void destroy(String id) throws ClusterException;
	
	/**
	 * 
	 * @param cluster
	 * @throws ClusterException
	 */
	void update(ICluster cluster) throws ClusterException;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	ICluster get(String id);
	
	/**
	 * 
	 * @param id
	 * @param clazz
	 * @return
	 */
	<T extends ICluster> T get(String id, Class<T> clazz);
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	ICluster[] getByApp(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @return
	 */
	ICluster getByApp(String appName, String type);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	ICluster[] getByType(String type);
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @return
	 */
	ICluster[] getByType(String appName, String type);
	
	/**
	 * 
	 * @param type
	 * @param clazz
	 * @return
	 */
	<T extends ICluster> List<T> getByType(String type, Class<T> clazz);
	
	/**
	 * 
	 * @param type
	 * @param pageCond
	 * @return
	 */
	ICluster[] getByType(String type, IPageCond pageCond);
	
	/**
	 * 
	 * @param type
	 * @param pageCond
	 * @param clazz
	 * @return
	 */
	<T extends ICluster> List<T> getByType(String type, IPageCond pageCond, Class<T> clazz);
	
	/**
	 * 
	 * @param owner
	 * @param pageCond
	 * @return
	 */
	ICluster[] getByOwner(String owner, IPageCond pageCond);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @param pageCond
	 * @return
	 */
	ICluster[] getByOwner(String owner, String type, IPageCond pageCond);
	
	/**
	 * 
	 * @param owner
	 * @param type
	 * @param pageCond
	 * @param clazz
	 * @return
	 */
	<T extends ICluster> List<T> getByOwner(String owner, String type, IPageCond pageCond, Class<T> clazz);
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void start(String id) throws ServiceException;
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void stop(String id) throws ServiceException;
	
	/**
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	void restart(String id) throws ServiceException;
	
	/**
	 * 
	 * @return
	 */
	String[] getTypes();
	
	/**
	 * 
	 * @param upperCluster
	 * @param lowerCluster
	 * @throws ClusterException
	 */
	void bindCluster(String upperCluster, String lowerCluster) throws ClusterException;
	
	/**
	 * 
	 * @param upperCluster
	 * @param lowerCluster
	 * @throws ClusterException
	 */
	void unbindCluster(String upperCluster, String lowerCluster) throws ClusterException;
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	String getClusterId(String serviceId);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	String[] getRelationClustersId(String id);
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	String[] getRelationClustersId(String id, String type);
	
	/**
	 * 
	 * @param clusterOne
	 * @param clusterTwo
	 * @return
	 */
	boolean isExistsRelation(String clusterOne, String clusterTwo);
	
	/**
	 * 
	 * @param clusterId
	 * @param number
	 * @return
	 * @throws ServiceException
	 */
	<T extends IService> T[] increase(String clusterId, int number) throws ServiceException;
	
	/**
	 * 
	 * @param clusterId
	 * @param number
	 * @throws ServiceException
	 */
	void decrease(String clusterId, int number) throws ServiceException;
	
	
}
