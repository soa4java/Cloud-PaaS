/**
 * 
 */
package com.primeton.paas.manage.api.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IConnectionProvider {

	/**
	 * Get JDBC Connection <br>
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;
	
	
}
