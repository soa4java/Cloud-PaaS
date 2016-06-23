/**
 * 
 */
package com.primeton.paas.manage.api.service;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;


/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class JettyService extends AbstractService {

	private static final long serialVersionUID = -503601462994069502L;
	
	public static final String TYPE = "Jetty";
	
	private static final String STORAGE_PATH = "storagePath";
	private static final String STORAGE_SIZE = "storageSize";
	private static final String STORAGE_ID = "storageId";
	private static final String APP_NAME = "appName";
	private static final String MIN_MEMORY_SIZE = "minMemory";
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	private static final String MIN_PERM_SIZE = "minPermSize";
	private static final String MAX_PERM_SIZE = "maxPermSize";
	private static final String ENABLE_SESSION = "enableSession";
	// ThreadPool [org.eclipse.jetty.util.thread.QueuedThreadPool]
	private static final String MIN_THREAD_SIZE = "minThreadSize";
	private static final String MAX_THREAD_SIZE = "maxThreadSize";
	
	/**
	 * Default constructor <br>
	 */
	public JettyService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
		setEnableSession(false);
	}

	/**
	 * 
	 * @return
	 */
	public String getStoragePath() {
		return getValue(STORAGE_PATH);
	}
	
	/**
	 * 
	 * @param storagePath
	 */
	public void setStoragePath(String storagePath) {
		setValue(STORAGE_PATH, storagePath);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStorageSize() {
		return getValue(STORAGE_SIZE, 0);
	}
	
	/**
	 * 
	 * @param storageSize
	 */
	public void setStorageSize(int storageSize) {
		setValue(STORAGE_SIZE, String.valueOf(storageSize));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStorageId() {
		return getValue(STORAGE_ID);
	}
	
	/**
	 * 
	 * @param storageId
	 */
	public void setStorageId(String storageId) {
		setValue(STORAGE_ID, storageId);
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return getValue(APP_NAME);
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		setValue(APP_NAME, appName);
	}

	/**
	 * @return the minMemorySize
	 */
	public int getMinMemorySize() {
		return getValue(MIN_MEMORY_SIZE, -1);
	}

	/**
	 * @param minMemorySize the minMemorySize to set
	 */
	public void setMinMemorySize(int minMemorySize) {
		setValue(MIN_MEMORY_SIZE, minMemorySize);
	}

	/**
	 * @return the maxMemorySize
	 */
	public int getMaxMemorySize() {
		return getValue(MAX_MEMORY_SIZE, -1);
	}


	/**
	 * @param maxMemorySize the maxMemorySize to set
	 */
	public void setMaxMemorySize(int maxMemorySize) {
		setValue(MAX_MEMORY_SIZE, maxMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMinPermMemorySize() {
		return getValue(MIN_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param minPermMemorySize
	 */
	public void setMinPermMemorySize(int minPermMemorySize) {
		setValue(MIN_PERM_SIZE, minPermMemorySize);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxPermMemorySize() {
		return getValue(MAX_PERM_SIZE, -1);
	}
	
	/**
	 * 
	 * @param maxPermMemorySize
	 */
	public void setMaxPermMemorySize(int maxPermMemorySize) {
		setValue(MAX_PERM_SIZE, maxPermMemorySize);
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
	
	/**
	 * queued blocking threadpool min size <br>
	 * 
	 * @return
	 */
	public int getMinThreadSize() {
		return getValue(MIN_THREAD_SIZE, 10);
	}
	
	/**
	 * 
	 * @param minThreadSize
	 */
	public void setMinThreadSize(int minThreadSize) {
		setValue(MIN_THREAD_SIZE, minThreadSize);
	}
	
	/**
	 * queued blocking threadpool max size <br>
	 * 
	 * @return
	 */
	public int getMaxThreadSize() {
		return getValue(MAX_THREAD_SIZE, 200);
	}
	
	/**
	 * 
	 * @param maxThreadSize
	 */
	public void setMaxThreadSize(int maxThreadSize) {
		setValue(MAX_THREAD_SIZE, maxThreadSize);
	}
	
}
