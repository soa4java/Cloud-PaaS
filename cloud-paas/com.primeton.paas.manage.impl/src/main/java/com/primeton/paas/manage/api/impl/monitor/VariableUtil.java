/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import org.gocom.cloud.cesium.common.api.exception.VariableException;
import org.gocom.cloud.cesium.common.api.manage.variable.VariableManager;
import org.gocom.cloud.cesium.common.api.manage.variable.VariableManagerFactory;
import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class VariableUtil {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(VariableUtil.class);

	/**
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getValue(String key, String defValue) {
		if (StringUtil.isEmpty(key)) {
			return defValue;
		}
		VariableManager manager = VariableManagerFactory.createVariableManager();
		Variable var = NamingUtil.lookupVariable(key);
		try {
			var = ((var == null) ? manager.getVariable(key) : var);
		} catch (VariableException e) {
			logger.error(e);
		}
		
		String value = defValue;
		value = (var == null) ? defValue : var.getValue();
		return value;
	}
	
	/**
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static Long getValue(String key, long defValue) {
		String value = getValue(key, String.valueOf(defValue));
		return Long.parseLong(value);
	}
	
	/**
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static Integer getValue(String key, int defValue) {
		String value = getValue(key, String.valueOf(defValue));
		return Integer.parseInt(value);
	}
	
}
