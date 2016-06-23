/**
 * 
 */
package com.primeton.paas.openapi.msgreduce.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class MsgReducePolicyManager {
	
	private static MsgReducePolicyManager instance = new MsgReducePolicyManager();

	private MsgReducePolicyModel policyModel;

	public static MsgReducePolicyManager getInstance() {
		return instance;
	}

	public void setMsgReducePolicy(MsgReducePolicyModel policyModel) {
		this.policyModel = policyModel;
	}

	public void removePolicy(String policyName) {
		if (policyModel == null)
			return;

		synchronized (policyModel) {
			policyModel.removePolicy(policyName);
		}
	}

	public void addReducedField(String policyName, String fieldName) {
		if (policyModel == null)
			policyModel = new MsgReducePolicyModel();

		synchronized (policyModel) {
			policyModel.addReducedField(policyName, fieldName);
		}
	}

	public String[] getReducedFields(String policyName) {
		if (policyModel == null)
			return new String[0];

		synchronized (policyModel) {
			return policyModel.getReducedFieldNames(policyName);
		}
	}
	
}
