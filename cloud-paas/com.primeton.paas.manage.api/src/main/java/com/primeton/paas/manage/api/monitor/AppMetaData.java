/**
 * 
 */
package com.primeton.paas.manage.api.monitor;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class AppMetaData extends MetaData {

	public static final String APP_NAME = "appName";
	
	/**
	 * 
	 * @return
	 */
	public String getAppName() {
		return get(APP_NAME);
	}
	
	/**
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		set(APP_NAME, appName);
	}
	
}
