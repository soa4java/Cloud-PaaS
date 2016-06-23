/**
 * 
 */
package com.primeton.paas.manage.spi.resource;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.spi.exception.VmException;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IVmManager {
	
	/**
	 * 
	 * @param imageId
	 * @param profileId
	 * @param number
	 * @param timeout
	 * @return
	 * @throws VmException
	 * @throws TimeoutException
	 */
	List<Host> create(String imageId, String profileId, int number, long timeout) throws VmException, TimeoutException;

	/**
	 * 
	 * @param hosts
	 * @param timeout
	 * @throws VmException
	 * @throws TimeoutException
	 */
	void destroy(String[] hosts, long timeout) throws VmException, TimeoutException;
	
	/**
	 * 
	 * @param vmID
	 * @param groupName
	 * @return
	 */
	boolean modifyVMGroup(String vmID, String groupName);
	
	/**
	 * 
	 * @param vmID
	 * @param profileID
	 * @return
	 */
	boolean modifyVMProfile(String vmID, String profileID);
	
	/**
	 * 
	 * @param vmID
	 * @param profileID
	 * @param timeout
	 * @return
	 * @throws VmException
	 * @throws TimeoutException
	 */
	 boolean upgradeVM(String vmID, String profileID,
			long timeout)  throws VmException, TimeoutException ;
	
}
