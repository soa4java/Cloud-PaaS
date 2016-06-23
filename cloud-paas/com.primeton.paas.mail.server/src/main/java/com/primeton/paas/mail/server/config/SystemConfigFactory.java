/**
 * 
 */
package com.primeton.paas.mail.server.config;

import java.util.ServiceLoader;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class SystemConfigFactory {

	private static ISystemConfig SYSTEM_CONFIG = null;

	private SystemConfigFactory() {
	}

	public static ISystemConfig getSystemConfig() {
		synchronized (SystemConfigFactory.class) {
			if (SYSTEM_CONFIG == null) {
				init();
			}
		}
		return SYSTEM_CONFIG;
	}

	/**
	 * 
	 */
	public static void init() {
		ServiceLoader<ISystemConfig> loader = ServiceLoader
				.load(ISystemConfig.class);
		if (loader != null) {
			for (ISystemConfig config : loader) {
				if (config != null) {
					SYSTEM_CONFIG = config;
				}
			}
		}
	}

}
