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
import com.primeton.paas.manage.api.manager.IEsbSAMServiceManager;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.EsbSAMService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbSAMServiceManager extends DefaultServiceManager 
		implements IEsbSAMServiceManager {

	public static final String TYPE = EsbSAMService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(EsbSAMServiceManager.class);
	
	private static Map<String, String> drivers = new HashMap<String, String>();
	
	static {
		drivers.put(EsbSAMService.DATABASE_MySQL, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
		drivers.put(EsbSAMService.DATABASE_ORACLE, "oracle.jdbc.driver.OracleDriver"); //$NON-NLS-1$
		drivers.put(EsbSAMService.DATABASE_DB2, "com.ibm.db2.jcc.DB2Driver"); //$NON-NLS-1$
	}
	
	/**
	 * Default. <br>
	 */
	public EsbSAMServiceManager() {
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
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		EsbSAMService service = getServiceQuery().get(id, EsbSAMService.class);
		if (null == service) {
			logger.warn("Can not start EsbSSM {0}, service not exists.", new Object[] { id });
			return;
		}
		beforeStart(service);
		// To start
		super.start(id);
	}
	
	/**
	 * @param service
	 */
	protected void beforeStart(EsbSAMService service) {
		if (null == service) {
			return;
		}
		// 修改Tomcat ${catlina.home}/conf/server.xml文件
		{
			String content = getServerConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbSAM/apache-tomcat-6.0.44/conf/server.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbSAM service {0} configuration file server.xml on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
		// 修改Tomcat ${catlina.home}/conf/context.xml文件
		{
			String content = getContextConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbSAM/apache-tomcat-6.0.44/conf/context.xml"); //$NON-NLS-1$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbSAM service {0} configuration file context.xml on host {1}.", 
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
	private String getContextConfig(EsbSAMService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("username", service.getJdbcUser()); //$NON-NLS-1$
		context.put("password", service.getJdbcPassword()); //$NON-NLS-1$
		context.put("driverClass", getDriver(service.getDatabaseType())); //$NON-NLS-1$
		context.put("jdbcUrl", service.getJdbcUrl()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbSAMServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/sam/context.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "context.xml"); //$NON-NLS-1$
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
	private String getServerConfig(EsbSAMService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("shutdownPort", service.getShutdownPort()); //$NON-NLS-1$
		context.put("ajpPort", service.getAjpPort()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbSAMServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/sam/server.xml"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "server.xml"); //$NON-NLS-1$
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
		dbType = null == dbType ? EsbSAMService.DATABASE_MySQL : dbType;
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
	
}
