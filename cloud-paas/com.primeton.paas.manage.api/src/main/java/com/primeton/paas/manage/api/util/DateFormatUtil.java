/**
 * 
 */
package com.primeton.paas.manage.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DateFormatUtil {
	
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String toString(Date date) {
		return toString(date, DEFAULT_PATTERN);
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		if(date == null) {
			return null;
		}
		if(StringUtil.isEmpty(pattern)) {
			pattern = DEFAULT_PATTERN;
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String date, String pattern) {
		if(StringUtil.isEmpty(date)) {
			return null;
		}
		if(StringUtil.isEmpty(pattern)) {
			pattern = DEFAULT_PATTERN;
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date toDate(String date) {
		return toDate(date, DEFAULT_PATTERN);
	}

}
