/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.StringMessage;
import org.gocom.cloud.cesium.mqclient.api.WriteFileMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.impl.util.VelocityUtil;
import com.primeton.paas.manage.api.manager.IJobCtrlServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.JobCtrlService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * JobCtrl服务管理器实现. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class JobCtrlServiceManager extends DefaultServiceManager 
		implements IJobCtrlServiceManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(JobCtrlServiceManager.class);
	
	public static final String TYPE = JobCtrlService.TYPE;
	
	/**
	 * Default. <br>
	 */
	public JobCtrlServiceManager() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		JobCtrlService service = getServiceQuery().get(id, JobCtrlService.class);
		if (null == service) {
			logger.warn("Can not start JobCtrl {0}, service not exists.", new Object[] { id });
			return;
		}
		beforeStart(service);
		// To start
		super.start(id);
	}
	
	/**
	 * Update configuration. <br>
	 * 
	 * @param service JobCtrl service instance
	 */
	protected void beforeStart(JobCtrlService service) {
		if (null == service) {
			return;
		}
		// 修改EOS用户配置文件:数据源配置
		String userConfig = getUserConfig(service);
		WriteFileMessage message = new WriteFileMessage(userConfig, SystemVariables.getProgramsHome() 
				+ "/JobCtrl/apache-tomcat-6.0.44/webapps/primetonJobCtrl/WEB-INF/_srv/config/user-config.xml"); //$NON-NLS-1$
		message.setEncoding(StringUtil.CHARSET_UTF8);
		message.setNeedResponse(true);
		
		for (int i=0; i<5; i++) {
			try {
				StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
				if (null != result && result.isOk()) {
					logger.info(result.getBody());
					break;
				}
				logger.error(null == result ? StringUtil.format("No response while send modify JobCtrl service {0} configuration file user-config.xml on host {1}.", new Object[] { service.getId(), service.getIp() }) : result.getBody());
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		
		// 修改EOS应用domain配置文件
		String domain = getDomain(service);
		WriteFileMessage message1 = new WriteFileMessage(domain, SystemVariables.getProgramsHome() 
				+ "/JobCtrl/apache-tomcat-6.0.44/webapps/primetonJobCtrl/WEB-INF/_srv/domain/domain.xml"); //$NON-NLS-1$
		message1.setEncoding(StringUtil.CHARSET_UTF8);
		message1.setNeedResponse(true);
		
		for (int i=0; i<5; i++) {
			try {
				StringMessage result = SendMessageUtil.sendMessage(message1, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
				if (null != result && result.isOk()) {
					logger.info(result.getBody());
					break;
				}
				logger.error(null == result ? StringUtil.format("No response while send modify JobCtrl service {0} configuration file domain.xml on host {1}.", new Object[] { service.getId(), service.getIp() }) : result.getBody());
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		
		// 修改Flex services-config.xml配置文件
		String servicesConfig = getServicesConfig(service);
		WriteFileMessage message2 = new WriteFileMessage(servicesConfig, SystemVariables.getProgramsHome() 
				+ "/JobCtrl/apache-tomcat-6.0.44/webapps/primetonJobCtrl/WEB-INF/flex/services-config.xml"); //$NON-NLS-1$
		message2.setEncoding(StringUtil.CHARSET_UTF8);
		message2.setNeedResponse(true);
		
		for (int i=0; i<5; i++) {
			try {
				StringMessage result = SendMessageUtil.sendMessage(message2, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
				if (null != result && result.isOk()) {
					logger.info(result.getBody());
					break;
				}
				logger.error(null == result ? StringUtil.format("No response while send modify JobCtrl service {0} configuration file services-config.xml on host {1}.", new Object[] { service.getId(), service.getIp() }) : result.getBody());
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		
		// 修改Scheduler config.properties配置文件
		String schedulerConfig = getSchedulerConfig(service);
		WriteFileMessage message3 = new WriteFileMessage(schedulerConfig, SystemVariables.getProgramsHome() 
				+ "/JobCtrl/apache-tomcat-6.0.44/webapps/scheduler/WEB-INF/classes/config/config.properties"); //$NON-NLS-1$
		message3.setEncoding(StringUtil.CHARSET_UTF8);
		message3.setNeedResponse(true);
		
		for (int i=0; i<5; i++) {
			try {
				StringMessage result = SendMessageUtil.sendMessage(message3, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
				if (null != result && result.isOk()) {
					logger.info(result.getBody());
					break;
				}
				logger.error(null == result ? StringUtil.format("No response while send modify JobCtrl service {0} configuration file config.properties on host {1}.", new Object[] { service.getId(), service.getIp() }) : result.getBody());
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		
		// 修改Scheduler SchedulerFailOverConfig.properties配置文件
		String schedulerConfig2 = getSchedulerConfig2(service);
		WriteFileMessage message4 = new WriteFileMessage(schedulerConfig2, SystemVariables.getProgramsHome() 
				+ "/JobCtrl/apache-tomcat-6.0.44/webapps/scheduler/WEB-INF/classes/config/SchedulerFailOverConfig.properties"); //$NON-NLS-1$
		message4.setEncoding(StringUtil.CHARSET_UTF8);
		message4.setNeedResponse(true);
		
		for (int i=0; i<5; i++) {
			try {
				StringMessage result = SendMessageUtil.sendMessage(message4, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
				if (null != result && result.isOk()) {
					logger.info(result.getBody());
					break;
				}
				logger.error(null == result ? StringUtil.format("No response while send modify JobCtrl service {0} configuration file SchedulerFailOverConfig.properties on host {1}.", new Object[] { service.getId(), service.getIp() }) : result.getBody());
			} catch (MessageException e) {
				logger.error(e);
			}
		}
		logger.info("Finish update jobctrl service {0} configurations.", new Object[] { service.getId() });
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getUserConfig(JobCtrlService service) {
		if (null == service) {
			return null;
		}
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("DatabaseType", service.getDatabaseType()); //$NON-NLS-1$
		context.put("C3p0Url", service.getC3p0Url()); //$NON-NLS-1$
		context.put("C3p0UserName", service.getC3p0UserName()); //$NON-NLS-1$
		context.put("C3p0Password", service.getC3p0Password()); //$NON-NLS-1$
		context.put("C3p0PoolSize", service.getC3p0PoolSize()); //$NON-NLS-1$
		context.put("C3p0MaxPoolSize", service.getC3p0MaxPoolSize()); //$NON-NLS-1$
		context.put("C3p0MinPoolSize", service.getC3p0MinPoolSize()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = JobCtrlServiceManager.class.getResourceAsStream("/META-INF/templates/JobCtrl/governor/user-config.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "user-config.xml"); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getDomain(JobCtrlService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("localhost", service.getIp()); //$NON-NLS-1$
		context.put("adminPort", service.getAdminPort()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = JobCtrlServiceManager.class.getResourceAsStream("/META-INF/templates/JobCtrl/governor/domain.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "domain.xml"); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getServicesConfig(JobCtrlService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("localhost", service.getIp()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = JobCtrlServiceManager.class.getResourceAsStream("/META-INF/templates/JobCtrl/governor/services-config.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "services-config.xml"); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getSchedulerConfig(JobCtrlService service) {
		if (null == service) {
			return null;
		}
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("saveDays", service.getSchedulerSaveDays()); //$NON-NLS-1$
		context.put("timeRound", service.getSchedulerTimeRound()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = JobCtrlServiceManager.class.getResourceAsStream("/META-INF/templates/JobCtrl/scheduler/config.properties"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "config.properties"); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getSchedulerConfig2(JobCtrlService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("localhost", service.getIp()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = JobCtrlServiceManager.class.getResourceAsStream("/META-INF/templates/JobCtrl/scheduler/SchedulerFailOverConfig.properties"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "SchedulerFailOverConfig.properties"); //$NON-NLS-1$
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
}
