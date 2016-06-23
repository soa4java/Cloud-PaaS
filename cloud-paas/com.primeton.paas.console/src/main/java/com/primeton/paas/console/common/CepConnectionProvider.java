/**
 * 
 */
package com.primeton.paas.console.common;

import java.sql.Connection;
import java.sql.SQLException;



//import com.eos.common.connection.DataSourceHelper;
import com.primeton.paas.cep.api.ConnectionProvider;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CepConnectionProvider implements ConnectionProvider {

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.cep.api.ConnectionProvider#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return ConnectionHelper.getConnection();
	}

}
