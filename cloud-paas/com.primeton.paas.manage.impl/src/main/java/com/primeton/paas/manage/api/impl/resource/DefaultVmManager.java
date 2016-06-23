/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.exception.VmException;
import com.primeton.paas.manage.spi.resource.IVmManager;
import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.ResourceManagerFactory;
import com.primeton.upcloud.ws.api.VServer;
import com.primeton.upcloud.ws.api.VmJobResult;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultVmManager implements IVmManager {
	
	private ResourceManager manager;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultVmManager.class);
	
	public DefaultVmManager() {
		super();
		manager = ResourceManagerFactory.getManager();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmManager#create(java.lang.String, java.lang.String, int, long)
	 */
	public List<Host> create(String imageId, String profileId, int number,
			long timeout) throws VmException, TimeoutException {
		List<Host> hosts = new ArrayList<Host>();
		if (StringUtil.isEmpty(imageId) || StringUtil.isEmpty(profileId)
				|| number < 1) {
			return hosts;
		}
		JobResult rs = manager.createVM(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), 
				WebServiceUtil.getPassword(), WebServiceUtil.getBizZoneID(), 
				WebServiceUtil.getDefaultGroup(), imageId, profileId, number);
		if (rs == null || !JobResult.STATUS_OK.equals(rs.getStatus())) {
			String message = (rs != null) ? rs.getMessage() : "Create vm [imageId:" + imageId + ", profileId:"
					+ profileId + ", number:" + number + ", timeout:" + timeout + "] error.";
			throw new VmException(message);
		}
		String jobID = rs.getId();
		if (logger.isInfoEnabled()) {
			logger.info("Submit create vm [imageId:" + imageId + ", profileId:"
					+ profileId + ", number:" + number + ", timeout:" + timeout + "] request to iaas success, return jobID is " + jobID + ".");
		}
		
		long begin = System.currentTimeMillis();
		long end = begin;
		timeout = timeout > 0 ? timeout : 300000L;
		timeout = timeout * number;
		VmJobResult vrs = null;
		boolean isTimeout= false;
		while (true) {
			end  = System.currentTimeMillis();
			if (end - begin > timeout) {
				isTimeout = true;
				break;
			}
			vrs = manager.queryVMJob(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), WebServiceUtil.getPassword(), jobID);
			logger.info("VmJobResult:" + vrs);
			if (vrs != null && (VmJobResult.STATUS_SUCCESS.equals(vrs.getStatus())
					|| VmJobResult.STATUS_FAIL.equals(vrs.getStatus()))) {
				break;
			}
			
			ThreadUtil.sleep(10000L);
		}
		
		if (isTimeout) {
			throw new TimeoutException("Create vm [imageId:" + imageId + ", profileId:"
					+ profileId + ", number:" + number + ", timeout:" + timeout + "] timeout.");
		}
		
		if (vrs == null || !VmJobResult.STATUS_SUCCESS.equals(vrs.getStatus())) {
			throw new VmException("Create vm [imageId:" + imageId + ", profileId:"
					+ profileId + ", number:" + number + ", timeout:" + timeout + "] error.");
		}
		
		List<VServer> servers = vrs.getServers();
		if (servers != null) {
			for (VServer server : servers) {
				Host host = new Host();
				host.setAssigned(false);
				host.setId(server.getId());
				host.setIp(server.getIp());
				host.setName(server.getName());
				host.setStandalone(false);
				hosts.add(host);
			}
		}
		
		return hosts;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmManager#destroy(java.lang.String[], long)
	 */
	public void destroy(String[] hosts, long timeout) throws VmException,
			TimeoutException {
		if (hosts == null || hosts.length == 0) {
			return;
		}
		for (String vmID : hosts) {
			JobResult rs = manager.destroyVM(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), 
					WebServiceUtil.getPassword(), vmID);
			if (rs != null && JobResult.STATUS_OK.equals(rs.getStatus())) {
				logger.info("Destroy vm [" + vmID + "] success.");
			} else {
				logger.info("Destroy vm [" + vmID + "] error.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmManager#modifyVMGroup(java.lang.String, java.lang.String)
	 */
	public boolean modifyVMGroup(String vmID, String groupName) {
		if (StringUtil.isEmpty(vmID) || StringUtil.isEmpty(groupName)) {
			return false;
		}
		return manager.modifyVMGroup(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), 
				WebServiceUtil.getPassword(), vmID, groupName);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.spi.resource.IVmManager#modifyVMProfile(java.lang.String, java.lang.String)
	 */
	public boolean modifyVMProfile(String vmID, String profileID) {
		if (StringUtil.isEmpty(vmID) || StringUtil.isEmpty(profileID)) {
			return false;
		}
		return manager.modifyVMProfile(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), 
				WebServiceUtil.getPassword(), vmID, profileID);
	}
	
	public boolean upgradeVM(String vmID, String profileID,
			long timeout)  throws VmException, TimeoutException {
		//Host hosts = new Host();
		if (StringUtil.isEmpty(vmID) || StringUtil.isEmpty(profileID)) {
			return false;
		}
		JobResult rs = manager.upgradeVM(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), 
				WebServiceUtil.getPassword(), vmID, profileID);
		if (rs == null || !JobResult.STATUS_OK.equals(rs.getStatus())) {
			String message = (rs != null) ? rs.getMessage() : "Upgrade vm [vmID:" + vmID + ", profileId:"
					+ profileID +  ", timeout:" + timeout + "] error.";
			throw new VmException(message);
		}
		String jobID = rs.getId();
		if (logger.isInfoEnabled()) {
			logger.info("Submit upgrade vm [vmID:" + vmID + ", profileId:"
					+ profileID + ", timeout:" + timeout + "] request to iaas success, return jobID is " + jobID + ".");
		}
		
		long begin = System.currentTimeMillis();
		long end = begin;
		timeout = timeout > 0 ? timeout : 300000L;
		//timeout = timeout * number;
		VmJobResult vrs = null;
		boolean isTimeout= false;
		while (true) {
			end  = System.currentTimeMillis();
			if (end - begin > timeout) {
				isTimeout = true;
				break;
			}
			vrs = manager.queryVMJob(WebServiceUtil.getWsUrl(), WebServiceUtil.getUsername(), WebServiceUtil.getPassword(), jobID);
			if (logger.isInfoEnabled()) {
				logger.info("Query upgrade vm job result is : " + vrs);
			}
			if (vrs != null && (VmJobResult.STATUS_SUCCESS.equals(vrs.getStatus())
					|| VmJobResult.STATUS_FAIL.equals(vrs.getStatus()))) {
				break;
			}
			
			ThreadUtil.sleep(10000L);
		}
		
		if (isTimeout) {
			throw new TimeoutException("Upgrade vm [vmID:" + vmID + ", profileId:"
					+ profileID + ", timeout:" + timeout + "] timeout.");
		}
		
		if (vrs == null || !VmJobResult.STATUS_SUCCESS.equals(vrs.getStatus())) {
			throw new VmException("Upgrade vm [vmID:" + vmID + ", profileId:"
					+ profileID +", timeout:" + timeout + "] error.");
		}
		return true;
	}

}
