/**
 * 
 */
package com.primeton.paas.openapi.admin.config;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ModuleBean {
	
	private String moduleName;
	
	private Set<String> dependModuleNameSet = new LinkedHashSet<String>();
	
	private Set<String> dependedModuleNameSet = new LinkedHashSet<String>();
	
	private Class<?> moduleHandler;
	
	private AbstractConfigurationHandler<IConfigModel> moduleHandlerObject;

	public ModuleBean(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return Returns the moduleName.
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @return Returns the dependModuleNameSet.
	 */
	public Set<String> getDependModuleNameSet() {
		return dependModuleNameSet;
	}

	/**
	 * @param dependModuleNameSet The dependModuleNameSet to set.
	 */
	public void addDependModuleNameSet(Set<String> dependModuleNameSet) {
		this.dependModuleNameSet.addAll(dependModuleNameSet);
	}
	
	/**
	 * @param dependModuleNameSet The dependModuleNameSet to set.
	 */
	public void addDependModuleNameSet(String[] dependModuleNames) {
		if (dependModuleNameSet != null){
			for (String moduleName : dependModuleNames) {
				this.dependModuleNameSet.add(moduleName);
			}
		}
	}
	
	/**
	 * 
	 * @param moduleName
	 */
	public void addDependModuleName(String moduleName) {
		if (moduleName != null){
			this.dependModuleNameSet.add(moduleName);
		}
	}

	/**
	 * @return Returns the dependedModuleNameSet.
	 */
	public Set<String> getDependedModuleNameSet() {
		return dependedModuleNameSet;
	}

	/**
	 * @param dependedModuleNameSet The dependedModuleNameSet to set.
	 */
	public void addDependedModuleNameSet(Set<String> dependedModuleNameSet) {
		this.dependedModuleNameSet.addAll(dependedModuleNameSet);
	}
	
	/**
	 * @param dependedModuleNameSet The dependedModuleNameSet to set.
	 */
	public void addDependedModuleName(String moduleName) {
		this.dependedModuleNameSet.add(moduleName);
	}

	/**
	 * @return Returns the moduleHandler.
	 */
	public Class<?> getModuleHandler() {
		return moduleHandler;
	}

	/**
	 * @param moduleHandler The moduleHandler to set.
	 */
	public void setModuleHandler(Class<?> moduleHandler) {
		this.moduleHandler = moduleHandler;
	}

	/**
	 * @return Returns the moduleHandlerObject.
	 */
	public AbstractConfigurationHandler<IConfigModel> getModuleHandlerObject() {
		return moduleHandlerObject;
	}

	/**
	 * @param moduleHandlerObject The moduleHandlerObject to set.
	 */
	public void setModuleHandlerObject(
			AbstractConfigurationHandler<IConfigModel> moduleHandlerObject) {
		this.moduleHandlerObject = moduleHandlerObject;
	}
	
}