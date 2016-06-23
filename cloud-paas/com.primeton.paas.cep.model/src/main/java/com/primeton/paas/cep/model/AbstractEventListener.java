/**
 * 
 */
package com.primeton.paas.cep.model;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public abstract class AbstractEventListener implements EventListener {

	/* (non-Javadoc)
	 * @see com.primeton.paas.cep.api.EventListener#getId()
	 */
	public String getId() {
		return getClass().getName();
	}
	
}
