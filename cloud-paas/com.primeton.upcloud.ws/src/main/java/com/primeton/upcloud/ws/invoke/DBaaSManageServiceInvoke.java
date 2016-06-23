/**
 * 
 */
package com.primeton.upcloud.ws.invoke;


import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.primeton.upcloud.ws.client.DefaultServiceClient;
import com.primeton.upcloud.ws.client.ServiceClient;
import com.primeton.upcloud.ws.client.ServiceClientFactory;

/**
 * @author liming(mailto:li-ming@primeton.com)
 *
 */
public class DBaaSManageServiceInvoke {
	
	private static final String NAMESPACE_RESOURCE = "dba";
	
	private static final String OPT_CREATE_DB_INSTANCE = "createDBInstance";
	private static final String OPT_DESTROY_DB_INSTANCE = "destroyDBInstance";
	private static final String OPT_QUERY_DB_INSTANCE_JOB = "queryDBInstanceJob";
	private static final String OPT_QUERY_DB_INSTANCE = "queryDBInstance";
	private static final String OPT_START_DB_INSTANCE = "startDBInstance";
	private static final String OPT_STOP_DB_INSTANCE = "stopDBInstance";
	private static final String OPT_BACKUP_DB_INSTANCE = "backupDBInstance";
	private static final String OPT_QUERY_DB_INSTANCE_BACKUP_JOB = "queryDBInstanceBackupJob";
	private static final String OPT_QUERY_DB_BACKUP = "queryDBBackup";
	private static final String OPT_RESTORE_DB_INSTANCE = "restoreDBInstance";
	private static final String OPT_QUERY_DB_INSTANCE_RESTORE_JOB = "queryDBInstanceRestoreJob";
	private static final String OPT_ENLARGE_PERFORMANCE = "enlargePerformance";
	private static final String OPT_ENLARGE_STORAGE = "enlargeStorage";
	private static final String OPT_UPDATE_WHITELIST = "updateWhiteList";
	
	
	public static final String NAMESPACE_URL = "http://www.primeton.com/DBaaSManageService";

	private ServiceClient client = null;
	private String wsUrl;
	private String binding = SOAPBinding.SOAP11HTTP_BINDING;
	private QName service = null;
	private QName port = null;
	private MessageFactory messageFactory;
	
	private static Logger logger = Logger.getLogger(DBaaSManageServiceInvoke.class);
	
