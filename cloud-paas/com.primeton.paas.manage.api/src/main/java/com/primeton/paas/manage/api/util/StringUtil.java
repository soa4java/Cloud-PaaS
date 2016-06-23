/**
 * 
 */
package com.primeton.paas.manage.api.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class StringUtil {
	
	public static final String DEFAULT_CHARSET = "ISO8859-1";
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public static boolean isNotEmpty(String ... values) {
		if (values == null || values.length == 0) {
			return false;
		}
		boolean flag = true;
		for (String value : values) {
			if ((value == null || value.trim().length() == 0)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param bytes
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytes2String(byte[] bytes, String charset) throws UnsupportedEncodingException {
		if(bytes == null) return null;
		if(charset == null || charset.trim().length() == 0) {
			charset = DEFAULT_CHARSET;
		}
		return new String(bytes, charset);
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytes2String(byte[] bytes) throws UnsupportedEncodingException {
		return bytes2String(bytes, CHARSET_UTF8);
	}
	
	/**
	 * 
	 * @param str
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] string2Bytes(String str, String charset) throws UnsupportedEncodingException {
		if(str == null) {
			return null;
		}
		if(charset == null || charset.trim().length() == 0) {
			charset = DEFAULT_CHARSET;
		}
		return str.getBytes(charset);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] string2Bytes(String str) throws UnsupportedEncodingException {
		return string2Bytes(str, CHARSET_UTF8);
	}
	
	/**
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringAfter(String str,String separator){
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return "";
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}
	
	/**
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String substringBefore(String str,String separator){
		if ((isEmpty(str)) || (separator == null)) {
			return str;
		}
		if (separator.length() == 0) {
			return "";
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}
	
	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean contain(String str1, String str2) {
		if (str1 == null) {
			return false;
		}
		if (str2 == null) {
			return true;
		}
		return str1.contains(str2);
	}
	
	/**
	 * 
	 * @param message
	 * @param params
	 * @return
	 */
	public static String format(String message, Object[] params) {
		if (isEmpty(message) || null == params || params.length == 0) {
			return message;
		}
		return new MessageFormat(message).format(params);
	}
	
	/**
	 * 
	 * @param message
	 * @param params
	 * @return
	 */
	public static String format(String message, Collection<Object> params) {
		return format(message, null == params ? null : params.toArray());
	}
	
}
