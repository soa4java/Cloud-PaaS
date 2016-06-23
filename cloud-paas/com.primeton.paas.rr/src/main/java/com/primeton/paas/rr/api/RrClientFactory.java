/**
 * 
 */
package com.primeton.paas.rr.api;

import com.primeton.paas.rr.impl.RrClientFactoryImpl;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public abstract class RrClientFactory {
	
	private static RrClientFactory FACTORY = null;

	/**
	 * 
	 * @return
	 */
	public static RrClientFactory getInstance() {
		if (FACTORY != null) {
			return FACTORY;
		}
		synchronized (RrClientFactory.class) {
			if (FACTORY != null) {
				return FACTORY;
			}
			FACTORY = new RrClientFactoryImpl();
			return FACTORY;
		}
	}

	/**
	 * 
	 * @param rrServeUrl
	 * @param repoName
	 * @param user
	 * @param pwd
	 * @return
	 */
	public abstract IRrClient getClient(String rrServeUrl, String repoName, String user, String pwd);

	/**
	 * 
	 */
	public abstract void destoryClient();
	
}
