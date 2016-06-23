/**
 * 
 */
package com.primeton.paas.openapi.flowcontrol;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FlowControlProviderManager {
	
	private static IFlowControlProvider provider = null;

	/**
	 * 
	 * @return
	 */
	public static synchronized IFlowControlProvider getMsgReduceProvider() {
		if (provider == null) {
			try {
				ServiceExtensionLoader<IFlowControlProvider> loader = ServiceExtensionLoader.load(IFlowControlProvider.class);
				for (Iterator<IFlowControlProvider> it = loader.iterator(); it.hasNext();) {
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
