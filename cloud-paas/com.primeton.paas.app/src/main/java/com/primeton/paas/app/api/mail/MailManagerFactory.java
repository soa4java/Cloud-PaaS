/**
 * 
 */
package com.primeton.paas.app.api.mail;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.mail.CloudMailManager;
import com.primeton.paas.app.mail.SimulatorMailManager;


/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MailManagerFactory {
	
	private static IMailManager manager;

	private MailManagerFactory() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IMailManager getManager() {
		if (null == manager) {
			manager = ServerContext.getInstance().getRunMode() == AppConstants.RUN_MODE_CLOUD 
					? new CloudMailManager()
					: new SimulatorMailManager();
		}
		return manager;
	}

}
