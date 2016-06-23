/**
 * 
 */
package com.primeton.paas.sms.server.impl;

import com.primeton.paas.sms.api.ISmsService;
import com.primeton.paas.sms.api.SmsServiceFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultSmsServiceFactory extends SmsServiceFactory {

	private ISmsService smsService = null;
	
	private void init() {
		if (smsService == null) {
			// smsService = new SmsServiceImpl(); // Comment by ZhongWen.Li
		}
	}
	
	public ISmsService getSmsService() {
		if(smsService == null) {
			synchronized(DefaultSmsServiceFactory.class) {
				init();
			}
		}
		return smsService;
	}
}
