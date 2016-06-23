/**
 * 
 */
package com.primeton.paas.cep.util;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StringUtil {
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(String ... array) {
		if (array == null || array.length == 0) {
			return true;
		}
		
		boolean flag = true;
		for (String value : array) {
			if ((value == null || value.trim().length() == 0)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

}
