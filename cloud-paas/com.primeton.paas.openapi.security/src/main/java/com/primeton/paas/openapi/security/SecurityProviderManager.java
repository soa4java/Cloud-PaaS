/**
 * 
 */
package com.primeton.paas.openapi.security;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SecurityProviderManager {
	
	private static ISecurityProvider provider = null;

	public static synchronized ISecurityProvider getMsgReduceProvider() {
		if (provider == null) {
			try {
				ServiceExtensionLoader<ISecurityProvider> loader = ServiceExtensionLoader.load(ISecurityProvider.class);
				for (Iterator<ISecurityProvider> it = loader.iterator(); it.hasNext();) {
					provider = it.next();
					break;
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return provider;
	}
	
}
