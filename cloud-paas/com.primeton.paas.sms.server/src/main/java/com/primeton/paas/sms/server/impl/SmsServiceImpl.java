/*package com.primeton.paas.sms.server.impl;

import java.io.File;
import java.util.List;

import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.logger.spi.log4j.Log4jLoggerProvider;

import com.primeton.paas.sms.api.ISmsService;
import com.primeton.paas.sms.api.SmsServiceFactory;
import com.primeton.paas.sms.model.SmsResult;
import com.primeton.paas.sms.server.factory.SmsClientFactory;
import com.primeton.paas.sms.spi.SmsRuntimeException;

public class SmsServiceImpl implements ISmsService {

	private static final ILogger log = LoggerFactory.getLogger(SmsServiceImpl.class);
	
	public List<SmsResult> send(String number, String content, long timeout) throws SmsRuntimeException {
		log.info("In SMS Server. Begin send sms {number="+number + ",content= " + content + ",timeout="+timeout+"}.") ;
		SmsClient smsClient = SmsClientFactory.getSmsClient();
		// initialize client
		try {
			smsClient.initialize();
		} catch (Exception e) {
			throw new SmsRuntimeException("Init Mmpp2Client failed:" + e.getMessage());
		}
		List<SmsResult> resultList = smsClient.send(number, content,timeout);
		// close client
		smsClient.close();
		return resultList;
	}
	
}
*/