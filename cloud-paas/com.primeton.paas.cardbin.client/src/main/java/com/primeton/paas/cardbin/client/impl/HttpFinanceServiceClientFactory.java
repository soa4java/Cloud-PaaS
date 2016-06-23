/**
 * 
 */
package com.primeton.paas.cardbin.client.impl;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;

import com.primeton.paas.cardbin.api.FinanceServiceFactory;
import com.primeton.paas.cardbin.api.ICardBinService;
import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;
import com.primeton.paas.common.spi.http.HttpClient;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpFinanceServiceClientFactory extends FinanceServiceFactory {
	
	private static ICardBinService cardBinService = null;

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cardbin.api.FinanceServiceFactory#getCardBinService()
	 */
	public ICardBinService getCardBinService() {
		String host = null;
		int port = 0;
		RuntimeInstance[] instances;
		try {
			instances = InstanceManagerFactory.createInstanceManager().getInstances(CardBinConstants.TYPE);
			if (instances != null && instances.length > 0) {
				RuntimeInstance instance = instances[0];
				host = instance.getIp();
				port = instance.getPort();
			} else {
				throw new CardBinRuntimeException("Can't find CardBin Service Instance on Zookeeper.");
			}
		} catch (TimeoutException e) {
			throw new CardBinRuntimeException(e);
		}
		if (cardBinService == null) {
			synchronized(FinanceServiceFactory.class) {
				if (cardBinService == null) {
					try {
						return HttpClient.getRemoteObject(host, port, CardBinConstants.FINANCE_CARDBIN_SERVICE_REMOTE_OBJECT_NAME, ICardBinService.class, true);
					} catch (Exception e) {
						throw new CardBinRuntimeException(e);
					}
				}
			}
		}
		return cardBinService;
	}

}
