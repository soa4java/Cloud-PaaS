/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

import java.util.Map;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface EventListener {

	/**
	 * 
	 * @param event
	 * @return
	 */
	Map<String, Object> handle(Map<String, Object> event);
	
}
