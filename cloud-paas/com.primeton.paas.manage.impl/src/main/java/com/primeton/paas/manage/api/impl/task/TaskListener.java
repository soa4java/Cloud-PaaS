/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface TaskListener {

	/**
	 * 
	 * @param TaskEvent
	 */
	public void handle(TaskEvent event);
	
}
