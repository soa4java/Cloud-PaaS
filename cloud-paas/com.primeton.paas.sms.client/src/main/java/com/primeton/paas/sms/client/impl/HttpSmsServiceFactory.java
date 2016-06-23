/**
 * 
 */
package com.primeton.paas.sms.client.impl;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;

import com.primeton.paas.common.spi.http.HttpClient;
import com.primeton.paas.sms.api.ISmsService;
import com.primeton.paas.sms.api.SmsServiceFactory;
import com.primeton.paas.sms.spi.SmsRuntimeException;
import com.primeton.paas.sms.spi.SmsConstants;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class HttpSmsServiceFactory extends SmsServiceFactory {

	private ISmsService smsService = null;
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.sms.api.SmsServiceFactory#getSmsService()
	 */
	public ISmsService getSmsService() {
		String host = null;
		int port = 0;
		RuntimeInstance[] instances;
		try {
			instances = InstanceManagerFactory.createInstanceManager().getInstances(SmsConstants.TYPE);
			if (instances != null && instances.length > 0) {
				RuntimeInstance instance = instances[0];
				host = instance.getIp();
				port = instance.getPort();
			} else {
				throw new SmsRuntimeException("Can't find SMS Service Instance on Zookeeper.");
			}
		} catch (TimeoutException e) {
			throw new SmsRuntimeException(e);
		}
		
		if (smsService == null) {
			synchronized(SmsServiceFactory.class) {
				if (smsService == null) {
					try {
						return HttpClient.getRemoteObject(host, port, SmsConstants.SMS_SERVICE_REMOTE_OBJECT_NAME, ISmsService.class, true);
					} catch (Exception e) {
						throw new SmsRuntimeException(e);
					}
				}
			}
		}
		
		return smsService;
	}

}
