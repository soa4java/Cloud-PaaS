/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.AppConstants;
import com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.Configuration.Group;
import com.primeton.paas.openapi.admin.config.Configuration.Module;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpOutConfigHandler extends AbstractConfigurationHandler<HttpOutConfigModel> {
	
	private static final ILogger log = LoggerFactory.getLogger(HttpOutConfigHandler.class);
	
	/**
	 * 
	 * @param configs
	 */
	public HttpOutConfigHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public HttpOutConfigHandler(String[] fileLoactions) {
		super(fileLoactions);
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModel(com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public HttpOutConfigModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		HttpOutConfigModel model = new HttpOutConfigModel();
		Group group = module.getGroups().get(AppConstants.DEFAULT_GROUP_NAME);
		String encoding=group.getConfigValue("encoding");
		model.setEncoding(encoding);
		String connTimeout=group.getConfigValue("ConnectionTimeout");
		try {
			model.setConnectionTimeout(Integer.parseInt(connTimeout));
		} catch (Exception e) {
			log.error("Invalid AsyncHttpIn config value: connTimeout="+connTimeout,e);
		}
		String reqTimeout=group.getConfigValue("RequestTimeout");
		try {
			model.setRequestTimeout(Integer.parseInt(reqTimeout));
		} catch (Exception e) {
			log.error("Invalid AsyncHttpIn config value: reqTimeout="+reqTimeout,e);
		}
		return model;

	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModule(com.primeton.openplatform.openapi.admin.config.IConfigModel, com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public void toModule(HttpOutConfigModel model, Module[] modules) {
		Module module = modules[0];
		Group group=module.getGroup(AppConstants.DEFAULT_GROUP_NAME);
		group.setValue("ConnectionTimeout", String.valueOf(model.getConnectionTimeout())); //$NON-NLS-1$
		group.setValue("RequestTimeout", String.valueOf(model.getRequestTimeout())); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toLoad(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toLoad(HttpOutConfigModel model) {
		EngineConfigManager.setHttpOutConfigModel(model);
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toUnload(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toUnload(HttpOutConfigModel model) {
		//nothing to do
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toDeleteItems(com.primeton.openplatform.openapi.admin.config.IConfigModel, java.lang.String[])
	 */
	public void toDeleteItems(HttpOutConfigModel model, String[] itemIds) {
		// nothing to do
	}
	
}
