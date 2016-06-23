/**
 * 
 */
package com.primeton.paas.cep.model;

import com.espertech.esper.client.UpdateListener;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EventListener extends UpdateListener {

	/**
	 * className <br>
	 * 
	 * @return className
	 */
	public String getId();
	
	/**
	 * 
	 * @param context
	 * @param newEvents
	 * @param oldEvents
	 */
	//	public void update(EventContext context, EventBean[] newEvents, EventBean[] oldEvents);
	
}
