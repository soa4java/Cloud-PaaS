/**
 * 
 */
package com.primeton.paas.openapi.httpin.startup;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.primeton.paas.openapi.admin.ServerContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class StartupService implements ServletContextListener {
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		initServerContext(context);

		Server server = Server.getInstance();
		server.start();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		Server server=Server.getInstance();
		server.stop();
	}

	private void initServerContext(ServletContext context) {
		ServerContext scontext = ServerContext.getInstance();
		String webContextPath = context.getServletContextName(); 
		String warRealPath = context.getRealPath("/"); //$NON-NLS-1$
		scontext.setWebContextPath(webContextPath);
		scontext.setWarRealPath(warRealPath);
		
		File warFile = new File(warRealPath);
		String warFileName = warFile.getName();

		String warName = null;
		if (warFileName.toLowerCase().endsWith(".war")) { //$NON-NLS-1$
			warName = warFileName.substring(0, warFileName.lastIndexOf(".war")); //$NON-NLS-1$
		} else {
			warName = warFileName;
		}
		scontext.setWarName(warName);
		scontext.getConfigDirPath();
	}
	
}
