/**
 * 
 */
package com.primeton.paas.app.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.app.AppConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationManager {
	
	public static String CONFIG_FILE_TYPE_SYSTEM = "system";

	public static String CONFIG_FILE_TYPE_USER = "user";

	public static String CONFIG_FILE_TYPE_SIMULATOR = "simulator";

	private static Map<String/** configFileType */ , String/** fileName */> configFileMap 
			= new LinkedHashMap<String, String>();

	private static ConfigurationManager instance = new ConfigurationManager();
	
	private ConfigurationManager() {}
	
	private Map<String, Configuration> localConfigs = new ConcurrentHashMap<String, Configuration>();

	static {
		configFileMap.put(CONFIG_FILE_TYPE_SYSTEM, AppConstants.CONFIG_FILE_NAME_SYSTEM);
		configFileMap.put(CONFIG_FILE_TYPE_USER, AppConstants.CONFIG_FILE_NAME_USER);
		configFileMap.put(CONFIG_FILE_TYPE_SIMULATOR, AppConstants.CONFIG_FILE_NAME_SIMULATOR);
	}
	
	/**
	 * 
	 * @param configFileType
	 * @param configFileName
	 */
	public static void addConfigFileType(String configFileType,String configFileName) {
		configFileMap.put(configFileType, configFileName);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ConfigurationManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param configFileType
	 * @param config
	 */
	public void register(String configFileType, Configuration config) {
		localConfigs.put(configFileType, config);
	}

	/**
	 * 
	 * @param configFileType
	 * @return
	 */
	public Configuration getConfiguration(String configFileType) {
		return localConfigs.get(configFileType);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getConfigFileTypes() {
		return configFileMap.keySet().toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param configFileType
	 * @return
	 */
	public static String getConfigFileName(String configFileType) {
		return configFileMap.get(configFileType);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getConfigFileNames() {
		String[] configFileTypes=getConfigFileTypes();
		String[] ret=new String[configFileTypes.length];
		for(int i=0;i<configFileTypes.length;i++) {
			ret[i] = configFileMap.get(configFileTypes[i]);
		}
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public Configuration[] getConfigurations() {
		Configuration[] configs=new Configuration[this.localConfigs.size()];
		int i=0;
		for(Iterator<Configuration> it=this.localConfigs.values().iterator();it.hasNext();) {
			configs[i++] = it.next();
		}
		return configs;
	}
	
	/**
	 * 
	 * @param configFileType
	 */
	public void unRegister(String configFileType) {
		localConfigs.remove(configFileType);
	}
	
	/**
	 * clean. <br>
	 */
	public void clear() {
		localConfigs.clear();
	}
	
}
