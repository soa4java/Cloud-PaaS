/**
 * 
 */
package com.primeton.paas.app.mail;

import java.util.ServiceLoader;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class SystemConfigFactory {

	private static ISystemConfig SYSTEM_CONFIG = null;
	
	private SystemConfigFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static ISystemConfig getSystemConfig() {
		synchronized (SystemConfigFactory.class) {
			if(SYSTEM_CONFIG == null) {
				ServiceLoader<ISystemConfig> loader = ServiceLoader.load(ISystemConfig.class);
				if(loader != null) {
					for (ISystemConfig provider : loader) {
						if (provider != null) {
							SYSTEM_CONFIG = provider;
							return SYSTEM_CONFIG;
						}
					}
				}
			}
		}
		return SYSTEM_CONFIG;
	}
	
}
