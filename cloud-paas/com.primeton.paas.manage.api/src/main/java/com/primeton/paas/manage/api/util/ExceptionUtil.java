/**
 * 
 */
package com.primeton.paas.manage.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ExceptionUtil {
	
	/**
	 * 
	 * @param cause
	 * @return
	 */
	public static String getCauseMessage(Throwable cause) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		cause.printStackTrace(printWriter);
		String message = stringWriter.toString();
		return message;
	}

}
