/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.WarService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class WarCluster extends AbstractCluster {

	private static final long serialVersionUID = -161669759742255459L;
	
	public static final String TYPE = WarService.TYPE;
	
	private static final String CURRENT_WAR_VERSION = "currentWarVersion";

	public WarCluster() {
		super();
		setType(TYPE);
	}
	
	public String getCurrentWarVersion() {
		return getValue(CURRENT_WAR_VERSION);
	}
	
	public void setCurrentWarVersion(String currentWarVersion) {
		setValue(CURRENT_WAR_VERSION, currentWarVersion);
	}
	
}
