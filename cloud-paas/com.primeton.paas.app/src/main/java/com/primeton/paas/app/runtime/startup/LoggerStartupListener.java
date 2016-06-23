/**
 * 
 */
package com.primeton.paas.app.runtime.startup;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.api.log.IUserLogModify;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.model.SystemLogModel;
import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.app.runtime.IRuntimeListener;
import com.primeton.paas.app.runtime.RuntimeEvent;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.spi.log.UserLogModifyFactory;
import com.primeton.paas.app.startup.Server;
import com.primeton.paas.app.util.AppUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LoggerStartupListener implements IRuntimeListener {

	private static ILogger logger = SystemLoggerFactory.getLogger(LoggerStartupListener.class);
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#start(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void start(RuntimeEvent envent) {
		ServerContext serverContext = ServerContext.getInstance();
		int mode = serverContext.getRunMode();
		// Development Mode
		if (AppConstants.RUN_MODE_SIMULATOR == mode) {
			return;
		}
		
		// Product Mode
		UserLogModel userLogModel = ConfigModelManager.getUserLogModel();
		SystemLogModel systemLogModel = ConfigModelManager.getSystemLogModel();
		
		String configPath = serverContext.getConfigDirPath();
		final String userLogConfig = configPath + File.separator + AppConstants.LOG_CONFIG_FILE_NAME_USER;
		final String systemLogConfig = configPath + File.separator + AppConstants.LOG_CONFIG_FILE_NAME_SYSTEM;
		if (userLogModel != null) {
			try {
				AppUtil.updateUserLoggerLevel(userLogModel.getLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE), userLogConfig);
				logger.info("Update local file {" + userLogConfig + "}, User Logger level change to {" + userLogModel.getLogLevel(UserLogModel.DEFAULT_USERLOG_TYPE) + "}");
			} catch (Throwable t) {
				if(logger.isErrorEnabled()) {
					logger.error(t);
				}
			}
		}
		
		if (systemLogModel != null) {
			try {
				AppUtil.updateSystemLoggerLevel(systemLogModel.getLogLevel(), systemLogConfig);
				logger.info("Update local file {" + systemLogConfig + "}, System Logger level change to {" + systemLogModel.getLogLevel() + "}");
			} catch (Throwable t) {
				if(logger.isErrorEnabled()) {
					logger.error(t);
				}
			}
		}
		
		Server.getInstance().initLogger(systemLogModel != null, userLogModel != null);
		
		// Customs
		List<IUserLogModify> list = UserLogModifyFactory.getModifies();
		logger.info("Had load "+list.size()+" IUserLogModify implements.Do modify.");
		Map<String,String> userlogs = userLogModel.getUserLogs();
		for (IUserLogModify userLog : list){
			Set<String> keySet = userlogs.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) { 
			   String type = it.next();
			   userLog.doModifyLevel(type, userlogs.get(type)); 
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.app.runtime.IRuntimeListener#stop(com.primeton.paas.app.runtime.RuntimeEvent)
	 */
	public void stop(RuntimeEvent envent) {
	}

}
