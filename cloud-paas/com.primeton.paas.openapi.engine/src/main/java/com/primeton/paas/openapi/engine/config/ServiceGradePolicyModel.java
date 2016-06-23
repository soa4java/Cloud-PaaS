/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

import java.util.LinkedHashMap;
import java.util.Map;

import com.primeton.paas.openapi.admin.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceGradePolicyModel implements IConfigModel {
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6617053929015810491L;

	private Map<String/* bizCode */, Boolean/* is High priority */> _bizPriorities = new LinkedHashMap<String, Boolean>();

	public void addPolicy(String bizCode, boolean isHighPriority) {
		_bizPriorities.put(bizCode, isHighPriority);
	}

	public boolean isHighPriority(String bizCode) {
		Boolean ret = _bizPriorities.get(bizCode);
		if (ret != null)
			return ret.booleanValue();
		else
			return false;
	}

	public String[] listBizCodes() {
		return _bizPriorities.keySet().toArray(new String[0]);
	}

	public void removePolicy(String bizCode) {
		_bizPriorities.remove(bizCode);
	}
	
}
