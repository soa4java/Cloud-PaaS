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
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ResourceServiceInvoke {
	
	private static final String NAMESPACE_RESOURCE = "res";
	
	private static final String OPT_CREATE_VM = "createVM";
	private static final String OPT_QUERY_VMJOB = "queryVMJob";
	private static final String OPT_DESTROY_VM = "destroyVM";
	private static final String OPT_QUERY_VMDETAIL = "queryVMDetail";
	private static final String OPT_MODIFY_VMGROUP = "modifyVMGroup";
	private static final String OPT_MODIFY_VMPROFILE = "modifyVMProfile";
	private static final String OPT_UPGRADE_VM = "upgradeVM";
	
	private static final String OPT_CREATE_STORAGENASVOLUME = "createStorageNasVolume";
	private static final String OPT_DESTROY_STORAGENASVOLUME = "destoryStorageNasVolume";
	private static final String OPT_MODIFY_STORAGENASVOLUMEGROUP = "modifyStorageNasVolumeGroup";
	private static final String OPT_MODIFY_STORAGENASVOLUMESIZE = "modifyStorageNasVolumeSize";
	private static final String OPT_MODIFY_STORAGENASVOLUMEWHITELIST = "modifyStorageNasVolumeWhiteList";
	private static final String OPT_QUERY_STORAGENASVOLUMEDETAIL = "queryStorageNasVolumeDetail";
	private static final String OPT_QUERY_STORAGENASVOLUMEJOB = "queryStorageNasVolumeJob";
	
	public static final String NAMESPACE_URL = "http://www.primeton.com/ResourceService";

	private ServiceClient client = null;
	private String wsUrl;
	private String binding = SOAPBinding.SOAP11HTTP_BINDING;
	private QName service = null;
	private QName port = null;
	private MessageFactory messageFactory;
	
	private static Logger logger = Logger.getLogger(ResourceServiceInvoke.class);
	
	/**
	 * 
	 */
	public ResourceServiceInvoke(String wsUrl) {
		super();
		this.client = ServiceClientFactory.createServiceClient();
		this.wsUrl = wsUrl;
		this.service = new QName(NAMESPACE_URL, "ResourceServiceService"); //$NON-NLS-1$ 
		this.port = new QName(NAMESPACE_URL, "ResourceServicePort"); //$NON-NLS-1$ 
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
	 * 
	 * @param username
	 * @param password
	 * @param bizZoneID
	 * @param groupName
	 * @param imageID
	 * @param profileID
	 * @param number
	 * @return
	 */
	public Object createVM(String username, String password, String bizZoneID, String groupName, String imageID, String profileID, int number) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_CREATE_VM, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(bizZoneID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(groupName); //$NON-NLS-1$ 
			op.addChildElement("in2").addTextNode(imageID); //$NON-NLS-1$ 
			op.addChildElement("in3").addTextNode(profileID); //$NON-NLS-1$ 
			op.addChildElement("in4").addTextNode(String.valueOf(number));
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_CREATE_VM, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param jobID
	 * @return
	 */
	public Object queryVMJob(String username, String password, String jobID) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_VMJOB, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(jobID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_VMJOB, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param vmID
	 * @return
	 */
	public Object destroyVM(String username, String password, String vmID) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_DESTROY_VM, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(vmID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_DESTROY_VM, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param vmID
	 * @return
	 */
	public Object queryVMDetail(String username, String password, String vmID) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_VMDETAIL, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(vmID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_VMDETAIL, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param vmID
	 * @param groupName
	 * @return
	 */
	public Object modifyVMGroup(String username, String password, String vmID, String groupName) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_MODIFY_VMGROUP, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(vmID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(groupName); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_VMGROUP, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param vmID
	 * @param profileID
	 * @return
	 */
	public Object modifyVMProfile(String username, String password, String vmID, String profileID) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_MODIFY_VMPROFILE, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(vmID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(profileID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_VMPROFILE, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param vmID
	 * @param profileID
	 * @return
	 */
	public Object upgradeVM(String username, String password, String vmID, String profileID) {
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_UPGRADE_VM, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(vmID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(profileID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_VMPROFILE, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param nasZoneID
	 * @param volumeName
	 * @param groupName
	 * @param size
	 * @param whiteList
	 * @return
	 */
	public Object createStorageNasVolume(String username,String password,String nasZoneID,String volumeName,String groupName,int size,List<String> whiteList){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_CREATE_STORAGENASVOLUME, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(nasZoneID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(volumeName); //$NON-NLS-1$ 
			op.addChildElement("in2").addTextNode(groupName); //$NON-NLS-1$ 
			op.addChildElement("in3").addTextNode(String.valueOf(size)); //$NON-NLS-1$ 
			SOAPElement in4 = op.addChildElement("in4"); //$NON-NLS-1$ 
			if(whiteList!=null&&!whiteList.isEmpty()){
				for(String whiteIP:whiteList){
					in4.addChildElement("string").addTextNode(whiteIP); //$NON-NLS-1$ 
				}
			}
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_CREATE_STORAGENASVOLUME, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * @param username
	 * @param password
	 * @param volumeID NAS�洢Ŀ¼ID
	 * @return
	 */
	public Object destoryStorageNasVolume(String username,String password,String volumeID){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_DESTROY_STORAGENASVOLUME, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(volumeID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_DESTROY_STORAGENASVOLUME, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param groupName
	 * @return
	 */
	public Object modifyStorageNasVolumeGroup(String username,String password,String volumeID,String groupName){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_MODIFY_STORAGENASVOLUMEGROUP, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(volumeID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(groupName); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_STORAGENASVOLUMEGROUP, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param size
	 * @return
	 */
	public Object modifyStorageNasVolumeSize(String username,String password,String volumeID,int size){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_MODIFY_STORAGENASVOLUMESIZE, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(volumeID); //$NON-NLS-1$ 
			op.addChildElement("in1").addTextNode(String.valueOf(size)); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_STORAGENASVOLUMESIZE, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param volumeID
	 * @param whiteList
	 * @return
	 */
	public Object modifyStorageNasVolumeWhiteList(String username,String password,String volumeID,List<String> whiteList){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_MODIFY_STORAGENASVOLUMEWHITELIST, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(volumeID); //$NON-NLS-1$ 
			SOAPElement in4 = op.addChildElement("in1"); //$NON-NLS-1$ 
			if(whiteList!=null&&!whiteList.isEmpty()){
				for(String whiteIP:whiteList){
					in4.addChildElement("string").addTextNode(whiteIP); //$NON-NLS-1$ 
				}
			}
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_MODIFY_STORAGENASVOLUMEWHITELIST, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param volumeID
	 * @return
	 */
	public Object queryStorageNasVolumeDetail(String username,String password,String volumeID){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_STORAGENASVOLUMEDETAIL, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(volumeID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_STORAGENASVOLUMEDETAIL, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param jobID
	 * @return
	 */
	public Object queryStorageNasVolumeJob(String username,String password,String jobID){
		SOAPMessage request = createNewMessage(username, password);
		Object result = null;
		try {
			SOAPBody body = request.getSOAPPart().getEnvelope().getBody();
			SOAPElement op = body.addChildElement(OPT_QUERY_STORAGENASVOLUMEJOB, NAMESPACE_RESOURCE);
			op.addChildElement("in0").addTextNode(jobID); //$NON-NLS-1$ 
			request.saveChanges();
			result = client.invokeService(wsUrl, binding, service, port, OPT_QUERY_STORAGENASVOLUMEJOB, request);
		} catch (SOAPException e) {
			logger.error(e);
		}
		return result;
	}
	
}
