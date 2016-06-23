/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.CEPEngineService;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class CEPEngineCluster extends AbstractCluster {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -6463454443123167093L;
	
	public static final String TYPE = CEPEngineService.TYPE;
	
	public CEPEngineCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
	}

}
