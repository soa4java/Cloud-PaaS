/**
 * 
 */
package com.primeton.paas.mail.server.startup;

import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.AbstractMessageConsumer;
import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.Message;
import org.gocom.cloud.cesium.mqclient.api.exception.ConnectionException;

import com.primeton.paas.mail.model.MergeMail;
import com.primeton.paas.mail.model.MergeMailMessage;
import com.primeton.paas.mail.model.Result;
import com.primeton.paas.mail.model.ResultMessage;
import com.primeton.paas.mail.server.model.MailInfo;
import com.primeton.paas.mail.server.util.MailConsumerUtil;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class MailConsumer extends AbstractMessageConsumer {

	/**
	 * 
	 * @param qName
	 */
	public MailConsumer(String qName) {
		super(qName);
	}

	/**
	 * 
	 * @param qName
	 * @param mqClient
	 * @throws ConnectionException
	 */
	public MailConsumer(String qName, MQClient mqClient)
			throws ConnectionException {
		super(qName, mqClient);
	}

	/**
	 * 
	 * @param qName
	 * @param args
	 * @throws ConnectionException
	 */
	public MailConsumer(String qName, Map<String, String> args)
			throws ConnectionException {
		super(qName, args);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.mqclient.api.AbstractMessageConsumer#doMessage(org.gocom.cloud.cesium.mqclient.api.Message)
	 */
	public Message<?> doMessage(Message<?> message) {
		MergeMailMessage msg = (MergeMailMessage) message;
		MergeMail mergeMail = msg.getBody();
		MailInfo mail = MailConsumerUtil.insertToDb(mergeMail);
		Result r = new Result();
		if (msg.needResponse() && mail.isIfBack()) {
			r = MailConsumerUtil.send(mail);
		}
		ResultMessage rs = new ResultMessage();
		r.setMailId(mergeMail.getMailId());
		rs.setBody(r);
		return rs;
	}

}
