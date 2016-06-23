/**
 * 
 */
package com.primeton.paas.manage.api.impl.resource;

import com.primeton.paas.manage.api.impl.util.SystemVariables;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WebServiceUtil {
	
	private static final String WEBSERVICE_URL = "iaas_web_service_url";
	private static final String USER_NAME = "iaas_user_name";
	private static final String USER_PASSWORD = "iaas_user_password";
	private static final String BIZ_ZONE_ID = "iaas_biz_zone_id";
	private static final String NAS_ZONE_ID = "iaas_nas_zone_id";
	private static final String DEFAULT_GROUP = "iaas_default_group";

	/**
	 * 
	 * @return
	 */
	public static String getWsUrl() {
		return SystemVariables.getStringValue(WEBSERVICE_URL, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getUsername() {
		return SystemVariables.getStringValue(USER_NAME, "kitty");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPassword() {
		return SystemVariables.getStringValue(USER_PASSWORD, "000000");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getBizZoneID() {
		return SystemVariables.getStringValue(BIZ_ZONE_ID, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getNasZoneID() {
		return SystemVariables.getStringValue(NAS_ZONE_ID, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultGroup() {
		return SystemVariables.getStringValue(DEFAULT_GROUP, "PAAS_PLATEFORM");
	}
	
}
