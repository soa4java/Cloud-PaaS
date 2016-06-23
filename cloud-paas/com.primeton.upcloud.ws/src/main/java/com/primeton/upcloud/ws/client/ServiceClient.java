/**
 * 
 */
package com.primeton.upcloud.ws.client;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface ServiceClient {

	/**
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding);

	/**
	 * 
	 * @return
	 */
	public String getEncoding();

	/**
	 * 
	 * @param chunked
	 */
	public void setChunked(boolean chunked);

	/**
	 * 
	 * @return
	 */
	public boolean isChunked();

	/**
	 * 
	 * @param wsUrl
	 * @param opQName
	 * @param opName
	 * @param inputs
	 * @param returnTypes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object[] invokeService(String wsUrl, QName opQName, String opName, Object[] inputs, Class[] returnTypes);
	
	/**
	 * 
	 * @param wsUrl
	 * @param binding javax.xml.ws.soap.SOAPBinding
	 * @param serviceQName
	 * @param portQName
	 * @param opName
	 * @param request
	 * @return
	 */
	public Object invokeService(String wsUrl, String binding, QName serviceQName, QName portQName, String opName, SOAPMessage request);
	
}

