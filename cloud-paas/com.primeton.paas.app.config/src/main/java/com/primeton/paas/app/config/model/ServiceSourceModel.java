/**
 * 
 */
package com.primeton.paas.app.config.model;

import com.primeton.paas.app.config.IConfigModel;

/**
 * /Cloud/Cesium/Config/App/appName/serviceSource. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServiceSourceModel implements IConfigModel, IServiceConstants {

	private static final long serialVersionUID = 7168898946096860771L;
	
	private String type;
	private String clusterId;
	
	/**
	 * Default. <br>
	 */
	public ServiceSourceModel() {
		super();
	}

	/**
	 * 
	 * @param type
	 * @param clusterId
	 */
	public ServiceSourceModel(String type, String clusterId) {
		super();
		this.type = type;
		this.clusterId = clusterId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the clusterId
	 */
	public String getClusterId() {
		return clusterId;
	}

	/**
	 * @param clusterId the clusterId to set
	 */
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	
}
