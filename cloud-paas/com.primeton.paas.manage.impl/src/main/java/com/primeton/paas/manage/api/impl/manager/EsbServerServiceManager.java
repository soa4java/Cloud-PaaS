/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.mqclient.api.StringMessage;
import org.gocom.cloud.cesium.mqclient.api.WriteFileMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.ServiceUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.impl.util.VelocityUtil;
import com.primeton.paas.manage.api.manager.IEsbServerServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.EsbServerService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbServerServiceManager extends DefaultServiceManager implements
		IEsbServerServiceManager {

	/**
	 * TYPE. <br>
	 */
	public static final String TYPE = EsbServerService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(EsbServerServiceManager.class);
	
	private static Map<String, String> drivers = new HashMap<String, String>();
	
	static {
		drivers.put(EsbServerService.DATABASE_MySQL, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
		drivers.put(EsbServerService.DATABASE_ORACLE, "oracle.jdbc.driver.OracleDriver"); //$NON-NLS-1$
		drivers.put(EsbServerService.DATABASE_DB2, "com.ibm.db2.jcc.DB2Driver"); //$NON-NLS-1$
	}

	/**
	 * Default. <br>
	 */
	public EsbServerServiceManager() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#getType()
	 */
	public String getType() {
		return TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#add(com.primeton.paas.manage.api.model.IService, java.lang.String)
	 */
	public <T extends IService> T add(T service, String clusterId)
			throws ServiceException {
		return create(service, clusterId);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IEsbServerServiceManager#add(com.primeton.paas.manage.api.service.EsbServerService, java.lang.String, int)
	 */
	public List<EsbServerService> add(EsbServerService service,
			String clusterId, int number) throws ServiceException {
		List<EsbServerService> services = new ArrayList<EsbServerService>();
		if (null == service || StringUtil.isEmpty(clusterId) || number <= 0) {
			return services;
		}
		for (int i=0; i<number; i++) {
			EsbServerService clone = ServiceUtil.copy(service);
			EsbServerService instance = add(clone, clusterId);
			if (null != instance) {
				services.add(instance);
			}
		}
		return services;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		EsbServerService service = getServiceQuery().get(id, EsbServerService.class);
		if (null == service) {
			logger.warn("Can not start EsbServer {0}, service not exists.", new Object[] { id });
			return;
		}
		beforeStart(service);
		// To start
		super.start(id);
	}

	/**
	 * @param service
	 */
	protected void beforeStart(EsbServerService service) {
		if (null == service) {
			return;
		}
		// 修改 EOS user-config.xml 文件
		{
			String content = getUserConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbServer/server/EOS/_srv/config/user-config.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbServer service {0} configuration file user-config.xml on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
		
		// 修改 fts-server-config.xml 文件
		{
			String content = getFtsServerConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbServer/server/EOS/_srv/config/fts-server-config.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbServer service {0} configuration file fts-server-config.xml on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
		// 修改 esb-mq-config.xml 文件
		{
			String content = getMqConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbServer/server/EOS/_srv/config/esb-mq-config.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbServer service {0} configuration file esb-mq-config.xml on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * @param service
	 * @return
	 */
	private String getUserConfig(EsbServerService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("DatabaseType", StringUtil.isEmpty(service.getDatabaseType()) //$NON-NLS-1$
				? EsbServerService.DATABASE_MySQL : service.getDatabaseType());
		context.put("C3p0Url", service.getC3p0Url()); //$NON-NLS-1$
		context.put("C3p0UserName", service.getC3p0UserName()); //$NON-NLS-1$
		context.put("C3p0Password", service.getC3p0Password()); //$NON-NLS-1$
		context.put("C3p0PoolSize", service.getC3p0PoolSize()); //$NON-NLS-1$
		context.put("C3p0MaxPoolSize", service.getC3p0MaxPoolSize()); //$NON-NLS-1$
		context.put("C3p0MinPoolSize", service.getC3p0MinPoolSize()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbServerServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/server/user-config.xml"); //$NON-NLS-1$
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
	 * @param dbType
	 * @return
	 */
	private static String getDriver(String dbType) {
		dbType = null == dbType ? EsbServerService.DATABASE_MySQL : dbType;
		String driver = drivers.get(dbType);
		if (null == driver) {
			for (String type : drivers.keySet()) {
				if (dbType.equalsIgnoreCase(type)) {
					return drivers.get(type);
				}
			}
		}
		return driver;
	}

	/**
	 * @param service
	 * @return
	 */
	private String getFtsServerConfig(EsbServerService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("DatabaseType", StringUtil.isEmpty(service.getDatabaseType()) //$NON-NLS-1$
				? EsbServerService.DATABASE_MySQL : service.getDatabaseType());
		context.put("C3p0Url", service.getC3p0Url()); //$NON-NLS-1$
		context.put("C3p0UserName", service.getC3p0UserName()); //$NON-NLS-1$
		context.put("C3p0Password", service.getC3p0Password()); //$NON-NLS-1$
		context.put("C3p0PoolSize", service.getC3p0PoolSize()); //$NON-NLS-1$
		context.put("C3p0MaxPoolSize", service.getC3p0MaxPoolSize()); //$NON-NLS-1$
		context.put("C3p0MinPoolSize", service.getC3p0MinPoolSize()); //$NON-NLS-1$
		
		String driver = getDriver(service.getDatabaseType());
		context.put("C3P0DriverClass", driver); //$NON-NLS-1$
		context.put("localhost", service.getIp()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		context.put("jmxPort", service.getJmxPort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbServerServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/server/fts-server-config.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "fts-server-config.xml"); //$NON-NLS-1$
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
	 * @param service
	 * @return
	 */
	private String getMqConfig(EsbServerService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("port", service.getPort()); //$NON-NLS-1$
		context.put("managePort", service.getManagePort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbServerServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/server/esb-mq-config.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "esb-mq-config.xml"); //$NON-NLS-1$
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
