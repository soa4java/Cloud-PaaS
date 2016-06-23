/**
 * 
 */
package com.primeton.paas.cardbin.server.ws;

import javax.jws.WebService;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@SuppressWarnings("restriction")
@WebService
public interface ICardbinSync {

	/**
	 * 
	 * @param paraCode
	 * @param batchNo
	 * @return
	 */
	public String doSynch(String paraCode, String batchNo);
	
}
