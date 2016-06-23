/**
 * 
 */
package com.primeton.paas.mail.server.config;

import java.util.Map;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.cesium.mqclient.api.MQServer;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * 
 * @author liuyi (mail-to:liu-yi@primeton.com)
 * 
 */
public class DefaultSystemConfig implements ISystemConfig {

	public static final String PREFIX = "PAAS.MAIL.";
	public static final String QUEUE_GROUP = PREFIX + "QueueGroup";
	public static final String MQSERVER = PREFIX + "MQServer";

	private String[] queueGroup = null;
	private MQServer server = null;
	private static ILogger logger = LoggerFactory
			.getLogger(DefaultSystemConfig.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.primeton.paas.mail.server.config.ISystemConfig#getQueueGroup()
	 */
	public String[] getQueueGroup() {
		if (this.queueGroup == null || this.queueGroup.length == 0) {
			Variable var = NamingUtil.lookupVariable(QUEUE_GROUP);
			if (var != null) {
				String val = var.getValue();
				if (val != null && val.trim().length() > 0) {
					this.queueGroup = val.split(",");
				}
			}
		}
		if (this.queueGroup == null || this.queueGroup.length == 0) {
			throw new RuntimeException("Mail queue group not found.");
		}
		return this.queueGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.primeton.paas.mail.server.config.ISystemConfig#getMQServer()
	 */
	@SuppressWarnings("unchecked")
	public MQServer getMQServer() {
		if (this.server == null) {
			Variable var = NamingUtil.lookupVariable(MQSERVER);
			if (var != null) {
				String val = var.getValue();
				if (val != null && val.trim().length() > 0) {
					try {
						Map<String, String> args = (Map<String, String>) JsonSerializerUtil
								.deserialize(val);
						server = new MQServer();
						server.addAttrs(args);
					} catch (Exception e) {
						if (logger.isErrorEnabled()) {
							logger.error(e);
						}
					}
				} else {
					throw new RuntimeException(
							"MQServer not found for send mail.");
				}
			}
		}
		return this.server;
	}

}
