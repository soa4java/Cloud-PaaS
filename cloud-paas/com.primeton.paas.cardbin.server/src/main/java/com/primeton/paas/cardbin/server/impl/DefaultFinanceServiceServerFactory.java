/**
 * 
 */
package com.primeton.paas.cardbin.server.impl;

import com.primeton.paas.cardbin.api.FinanceServiceFactory;
import com.primeton.paas.cardbin.api.ICardBinService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultFinanceServiceServerFactory extends FinanceServiceFactory {

	private ICardBinService cardBinService = null;
	
	/**
	 * 
	 */
	private void init() {
		if (cardBinService == null) {
			cardBinService = new CardBinServiceServerImpl();
		}
	}

	/**
	 * 
	 */
	public ICardBinService getCardBinService() {
		if(cardBinService == null) {
			synchronized(DefaultFinanceServiceServerFactory.class) {
				init();
			}
		}
		return cardBinService;
	}
	
}
