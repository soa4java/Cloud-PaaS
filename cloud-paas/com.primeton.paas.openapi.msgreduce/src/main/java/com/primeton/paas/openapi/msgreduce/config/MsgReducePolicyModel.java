/**
 * 
 */
package com.primeton.paas.openapi.msgreduce.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.primeton.paas.openapi.admin.config.IConfigModel;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgReducePolicyModel implements IConfigModel {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3457411761698131418L;

	private Map<String/* policyName */, List<String>> reducedFields = new LinkedHashMap<String, List<String>>();

	/**
	 * 
	 * @param policyName
	 * @param fieldName
	 */
	public void addReducedField(String policyName, String fieldName) {
		List<String> fields = reducedFields.get(policyName);
		if (fields == null) {
			fields = new ArrayList<String>();
			reducedFields.put(policyName, fields);
		}
		if (!fields.contains(fieldName))
			fields.add(fieldName);
	}

	public void removePolicy(String policyName) {
		reducedFields.remove(policyName);
	}

	public String[] getPolicyNames() {
		return reducedFields.keySet().toArray(new String[0]);
	}

	public String[] getReducedFieldNames(String policyName) {
		List<String> reducedFds = reducedFields.get(policyName);
		if (reducedFds == null) {
			return new String[0];
		} else {
			return reducedFds.toArray(new String[0]);
		}
	}
	
}
