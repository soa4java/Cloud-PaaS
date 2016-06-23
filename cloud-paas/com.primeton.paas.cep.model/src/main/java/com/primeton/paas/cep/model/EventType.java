/**
 * 
 */
package com.primeton.paas.cep.model;

import java.util.Map;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface EventType {
	
	/**
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * 
	 * @return
	 */
	Map<String, Object> getType();
	
}
