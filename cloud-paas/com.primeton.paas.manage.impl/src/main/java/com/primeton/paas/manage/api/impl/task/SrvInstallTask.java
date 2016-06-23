/**
 * 
 */
package com.primeton.paas.manage.api.impl.task;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.exception.TaskException;
import com.primeton.paas.manage.api.factory.HostAssignManagerFactory;
import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.Executable;
import com.primeton.paas.manage.api.manager.IHostAssignManager;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IServiceManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 服务安装任务. <br>
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class SrvInstallTask implements Executable {
	
	public static final String TASK_TYPE_SRV_INSTALL = "5";
	
	private String ip; // host
	private String type; // MySQL | Jetty | Memcached | etc.
	
	private IHostManager hostManager = HostManagerFactory.getHostManager();
	private IHostAssignManager assignManager = HostAssignManagerFactory.getManager();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SrvInstallTask.class);
	
	/**
	 * 
	 * @param ip
	 * @param type
	 */
	public SrvInstallTask(String ip, String type) {
		super();
		this.ip = ip;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#getType()
	 */
	public String getType() {
		return TASK_TYPE_SRV_INSTALL;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#init()
	 */
	public void init() {
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#execute()
	 */
	public String execute() throws TaskException {
		long begin = System.currentTimeMillis();
		if (StringUtil.isNotEmpty(ip) && StringUtil.isNotEmpty(type)) {
			Host host = hostManager.get(ip);
			if (host == null) {
				throw new TaskException("Host [" + ip + "] not found.");
			} else if (host.getTypes().contains(type)) {
				String message = "Service [" + type + "] has been installed on host [" + ip + "].";
				logger.info(message);
				return message;
			}
			IServiceManager manager = ServiceManagerFactory.getManager(type);
			try {
				manager.install(ip, assignManager.getInstallTimeout(type));
			} catch (ServiceException e) {
				String message = "Host [" + ip + "] install service [" + type + "] exception.";
				logger.info(message + "error:" + e.getMessage(),e);
				throw new TaskException(message + "error:" + e.getMessage(),e);
			}
		}
		long end = System.currentTimeMillis();
		return "Host [" + ip + "] install service [" + type + "] success. Time spent " + (end-begin)/1000L + "sec.";
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.Executable#clear()
	 */
	public void clear() {
	}

}
