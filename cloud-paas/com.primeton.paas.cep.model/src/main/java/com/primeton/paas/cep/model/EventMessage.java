/**
 * 
 */
package com.primeton.paas.cep.model;

import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.AbstractMessage;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class EventMessage extends AbstractMessage<Map<String, Object>> {

	private Map<String, Object> body = new HashMap<String, Object>();
	
	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.Message#getBody()
	 */
	public Map<String, Object> getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.Message#setBody(java.lang.Object)
	 */
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

}
