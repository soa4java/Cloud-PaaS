/**
 * 
 */
package com.primeton.paas.cesium.agent;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.cesium.mqclient.util.StringUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class VariableUtil {
	
	private static ILogger logger = LoggerFactory.getLogger(VariableUtil.class); 
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getValue(String key, String value) {
		if (StringUtil.isEmpty(key)) {
			return value;
		}
		Variable var = null;
		try {
			var = NamingUtil.lookupVariable(key);
		} catch (Throwable ignore) {
			logger.error(ignore);
		}
		String v = (var == null) ? value : var.getValue();
		return null == v ? value : v;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static int getValue(String key, int value) {
		try {
			return Integer.parseInt(getValue(key, String.valueOf(value)));
		} catch (NumberFormatException e) {
			return value;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static long getValue(String key, long value) {
		try {
			return Long.parseLong(getValue(key, String.valueOf(value)));
		} catch (NumberFormatException e) {
			return value;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean getValue(String key, boolean value) {
		return Boolean.parseBoolean(getValue(key, String.valueOf(false)));
	}
	
}
