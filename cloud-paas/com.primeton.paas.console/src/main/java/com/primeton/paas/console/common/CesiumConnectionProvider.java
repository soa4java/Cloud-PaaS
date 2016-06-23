/**
 *
 */
package com.primeton.paas.console.common;

import java.sql.Connection;
import java.sql.SQLException;

import org.gocom.cloud.cesium.common.api.ConnectionFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CesiumConnectionProvider implements ConnectionFactory {

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.common.api.ConnectionFactory#getConnection()
	 */
	public Connection getConnection() {
		try {
			return ConnectionHelper.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gocom.cloud.cesium.common.api.ConnectionFactory#getConnection4Id()
	 */
	public Connection getConnection4Id() {
		try {
			return ConnectionHelper.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
