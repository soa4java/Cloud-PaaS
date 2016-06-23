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
public class CEPEngineService extends AbstractService {

	/**
	 * serialVersionUID <br>
	 */
	private static final long serialVersionUID = 8196147755320650700L;
	
	public static final String TYPE = "CEPEngine";
	
	private static final String MIN_MEMORY = "minMemory";
	private static final String MAX_MEMORY = "maxMemory";
	private static final String MAX_PERM_SIZE = "maxPermSize";
	private static final String GROUP_NAME = "groupName";
	private static final String MQ_SERVER = "mqServer";
	private static final String MQ_DESTS = "mqDests";
	private static final String MQ_TYPES = "mqTypes";
	
	public static final String MQ_TYPE_QUEUE = "queue";
	public static final String MQ_TYPE_EXCHANGE = "exchange";
	
	public CEPEngineService() {
		super();
		setType(TYPE);
		setMode(IService.MODE_PHYSICAL);
	}
	
	public int getMinMemory() {
		return getValue(MIN_MEMORY, 128);
	}
	
	public void setMinMemory(int minMemory) {
		setValue(MIN_MEMORY, minMemory);
	}
	
	public int getMaxMemory() {
		return getValue(MAX_MEMORY, 128);
	}
	
	public void setMaxMemory(int maxMemory) {
		setValue(MAX_MEMORY, maxMemory);
	}
	
	public int getMaxPermSize() {
		return getValue(MAX_PERM_SIZE, 128);
	}
	
	public void setMaxPermSize(int maxPermSize) {
		setValue(MAX_PERM_SIZE, maxPermSize);
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
	
}
