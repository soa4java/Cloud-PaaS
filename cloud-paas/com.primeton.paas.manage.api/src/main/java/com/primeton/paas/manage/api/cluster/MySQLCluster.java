/**
 * 
 */
package com.primeton.paas.manage.api.cluster;

import com.primeton.paas.manage.api.model.AbstractCluster;
import com.primeton.paas.manage.api.service.MySQLService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class MySQLCluster extends AbstractCluster {

	private static final long serialVersionUID = -3177032898911218660L;
	
	private static final String VM_ORDER_ID = "vmOrderId";

	public static final String TYPE = MySQLService.TYPE;
	
	public MySQLCluster() {
		super();
		setType(TYPE);
		setName(TYPE + "-Cluster");
		setMaxSize(2);
		setMinSize(1);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSchema() {
		return getValue(MySQLService.SCHEMAL, "paas");
	}
	
	/**
	 * 
	 * @param schema
	 */
	public void setSchema(String schema) {
		setValue(MySQLService.SCHEMAL, StringUtil.isEmpty(schema) ? "paas" : schema);
	}

	/**
	 * 
	 * @return
	 */
	public String getUser() {
		return getValue(MySQLService.USER, "paas");
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		setValue(MySQLService.USER, StringUtil.isEmpty(user) ? "paas" : user);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return getValue(MySQLService.PASSWORD, "000000");
	}
	
	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		setValue(MySQLService.PASSWORD, StringUtil.isEmpty(password) ? "000000" : password);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJdbcDriver() {
		return getValue(MySQLService.JDBC_DRIVER, "com.mysql.jdbc.Driver");
	}
	
	/**
	 * 
	 * @param jdbcDriver
	 */
	public void setJdbcDriver(String jdbcDriver) {
		setValue(MySQLService.JDBC_DRIVER, StringUtil.isEmpty(jdbcDriver) ? "com.mysql.jdbc.Driver" : jdbcDriver);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVmOrderId(){
		return getValue(VM_ORDER_ID);
	}
	
	
	/**
	 * 
	 * @param vmOrderId
	 */
	public void setVmOrderId(String vmOrderId){
		setValue(VM_ORDER_ID, vmOrderId);
	}
	
}
