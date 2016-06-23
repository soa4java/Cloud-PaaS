/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceEPSRel {
	
	private Map<String, Object> metadata = new HashMap<String, Object>();
	
	private static final String CLUSTER_ID = "cluster_id";
	private static final String AVG_INST_ID = "avg_inst_id";
	private static final String INC_INST_ID = "inc_inst_id";
	private static final String DEC_INST_ID = "dec_inst_id";

	/**
	 * Default. <br>
	 */
	public ServiceEPSRel() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Object> T get(String key) {
		if (StringUtil.isNotEmpty(key)) {
			return (T)(metadata.get(key));
		}
		return null;
	}
	
	protected void set(String key, Object value) {
		metadata.put(key, value);
	}

	/**
	 * @return the appName
	 */
	public String getClusterId() {
		return get(CLUSTER_ID);
	}

	/**
	 * @param appName the appName to set
	 */
	public void setClusterId(String clusterId) {
		set(CLUSTER_ID, clusterId);
	}

	/**
	 * @return the avgInstId
	 */
	public String getAvgInstId() {
		return get(AVG_INST_ID);
	}

	/**
	 * @param avgInstId the avgInstId to set
	 */
	public void setAvgInstId(String avgInstId) {
		set(AVG_INST_ID, avgInstId);
	}

	/**
	 * @return the incInstId
	 */
	public String getIncInstId() {
		return get(INC_INST_ID);
	}

	/**
	 * @param incInstId the incInstId to set
	 */
	public void setIncInstId(String incInstId) {
		set(INC_INST_ID, incInstId);
	}

	/**
	 * @return the decInstId
	 */
	public String getDecInstId() {
		return get(DEC_INST_ID);
	}

	/**
	 * @param decInstId the decInstId to set
	 */
	public void setDecInstId(String decInstId) {
		set(DEC_INST_ID, decInstId);
	}

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	
}
