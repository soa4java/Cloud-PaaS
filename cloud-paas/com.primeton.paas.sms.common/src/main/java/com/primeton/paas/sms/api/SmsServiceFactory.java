/**
 * 
 */
package com.primeton.paas.sms.api;

import java.util.List;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class SmsServiceFactory {
	
	private static SmsServiceFactory FACTORY = null;
	
	/**
	 * Get factory instance <br>
	 * @return factory instance
	 */
	public static synchronized SmsServiceFactory getInstance() {
		if (FACTORY != null) {
			return FACTORY;
		}
		ServiceExtensionLoader<SmsServiceFactory> serviceLoader = ServiceExtensionLoader.load(SmsServiceFactory.class);
		List<SmsServiceFactory> factoryList = serviceLoader.getExtensions();
		if (factoryList.size() > 0) {
			FACTORY = factoryList.get(0);
			return FACTORY;
		}
		throw new PaasRuntimeException("Cannot load Factory for class '" + SmsServiceFactory.class.getName() + "'.");
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract ISmsService getSmsService();
	
}
