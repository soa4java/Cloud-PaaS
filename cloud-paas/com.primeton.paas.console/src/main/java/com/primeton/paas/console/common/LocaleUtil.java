/**
 * 
 */
package com.primeton.paas.console.common;

import java.util.Locale;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LocaleUtil {
	
	/**
	 * Default. <br>
	 */
	private LocaleUtil() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static Locale getDefaultLocale() {
		// JVM arguments -Duser.language=zh -Duser.region=CN
		Locale locale = Locale.getDefault();
		if (null == locale) {
			Locale[] locales = Locale.getAvailableLocales();
			return null == locales || locales.length == 0 ? null : locales[0];
		}
		return locale;
	}
	
	/**
	 * 
	 * @param language
	 * @param country
	 * @return
	 */
	public static Locale getLocale(String language, String country) {
		return new Locale(language, country);
	}
	
}
