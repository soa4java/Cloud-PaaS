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
public class ResponseHandleThreadPoolConfigHandler extends AbstractConfigurationHandler<ResponseHandleThreadPoolConfigModel> {

	private static final ILogger log = LoggerFactory.getLogger(ResponseHandleThreadPoolConfigHandler.class);
	
	/**
	 * 
	 * @param configs
	 */
	public ResponseHandleThreadPoolConfigHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public ResponseHandleThreadPoolConfigHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModel(com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public ResponseHandleThreadPoolConfigModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		ResponseHandleThreadPoolConfigModel model = new ResponseHandleThreadPoolConfigModel();
		Group group = module.getGroups().get(AppConstants.DEFAULT_GROUP_NAME);
		
		String minPoolSize=group.getConfigValue("minPoolSize"); //$NON-NLS-1$
		try {
			model.setMinPoolSize(Integer.parseInt(minPoolSize));
		} catch (Exception e) {
			log.error("Invalid ResponseHandleThreadPool config value: minPoolSize="+minPoolSize,e);
		}

		String maxPoolSize=group.getConfigValue("maxPoolSize"); //$NON-NLS-1$
		try {
			model.setMaxPoolSize(Integer.parseInt(maxPoolSize));
		} catch (Exception e) {
			log.error("Invalid ResponseHandleThreadPool config value: maxPoolSize="+maxPoolSize,e);
		}

		String queueSize=group.getConfigValue("queueSize"); //$NON-NLS-1$
		try {
			model.setQueueSize(Integer.parseInt(queueSize));
		} catch (Exception e) {
			log.error("Invalid ResponseHandleThreadPool config value: queueSize="+queueSize,e);
		}

		String aliveTimeout=group.getConfigValue("aliveTimeout"); //$NON-NLS-1$
		try {
			model.setAliveTimeout(Integer.parseInt(aliveTimeout));
		} catch (Exception e) {
			log.error("Invalid ResponseHandleThreadPool config value: aliveTimeout="+aliveTimeout,e);
		}

		return model;
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toModule(com.primeton.openplatform.openapi.admin.config.IConfigModel, com.primeton.openplatform.openapi.admin.config.Configuration.Module[])
	 */
	public void toModule(ResponseHandleThreadPoolConfigModel model, Module[] modules) {
		Module module = modules[0];
		Group group=module.getGroup(AppConstants.DEFAULT_GROUP_NAME);
		group.setValue("minPoolSize", String.valueOf(model.getMaxPoolSize()));	
		group.setValue("maxPoolSize", String.valueOf(model.getMaxPoolSize()));	
		group.setValue("queueSize", String.valueOf(model.getQueueSize()));	
		group.setValue("aliveTimeout", String.valueOf(model.getAliveTimeout()));	
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toLoad(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toLoad(ResponseHandleThreadPoolConfigModel model) {
		EngineConfigManager.setResponseHandleThreadPoolConfigModel(model);
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toUnload(com.primeton.openplatform.openapi.admin.config.IConfigModel)
	 */
	public void toUnload(ResponseHandleThreadPoolConfigModel model) {
		//nothing to do
	}

	/* (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.admin.config.AbstractConfigurationHandler#toDeleteItems(com.primeton.openplatform.openapi.admin.config.IConfigModel, java.lang.String[])
	 */
	public void toDeleteItems(ResponseHandleThreadPoolConfigModel model, String[] itemIds) {
		//nothing to do
	}
	
}
