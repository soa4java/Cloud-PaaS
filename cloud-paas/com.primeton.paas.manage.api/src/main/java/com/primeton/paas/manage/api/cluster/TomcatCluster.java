/**
 *
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.TomcatService;

/**
 * Apache Tomcat Cluster. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TomcatCluster extends AbstractCluster {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5289879979013943684L;
	
	public static final String TYPE = TomcatService.TYPE;
	
	private static final String WAR_VERSION = "warVersion";

	/**
	 * Default. <br>
	 */
	public TomcatCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster"); //$NON-NLS-1$ 
	}
	
	/**
	 * 获取当前部署的WAR版本. <br>
	 * 
	 * @return WAR版本
	 */
	public String getWarVersion() {
		return getValue(WAR_VERSION, "#undefined#"); //$NON-NLS-1$
	}
	
	/**
	 * 设置当前部署的WAR版本. <br>
	 * 
	 * @param warVersion WAR版本
	 */
	public void setWarVersion(String warVersion) {
		setValue(WAR_VERSION, warVersion);
	}

}
