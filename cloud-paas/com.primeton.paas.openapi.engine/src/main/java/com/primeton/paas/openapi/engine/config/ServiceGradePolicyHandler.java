/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

import java.util.Iterator;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.Configuration.Group;
import com.primeton.paas.openapi.admin.config.Configuration.Module;
import com.primeton.paas.openapi.admin.config.Configuration.Value;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceGradePolicyHandler extends AbstractConfigurationHandler<ServiceGradePolicyModel> {

	private static final ILogger log = LoggerFactory.getLogger(ServiceGradePolicyHandler.class);

	private static final String GROUP_NAME = "priority";

	private static final String PRIORITY_HIGH = "high";

	private static final String PRIORITY_LOW = "low";

	/**
	 * 
	 * @param configs
	 */
	public ServiceGradePolicyHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public ServiceGradePolicyHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	public ServiceGradePolicyModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		ServiceGradePolicyModel model = new ServiceGradePolicyModel();
		Group group = module.getGroup(GROUP_NAME);
		Map<String, Value> valueMap = group.getValues();
		for (Iterator<String> it = group.getValues().keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Value value = valueMap.get(key);

			String priority = value.getValue();
			if (PRIORITY_HIGH.equals(priority))
				model.addPolicy(key, true);
			else
				model.addPolicy(key, false);
		}
		return model;
	}

	public void toModule(ServiceGradePolicyModel model, Module[] modules) {
		Module module = modules[0];
		Group group = module.getGroup(GROUP_NAME);
		Map<String, Value> valueMap = group.getValues();
		for (String bizCode : model.listBizCodes()) {
			if (valueMap.containsKey(bizCode))
				group.deleteValue(bizCode);

			boolean isHighPriority = model.isHighPriority(bizCode);
			if (isHighPriority) {
				group.addValue(bizCode, PRIORITY_HIGH);
			} else {
				group.addValue(bizCode, PRIORITY_LOW);
			}
		}
	}

	public void toLoad(ServiceGradePolicyModel model) {
		log.debug("Begin to load ServiceGradePolicyModel");
		for (String bizCode : model.listBizCodes()) {
			boolean isHighPriority = model.isHighPriority(bizCode);
			ServiceGradePolicyManager.getInstance().addServiceGrade(bizCode, isHighPriority);
		}
		log.debug("Finished loading ServiceGradePolicyModel");
	}

	public void toUnload(ServiceGradePolicyModel model) {
		// log.debug("Begin to unload ServiceGradePolicyModel "+model);
		// ServiceGradePolicyManager.getInstance().clear();
		// log.debug("Finished unloading ServiceGradePolicyModel "+model);
	}

	public void toDeleteItems(ServiceGradePolicyModel model, String[] itemIds) {
		for (String itemId : itemIds) {
			model.removePolicy(itemId);
		}
		ServiceGradePolicyManager.getInstance().setServiceGradePolicy(model);
	}

	/**
	 * 
	 * @param modules
	 * @param deletedItemId
	 */
	public void deleteGroupsFromModule(final Module[] modules, String[] deletedGroupNames) {
		Module module = modules[0];
		Group group = module.getGroup(GROUP_NAME);
		for (String itemName : deletedGroupNames) {
			group.deleteValue(itemName);
		}
	}
	
}
