/**
 *
 */
package com.primeton.paas.console.common;

import java.sql.Connection;
import java.sql.SQLException;

import com.primeton.paas.manage.api.jdbc.IConnectionProvider;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConsoleConnectionProvider implements IConnectionProvider {

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.jdbc.IConnectionProvider#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return ConnectionHelper.getConnection();
	}

}
