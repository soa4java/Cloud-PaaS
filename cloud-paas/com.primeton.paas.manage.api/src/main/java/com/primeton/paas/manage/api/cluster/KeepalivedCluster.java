/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.KeepalivedService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class KeepalivedCluster extends AbstractCluster {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 1246998436211412800L;
	
	public static final String TYPE = KeepalivedService.TYPE;
	
	public KeepalivedCluster() {
		super();
		setType(TYPE);
	}
	
}
