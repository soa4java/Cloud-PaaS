/**
 * 
 */
package com.primeton.upcloud.ws.client;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ServiceClientFactory {

	private ServiceClientFactory(){
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static ServiceClient createServiceClient(){
		return new DefaultServiceClient();
	}
	
}

