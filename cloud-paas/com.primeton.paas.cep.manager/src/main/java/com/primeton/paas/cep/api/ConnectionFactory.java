/**
 * 
 */
package com.primeton.paas.cep.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class ConnectionFactory {
	
	private static ConnectionProvider provider = null;
	
	private static ILogger logger = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private ConnectionFactory() {
		super();
	}
	
	public static ConnectionProvider getProvider() {
		synchronized (ConnectionFactory.class) {
			if (provider == null) {
				ServiceLoader<ConnectionProvider> loader = ServiceLoader.load(ConnectionProvider.class);
				if (loader != null) {
					Iterator<ConnectionProvider> iterator = loader.iterator();
					while (iterator.hasNext()) {
						provider = iterator.next();
						if (provider != null) {
							logger.info("Load " + provider + " success.");
							break;
						}
					}
				}
			}
		}
		return provider;
	}
	
	/**
	 * 
	 * @return java.sql.Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return provider == null ? getProvider().getConnection() : provider.getConnection();
	}

}
