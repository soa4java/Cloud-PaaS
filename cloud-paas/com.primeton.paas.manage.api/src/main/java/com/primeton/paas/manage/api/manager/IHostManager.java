/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import java.util.List;

import com.primeton.paas.manage.api.exception.HostException;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IPageCond;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IHostManager {
	
	/**
	 * 
	 * @param id
	 * @param number
	 * @return
	 * @throws HostException
	 */
	List<Host> apply(String id, int number) throws HostException;
	
	/**
	 * 
	 * @param ip
	 * @throws HostException
	 */
	public void release(String ip) throws HostException;
	
	/**
	 * 
	 * @param ips
	 * @throws HostException
	 */
	void release(String[] ips) throws HostException;
	
	/**
	 * 
	 * @param ip
	 * @throws HostException
	 */
	void delete(String ip) throws HostException;
	
	/**
	 * 
	 * @param host
	 * @throws HostException
	 */
	void add(Host host) throws HostException;
	
	/**
	 * 
	 * @param host
	 * @throws HostException
	 */
	void update(Host host) throws HostException;
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	Host get(String ip);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	Host[] getByType(String type);
	
	/**
	 * 
	 * @param type
	 * @param pageCond
	 * @return
	 */
	Host[] getByType(String type, IPageCond pageCond);
	
	/**
	 * 
	 * @param type
	 * @param packageId
	 * @return
	 */
	Host[] getByType(String type, String packageId);
	
	/**
	 * 
	 * @param packageId
	 * @return
	 */
	Host[] getByPackage(String packageId);
	
	/**
	 * 
	 * @return
	 */
	Host[] getAll();
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	Host[] getAll(IPageCond pageCond);
	
}