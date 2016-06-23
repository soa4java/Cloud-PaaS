/**
 * 
 */
package com.primeton.paas.openapi.msgreduce;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgReduceProviderManager {
	
	private static IMsgReduceProvider provider = null;

	public static synchronized IMsgReduceProvider getMsgReduceProvider() {
		if (provider == null) {
			try {
				ServiceExtensionLoader<IMsgReduceProvider> loader = ServiceExtensionLoader.load(IMsgReduceProvider.class);
				for (Iterator<IMsgReduceProvider> it = loader.iterator(); it.hasNext();) {
					provider = it.next();
					break;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return provider;
	}
	
}
