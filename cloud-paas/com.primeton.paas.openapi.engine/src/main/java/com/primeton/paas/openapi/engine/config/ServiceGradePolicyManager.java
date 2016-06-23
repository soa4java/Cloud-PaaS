/**
 * 
 */
package com.primeton.paas.openapi.engine.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceGradePolicyManager {
	
	private static ServiceGradePolicyManager instance = new ServiceGradePolicyManager();

	private ServiceGradePolicyModel policyModel = null;

	public static ServiceGradePolicyManager getInstance() {
		if (instance == null) {
			instance = new ServiceGradePolicyManager();
		}
		return instance;
	}

	public void setServiceGradePolicy(ServiceGradePolicyModel policyModel) {
		this.policyModel = policyModel;
	}

	public void addServiceGrade(String bizCode, boolean isHighPriority) {
		if (this.policyModel == null)
			this.policyModel = new ServiceGradePolicyModel();

		synchronized (this.policyModel) {
			this.policyModel.addPolicy(bizCode, isHighPriority);
		}
	}

	public void removeServiceGradePolicy(String bizCode) {
		if (this.policyModel == null)
			this.policyModel = new ServiceGradePolicyModel();

		synchronized (this.policyModel) {
			this.policyModel.removePolicy(bizCode);
		}
	}

	public boolean isHighPriortiy(String bizCode) {
		if (this.policyModel == null)
			return false;

		synchronized (this.policyModel) {
			return this.policyModel.isHighPriority(bizCode);
		}
	}

	public static void clear() {
		instance.policyModel = null;
		instance = null;
	}
	
}
