/**
 * 
 */
package com.primeton.paas.sms.server.startup;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.common.spi.http.HttpServer;
import com.primeton.paas.sms.api.ISmsService;
import com.primeton.paas.sms.api.SmsServiceFactory;
import com.primeton.paas.sms.spi.SmsRuntimeException;
import com.primeton.paas.sms.spi.SmsConstants;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public class SmsServerStartupListener extends AbstractStartupListener {
	
	private HttpServer server = null;
	
	protected void doStart() {
		try {
			String host = SystemProperties.getProperty(SmsConstants.HOST_KEY, SmsConstants.DEFAULT_HOST_VALUE);
			int port = SystemProperties.getProperty(SmsConstants.PORT_KEY, Integer.class, SmsConstants.DEFAULT_PORT_VALUE);
			server = HttpServer.getHttpServer(host, port);
			server.start();
			ISmsService smsService = SmsServiceFactory.getInstance().getSmsService();
			server.publish(SmsConstants.SMS_SERVICE_REMOTE_OBJECT_NAME, smsService);
		} catch (Exception e) {
			throw new SmsRuntimeException(e);
		}
	}

	protected void doStop() {
		try {
			if (server != null) {
				server.unpublish(SmsConstants.SMS_SERVICE_REMOTE_OBJECT_NAME);
				server.stop();
				server = null;
			}
		} catch (Exception e) {
			throw new SmsRuntimeException(e);
		}
	}

}
