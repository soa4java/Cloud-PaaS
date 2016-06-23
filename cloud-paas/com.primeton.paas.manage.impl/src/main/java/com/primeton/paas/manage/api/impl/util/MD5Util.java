/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.gocom.cloud.common.logger.api.ILogger;

import sun.misc.BASE64Encoder;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;

/**
 * 
 * @author liyanping(mailto:liyp@primeton.com)
 * 
 */
public class MD5Util {

	private static ILogger logger = ManageLoggerFactory
			.getLogger(MD5Util.class);

	private static MessageDigest digest = null;

	public static final String CHARTSET_UTF8 = "UTF-8";
	public static final String CHARSET_ISO8859_1 = "ISO8859-1";

	static {
		init();
	}

	/**
	 * Default. <br>
	 */
	private MD5Util() {
		super();
	}

	private static void init() {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return md5(str, CHARSET_ISO8859_1);
	}

	/**
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String md5(String str, String charset) {
		if (str == null)
			return null;
		if (charset == null || charset.trim().length() == 0) {
			charset = CHARSET_ISO8859_1;
		}
		if (digest == null) {
			init();
		}
		try {
			byte[] bytes = digest.digest(str.getBytes(charset));
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(bytes);
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
		return null;
	}

}
