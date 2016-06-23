/**
 * 
 */
package com.primeton.paas.cep.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface ConnectionProvider {
	
	/**
	 * 
	 * @return java.sql.Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException;

}
