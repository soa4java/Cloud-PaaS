/**
 * 
 */
package com.primeton.paas.cardbin.server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.primeton.paas.cardbin.spi.CardBinRuntimeException;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JdbcUtils {

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static interface IPreparedStatementSetter {
		
		/**
		 * 
		 * @param pstmt
		 * @throws SQLException
		 */
		void execute(PreparedStatement pstmt) throws SQLException;
		
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static interface IResultSetGetter {
		Object[] execute(ResultSet rs) throws SQLException;
	}

	/**
	 * 
	 * @param sql
	 * @param setter
	 * @return
	 */
	public static Object execute(String sql, IPreparedStatementSetter setter) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if (setter != null) {
				setter.execute(pstmt);
			}
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeStatement(pstmt);
			closeConnection(conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @param setter
	 * @param getter
	 * @return
	 */
	public static Object executeQuery(String sql, IPreparedStatementSetter setter, IResultSetGetter getter) {
		if (getter == null) {
			throw new NullPointerException("The rs-getter is null.");
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if (setter != null) {
				setter.execute(pstmt);
			}
			rs = pstmt.executeQuery();
			return getter.execute(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConnection(conn);
		}
	}

	/**
	 * 
	 * @return
	 */
	private static Connection getConnection() {
		try {
			return ConnectionFactory.getConnection();
		} catch (Throwable e) {
			throw new CardBinRuntimeException(e);
		}
	}

	/**
	 * 
	 * @param conn
	 */
	private static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// ignored
			}
		}
	}

	/**
	 * 
	 * @param stmt
	 */
	private static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// ignored
			}
		}
	}

	/**
	 * 
	 * @param rs
	 */
	private static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// ignored
			}
		}
	}
	
}
