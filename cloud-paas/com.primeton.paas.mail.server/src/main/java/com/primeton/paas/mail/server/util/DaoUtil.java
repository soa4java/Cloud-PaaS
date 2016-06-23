/**
 * 
 */
package com.primeton.paas.mail.server.util;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.primeton.paas.mail.server.model.MailInfo;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DaoUtil {

	public final static String ENCODING = "UTF-8";

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static MailInfo convertToMailInfo(ResultSet rs) throws SQLException {
		MailInfo mi = new MailInfo();

		mi.setMailId(rs.getString("MAILID")); //$NON-NLS-1$
		mi.setAppName(rs.getString("APPNAME")); //$NON-NLS-1$
		mi.setMailServerHost(rs.getString("MAILSERVERHOST")); //$NON-NLS-1$
		mi.setMailServerPort(rs.getString("MAILSERVERPORT")); //$NON-NLS-1$
		mi.setValidate(rs.getBoolean("VALIDATE")); //$NON-NLS-1$
		mi.setIfBack(rs.getBoolean("IFBACK")); //$NON-NLS-1$
		mi.setUsername(rs.getString("USERNAME")); //$NON-NLS-1$
		mi.setPassword(rs.getString("PASSWORD")); //$NON-NLS-1$
		mi.setSendFrom(rs.getString("SEND_FROM")); //$NON-NLS-1$

		String strTo = rs.getString("SEND_TO"); //$NON-NLS-1$
		if (strTo != null) {
			List<String> to = Arrays.asList(strTo.split(",")); //$NON-NLS-1$
			mi.setSendTo(to);
		}

		String strCc = rs.getString("SEND_CC"); //$NON-NLS-1$
		if (strCc != null) {
			List<String> cc = Arrays.asList(strCc.split(",")); //$NON-NLS-1$
			mi.setSendCc(cc);
		}

		mi.setSubject(rs.getString("SUBJECT")); //$NON-NLS-1$
		mi.setContentType(rs.getString("CONTENTTYPE")); //$NON-NLS-1$
		mi.setContent(rs.getString("CONTENT")); //$NON-NLS-1$

		String strAttId = rs.getString("ATTACHMENTSID"); //$NON-NLS-1$
		if (strAttId != null) {
			List<String> attId = Arrays.asList(strAttId.split(",")); //$NON-NLS-1$
			mi.setAttachmentsId(attId);
		}

		mi.setCreateDate(rs.getDate("CREATEDATE")); //$NON-NLS-1$
		mi.setStatus(rs.getString("STATUS")); //$NON-NLS-1$
		mi.setExceptionCode(rs.getString("EXCEPTIONCODE")); //$NON-NLS-1$
		mi.setExceptionMessage(rs.getString("EXCEPTIONMESSAGE")); //$NON-NLS-1$
		return mi;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static String listToString(List<String> list) {
		String str = null;
		for (int i = 0; i < list.size(); i++) {
			if (str == null) {
				str = list.get(i);
			} else {
				str = str + "," + list.get(i);
			}
		}
		return str;
	}

	/**
	 * 
	 * @param con
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection con, PreparedStatement stmt,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {

		}
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
