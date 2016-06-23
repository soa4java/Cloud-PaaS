/**
 * 
 */
package com.primeton.upcloud.ws.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultServiceClient implements ServiceClient {
	
	static {
		//$NON-NLS-1$ //$NON-NLS-2$
		System.setProperty("javax.xml.soap.MetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
	}
	
	private String encoding = "UTF-8";
	
	private boolean isChunked = false;
	
	private static Logger logger = Logger.getLogger(DefaultServiceClient.class);

	public DefaultServiceClient() {
		super();
	}

	public String getEncoding() {
		return encoding;
	}

	@SuppressWarnings("rawtypes")
	public Object[] invokeService(String wsUrl, QName opQName, String opName,
			Object[] inputs, Class[] returnTypes) {
		logger.info("Invoke webservice" + wsUrl + ", QName:" + opQName + ", opName:" + opName + ", inputs:" + inputs + ", returnTypes:" + returnTypes);
		RPCServiceClient client;
		try {
			client = new RPCServiceClient();
			Options options = client.getOptions();
			options.setTo(new EndpointReference(wsUrl));
			options.setAction(opName);
			options.setTimeOutInMilliSeconds(1000000);
			options.setExceptionToBeThrownOnSOAPFault(false);
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, isChunked());
			options.setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, getEncoding());
			client.setOptions(options);
			Object[] elements = client.invokeBlocking(opQName,inputs,returnTypes);
	        logger.info("Invoke webservice" + wsUrl + ", output: " + elements);
	        return elements;
		} catch (AxisFault e) {
			logger.error(e);
		}
		return null;
	}

	public Object invokeService(String wsUrl,String binding,QName serviceQName,
			QName portQName, String opName, SOAPMessage request) {
			Service  service = null;
			try{
				service = Service.create(new URL(wsUrl),serviceQName);
			   } catch(Exception e){
				   logger.error(e);
			 }
			 
		   /** Create a Dispatch instance from a service.**/
		   Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);

		   /** Add SOAPAction */
		   dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
		   dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, opName);
		   dispatch.getRequestContext().put(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, isChunked());
		   dispatch.getRequestContext().put(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT, 600000);
		   dispatch.getRequestContext().put(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT, 1200000);
		   dispatch.getRequestContext().put(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, getEncoding());
		   /** Invoke the service endpoint. **/
		   try{
			   SOAPMessage response = dispatch.invoke(request);
			   return response;
		   } catch(SOAPFaultException e){
			   SOAPFault fault = e.getFault();
			   logger.error(e);
			   return fault;
		   } catch(WebServiceException e){
			   logger.error(e);
			   return null;
		   }
		   
	}
	
	/**
	 * 
	 * @param header
	 * @param userName
	 * @param password
	 * @throws SOAPException
	 */
	public static void createSecurityInfo(SOAPHeader header, String userName, String password) throws SOAPException {
		SOAPHeaderElement security = header.addHeaderElement(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", //$NON-NLS-1$
				"Security","wsse")); //$NON-NLS-1$ //$NON-NLS-2$
		security.setMustUnderstand(false);
		SOAPElement token = security.addChildElement("UsernameToken", "wsse"); //$NON-NLS-1$ //$NON-NLS-2$
		token.addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"); //$NON-NLS-1$ //$NON-NLS-2$
		token.addAttribute(new QName("wsu","Id"), "UsernameToken-1815911473"); //$NON-NLS-1$ //$NON-NLS-2$
		token.addChildElement("Username", "wsse").addTextNode(userName); //$NON-NLS-1$ //$NON-NLS-2$
		SOAPElement ps = token.addChildElement("Password","wsse");
		ps.addAttribute(new QName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText"); //$NON-NLS-1$ //$NON-NLS-2$
		ps.addTextNode(password);
		
		SOAPHeaderElement userObject = header.addHeaderElement(new QName("http://www.primeton.com/EOS", "UserObject", "eos")); //$NON-NLS-1$ //$NON-NLS-2$
		userObject.setMustUnderstand(false);
		SOAPElement userId = userObject.addChildElement("UserId", "eos"); //$NON-NLS-1$ //$NON-NLS-2$
		userId.addTextNode(userName);
	}
	
	public boolean isChunked() {
		return isChunked;
	}

	public void setChunked(boolean chunked) {
		this.isChunked = chunked;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}

