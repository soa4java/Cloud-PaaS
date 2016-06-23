/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.common.api.manage.variable.VariableManager;
import org.gocom.cloud.cesium.common.api.manage.variable.VariableManagerFactory;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.client.ConfigManagerFactory;
import org.gocom.cloud.cesium.config.template.api.ConfigTemplateManager;
import org.gocom.cloud.cesium.config.template.api.ConfigTemplateManagerFactory;
import org.gocom.cloud.cesium.manage.cluster.api.ClusterManager;
import org.gocom.cloud.cesium.manage.cluster.api.ClusterManagerFactory;
import org.gocom.cloud.cesium.manage.cluster.api.exception.ClusterManageFactoryException;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManager;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.service.api.ServiceInstanceManager;
import org.gocom.cloud.cesium.manage.service.api.factory.ServiceInstanceManagerFactory;
import org.gocom.cloud.cesium.manage.service.api.util.MessageProducerUtil;
import org.gocom.cloud.cesium.mqclient.api.MQClient;
import org.gocom.cloud.cesium.mqclient.api.MQClientFactory;
import org.gocom.cloud.cesium.mqclient.api.MessageProducer;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CesiumFactory {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(CesiumFactory.class);
	
	private static ServiceInstanceManager serviceInstanceManager = null; // db
	private static InstanceManager instanceManager = null; // zk
	private static ConfigManager configManager = null; // zk
	private static ConfigTemplateManager configTemplateManager = null;
	private static VariableManager variableManager = null;
	private static ClusterManager clusterManager = null;
	
	/**
	 * Default. <br>
	 */
	private CesiumFactory() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized ServiceInstanceManager getServiceInstanceManager() {
		if (serviceInstanceManager == null) {
			serviceInstanceManager = ServiceInstanceManagerFactory
					.createDefaultManager();
		}
		return serviceInstanceManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized InstanceManager getInstanceManager() {
		if (instanceManager == null) {
			try {
				instanceManager = InstanceManagerFactory
						.createInstanceManager();
			} catch (Throwable t) {
				logger.error(t.getMessage());
			}
		}
		return instanceManager;
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized ConfigManager getConfigManager() {
		if (configManager == null) {
			try {
				configManager = ConfigManagerFactory.createConfigManager();
			} catch (TimeoutException e) {
				logger.error(e);
			}
		}
		return configManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized ConfigTemplateManager getConfigTemplateManager() {
		if (configTemplateManager == null) {
			configTemplateManager = ConfigTemplateManagerFactory
					.createConfigTemplateManager();
		}
		return configTemplateManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static MessageProducer getMessageProducer() {
		MessageProducer producer = MessageProducerUtil.createProducer();
		if (producer != null && producer.isOpen()) {
			return producer;
		} else {
			MessageProducerUtil.resetProducer();
			return MessageProducerUtil.createProducer();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static MQClient getMQClient() {
		return MQClientFactory.getMQClient();
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized VariableManager getVariableManager() {
		if (variableManager == null) {
			variableManager = VariableManagerFactory.createVariableManager();
		}
		return variableManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized ClusterManager getClusterManager() {
		if (null == clusterManager) {
			try {
				clusterManager = ClusterManagerFactory.getInstance();
			} catch (ClusterManageFactoryException e) {
				logger.error(e);
			}
		}
		return clusterManager;
	}
	
}
