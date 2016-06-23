/**
 * 
 */
package com.primeton.paas.manage.api.listener;

import com.primeton.paas.manage.api.model.WebApp;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface AppEventListener {
	
	/**
	 * 
	 * @param webApp
	 */
	void doCreate(WebApp webApp);
	
	/**
	 * 
	 * @param webApp
	 */
	void doDestroy(WebApp webApp);
	
	/**
	 * 
	 * @param oldWebapp
	 * @param newWebapp
	 */
	void doModify(WebApp oldWebapp, WebApp newWebapp);

}
