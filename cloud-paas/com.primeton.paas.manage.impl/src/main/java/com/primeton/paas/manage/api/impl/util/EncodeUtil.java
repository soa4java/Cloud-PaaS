/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 用户密码加解密工具类
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class EncodeUtil {

	public final static String ENCODING = "UTF-8";

	/**
	 * Default. <br>
	 */
	private EncodeUtil() {
		super();
	}

	/**
	 * 
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encoded(String password)
			throws UnsupportedEncodingException {
		byte[] b = Base64.encodeBase64(password.getBytes(ENCODING));
		return new String(b, ENCODING);
	}

	/**
	 * 加密,遵循RFC标准
	 * 
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodedSafe(String password)
			throws UnsupportedEncodingException {
		byte[] b = Base64.encodeBase64(password.getBytes(ENCODING), true);
		return new String(b, ENCODING);
	}

	/**
	 * 
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String password)
			throws UnsupportedEncodingException {
		byte[] b = Base64.decodeBase64(password.getBytes(ENCODING));
		return new String(b, ENCODING);
	}

}
