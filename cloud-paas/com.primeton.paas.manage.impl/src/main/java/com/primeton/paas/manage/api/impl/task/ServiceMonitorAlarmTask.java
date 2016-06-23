/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.IServiceWarnStrategyManager;
import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceWarnStrategyManagerFactory;
import com.primeton.paas.manage.api.manager.Executable;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceMonitorAlarmTask implements Executable {

	public static final String TYPE = "9";

	private static ILogger logger = ManageLoggerFactory
			.getLogger(ServiceMonitorAlarmTask.class);

	private static IServiceWarnStrategyManager serviceWarnManager = ServiceWarnStrategyManagerFactory
			.getManager();

	private long timeout;
	private ServiceWarnStrategy strategy;

	public ServiceMonitorAlarmTask(ServiceWarnStrategy strategy, long timeout) {
		this.strategy = strategy;
		this.timeout = timeout;
	}

	public String getType() {
		return TYPE;
	}

	public void init() {
	}

	public String execute() throws TaskException {
		String retMsg = null;
		if (strategy == null || strategy.getClusterId() == null) {
			retMsg = "Service Alarm cancelled, parameter is null.";
			logger.error(retMsg);
			throw new TaskException(retMsg);
		}
		String clusterId = strategy.getClusterId();
		logger.info("Service Alarm clusterId:" + clusterId + ",timeout:"
				+ timeout);
		ServiceWarnStrategy sws = serviceWarnManager.get(clusterId);
		String type = sws.getAlarmType();

		if (ServiceWarnStrategy.MAIL_ALARM.equals(type)) {
			
		} else if (ServiceWarnStrategy.SMS_ALARM.equals(type)) {
			
		} else {
			
		}
		return retMsg;
	}

	public void clear() {
	}
	
}
