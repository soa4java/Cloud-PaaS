/**
 * 
 */
package com.primeton.paas.rr.impl;

import com.primeton.paas.rr.api.IRrClient;
import com.primeton.paas.rr.api.RrClientFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class RrClientFactoryImpl extends RrClientFactory {

	private IRrClient rrClient = null;

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.rr.api.RrClientFactory#getClient(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IRrClient getClient(String rrServeUrl, String repoName, String user, String pwd) {
		rrClient = new RrClientImpl(rrServeUrl, repoName, user, pwd);
		return rrClient;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.rr.api.RrClientFactory#destoryClient()
	 */
	public void destoryClient() {
		if (rrClient != null && rrClient instanceof RrClientImpl) {
			((RrClientImpl) rrClient).destory();
		}
	}
	
}
