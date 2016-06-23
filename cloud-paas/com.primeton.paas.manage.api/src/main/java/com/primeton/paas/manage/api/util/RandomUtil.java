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
public class RandomUtil {

	/**
	 * 
	 * @return
	 */
	public static String generateId() {
		String orderId = "";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		int random = (new Random()).nextInt(9999);//
		if (random < 1000)
			random += 1000;
		orderId = sf.format(date) + random;
		return orderId;
	}

}
