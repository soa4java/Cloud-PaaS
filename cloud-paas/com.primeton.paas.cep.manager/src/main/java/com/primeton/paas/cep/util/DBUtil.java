/**
 * 
 */
package com.primeton.paas.cep.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DBUtil {
	
	private static ILogger logger = LoggerFactory.getLogger(DBUtil.class);

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(conn);
	}
	
}
