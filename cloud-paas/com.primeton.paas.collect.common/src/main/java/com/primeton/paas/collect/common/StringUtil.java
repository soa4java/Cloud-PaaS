/**
 * 
 */
package com.primeton.paas.collect.common;

import java.io.UnsupportedEncodingException;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-12-17
 *
 */
public class StringUtil {
	
	public static final String DEFAULT_CHARSET = "ISO8859-1";
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 
	 * @param bytes
	 * @param charset
	 * @return
	 */
	public static String toString(byte[] bytes, String charset) {
		if(bytes == null) return null;
		if(charset == null || charset.trim().length() == 0) {
			charset = DEFAULT_CHARSET;
		}
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toString(byte[] bytes) {
		return toString(bytes, CHARSET_UTF8);
	}
	
	/**
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static byte[] toBytes(String str, String charset) {
		if(str == null) {
			return null;
		}
		if(charset == null || charset.trim().length() == 0) {
			charset = DEFAULT_CHARSET;
		}
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		return toBytes(str, CHARSET_UTF8);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

}
