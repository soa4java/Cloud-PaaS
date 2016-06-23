/**
 * 
 */
package com.primeton.paas.cardbin.api;

import com.primeton.paas.cardbin.model.BankCard;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface ICardBinService {
	
	/**
	 * 
	 * @param pan
	 * @return
	 */
	public BankCard getCardByPan(String pan);
	
	/**
	 * 
	 * @param pan
	 * @param map
	 * @return
	 */
	public BankCard getCardByPan(String pan, int map);
	
}
