/**
 * 
 */
package com.primeton.paas.manage.api.service;

import java.util.HashMap;
import java.util.Map;

import com.primeton.paas.manage.api.model.AbstractService;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class CollectorService extends AbstractService {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = -4615590336087112920L;
	
	public static final String TYPE = "Collector";
	
	private static final String MIN_MEMORY_SIZE = "minMemory";
	private static final String MAX_MEMORY_SIZE = "maxMemory";
	private static final String MAX_PERM_SIZE = "maxPermSize";
	private static final String GROUP_NAME = "groupName";
	private static final String MQ_SERVER = "mqServer";
	private static final String MQ_DESTS = "mqDests";
	private static final String MQ_TYPES = "mqTypes";
	private static final String LOG_ROOT = "logRoot";
	private static final String APPENDER_BUFFER = "appenderBuffer";
	
	public static final String MQ_TYPE_QUEUE = "queue";
	public static final String MQ_TYPE_EXCHANGE = "exchange";
	
	/**
	 * 
	 */
	public CollectorService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	/**
	 * @return the minMemorySize
	 */
	public int getMinMemorySize() {
		return (int)getValue(MIN_MEMORY_SIZE, -1);
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
		return (int)getValue(MAX_MEMORY_SIZE, -1);
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
	public int getMaxPermMemorySize() {
		return (int)getValue(MAX_PERM_SIZE, -1);
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
	public String getGroupName() {
		return getValue(GROUP_NAME, "default"); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		setValue(GROUP_NAME, groupName);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMQServer() {
		return getValue(MQ_SERVER);
	}
	
	/**
	 * 
	 * @param mqServer
	 */
	public void setMQServer(String mqServer) {
		setValue(MQ_SERVER, mqServer);
	}
	
	/**
	 * 
	 * @param dests
	 */
	public void setMQDests(Map<String, String> dests) {
		if (dests == null || dests.isEmpty()) {
			return;
		}
		StringBuffer types = new StringBuffer();
		StringBuffer names = new StringBuffer();
		
		for (String name : dests.keySet()) {
			if (StringUtil.isNotEmpty(name)) {
				names.append(name).append(",");
				String type = dests.get(name);
				type = StringUtil.isEmpty(type) ? MQ_TYPE_EXCHANGE : type;
				types.append(type).append(",");
			}
		}
		if (types.length() > 1) {
			types.deleteCharAt(types.length() - 1);
			names.deleteCharAt(names.length() - 1);
		}
		
		setValue(MQ_DESTS, names.toString());
		setValue(MQ_TYPES, types.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getMQDests() {
		Map<String, String> dests = new HashMap<String, String>();
		String names = getValue(MQ_DESTS);
		String types = getValue(MQ_TYPES);
		if (StringUtil.isNotEmpty(names, types)) {
			String[] arrayName = names.split(",");
			String[] arrayType = types.split(",");
			
			if (arrayName.length == arrayType.length) {
				for (int i = 0; i < arrayType.length; i++) {
					String name = arrayName[i];
					String type = arrayType[i];
					if (StringUtil.isNotEmpty(name)) {
						type = StringUtil.isNotEmpty(type) ? type : MQ_TYPE_EXCHANGE;
						dests.put(name, type);
					}
				}
			}
		}
		return dests;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLogRoot() {
		return getValue(LOG_ROOT);
	}
	
	/**
	 * 
	 * @param logRoot
	 */
	public void setLogRoot(String logRoot) {
		setValue(LOG_ROOT, logRoot);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAppenderBuffer() {
		return getValue(APPENDER_BUFFER, 1024000); // 1000KB = 1024B * 1000
	}
	
	/**
	 * 
	 * @param appenderBuffer
	 */
	public void setAppenderBuffer(int appenderBuffer) {
		setValue(APPENDER_BUFFER, appenderBuffer);
	}
	
}
