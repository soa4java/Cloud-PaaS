/**
 * 
 */
package com.primeton.upcloud.ws.api.impl;

import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import com.primeton.upcloud.ws.api.DBBackUp;
import com.primeton.upcloud.ws.api.DBBackUpJobResult;
import com.primeton.upcloud.ws.api.DBJobResult;
import com.primeton.upcloud.ws.api.DBServer;
import com.primeton.upcloud.ws.api.JobResult;
import com.primeton.upcloud.ws.api.ResourceManager;
import com.primeton.upcloud.ws.api.StorageNasJobResult;
import com.primeton.upcloud.ws.api.StorageNasVolume;
import com.primeton.upcloud.ws.api.VServer;
import com.primeton.upcloud.ws.api.VmJobResult;
import com.primeton.upcloud.ws.invoke.DBaaSManageServiceInvoke;
import com.primeton.upcloud.ws.invoke.ResourceServiceInvoke;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultResourceManager implements ResourceManager {
	
	private static Logger logger = Logger.getLogger(DefaultResourceManager.class);
	
	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#createVM(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public JobResult createVM(String wsUrl, String username, String password,
			String bizZoneID, String groupName, String imageID,
			String profileID, int number) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.createVM(username, password, bizZoneID, groupName, imageID, profileID, number);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#queryVMJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public VmJobResult queryVMJob(String wsUrl, String username,
			String password, String jobID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryVMJob(username, password, jobID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:message").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:jobStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			VmJobResult rs = new VmJobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			if (VmJobResult.STATUS_SUCCESS.equals(status)) {
				NodeList nodeList = out.getElementsByTagName("ns2:vmList").item(0).getChildNodes(); //$NON-NLS-1$
				for (int i = 0; i < nodeList.getLength(); i++) {
					NodeList attrList = nodeList.item(i).getChildNodes();
					VServer server = new VServer();
					for (int j = 0; j < attrList.getLength(); j++) {
						String nodeName = attrList.item(j).getNodeName();
						nodeName = nodeName.substring(nodeName.indexOf("ns3:") + 4); //$NON-NLS-1$
						String value = attrList.item(j).getTextContent();
						server.getAttributes().put(nodeName, value);
						System.out.println("\t" + nodeName + ": " + value);
					}
					rs.addServer(server);
				}
			}
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#destroyVM(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult destroyVM(String wsUrl, String username, String password,
			String vmID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.destroyVM(username, password, vmID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#queryVMDetail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public VServer queryVMDetail(String wsUrl, String username,
			String password, String vmID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryVMDetail(username, password, vmID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			//	String id = out.getElementsByTagName("ns2:pkVserver").item(0).getTextContent();
			String name = out.getElementsByTagName("ns2:name").item(0).getTextContent(); //$NON-NLS-1$
			String ip = out.getElementsByTagName("ns2:privateNetAddress").item(0).getTextContent(); //$NON-NLS-1$
			VServer server = new VServer();
			server.setId(vmID);
			server.setIp(ip);
			server.setName(name);
			return server;
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#modifyVMGroup(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean modifyVMGroup(String wsUrl, String username,
			String password, String vmID, String groupName) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.modifyVMGroup(username, password, vmID, groupName);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			String success = out.getTextContent();
			return Boolean.parseBoolean(success);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#modifyVMProfile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean modifyVMProfile(String wsUrl, String username,
			String password, String vmID, String profileID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.modifyVMProfile(username, password, vmID, profileID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			
			return status != null && JobResult.STATUS_OK.equals(status);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#upgradeVM(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult upgradeVM(String wsUrl, String username,
			String password, String vmID, String profileID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.upgradeVM(username, password, vmID, profileID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#createStorageNasVolume(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.List)
	 */
	public JobResult createStorageNasVolume(String wsUrl, String username, String password, String nasZoneID, String volumeName, String groupName, int size, List<String> whiteList) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.createStorageNasVolume(username, password, nasZoneID, volumeName, groupName, size, whiteList);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
		} catch (SOAPException e) {
			logger.error(e);
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#destoryStorageNasVolume(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult destoryStorageNasVolume(String wsUrl, String username, String password, String volumeID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.destoryStorageNasVolume(username, password, volumeID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#modifyStorageNasVolumeGroup(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean modifyStorageNasVolumeGroup(String wsUrl, String username, String password, String volumeID, String groupName) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.modifyStorageNasVolumeGroup(username, password, volumeID, groupName);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			String success = out.getTextContent();
			return Boolean.parseBoolean(success);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#modifyStorageNasVolumeSize(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public boolean modifyStorageNasVolumeSize(String wsUrl, String username, String password, String volumeID, int size) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.modifyStorageNasVolumeSize(username, password, volumeID, size);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String success = out.getTextContent();
			return Boolean.parseBoolean(success);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#modifyStorageNasVolumeWhiteList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public boolean modifyStorageNasVolumeWhiteList(String wsUrl, String username, String password, String volumeID, List<String> whiteList) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.modifyStorageNasVolumeWhiteList(username, password, volumeID, whiteList);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			String success = out.getTextContent();
			return Boolean.parseBoolean(success);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#queryStorageNasVolumeDetail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public StorageNasVolume queryStorageNasVolumeDetail(String wsUrl, String username, String password, String volumeID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryStorageNasVolumeDetail(username, password, volumeID);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String volumeId = out.getElementsByTagName("ns2:pkStorageNasVolume").item(0).getTextContent(); //$NON-NLS-1$
			String volumeName = out.getElementsByTagName("ns2:volumeName").item(0).getTextContent(); //$NON-NLS-1$
			String volumePath = out.getElementsByTagName("ns2:volumePath").item(0).getTextContent(); //$NON-NLS-1$
			String volumeSize = out.getElementsByTagName("ns2:volumeSize").item(0).getTextContent(); //$NON-NLS-1$
			String volumeWhiteList = out.getElementsByTagName("ns2:volumeWhiteList").item(0).getTextContent(); //$NON-NLS-1$
			
			StorageNasVolume snv = new StorageNasVolume();
			snv.setVolumeID(volumeId);
			snv.setVolumeName(volumeName);
			snv.setVolumePath(volumePath);
			snv.setVolumeSize(volumeSize);
			snv.setVolumeWhiteList(volumeWhiteList);
			return snv;
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#queryStorageNasVolumeJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public StorageNasJobResult queryStorageNasVolumeJob(String wsUrl, String username, String password, String jobID) {
		ResourceServiceInvoke invoke = new ResourceServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryStorageNasVolumeJob(username, password, jobID);
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:message").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:jobStatus").item(0).getTextContent(); //$NON-NLS-1$
			StorageNasJobResult sr = new StorageNasJobResult();
			sr.setJobID(id);
			sr.setJobStatus(status);
			sr.setMessage(message);
			if(StorageNasJobResult.STATUS_SUCCESS.equals(status)){
				NodeList attrList = out.getElementsByTagName("ns2:nasVolume").item(0).getChildNodes(); //$NON-NLS-1$
				StorageNasVolume snv = new StorageNasVolume();
				for(int i=0;i<attrList.getLength();i++){
					String nodeName = attrList.item(i).getNodeName();
					nodeName = nodeName.substring(nodeName.indexOf("ns3:")+4); //$NON-NLS-1$
					String value = attrList.item(i).getTextContent();
					snv.getAttributes().put(nodeName, value);
				}
				sr.setNasVolume(snv);
			}
			return sr;
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#createDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean, java.lang.String, int)
	 */
	public JobResult  createDBInstance(String wsUrl, String username,String password,String instName,String groupName,String perfId,String dbVersion,String charset,String bizZoneId,boolean haEnable,String haConstruct,boolean hasStandyBy,String dbUser,String dbPwd,String apUser,String apPwd,List<String> whiteList,boolean backupEnable,String backupCycle,int backupDays) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.createDBInstance(username,password,instName,groupName,perfId,dbVersion,charset,bizZoneId,haEnable,haConstruct,hasStandyBy,dbUser,dbPwd,apUser,apPwd,whiteList,backupEnable,backupCycle,backupDays);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			String jobID = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(jobID);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#queryVMJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DBJobResult queryDBJob(String wsUrl, String username,
			String password, String jobID) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryDBInstanceJob(username, password, jobID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:message").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:jobStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			DBJobResult rs = new DBJobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			if (VmJobResult.STATUS_SUCCESS.equals(status)) {
				NodeList nodeList = out.getElementsByTagName("ns2:dbInstanceList").item(0).getChildNodes(); //$NON-NLS-1$
				for (int i = 0; i < nodeList.getLength(); i++) {
					NodeList attrList = nodeList.item(i).getChildNodes();
					DBServer server = new DBServer();
					for (int j = 0; j < attrList.getLength(); j++) {
						String nodeName = attrList.item(j).getNodeName();
						nodeName = nodeName.substring(nodeName.indexOf("ns3:") + 4); //$NON-NLS-1$
						String value = attrList.item(j).getTextContent();
						server.getAttributes().put(nodeName, value);
						System.out.println("\t" + nodeName + ": " + value);
					}
					rs.addServer(server);
				}
			}
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see com.primeton.cloud.vm.ws.api.ResourceManager#queryVMJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DBServer queryDBInstance(String wsUrl,String userName, String password, String instId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryDBInstance(userName, password, instId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			NodeList attrList = out.getChildNodes();
			DBServer server = new DBServer();
			for (int j = 0; j < attrList.getLength(); j++) {
				String nodeName = attrList.item(j).getNodeName();
				nodeName = nodeName.substring(nodeName.indexOf("ns2:") + 4); //$NON-NLS-1$
				String value = attrList.item(j).getTextContent();
				server.getAttributes().put(nodeName, value);
				System.out.println("\t" + nodeName + ": " + value);
			}
			return server;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#destroyDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult destroyDBInstance(String wsUrl, String userName, String password, String instId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.destroyDBInstance(userName, password, instId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#startDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult startDBInstance(String wsUrl, String userName, String password, String instId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.startDBInstance(userName, password, instId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#stopDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult stopDBInstance(String wsUrl, String userName, String password, String instId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.stopDBInstance(userName, password, instId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#backupDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public JobResult backupDBInstance(String wsUrl, String userName,String password, String instId, String backupAlias,	int backupDays) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.backupDBInstance(userName, password, instId, backupAlias, backupDays);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#queryDBInstanceBackupJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DBBackUpJobResult queryDBInstanceBackupJob(String wsUrl,	String userName, String password, String jobID) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryDBInstanceBackupJob(userName, password, jobID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:jobStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			DBBackUpJobResult rs = new DBBackUpJobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			
			if (DBBackUpJobResult.STATUS_SUCCESS.equals(status)) {
					NodeList attrList = out.getElementsByTagName("ns2:dbBackup").item(0).getChildNodes();
					DBBackUp dbBkUp = new DBBackUp();
					for(int i=0;i<attrList.getLength();i++){
						String nodeName = attrList.item(i).getNodeName();
						nodeName = nodeName.substring(nodeName.indexOf("ns3:")+4); //$NON-NLS-1$
						String value = attrList.item(i).getTextContent();
						dbBkUp.getAttributes().put(nodeName, value);
						rs.setDbBackup(dbBkUp);
					}
			}
			System.out.println("\t" + rs);
			return rs;
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#queryDBBackup(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public DBBackUp queryDBBackup(String wsUrl, String userName, String password, int backupUniqueId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryDBBackup(userName, password, backupUniqueId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			DBBackUp dbBackUp = new DBBackUp();
			
			NodeList attrList = out.getChildNodes();
			for (int j = 0; j < attrList.getLength(); j++) {
				String nodeName = attrList.item(j).getNodeName();
				nodeName = nodeName.substring(nodeName.indexOf("ns2:") + 4); //$NON-NLS-1$
				String value = attrList.item(j).getTextContent();
				dbBackUp.getAttributes().put(nodeName, value);
			}
			
			return dbBackUp;
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#restoreDBInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public JobResult restoreDBInstance(String wsUrl, String userName, String password, String instId, int backupUniqueId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.restoreDBInstance(userName, password, instId, backupUniqueId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#queryDBInstanceRestoreJob(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DBJobResult queryDBInstanceRestoreJob(String wsUrl, String userName,	String password, String jobID) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.queryDBInstanceRestoreJob(userName, password, jobID);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:message").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:jobStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			DBJobResult rs = new DBJobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			if (DBBackUpJobResult.STATUS_SUCCESS.equals(status)) {
				NodeList nodeList = out.getElementsByTagName("ns2:dbInstanceList").item(0).getChildNodes(); //$NON-NLS-1$
				for (int i = 0; i < nodeList.getLength(); i++) {
					NodeList attrList = nodeList.item(i).getChildNodes();
					DBServer server = new DBServer();
					for (int j = 0; j < attrList.getLength(); j++) {
						String nodeName = attrList.item(j).getNodeName();
						nodeName = nodeName.substring(nodeName.indexOf("ns3:") + 4); //$NON-NLS-1$
						String value = attrList.item(j).getTextContent();
						server.getAttributes().put(nodeName, value);
					}
					rs.addServer(server);
				}
			}
			System.out.println(rs);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#enlargePerformance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JobResult enlargePerformance(String wsUrl, String userName, String password, String instId, String perfId) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.enlargePerformance(userName, password, instId, perfId);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#enlargeStorage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public JobResult enlargeStorage(String wsUrl, String userName, String password, String instId, String storageType, int size) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.enlargeStorage(userName, password, instId, storageType, size);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return null;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			
			String id = out.getElementsByTagName("ns2:jobID").item(0).getTextContent(); //$NON-NLS-1$
			String message = out.getElementsByTagName("ns2:responseMessage").item(0).getTextContent(); //$NON-NLS-1$
			String status = out.getElementsByTagName("ns2:responseStatus").item(0).getTextContent(); //$NON-NLS-1$
			
			JobResult rs = new JobResult();
			rs.setId(id);
			rs.setMessage(message);
			rs.setStatus(status);
			return rs;
			
		} catch (SOAPException e) {
			logger.error(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.upcloud.ws.api.ResourceManager#updateWhiteList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public boolean updateWhiteList(String wsUrl, String userName, String password, String instId, List<String> whiteList) {
		DBaaSManageServiceInvoke invoke = new DBaaSManageServiceInvoke(wsUrl); // http://192.168.1.78/default/ResourceService
		Object result = invoke.updateWhiteList(userName, password, instId, whiteList);
		
		if (result instanceof SOAPFault) {
			SOAPFault soapFault = (SOAPFault) result;
			logger.error("Error occurred when call web service. Error message:" + SOAPFaultParserUtil.parseSOAPFualt(soapFault));
			return false;
		}
		
		try {
			SOAPMessage soapMessage = (SOAPMessage) result;
			SOAPElement response = (SOAPElement) soapMessage.getSOAPBody().getChildElements().next();
			SOAPElement out = (SOAPElement)response.getChildElements().next();
			String success = out.getTextContent();
			return Boolean.parseBoolean(success);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return false;
	}

}
