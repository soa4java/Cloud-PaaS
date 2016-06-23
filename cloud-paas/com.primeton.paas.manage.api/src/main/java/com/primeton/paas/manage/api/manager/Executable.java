/**
 * 
 */
package com.primeton.paas.manage.api.manager;

import com.primeton.paas.manage.api.exception.TaskException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface Executable {
	
	/**
	 * 
	 * @return
	 */
	String getType();

	/**
	 * 
	 */
	void init();
	
	/**
	 * 
	 * @return
	 * @throws TaskException
	 */
	String execute() throws TaskException;
	
	/**
	 * 
	 */
	void clear();
	
}
