/**
 * 
 */
package com.primeton.upcloud.ws.api.impl;

import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFault;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SOAPFaultParserUtil {

	/**
	 * 
	 * @param soapFault
	 * @return
	 */
	public static String parseSOAPFualt(SOAPFault soapFault) {
		if (soapFault == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("FaultCode: " + soapFault.getFaultCode());
		sb.append(", FaultString: " + soapFault.getFaultString());
		Detail faultDetail = soapFault.getDetail();
		SOAPElement faultContent = (SOAPElement) faultDetail.getDetailEntries().next();
		sb.append("Detail: " + faultContent.getTextContent());
		return sb.toString();
	}
	
}
