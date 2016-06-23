/**
 * 
 */
package com.primeton.paas.mail.model;

import org.gocom.cloud.cesium.mqclient.api.AbstractMessage;

/**
 * 
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MergeMailMessage extends AbstractMessage<MergeMail> {

	private MergeMail mm;

	public MergeMail getBody() {
		return mm;
	}

	public void setBody(MergeMail mm) {
		this.mm = mm;
	}

}
