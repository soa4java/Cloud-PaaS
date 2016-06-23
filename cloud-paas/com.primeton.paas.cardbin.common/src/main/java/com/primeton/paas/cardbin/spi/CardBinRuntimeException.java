/**
 * 
 */
package com.primeton.paas.cardbin.spi;

import com.primeton.paas.exception.api.PaasRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CardBinRuntimeException extends PaasRuntimeException {
	
	private static final long serialVersionUID = 2552744130642516948L;

	public CardBinRuntimeException(Throwable cause) {
		super(cause);
	}

	public CardBinRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CardBinRuntimeException(String message, Object[] param, Throwable cause) {
		super(message, param, cause);
	}

	public CardBinRuntimeException(String message) {
		super(message);
	}
	
	public CardBinRuntimeException(String message, Object[] param) {
		super(message, param);
	}

}

