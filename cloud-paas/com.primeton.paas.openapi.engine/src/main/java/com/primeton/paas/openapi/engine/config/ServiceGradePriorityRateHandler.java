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
import com.primeton.paas.openapi.engine.BizRequestQueueManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceGradePriorityRateHandler extends AbstractConfigurationHandler<ServiceGradePriorityRateModel> {
	
	private static final ILogger log = LoggerFactory.getLogger(ServiceGradePriorityRateHandler.class);

	/**
	 * 
	 * @param configs
	 */
	public ServiceGradePriorityRateHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public ServiceGradePriorityRateHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	public ServiceGradePriorityRateModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		ServiceGradePriorityRateModel model = new ServiceGradePriorityRateModel();
		Group group = module.getGroups().get(AppConstants.DEFAULT_GROUP_NAME);
		String rateStr = group.getConfigValue("rate");
		try {
			model.setPriorityRate(Integer.parseInt(rateStr));
		} catch (Exception e) {
			log.error("Invalid ServiceGradePriorityRate config value: rate=" + rateStr, e);
		}
		return model;
	}

	public void toModule(ServiceGradePriorityRateModel model, Module[] modules) {
		Module module = modules[0];
		Group group = module.getGroup(AppConstants.DEFAULT_GROUP_NAME);
		group.deleteValue("rate");
		group.addValue("rate", String.valueOf(model.getPriorityRate()));
	}

	public void toLoad(ServiceGradePriorityRateModel model) {
		log.debug("Begin to load ServiceGradePriorityRateModel");
		if (model.getPriorityRate() > 0)
			BizRequestQueueManager.setPriorityRate(model.getPriorityRate());
		log.debug("Finished loading ServiceGradePriorityRateModel");
	}

	public void toUnload(ServiceGradePriorityRateModel model) {
		log.debug("Begin to unload ServiceGradePriorityRateModel " + model);
		BizRequestQueueManager.resetPriorityRate();
		log.debug("Finished unloading ServiceGradePriorityRateModel " + model);
	}

	public void toDeleteItems(ServiceGradePriorityRateModel model, String[] itemIds) {
		// nothing to do
	}
	
}
