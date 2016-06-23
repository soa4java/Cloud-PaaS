/**
 * 
 */
package com.primeton.paas.openapi.httpin.config;

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
public class HttpInConfigHandler extends AbstractConfigurationHandler<HttpInConfigModel> {
	
	private static final ILogger log = LoggerFactory.getLogger(HttpInConfigHandler.class);

	/**
	 * 
	 * @param configs
	 */
	public HttpInConfigHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public HttpInConfigHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModel(com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public HttpInConfigModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		HttpInConfigModel model = new HttpInConfigModel();
		Group group = module.getGroups().get(AppConstants.DEFAULT_GROUP_NAME);
		String timeout = group.getConfigValue("timeout");
		try {
			model.setTimeout(Integer.parseInt(timeout));
		} catch (Exception e) {
			log.error("Invalid AsyncHttpIn config value: timeout=" + timeout, e);
		}
		return model;

	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModule(com.primeton.openplatform.openapi.admin.config.IConfigModel,
	 *      com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public void toModule(HttpInConfigModel model, Module[] modules) {
		Module module = modules[0];
		Group group = module.getGroup(AppConstants.DEFAULT_GROUP_NAME);
		group.deleteValue("timeout"); //$NON-NLS-1$
		group.addValue("timeout", String.valueOf(model.getTimeout())); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toLoad(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toLoad(HttpInConfigModel model) {
		HttpInConfigModelManager.setModel(model);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toUnload(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toUnload(HttpInConfigModel model) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toDeleteItems(com.primeton.openplatform.openapi.admin.config.IConfigModel,
	 *      java.lang.String[])
	 */
	public void toDeleteItems(HttpInConfigModel model, String[] itemIds) {
		// nothing to do
	}

}
