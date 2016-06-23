/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.utils.ThreadUtil;
import org.gocom.cloud.cesium.mqclient.api.CommandResultMessage;
import org.gocom.cloud.cesium.mqclient.api.StringMessage;
import org.gocom.cloud.cesium.mqclient.api.WriteFileMessage;
import org.gocom.cloud.cesium.mqclient.api.exception.MessageException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.impl.util.VelocityUtil;
import com.primeton.paas.manage.api.manager.IEsbSSMServiceManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.EsbSSMService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * ESB SSM 服务管理器实现. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class EsbSSMServiceManager extends DefaultServiceManager 
		implements IEsbSSMServiceManager {

	public static final String TYPE = EsbSSMService.TYPE;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(EsbSSMServiceManager.class);
	
	private static Map<String, String> drivers = new HashMap<String, String>();
	
	static {
		drivers.put(EsbSSMService.DATABASE_MySQL, "com.mysql.jdbc.Driver"); //$NON-NLS-1$
		drivers.put(EsbSSMService.DATABASE_ORACLE, "oracle.jdbc.driver.OracleDriver"); //$NON-NLS-1$
		drivers.put(EsbSSMService.DATABASE_DB2, "com.ibm.db2.jcc.DB2Driver"); //$NON-NLS-1$
	}
	
	/**
	 * Default. <br>
	 */
	public EsbSSMServiceManager() {
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
		if (null == service || StringUtil.isEmpty(clusterId)) {
			return null;
		}
		if (StringUtil.isEmpty(service.getIp())) {
			throw new ServiceException(StringUtil.format("Can not add service {0} to cluster {1}, ip is empty.", 
					new Object[] { service, clusterId }));
		}
		// check host
		Host host = getHostManager().get(service.getIp());
		if (null == host) {
			throw new ServiceException(StringUtil.format("Can not add service {0} to cluster {1}, host {2} not exists.", 
					new Object[] { service, clusterId, service.getIp() }));
		}
		// install SSM if need
		List<String> types = host.getTypes();
		if (null == types || !types.contains(EsbSSMService.TYPE)) {
			install(service.getIp(), getHostAssignManager().getInstallTimeout(EsbSSMService.TYPE));
		}
		// create SSM instance
		T instance = super.add(service, clusterId);
		Map<String, String> args = new HashMap<String, String>();
		args.put("serviceId", instance.getId()); //$NON-NLS-1$
		
		for (int i=0; i<5; i++) {
			try {
				CommandResultMessage message = SendMessageUtil.sendMessage(instance.getIp(), 
						SystemVariables.getScriptPath(EsbSSMService.TYPE, SH_CREATE), args, true);
				if (null != message && null != message.getBody() && message.getBody().getSuccess()) {
					logger.info("Execute create.sh to create ESB SSM instance {0} on host {1} success.", //$NON-NLS-1$
							new Object[] { instance.getId(), instance.getIp() });
					break;
				}
				ThreadUtil.sleep(1000L * 5);
			} catch (MessageException e) {
				logger.error("Execute create.sh to create ESB SSM instance {0} on host {1} error.", //$NON-NLS-1$ 
						new Object[] { instance.getId(), instance.getIp() }, e);
			}
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#destroy(java.lang.String)
	 */
	public void destroy(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		super.destroy(id);
		EsbSSMService instance = getServiceQuery().get(id, EsbSSMService.class);
		if (null != instance) {
			Map<String, String> args = new HashMap<String, String>();
			args.put("serviceId", instance.getId()); //$NON-NLS-1$
			for (int i=0; i<5; i++) {
				try {
					CommandResultMessage message = SendMessageUtil.sendMessage(instance.getIp(), 
							SystemVariables.getScriptPath(EsbSSMService.TYPE, SH_DESTROY), args, true);
					if (null != message && null != message.getBody() && message.getBody().getSuccess()) {
						logger.info("Execute destroy.sh to destroy ESB SSM instance {0} on host {1} success.", //$NON-NLS-1$
								new Object[] { instance.getId(), instance.getIp() });
						break;
					}
					ThreadUtil.sleep(1000L * 5);
				} catch (MessageException e) {
					logger.error("Execute destroy.sh to destroy ESB SSM instance {0} on host {1} error.", //$NON-NLS-1$ 
							new Object[] { instance.getId(), instance.getIp() }, e);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultServiceManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		EsbSSMService service = getServiceQuery().get(id, EsbSSMService.class);
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
	protected void beforeStart(EsbSSMService service) {
		if (null == service) {
			return;
		}
		// 修改 ssm.user.properties 配置文件
		{
			String content = getSsmUserConfig(service);
			WriteFileMessage message = new WriteFileMessage(content, SystemVariables.getProgramsHome() 
					+ "/EsbSSM/ssm/instances/" + service.getId() + "/conf/ssm.user.properties"); //$NON-NLS-1$ //$NON-NLS-2$
			message.setEncoding(StringUtil.CHARSET_UTF8);
			message.setNeedResponse(true);
			
			for (int i=0; i<5; i++) {
				try {
					StringMessage result = SendMessageUtil.sendMessage(message, service.getIp(), 1000L * 60 * 5); //$NON-NLS-1$
					if (null != result && result.isOk()) {
						logger.info(result.getBody());
						break;
					}
					logger.error(null == result ? StringUtil.format("No response while send modify EsbSSM service {0} configuration file ssm.user.properties on host {1}.", 
							new Object[] { service.getId(), service.getIp() }) : result.getBody());
				} catch (MessageException e) {
					logger.error(e);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	private String getSsmUserConfig(EsbSSMService service) {
		if (null == service) {
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("driverClass", getDriver(service.getDatabaseType())); //$NON-NLS-1$
		context.put("jdbcUrl", service.getJdbcUrl()); //$NON-NLS-1$
		context.put("username", service.getJdbcUser()); //$NON-NLS-1$
		context.put("password", service.getJdbcPassword()); //$NON-NLS-1$
		context.put("localhost", service.getIp()); //$NON-NLS-1$
		context.put("port", service.getPort()); //$NON-NLS-1$
		
		context.put("logDirectory", SystemVariables.getProgramsHome() //$NON-NLS-1$
				+ "/EsbServer/server/EOS/_srv/work/esbmonitor"); //$NON-NLS-1$
		
		InputStream in = null;
		try {
			in = EsbSSMServiceManager.class.getResourceAsStream("/META-INF/templates/ESB/ssm/ssm.user.properties"); //$NON-NLS-1$
			String content = VelocityUtil.parse(in, context, "ssm.user.properties"); //$NON-NLS-1$
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
		dbType = null == dbType ? EsbSSMService.DATABASE_MySQL : dbType;
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
