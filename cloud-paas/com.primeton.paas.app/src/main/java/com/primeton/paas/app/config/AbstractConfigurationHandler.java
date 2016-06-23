/**
 * 
 */
package com.primeton.paas.app.config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.Configuration.Module;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 * @param <M> IConfigModel
 */
public abstract class AbstractConfigurationHandler<M extends IConfigModel> implements IConfigHandler<M> {
	
	private static ILogger log = SystemLoggerFactory.getLogger(AbstractConfigurationHandler.class);
	
	protected Configuration[] _configs;
   	
   	private Module[] _moduleCurrent = null;
	
	private String _moduleName;
	
	/**
	 * 
	 * @param configs
	 */
	@SuppressWarnings("unchecked")
	protected AbstractConfigurationHandler(Configuration[] configs) {
		init(configs, (Class<? extends AbstractConfigurationHandler<? extends IConfigModel>>)this.getClass());	
	}
	
	/**
	 * 
	 * @param fileLoactions
	 */
	@SuppressWarnings("unchecked")
	protected AbstractConfigurationHandler(String[] fileLoactions) {
		List<Configuration> configs = new ArrayList<Configuration>();
		for (int i = 0; i < fileLoactions.length; i++) {
			configs.add(Configuration.initConfiguration(fileLoactions[i]));
		}
		init(configs.toArray(new Configuration[0]), (Class<? extends AbstractConfigurationHandler<? extends IConfigModel>>)this.getClass());
	}
	
	private void init(Configuration[] configs, Class<? extends AbstractConfigurationHandler<? extends IConfigModel>> configHandlerClazz) {
		_configs = configs;
		_moduleName = getModuleName(configHandlerClazz);
		_moduleCurrent = getModules(_configs, _moduleName);
	}
	
	private static String getModuleName(Class<? extends AbstractConfigurationHandler<? extends IConfigModel>> configHandlerClazz) {
		String qModuleNames = ModuleHandlerRegister.getModuleBean(configHandlerClazz).getModuleName();
		int index = qModuleNames.indexOf(".");
		if (index != -1) {
			return qModuleNames.substring(index + 1);
		} else {
			return qModuleNames;
		}
	}
	
	/**
	 * 
	 * @param _configs
	 * @param moduleName
	 * @return
	 */
	private static Module[] getModules(Configuration[] _configs, String moduleName) {
		Set<Module> moduleSet = new LinkedHashSet<Module>();
		for (int i = 0; i < _configs.length; i++) {
			Module module = _configs[i].getModule(moduleName);
			if (module == null) {
				continue;
			}
			module = module.clone();
			module.setName(i + "." + moduleName);
			moduleSet.add(module);
		}
		return moduleSet.toArray(new Module[0]);
	}
	
	/**
	 * 
	 */
	public M doLoad() {
		long begin = System.currentTimeMillis();
		log.debug("AbstractConfigurationHandler doLoad begin.");
		Module[] modules = getModules(_configs, _moduleName);
		if (_configs == null || _configs.length == 0 || modules.length == 0) {
			return (M)null;
		}
		M model = toModel(modules);
		
		log.debug("AbstractConfigurationHandler doLoad end.({0}ms)", new Object[]{System.currentTimeMillis() - begin});
		return model;
	}
	
	/**
	 * 
	 */
	public void deleteItems(M model,String[] itemIds) {
		if (model == null) {
			return;
		}
		long begin = System.currentTimeMillis();
		log.debug("AbstractConfigurationHandler deleteItem model {0} begin.",new Object[] {model.toString()} );
		toDeleteItems(model,itemIds);
		Module[] modules = getModules(_configs, _moduleName);
		deleteGroupsFromModule(modules, itemIds);
	
		doSave(modules);

		log.debug("AbstractConfigurationHandler deleteItem model {0} end.({1}ms)", new Object[]{model.toString(),System.currentTimeMillis() - begin});
	}
	
