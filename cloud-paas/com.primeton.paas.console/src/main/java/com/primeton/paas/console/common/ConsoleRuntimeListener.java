/**
 * 
 */
package com.primeton.paas.console.common;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.startup.IManageStartup;
import com.primeton.paas.manage.api.startup.StartupFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConsoleRuntimeListener implements ServletContextListener  {
	
	private static ILogger logger = LoggerFactory.getLogger(ConsoleRuntimeListener.class);

	private static IManageStartup startup = StartupFactory.getStartup();

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		getStartup().stop();
		logger.info("PAAS Console Shutdown !");
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		String appConfigPath = getConfigPath();
		// init log4j
		LoggerFactory.init(appConfigPath + File.separator + "log4j-console.xml"); //$NON-NLS-1$
		logger = LoggerFactory.getLogger(ConsoleRuntimeListener.class);
		
		// 启动Management
		getStartup().start(appConfigPath);
		
		// 校验License
		// validateLicense();
		
		logger.info("PAAS Console Startup !");
	}

	/**
	 * 
	 * @return
	 */
	protected static IManageStartup getStartup() {
		return startup = (null == startup) ? StartupFactory.getStartup() : startup;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getConfigPath() {
		String path = getClass().getResource("/").getFile().toString(); //$NON-NLS-1$
		return path	+ ((path.charAt(path.length() - 1) == File.separatorChar) 
						? "" : File.separator) //$NON-NLS-1$
						+ "config" + File.separator + "app"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * 产品License合法性校验. <br>
	 */
	/*
	protected void validateLicense() {
		String licenseFile = getClass().getResource("/").getFile().toString(); //$NON-NLS-1$
		licenseFile = licenseFile	+ ((licenseFile.charAt(licenseFile.length() - 1) == File.separatorChar) 
						? "" : File.separator) //$NON-NLS-1$
						+ "license" + File.separator + "license.xml"; //$NON-NLS-1$ //$NON-NLS-2$
		try {
			LicenseManager.getManager().validate(licenseFile);
			logger.info("Validate Primeton PAAS Product License {0} success. CPU core total number limit {1}.",
					new Object[] { licenseFile, LicenseManager.getManager().getCpuLimits() });
		} catch (Throwable t) {
			logger.error("Validate Primeton PAAS Product License {0} error.", new Object[] { licenseFile }, t);
		}
	}
	*/
	
}
