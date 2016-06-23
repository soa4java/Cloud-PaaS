/**
 * 
 */
package com.primeton.paas.app.util;

import java.util.List;
import java.util.regex.Pattern;

import com.primeton.paas.app.api.mail.IMail;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 */
public class EmailUtil {

	private static Pattern EMAIL_PATTERN = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	/**
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean validateMailParameter(IMail mail) {
		if (!isMail(mail.getFrom())) {
			return false;
		}
		if (!isMail(mail.getTo())) {
			return false;
		}
		if (!checkNull(mail.getSubject())) {
			return false;
		}
		if (!checkNull(mail.getUsername())) {
			return false;
		}
		if (!checkNull(mail.getPassword())) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param mailList
	 * @return
	 */
	public static boolean isMail(List<String> mailList) {
		for (String email : mailList) {
			if (!EMAIL_PATTERN.matcher(email).matches()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isMail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkNull(String str) {
		return (null != str) && (!"".equals(str.trim()));
	}

}