	/**
	 * 
	 * @param modules
	 */
	private void doSave(Module[] modules) {
		for (Module module : modules) {
			String bakModuleName = module.getName();
			int indexDELIMETER = bakModuleName.indexOf(".");			
			int i = 0;
			if (indexDELIMETER <= 0) {
				continue;
			} else {
				i = Integer.parseInt(bakModuleName.substring(0, indexDELIMETER));
			}
			module.setName(_moduleName);
			_configs[i].setModule(module.clone());
			_configs[i].save();
			module.setName(bakModuleName);
		}	
	}
	
	/**
	 * 
	 * @param configs
	 */
	public void setConfigurations(Configuration[] configs) {
		_configs = configs;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.app.config.IConfigHandler#updateModel(com.primeton.paas.app.config.IConfigModel)
	 */
	public void updateModel(M model) {
		if (model == null) {
			return;
		}
		long begin = System.currentTimeMillis();
		log.debug("AbstractConfigurationHandler doUpdate model {0} begin.",new Object[] {model.toString()} );
		Module[] modulesNew=doApply(model);
		doSave(modulesNew);
		log.debug("AbstractConfigurationHandler doUpdate model {0} end.({1}ms)", new Object[]{model.toString(),System.currentTimeMillis() - begin});
	}	

	/**
	 * 
	 * @param model
	 * @return
	 */
	public Module[] doApply(M model) {
		if (model == null) {
			return null;
		}
		long begin = System.currentTimeMillis();
		log.debug("AbstractConfigurationHandler doApply begin.");
		Module[] modulesNew = cloneModule(_moduleCurrent);
		Module[] modulesOld = cloneModule(_moduleCurrent);
		
		toModule(model, modulesNew);
		boolean isModify = false;
		for (int i = 0; i < modulesNew.length; i++) {
			modulesOld[i].setName(modulesNew[i].getName());
			if (!modulesOld[i].equals(modulesNew[i])) {
				isModify = true;
				break;
			}			
		}
		
		if (isModify) {			
			toReload(model);
			_moduleCurrent = cloneModule(modulesNew);
		}
		log.debug("AbstractConfigurationHandler doApply end.({0})", new Object[]{System.currentTimeMillis() - begin});
		return modulesNew;
	}	
	
	/**
	 * 
	 * @param modules
	 * @return
	 */
	private static Module[] cloneModule(Module[] modules) {
		Module[] clone = new Module[modules.length];
		for (int i = 0; i < modules.length; i++) {
			if (modules[i] != null) {
				clone[i] = modules[i].clone();
			}
		}
		return clone;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getConfigDirPath() {
		if (_configs.length > 0) {
			return new java.io.File(_configs[0].getConfigFilePath()).getParent();
		} else {
			return null;
		}
	}
	
    /**
     * 
     * @param modules
     * @return
     */
	abstract public M toModel(final Module[] modules);
	
	/**
	 * 
	 * @param model
	 * @param modules
	 */
	abstract public void toModule(M model, final Module[] modules);
	
	/**
	 * 
	 * @param modules
	 * @param deletedGroupNames
	 */
	public void deleteGroupsFromModule(final Module[] modules,String[] deletedGroupNames) {
		for(Module md:modules) {
			for(String deletedGroupName:deletedGroupNames) {
				md.deleteGroup(deletedGroupName);
			}
		}
	}

	/**
	 * 
	 * @param model
	 */
	abstract public void toLoad(M model);
	
	/**
	 * 
	 * @param model
	 */
	public void toReload(M model) {
		toUnload(model);
		toLoad(model);
	}	
	
	/**
	 * 
	 * @param model
	 */
	abstract public void toUnload(M model);
	
	/**
	 * 
	 * @param model
	 * @param itemIds
	 */
	abstract public void toDeleteItems(M model,String[] itemIds);
	
	/**
	 * 
	 * @param model
	 */
	public void start(M model) {
		// override
	}
	
	/**
	 * 
	 * @param model
	 */
	public void stop(M model) {
		// override
	}
	
}
