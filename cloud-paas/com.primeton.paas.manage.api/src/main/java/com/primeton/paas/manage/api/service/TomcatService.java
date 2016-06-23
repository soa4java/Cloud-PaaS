/**
 *
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;

/**
 * Apache Tomcat Service. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class TomcatService extends AbstractService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6509366989973342505L;
	
	public static final String TYPE = "Tomcat";
	
	private static final String APP_NAME = "appName";
	
	private static final String MIN_MEMORY_SIZE = "minMemory";
	
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	
	private static final String MIN_PERM_SIZE = "minPermSize";
	
	private static final String MAX_PERM_SIZE = "maxPermSize";
	
	private static final String SHUTDOWN_PORT = "shutdownPort";
	
	private static final String AJP_PORT = "ajpPort";
	
	private static final String ADMIN_PORT = "adminPort";
	
	private static final String ENABLE_SESSION = "enableSession";
	
	/**
	 * Default. <br>
	 */
	public TomcatService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
		setShutdownPort(-1); //$NON-NLS-1$
		setAjpPort(-1); //$NON-NLS-1$
		setAdminPort(-1); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppName() {
		return getValue(APP_NAME);
	}
	
	/**
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		setValue(APP_NAME, appName);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, 128); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param minMemorySize
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, 256); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param maxMemorySize
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermSize() {
		return getValue(MIN_PERM_SIZE, 64); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param minPermSize
	 */
	public void setMinPermSize(int minPermSize) {
		setValue(MIN_PERM_SIZE, minPermSize);
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPermSize() {
		return getValue(MAX_PERM_SIZE, 64); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param maxPermSize
	 */
	public void setMaxPermSize(int maxPermSize) {
		setValue(MAX_PERM_SIZE, maxPermSize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getShutdownPort() {
		return getValue(SHUTDOWN_PORT, 8005); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param shutdownPort
	 */
	public void setShutdownPort(int shutdownPort) {
		setValue(SHUTDOWN_PORT, shutdownPort);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAjpPort() {
		return getValue(AJP_PORT, 8009); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param ajpPort
	 */
	public void setAjpPort(int ajpPort) {
		setValue(AJP_PORT, ajpPort);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAdminPort() {
		return getValue(ADMIN_PORT, 6200); //$NON-NLS-1$S
	}
	
	/**
	 * 
	 * @param adminPort
	 */
	public void setAdminPort(int adminPort) {
		setValue(ADMIN_PORT, adminPort);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEnableSession() {
		return getValue(ENABLE_SESSION, false);
	}
	
	/**
	 * 
	 * @param enableSession
	 */
	public void setEnableSession(boolean enableSession) {
		setValue(ENABLE_SESSION, enableSession);
	}
	
}
