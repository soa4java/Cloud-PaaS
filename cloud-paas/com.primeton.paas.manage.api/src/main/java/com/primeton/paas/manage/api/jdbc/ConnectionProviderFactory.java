/**
 * 
 */
package com.primeton.paas.manage.api.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ConnectionProviderFactory {
	
	private static IConnectionProvider provider = null;

	private ConnectionProviderFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IConnectionProvider getConnectionProvider() {
		if (null == provider) {
			ServiceLoader<IConnectionProvider> loader = ServiceLoader
					.load(IConnectionProvider.class);
			if (loader != null) {
				Iterator<IConnectionProvider> iterator = loader.iterator();
				while (iterator.hasNext()) {
					return provider = iterator.next();
				}
			}
		}
		return provider;
	}
	
	private static synchronized void init() {
		if (provider == null) {
			provider = getConnectionProvider();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (provider == null) {
			init();
		}
		return null == provider ? null : provider.getConnection();
	}
	
}
