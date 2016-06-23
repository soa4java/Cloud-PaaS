/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.JettyService;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class JettyCluster extends AbstractCluster {

	private static final long serialVersionUID = -7717189493741542764L;

	public static final String TYPE = JettyService.TYPE;
	
	private static final String VERSION = "version";

	public JettyCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}

	
	/**
	 * 
	 * @return
	 */
	public String getVersion() {
		return getValue(VERSION);
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		setValue(VERSION, version);
	}
	
}
