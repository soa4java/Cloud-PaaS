/**
 * 
 */
package com.primeton.paas.mail.model;

import org.gocom.cloud.cesium.mqclient.api.AbstractMessage;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class ResultMessage extends AbstractMessage<Result> {

	private Result mr;

	public Result getBody() {
		return mr;
	}

	public void setBody(Result mr) {
		this.mr = mr;
	}

}
