/**
 * 
 */
package com.primeton.paas.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.Configuration.Group;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.util.StringUtil;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ModuleHandlerRegister {
	
	private static ILogger log = SystemLoggerFactory.getLogger(ModuleHandlerRegister.class);
	
	private static final String MODULE_HANDLER_NAME = "META-INF/moduleHandler.xml";
	
	private static Map<String, ModuleBean> moduleNameCache = new ConcurrentHashMap<String, ModuleBean>();
	@SuppressWarnings("rawtypes")
	private static Map<Class, ModuleBean> handlerClassCache = new ConcurrentHashMap<Class, ModuleBean>();
	
	/**
	 * Default. <br>
	 */
	private ModuleHandlerRegister() {
	}
	
	/**
	 * init. <br>
	 */
	static {
		Enumeration<URL> moduleHandlerUrls = null;
		try {
			moduleHandlerUrls = ModuleHandlerRegister.class.getClassLoader()
					.getResources(MODULE_HANDLER_NAME);
		} catch (IOException ignore) {
			log.warn("Search [resource={0}] error!",
					new Object[] { MODULE_HANDLER_NAME }, ignore);
		}
		if (moduleHandlerUrls != null) {
			while (moduleHandlerUrls.hasMoreElements()) {				
				register(moduleHandlerUrls.nextElement());
			}
			updateModuleBeanDepended();
		}
	}
	
	/**
	 * 
	 * @param configModuleHandlerUrl
	 */
	public static void register(URL configModuleHandlerUrl) {
		Configuration moduleHandlerConfig = null;
		InputStream stream = null;
		try {
			stream = configModuleHandlerUrl.openStream();
			moduleHandlerConfig = Configuration.initConfiguration(stream);
			for (Group group : moduleHandlerConfig.getModule("ConfigFrame").getGroups().values()) {
				String moduleName = group.getName();
				String depends = group.getConfigValue("depend");
				String handlerClassName = group.getConfigValue("handler");
				try {
					ModuleBean bean = new ModuleBean(moduleName);
					bean.setModuleHandler(Class.forName(handlerClassName));
					if (StringUtil.isNotNull(depends)) {
						bean.addDependModuleNameSet(depends.split(","));
					}
					register(moduleName, bean);
				} catch (Throwable ignore) {
					log.warn("[ModuleName={0}, ModuleHandler={1}] cannot load to a class!", new Object[] {
							moduleName, handlerClassName }, ignore);
				}
			}
		} catch (IOException ignore) {
			log.warn("Load [resource={0}] error!", new Object[] { configModuleHandlerUrl.getFile() }, ignore);
		} catch (Throwable ignore) {
			log.warn("Load [resource={0}] error!", new Object[] { configModuleHandlerUrl.getFile() }, ignore);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignore) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @param configModuleHandlerUrl
	 */
	public static void unRegister(URL configModuleHandlerUrl) {
		Configuration moduleHandlerConfig = null;
		InputStream stream = null;
		try {
			stream = configModuleHandlerUrl.openStream();
			moduleHandlerConfig = Configuration.initConfiguration(stream);
			for (Group group : moduleHandlerConfig.getModule("ConfigFrame").getGroups().values()) {
				String moduleName = group.getName();
				unRegister(moduleName);
			}
		} catch (IOException ignore) {
			log.warn("Load [resource={0}] error!",
					new Object[] { configModuleHandlerUrl.getFile() }, ignore);
		} catch (Throwable ignore) {
			log.warn("Load [resource={0}] error!",
					new Object[] { configModuleHandlerUrl.getFile() }, ignore);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignore) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @param moduleName
	 * @param bean
	 */
	public static void register(String moduleName, ModuleBean bean) {
		moduleNameCache.put(moduleName, bean);
		handlerClassCache.put(bean.getModuleHandler(), bean);
	}
	
	/**
	 * 
	 * @param moduleName module
	 */
	public static void unRegister(String moduleName) {
		ModuleBean bean = moduleNameCache.remove(moduleName);
		handlerClassCache.remove(bean.getModuleHandler());
	}
	
	/**
	 * 
	 * @param handlerClazz handlerClass
	 */
	@SuppressWarnings("rawtypes")
	public static void unRegister(Class handlerClazz) {
		ModuleBean bean = handlerClassCache.remove(handlerClazz);
		moduleNameCache.remove(bean.getModuleName());
	}
	
	/**
	 * 
	 * @param moduleName module
	 * @return handlerClass
	 */
	public static ModuleBean getModuleBean(String moduleName) {
		return moduleNameCache.get(moduleName);
	}
	
	/**
	 * 
	 * @param handlerClazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ModuleBean getModuleBean(Class handlerClazz) {
		return handlerClassCache.get(handlerClazz);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Set<String> keySetModuleName() {
		return moduleNameCache.keySet();
	}
	
	/**
	 * 更新. <br>
	 */
	public static void updateModuleBeanDepended() {
		for (ModuleBean bean : moduleNameCache.values()) {
			Set<String> depends = bean.getDependModuleNameSet();
			if (depends.size() > 0) {
				String moduleNameDepended = bean.getModuleName();
				for (String moduleName : depends) {
					ModuleBean dependBean =  moduleNameCache.get(moduleName);
					if (dependBean != null) {
						dependBean.addDependedModuleName(moduleNameDepended);
					} else {
						log.warn("[module={0}] is depended by [module={1}], but not register!", new Object[]{moduleName, moduleNameDepended});
					}
				}
			}
		}
	}
	
}
