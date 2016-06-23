/**
 * 
 */
package com.primeton.paas.openapi.admin.config.impl;

import java.util.HashSet;
import java.util.Set;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.config.AbstractConfigurationHandler;
import com.primeton.paas.openapi.admin.config.Configuration;
import com.primeton.paas.openapi.admin.config.IConfigModel;
import com.primeton.paas.openapi.admin.config.ModuleBean;
import com.primeton.paas.openapi.admin.config.ModuleHandlerRegister;


/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConfigurationLoader {
	
	private static ILogger log = LoggerFactory.getLogger(ConfigurationLoader.class);
	
	/**
	 * 
	 * @param configs
	 */
	public static void initLoad(Configuration[] configs) {
		log.debug("Begin to load configurations");
		doHandler(configs, new HandlerExecutor() {
			public void doHandler(AbstractConfigurationHandler<IConfigModel> handler) {
				if (handler != null) {
					IConfigModel model=handler.doLoad();
					handler.start(model); 
					handler.toLoad(model); 
				}
			}
		}, true);
		log.debug("Finished loading configurations");
	}

	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	interface HandlerExecutor {
		
		void doHandler(AbstractConfigurationHandler<IConfigModel> handler);
		
	}

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
