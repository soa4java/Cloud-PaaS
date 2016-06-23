/**
 * 
 */
package com.primeton.paas.manage.api.config;

import java.util.Iterator;

import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class AppConfigManagerFactory {

	/**
	 * Default. <br>
	 */
	private AppConfigManagerFactory() {
	}
	
	private static IDataSourceConfigManager dataSourceConfigManager;
	private static IVariableConfigManager variableConfigManager;
	private static IUserLoggerConfigManager userLoggerConfigManager;
	
	private static IEosAppConfigManager eosAppConfigManager;

	/**
	 * 
	 * @return
	 */
	public static IDataSourceConfigManager getDataSourceConfigManager() {
		if (null == dataSourceConfigManager) {
			ServiceExtensionLoader<IDataSourceConfigManager> loader = ServiceExtensionLoader
					.load(IDataSourceConfigManager.class);
			if (loader != null) {
				Iterator<IDataSourceConfigManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					dataSourceConfigManager = iterator.next();
					break;
				}
			}
		}
		return dataSourceConfigManager;
	}

	/**
	 * 
	 * @return
	 */
	public static IVariableConfigManager getVariableConfigManager() {
		if (null == variableConfigManager) {
			ServiceExtensionLoader<IVariableConfigManager> loader = ServiceExtensionLoader
					.load(IVariableConfigManager.class);
			if (loader != null) {
				Iterator<IVariableConfigManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					variableConfigManager = iterator.next();
					break;
				}
			}
		}
		return variableConfigManager;
	}

	/**
	 * 
	 * @return
	 */
	public static IUserLoggerConfigManager getUserLoggerConfigManager() {
		if (null == userLoggerConfigManager) {
			ServiceExtensionLoader<IUserLoggerConfigManager> loader = ServiceExtensionLoader
					.load(IUserLoggerConfigManager.class);
			if (loader != null) {
				Iterator<IUserLoggerConfigManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					userLoggerConfigManager = iterator.next();
					break;
				}
			}
		}
		return userLoggerConfigManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static IEosAppConfigManager getEosAppConfigManager() {
		if (null == eosAppConfigManager) {
			ServiceExtensionLoader<IEosAppConfigManager> loader = ServiceExtensionLoader
					.load(IEosAppConfigManager.class);
			if (null != loader) {
				Iterator<IEosAppConfigManager> iterator = loader.iterator();
				while (iterator.hasNext()) {
					eosAppConfigManager = iterator.next();
					break;
				}
			}
		}
		return eosAppConfigManager;
	}

}
