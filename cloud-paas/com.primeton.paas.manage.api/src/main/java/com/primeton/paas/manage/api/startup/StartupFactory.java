/**
 * 
 */
package com.primeton.paas.manage.api.startup;

import java.util.ServiceLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StartupFactory {
	
	private static IManageStartup startup = null;

	private StartupFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IManageStartup getStartup() {
		if (startup != null) {
			return startup;
		}
		ServiceLoader<IManageStartup> loader = ServiceLoader.load(IManageStartup.class);
		if (loader != null) {
			for (IManageStartup obj : loader) {
				if (obj != null) {
					startup = obj;
				}
			}
		}
		return startup;
	}
	
}
