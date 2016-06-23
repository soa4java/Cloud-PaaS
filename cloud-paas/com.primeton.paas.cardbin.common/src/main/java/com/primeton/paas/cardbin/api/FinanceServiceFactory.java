/**
 * 
 */
package com.primeton.paas.cardbin.api;

import java.util.List;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class FinanceServiceFactory {

	private static FinanceServiceFactory FACTORY = null;
	
	/**
	 * Get factory instance <br>
	 * @return factory instance
	 */
	public static FinanceServiceFactory getInstance() {
		if (FACTORY != null) {
			return FACTORY;
		}
		synchronized (FinanceServiceFactory.class) {
			if (FACTORY != null) {
				return FACTORY;
			}
			ServiceExtensionLoader<FinanceServiceFactory> serviceLoader = ServiceExtensionLoader.load(FinanceServiceFactory.class);
			List<FinanceServiceFactory> factoryList = serviceLoader.getExtensions();
			if (factoryList.size() > 0) {
				FACTORY = factoryList.get(0);
				return FACTORY;
			}
			throw new PaasRuntimeException("Cannot load Factory for class '" + FinanceServiceFactory.class.getName() + "'.");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract ICardBinService getCardBinService();
	
}
