/**
 * 
 */
package com.primeton.paas.openapi.httpin.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpInConfigModelManager {
	
	private static HttpInConfigModel model;

	public static HttpInConfigModel getModel() {
		return model;
	}

	public static void setModel(HttpInConfigModel model) {
		HttpInConfigModelManager.model = model;
	}
	
}
