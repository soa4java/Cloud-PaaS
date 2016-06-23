/**
 * 
 */
package com.primeton.paas.openapi.msgreduce.config;

import java.util.Iterator;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.Configuration.Group;
import com.primeton.paas.openapi.admin.config.Configuration.Module;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgReducePolicyHandler extends AbstractConfigurationHandler<MsgReducePolicyModel> {

	private static final ILogger log = LoggerFactory.getLogger(MsgReducePolicyHandler.class);

	/**
	 * 
	 * @param configs
	 */
	public MsgReducePolicyHandler(Configuration[] configs) {
		super(configs);
	}

	/**
	 * 
	 * @param fileLoactions
	 */
	public MsgReducePolicyHandler(String[] fileLoactions) {
		super(fileLoactions);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler#toModel(com.primeton.paas.openapi.admin.config.Configuration.Module[])
	 */
	public MsgReducePolicyModel toModel(Module[] modules) {
		Module module = new Module();
		for (Module md : modules) {
			module.mergeModule(md);
		}
		MsgReducePolicyModel model = new MsgReducePolicyModel();
		for (String groupName : module.groupNames()) {
			Group group = module.getGroup(groupName);

			for (Iterator<String> it = group.getValues().keySet().iterator(); it.hasNext();) {
				model.addReducedField(groupName, it.next());
			}
		}
		return model;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler#toModule(com.primeton.paas.openapi.admin.config.IConfigModel, com.primeton.paas.openapi.admin.config.Configuration.Module[])
	 */
	public void toModule(MsgReducePolicyModel model, Module[] modules) {
		Module module = modules[0];
		for (String policyName : model.getPolicyNames()) {
			if (module.getGroups().keySet().contains(policyName)) {
				module.deleteGroup(policyName);
			}
			Group group = module.addGroup(policyName);
			for (String fieldName : model.getReducedFieldNames(policyName)) {
				group.addValue(fieldName, null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler#toLoad(com.primeton.paas.openapi.admin.config.IConfigModel)
	 */
	public void toLoad(MsgReducePolicyModel model) {
		log.debug("Begin to load MsgReducePolicyModel");
		for (String policyName : model.getPolicyNames()) {
			for (String fieldName : model.getReducedFieldNames(policyName)) {
				MsgReducePolicyManager.getInstance().addReducedField(policyName, fieldName);
			}
		}
		log.debug("Finished loading MsgReducePolicyModel");
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler#toUnload(com.primeton.paas.openapi.admin.config.IConfigModel)
	 */
	public void toUnload(MsgReducePolicyModel model) {
		log.debug("Begin to unload MsgReducePolicyModel " + model);
		for (String policyName : model.getPolicyNames()) {
			MsgReducePolicyManager.getInstance().removePolicy(policyName);
		}
		log.debug("Finished unloading MsgReducePolicyModel " + model);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler#toDeleteItems(com.primeton.paas.openapi.admin.config.IConfigModel, java.lang.String[])
	 */
	public void toDeleteItems(MsgReducePolicyModel model, String[] itemIds) {
		for (String itemId : itemIds) {
			model.removePolicy(itemId);
		}
		MsgReducePolicyManager.getInstance().setMsgReducePolicy(model);
	}
	
}
