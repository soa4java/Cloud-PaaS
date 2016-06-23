/**
 * 
 */
package com.primeton.paas.app.config.impl;

import java.util.HashSet;
import java.util.Set;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.AbstractConfigurationHandler;
import com.primeton.paas.app.config.Configuration;
import com.primeton.paas.app.config.IConfigModel;
import com.primeton.paas.app.config.ModuleBean;
import com.primeton.paas.app.config.ModuleHandlerRegister;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;


/**
 * 配置加载器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationLoader {
	
	private static ILogger log = SystemLoggerFactory.getLogger(ConfigurationLoader.class);
	
	/**
	 * 
	 * @param configs
	 */
	public static void initLoad(Configuration[] configs) {
		long begin = System.currentTimeMillis();
		log.debug("Begin to load configurations.");
		doHandler(configs, new HandlerExecutor() {
			public void doHandler(AbstractConfigurationHandler<IConfigModel> handler) {
				if (handler != null) {
					IConfigModel model = handler.doLoad();
					handler.start(model);
					handler.toLoad(model);
				}
			}
		}, true);
		long end = System.currentTimeMillis();
		log.debug("Finished loading configurations, spent {0} ms.", new Object[] { end - begin });
	}

	/**
	 * 扩展执行器. <br>
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	interface HandlerExecutor {
		
		/**
		 * 
		 * @param handler
		 */
		void doHandler(AbstractConfigurationHandler<IConfigModel> handler);
		
	}

	/**
	 * 
	 * @param configs
	 * @param handlerMethod
	 * @param isDependSort
	 */
	private static void doHandler(Configuration[] configs, HandlerExecutor handlerMethod, boolean isDependSort) {
		Set<String> alreadyHandledModuleSet = new HashSet<String>();
		Set<String> waitHandleModuleSet = new HashSet<String>();
		for (Configuration config : configs) {
			if (config == null) {
				continue;
			}
			for (String moduleName : config.moduleNames()) {
				handlerModule(isDependSort, moduleName, handlerMethod, configs, alreadyHandledModuleSet, waitHandleModuleSet);
			}
		}
	}

	/**
	 * 
	 * @param isDepend
	 * @param moduleName
	 * @param handlerMethod
	 * @param configs
	 * @param alreadyHandleModuleSet
	 * @param waitHandleModuleSet
	 */
	private static void handlerModule(boolean isDepend, String moduleName, HandlerExecutor handlerMethod, Configuration[] configs, Set<String> alreadyHandleModuleSet, Set<String> waitHandleModuleSet) {
		if (alreadyHandleModuleSet.contains(moduleName)) {
			return;
		}
		ModuleBean moduleBean = ModuleHandlerRegister.getModuleBean(moduleName);
		if (moduleBean != null) {
			Set<String> depends = null;
			if (isDepend) {
				depends = moduleBean.getDependModuleNameSet();
			} else {
				depends = moduleBean.getDependedModuleNameSet();
			}
			if (depends.size() > 0) {
				waitHandleModuleSet.add(moduleName);
				for (String moduleDependName : depends) {
					if (alreadyHandleModuleSet.contains(moduleDependName)) {
						continue;
					}
					if (waitHandleModuleSet.contains(moduleDependName)) {
						log.warn("[moduleName={0}] is already depended, so it's a loop depend!", new Object[]{moduleDependName});
						alreadyHandleModuleSet.add(moduleDependName);
						waitHandleModuleSet.remove(moduleDependName);
						continue;
					}
					handlerModule(isDepend, moduleDependName, handlerMethod, configs, alreadyHandleModuleSet, waitHandleModuleSet);
				}
			}
			
			try {
				long begin = System.currentTimeMillis();
				// do handler
				handlerMethod.doHandler(getHandler(moduleBean, configs));
				log.debug("[moduleName={0}] handler spends {1}ms.", new Object[]{moduleName,System.currentTimeMillis() - begin});
			} catch (Exception ignore) {
				log.warn("[moduleName={0}] handle error!", new Object[]{moduleName}, ignore);
			}

			alreadyHandleModuleSet.add(moduleName);
			waitHandleModuleSet.remove(moduleName);
		}
	}

	/**
	 * 
	 * @param configs
	 */
	public static void reloadAndApply(Configuration[] configs) {
		for (Configuration config : configs) {
			config.reload();
		}
		doHandler(configs, new HandlerExecutor() {
			public void doHandler(AbstractConfigurationHandler<IConfigModel> handler) {
				if (handler != null) {
					handler.doApply(handler.doLoad());
				}
			}
		}, true);
	}

	/**
	 * 
	 * @param configs
	 */
	public static void unLoad(Configuration[] configs) {
		doHandler(configs, new HandlerExecutor() {
			public void doHandler(AbstractConfigurationHandler<IConfigModel> handler) {
				if (handler != null) {
					IConfigModel model=handler.doLoad();
					handler.toUnload(model);
					handler.stop(model);
				}
			}
		}, false);
	}

	/**
	 * 
	 * @param moduleBean
	 * @param configs
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static AbstractConfigurationHandler<IConfigModel> getHandler(ModuleBean moduleBean, Configuration[] configs) {
		if (moduleBean.getModuleHandlerObject() == null) {
			Class handlerClazz = moduleBean.getModuleHandler();
			if (handlerClazz != null) {
				try {
					moduleBean.setModuleHandlerObject(
									(AbstractConfigurationHandler<IConfigModel>) handlerClazz.getConstructor(Configuration[].class)
									.newInstance(new Object[] { configs }));
				} catch (Exception ignore) {
					log.warn("[ModuleName={0}, ModuleHandler={1}] cannot load to a instance!", new Object[] { moduleBean.getModuleName(),
									handlerClazz.getName() }, ignore);
				}
			}
		}
		if (moduleBean.getModuleHandlerObject() != null) {
			moduleBean.getModuleHandlerObject().setConfigurations(configs);
		}
		return moduleBean.getModuleHandlerObject();
	}

}
