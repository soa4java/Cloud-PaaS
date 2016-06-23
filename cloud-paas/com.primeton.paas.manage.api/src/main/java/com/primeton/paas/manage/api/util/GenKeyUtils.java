/**
 * 
 */
package com.primeton.paas.manage.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class GenKeyUtils {

	public static String lastGlobalKey = null;

	/**
	 * 
	 * @return
	 */
	public static synchronized String genKey() {
		String tempKey = genKeyOnce();
		if (lastGlobalKey == null || !tempKey.equals(lastGlobalKey)) {
			lastGlobalKey = tempKey;
			return tempKey;
		} else {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			lastGlobalKey = genKeyOnce();
			return lastGlobalKey;
		}
	}

	/**
	 * 
	 * @return
	 */
	private static String genKeyOnce() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		String date = sdf.format(dt);
		String key = "";
		if (date.length() == 16) {
			key = date.substring(0, date.length());
		} else {
			key = date.substring(0, date.length() - 1);

		}
		return key;
	}

	/**
	 * 
	 * @return
	 */
	public static String random() {
		String result = "";
		int[] seed = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		Random ran = new Random();
		for (int i = 0; i < seed.length; i++) {
			int j = ran.nextInt(seed.length - i);
			result = result + seed[j];
			seed[j] = seed[seed.length - 1 - i];
		}
		return result;
	}

	/**
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

}
