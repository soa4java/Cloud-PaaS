/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.List;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.spi.exception.AddressException;
import com.primeton.paas.manage.spi.model.VIPSegment;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IVIPManager {
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	String add(VIPSegment segment);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	boolean remove(String id);
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	boolean update(VIPSegment segment);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	VIPSegment get(String id);
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	List<VIPSegment> getAll(IPageCond pageCond);
	
	/**
	 * 
	 * @return
	 * @throws AddressException
	 */
	String apply() throws AddressException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws AddressException
	 */
	String apply(String id) throws AddressException;
	
	/**
	 * 
	 * @param vip
	 * @throws AddressException
	 */
	void release(String vip) throws AddressException;
	
	/**
	 * 
	 * @return
	 */
	List<String> getUsedVIPs();
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<String> getUsedVIPs(String id);
	
	/**
	 * 
	 * @param id
	 * @param page
	 * @return
	 */
	List<String> getUsedVIPs(String id,IPageCond page);
	
}
