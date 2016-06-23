/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.factory;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.primeton.paas.manage.api.manager.IHostTemplateManager;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HostTemplateManagerFactory {
	
	private static IHostTemplateManager manager;
	
	/**
	 * 
	 * @return
	 */
	public static IHostTemplateManager getManager() {
		if (null == manager) {
			ServiceLoader<IHostTemplateManager> loader = ServiceLoader.load(IHostTemplateManager.class);
			Iterator<IHostTemplateManager> iterator = loader.iterator();
			if (iterator.hasNext()) {
				return manager = iterator.next();
			}
		}
		return manager;
	}

}