	/**
	 * 
	 */
	public DBaaSManageServiceInvoke(String wsUrl) {
		super();
		this.client = ServiceClientFactory.createServiceClient();
		this.wsUrl = wsUrl;
		this.service = new QName(NAMESPACE_URL, "DBaaSManageServiceService");
		this.port = new QName(NAMESPACE_URL, "DBaaSManageServicePort");
		try {
			this.messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
		} catch (SOAPException e) {
			logger.error(e);
		}
	}
	
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	private SOAPMessage createNewMessage(String userName, String password) {
		SOAPMessage request = null;
		try {
			request = messageFactory.createMessage();
			SOAPPart part = request.getSOAPPart();
			SOAPEnvelope env = part.getEnvelope();
			env.addNamespaceDeclaration(NAMESPACE_RESOURCE, NAMESPACE_URL);
			DefaultServiceClient.createSecurityInfo(request.getSOAPHeader(), userName, password);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return request;
	}
	
	
	/**
	 * @param instName
	 * @param groupName
	 * @param perfId
	 * @param dbVersion
	 * @param charset
	 * @param bizZoneId
	 * @param haEnable
	 * @param haConstruct
	 * @param hasStandyBy
	 * @param dbUser
	 * @param dbPwd
	 * @param apUser
	 * @param apPwd
	 * @param whiteList
	 * @param backupEnable
	 * @param backupCycle
	 * @param backupDays
	 * @return
	 */
	public Object createDBInstance(String username,String password,String instName,String groupName,String perfId,String dbVersion,String charset,String bizZoneId,boolean haEnable,String haConstruct,boolean hasStandyBy,String dbUser,String dbPwd,String apUser,String apPwd,List<String> whiteList,boolean backupEnable,String backupCycle,int backupDays){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_CREATE_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instName); //$NON-NLS-1$ 
			op.addChildElement("in1",NAMESPACE_RESOURCE).addTextNode(groupName); //$NON-NLS-1$ 
			op.addChildElement("in2",NAMESPACE_RESOURCE).addTextNode(perfId); //$NON-NLS-1$ 
			op.addChildElement("in3",NAMESPACE_RESOURCE).addTextNode(dbVersion); //$NON-NLS-1$ 
			op.addChildElement("in4",NAMESPACE_RESOURCE).addTextNode(charset); //$NON-NLS-1$ 
			op.addChildElement("in5",NAMESPACE_RESOURCE).addTextNode(bizZoneId); //$NON-NLS-1$ 
			op.addChildElement("in6",NAMESPACE_RESOURCE).addTextNode(Boolean.toString(haEnable)); //$NON-NLS-1$ 
			op.addChildElement("in7",NAMESPACE_RESOURCE).addTextNode(haConstruct); //$NON-NLS-1$ 
			op.addChildElement("in8",NAMESPACE_RESOURCE).addTextNode(Boolean.toString(hasStandyBy)); //$NON-NLS-1$ 
			op.addChildElement("in9",NAMESPACE_RESOURCE).addTextNode(dbUser); //$NON-NLS-1$ 
			op.addChildElement("in10",NAMESPACE_RESOURCE).addTextNode(dbPwd); //$NON-NLS-1$ 
			op.addChildElement("in11",NAMESPACE_RESOURCE).addTextNode(apUser); //$NON-NLS-1$ 
			op.addChildElement("in12",NAMESPACE_RESOURCE).addTextNode(apPwd); //$NON-NLS-1$ 
			SOAPElement whitelistNode = op.addChildElement("in13",NAMESPACE_RESOURCE); //$NON-NLS-1$ 
			for (String ip : whiteList) {
				whitelistNode.addChildElement("String", NAMESPACE_RESOURCE).addTextNode(ip); //$NON-NLS-1$ 
			}
			op.addChildElement("in14",NAMESPACE_RESOURCE).addTextNode(Boolean.toString(backupEnable)); //$NON-NLS-1$ 
			op.addChildElement("in15",NAMESPACE_RESOURCE).addTextNode(backupCycle); //$NON-NLS-1$ 
			op.addChildElement("in16",NAMESPACE_RESOURCE).addTextNode(Integer.toString(backupDays)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_CREATE_DB_INSTANCE, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	
	public Object destroyDBInstance(String userName, String password, String instId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_DESTROY_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_DESTROY_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	public Object startDBInstance(String userName, String password, String instId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_START_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_START_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object stopDBInstance(String userName, String password, String instId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_STOP_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_STOP_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object backupDBInstance(String userName, String password, String instId, String backupAlias, int backupDays) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_BACKUP_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			op.addChildElement("in1",NAMESPACE_RESOURCE).addTextNode(backupAlias); //$NON-NLS-1$ 
			op.addChildElement("in2",NAMESPACE_RESOURCE).addTextNode(String.valueOf(backupDays)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_BACKUP_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object restoreDBInstance(String userName, String password, String instId, int backupUniqueId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_RESTORE_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			op.addChildElement("in1",NAMESPACE_RESOURCE).addTextNode(String.valueOf(backupUniqueId)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_RESTORE_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object enlargePerformance(String userName, String password, String instId, String perfId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_ENLARGE_PERFORMANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			op.addChildElement("in1",NAMESPACE_RESOURCE).addTextNode(perfId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_ENLARGE_PERFORMANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object enlargeStorage(String userName, String password, String instId, String storageType, int size) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_ENLARGE_STORAGE, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			op.addChildElement("in1",NAMESPACE_RESOURCE).addTextNode(storageType); //$NON-NLS-1$ 
			op.addChildElement("in2",NAMESPACE_RESOURCE).addTextNode(String.valueOf(size)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_ENLARGE_STORAGE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object updateWhiteList(String userName, String password, String instId, List<String> whiteList) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_UPDATE_WHITELIST, NAMESPACE_RESOURCE);
			op.addChildElement("in0",NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			SOAPElement whitelistNode = op.addChildElement("in1",NAMESPACE_RESOURCE); //$NON-NLS-1$ 
			for (String ip : whiteList) {
				whitelistNode.addChildElement("String",NAMESPACE_RESOURCE).addTextNode(ip); //$NON-NLS-1$ 
			}
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_UPDATE_WHITELIST, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object queryDBInstance(String userName, String password, String instId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_DB_INSTANCE, NAMESPACE_RESOURCE);
			op.addChildElement("in0", NAMESPACE_RESOURCE).addTextNode(instId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_DB_INSTANCE, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object queryDBBackup(String userName, String password, int backupUniqueId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_DB_BACKUP, NAMESPACE_RESOURCE);
			op.addChildElement("in0", NAMESPACE_RESOURCE).addTextNode(String.valueOf(backupUniqueId)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_DB_BACKUP, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object queryDBInstanceJob(String userName, String password, String jobId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_DB_INSTANCE_JOB, NAMESPACE_RESOURCE);
			op.addChildElement("in0", NAMESPACE_RESOURCE).addTextNode(jobId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_DB_INSTANCE_JOB, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object queryDBInstanceBackupJob(String userName, String password, String jobId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_DB_INSTANCE_BACKUP_JOB, NAMESPACE_RESOURCE);
			op.addChildElement("in0", NAMESPACE_RESOURCE).addTextNode(jobId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_DB_INSTANCE_BACKUP_JOB, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	public Object queryDBInstanceRestoreJob(String userName, String password, String jobId) {
		SOAPMessage request = createNewMessage(userName, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_DB_INSTANCE_RESTORE_JOB, NAMESPACE_RESOURCE);
			op.addChildElement("in0", NAMESPACE_RESOURCE).addTextNode(jobId); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_DB_INSTANCE_RESTORE_JOB, request);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
}
