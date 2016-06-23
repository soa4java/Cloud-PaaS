/**
 * 
 */
package com.primeton.paas.cardbin.server.startup;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.cardbin.api.FinanceServiceFactory;
import com.primeton.paas.cardbin.api.ICardBinService;
import com.primeton.paas.cardbin.server.jdbc.ConnectionFactory;
import com.primeton.paas.cardbin.spi.CardBinConstants;
import com.primeton.paas.cardbin.spi.CardBinRuntimeException;
import com.primeton.paas.common.spi.AbstractStartupListener;
import com.primeton.paas.common.spi.http.HttpServer;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinServiceServerStartupListener extends AbstractStartupListener {
	
	private HttpServer server = null;
	
	protected void doStart() {
		try {
			ConnectionFactory.initDataSource();
			String host = SystemProperties.getProperty(CardBinConstants.HOST_KEY, CardBinConstants.DEFAULT_HOST_VALUE);
			int port = SystemProperties.getProperty(CardBinConstants.PORT_KEY, Integer.class, CardBinConstants.DEFAULT_PORT_VALUE);
			server = HttpServer.getHttpServer(host, port);
			server.start();
			ICardBinService cardBinService = FinanceServiceFactory.getInstance().getCardBinService();
			server.publish(CardBinConstants.FINANCE_CARDBIN_SERVICE_REMOTE_OBJECT_NAME, cardBinService);
		} catch (Exception e) {
			throw new CardBinRuntimeException(e);
		}
	}

	protected void doStop() {
		try {
			if (server != null) {
				server.unpublish(CardBinConstants.FINANCE_CARDBIN_SERVICE_REMOTE_OBJECT_NAME);
				server.stop();
				server = null;
			}
			ConnectionFactory.destroyDataSource();
		} catch (Exception e) {
			throw new CardBinRuntimeException(e);
		}
	}

}
