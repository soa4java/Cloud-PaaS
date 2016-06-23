/**
 * 
 */
package com.primeton.paas.console.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.IOUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class NLSMessageUtils {
	
	private static ILogger logger = LoggerFactory.getLogger(NLSMessageUtils.class);
	
	private NLSMessageUtils() {}
	
	private static Properties properties;
	
	private static Map<String, Properties> messages = new HashMap<String, Properties>();
	
	/**
	 * 
	 * @return
	 */
	public static String getNLSMessagesFile() {
		Locale locale = LocaleUtil.getDefaultLocale();
		String filePath = null == locale ? "messages.properties" : locale //$NON-NLS-1$
				.getLanguage()
				+ File.separator
				+ locale.getCountry()
				+ File.separator + "messages.properties"; //$NON-NLS-1$
		
		return NLSMessageUtils.class.getResource("/").getFile().toString() //$NON-NLS-1$
				+ File.separator + "system" + File.separator + filePath; //$NON-NLS-1$
	}
	
	/**
	 * init. <br>
	 */
	private static void init() {
		File f = new File(getNLSMessagesFile());
		if (!f.exists()  || f.isDirectory()) {
			logger.error("NLS file {0} not found.", new Object[] { f.getAbsoluteFile() });;
			return;
		}
		if (null == properties) {
			properties = new Properties();
			load(f, properties);
		}
		messages.put(LocaleUtil.getDefaultLocale().getLanguage()
				+ "_" + LocaleUtil.getDefaultLocale().getCountry(), properties); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param f
	 * @param p
	 */
	private static void load(File f, Properties p) {
		if (null == f || !f.exists() || f.isDirectory() || null == p) {
			return;
		}
		
		FileInputStream inStream = null;
		try {
			inStream = null;
			inStream = new FileInputStream(f);
			p.load(inStream);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			IOUtil.closeQuietly(inStream);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		if (null == key || key.trim().length() == 0) {
			return null;
		}
		if (null == properties) {
			init();
		}
		return properties.getProperty(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getMessage(String key, Locale locale) {
		if (null == key || key.trim().length() == 0) {
			return null;
		}
		return (null == locale) ? getMessage(key) : getMessage(key, locale.getLanguage(), locale.getCountry());
	}
	
	/**
	 * 
	 * @param key
	 * @param language
	 * @param country
	 * @return
	 */
	public static String getMessage(String key, String language, String country) {
		if (null == key || key.trim().length() == 0) {
			return null;
		}
		if (null == language || language.trim().length() == 0
				|| null == country || country.trim().length() == 0) {
			return getMessage(key);
		}
		final String _key_ = language + "_" + country;
		
		Properties p = messages.get(_key_);
		if (null == p) {
			p = new Properties();
			File f = new File(NLSMessageUtils.class.getResource(File.separator)
					.getFile().toString()
					+ File.separator
					+ "system" //$NON-NLS-1$
					+ File.separator
					+ language
					+ File.separator
					+ country
					+ File.separator
					+ "messages.properties"); //$NON-NLS-1$
			load(f, p);
			messages.put(_key_, p);
		}
		return null == p ? null : p.getProperty(key);
	}

}
