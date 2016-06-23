/**
 *
 */
package com.primeton.paas.console.common;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.primeton.paas.manage.api.impl.spring.SpringApplicationContext;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class ConnectionHelper {
	
	private ConnectionHelper() {
		super();
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dataSourceName)
			throws SQLException {
		ApplicationContext context = SpringApplicationContext.getInstance()
				.getApplicationContext();
		if (null == context) {
			return null;
		}
		ComboPooledDataSource dataSource = (StringUtil.isEmpty(dataSourceName)) 
				? context.getBean(ComboPooledDataSource.class) 
				: context.getBean(dataSourceName, ComboPooledDataSource.class);
		return null == dataSource ? null : dataSource.getConnection();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return getConnection("dataSource"); //$NON-NLS-1$
	}

}
