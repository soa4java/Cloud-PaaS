/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EngineConfigManager {
	
	private static HttpOutConfigModel httpOutConfigModel;

	private static ResponseHandleThreadPoolConfigModel responseHandleThreadPoolConfigModel;

	public static HttpOutConfigModel getHttpOutConfigModel() {
		return httpOutConfigModel;
	}

	public static void setHttpOutConfigModel(HttpOutConfigModel httpOutConfigModel) {
		EngineConfigManager.httpOutConfigModel = httpOutConfigModel;
	}

	public static ResponseHandleThreadPoolConfigModel getResponseHandleThreadPoolConfigModel() {
		return responseHandleThreadPoolConfigModel;
	}

	public static void setResponseHandleThreadPoolConfigModel(ResponseHandleThreadPoolConfigModel responseHandleThreadPoolConfigModel) {
		EngineConfigManager.responseHandleThreadPoolConfigModel = responseHandleThreadPoolConfigModel;
	}
	
}
