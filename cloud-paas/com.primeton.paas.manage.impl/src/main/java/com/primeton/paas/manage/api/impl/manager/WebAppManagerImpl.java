/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.common.api.model.PageCond;
import org.gocom.cloud.cesium.config.api.client.ConfigManager;
import org.gocom.cloud.cesium.config.api.model.ConfigItem;
import org.gocom.cloud.cesium.config.template.api.ConfigTemplateManager;
import org.gocom.cloud.cesium.manage.app.api.ApplicationManager;
import org.gocom.cloud.cesium.manage.app.api.exception.ApplicationManagerException;
import org.gocom.cloud.cesium.manage.app.api.factory.ApplicationManagerFactory;
import org.gocom.cloud.cesium.model.api.Application;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.api.ServiceExtensionLoader;

import com.primeton.paas.app.config.ZkConstants;
import com.primeton.paas.app.config.model.ServiceSourceModel;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.exception.WebAppException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.util.CesiumFactory;
import com.primeton.paas.manage.api.impl.util.ObjectUtil;
import com.primeton.paas.manage.api.listener.AppEventListener;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class WebAppManagerImpl implements IWebAppManager {
	
	private ApplicationManager applicationManager = null;
	private IClusterManager clusterManager = null;
	private ConfigManager configManager = null;
	private ConfigTemplateManager configTemplateManager = null;
	
	private static ILogger logger = ManageLoggerFactory.getLogger(WebAppManagerImpl.class);
	
	/**
	 * Default. <br>
	 */
	public WebAppManagerImpl() {
		super();
		applicationManager = ApplicationManagerFactory.createApplicationManager();
		clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
		configManager = CesiumFactory.getConfigManager(); // cesium
		configTemplateManager = CesiumFactory.getConfigTemplateManager(); // cesium
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#add(com.primeton.paas.manage.api.model.WebApp)
	 */
	public void add(WebApp webApp) throws WebAppException {
		if (webApp == null || StringUtil.isEmpty(webApp.getName())) {
			return;
		}
		WebApp app = get(webApp.getName());
		if (app != null) {
			throw new WebAppException(WebAppException.WEB_APP_EXISTS + " {"
					+ app.toString() + "}");
		}
		Application application = ObjectUtil.toCesium(webApp);
		try {
			applicationManager.saveApp(application);
		} catch (ApplicationManagerException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			throw new WebAppException(e);
		}
		List<AppEventListener> listeners = load();
		for (AppEventListener listener : listeners) {
			try {
				listener.doCreate(webApp);
			} catch (Throwable t) {
				logger.error(t);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#update(com.primeton.paas.manage.api.model.WebApp)
	 */
	public void update(WebApp webApp) throws WebAppException {
		if (webApp == null || StringUtil.isEmpty(webApp.getName())) {
			return;
		}
		WebApp app = get(webApp.getName());
		if (app == null) {
			throw new WebAppException(WebAppException.WEB_APP_NOT_EXISTS + " {"
					+ webApp.getName() + "}");
		}
		Application application = ObjectUtil.toCesium(webApp);
		try {
			applicationManager.saveApp(application);
		} catch (ApplicationManagerException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			throw new WebAppException(e);
		}
		
		List<AppEventListener> listeners = load();
		for (AppEventListener listener : listeners) {
			try {
				listener.doModify(app, webApp);
			} catch (Throwable t) {
				logger.error(t);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#remove(java.lang.String)
	 */
	public void delete(String name) {
		if (StringUtil.isEmpty(name)) {
			return;
		}
		WebApp webApp = get(name);
		if (webApp == null) {
			return;
		}
		List<AppEventListener> listeners = load();
		for (AppEventListener listener : listeners) {
			try {
				listener.doDestroy(webApp);
			} catch (Throwable t) {
				logger.error(t);
			}
		}
		try {
			ICluster[] clusters = clusterManager.getByApp(name);
			if(clusters != null && clusters.length > 0) {
				for (ICluster cluster : clusters) {
					unbind(name, cluster.getId());
				}
			}
			applicationManager.deleteApp(name);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#get(java.lang.String)
	 */
	public WebApp get(String name) {
		if(StringUtil.isEmpty(name)) {
			return null;
		}
		try {
			Application application = applicationManager.getApp(name);
			return ObjectUtil.toPAAS(application);
		} catch (Throwable t) {
			if(logger.isErrorEnabled()) {
				logger.error(t);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#getAll()
	 */
	public WebApp[] getAll() {
		List<WebApp> list = new ArrayList<WebApp>();
		try {
			List<Application> applications = applicationManager.getAllApps();
			if(applications != null) {
				for (Application application : applications) {
					if(application != null) {
						list.add(ObjectUtil.toPAAS(application));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return list.toArray(new WebApp[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#getAll(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public WebApp[] getAll(IPageCond pageCond) {
		if (pageCond == null) {
			return getAll();
		}
		return getByOwner(null, pageCond);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#getByOwner(java.lang.String)
	 */
	public WebApp[] getByOwner(String owner) {
		List<WebApp> list = new ArrayList<WebApp>();
		try {
			List<Application> applications = applicationManager.getApps(owner);
			if (applications != null) {
				for (Application application : applications) {
					if (application != null) {
						list.add(ObjectUtil.toPAAS(application));
					}
				}
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return list.toArray(new WebApp[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#getByOwner(java.lang.String, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public WebApp[] getByOwner(String owner, IPageCond pageCond) {
		if (pageCond == null) {
			return getByOwner(owner);
		}
		PageCond pc = new PageCond(pageCond.getBegin(), pageCond.getLength(), 0);
		List<WebApp> list = new ArrayList<WebApp>();
		try {
			List<Application> applications = applicationManager.getApps(owner, pc);
			if(applications != null) {
				for (Application application : applications) {
					if(application != null) {
						list.add(ObjectUtil.toPAAS(application));
					}
				}
				pageCond.setCount(pc.getCount());
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return list.toArray(new WebApp[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#bind(java.lang.String, java.lang.String)
	 */
	public void bind(String appName, String clusterId) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(clusterId)) {
			return;
		}
		WebApp webApp = get(appName);
		if (webApp == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Application [name:" + appName
						+ "] not exists, can not bind Cluster [id:" + clusterId
						+ "].");
			}
			return;
		}
		ICluster cluster = clusterManager.get(clusterId);
		if (cluster == null) {
			logger.debug("Cluster [id:" + clusterId
					+ "] not exists, Application [name:" + appName
					+ "] can not bind it.");
			return;
		}
		try {
			applicationManager.addAppClusterRel(appName, new String[] { clusterId });
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IWebAppManager#unbind(java.lang.String, java.lang.String)
	 */
	public void unbind(String appName, String clusterId) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(clusterId)) {
			return;
		}
		try {
			applicationManager.deleteAppClusterRel(appName,
					new String[] { clusterId });
			logger.info("Application [name:" + appName
					+ "] unbind cluster [id:" + clusterId + "]");
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWebAppManager#getRelationApp(java.lang.String)
	 */
	public String[] getRelationApp(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}
		try {
			List<String> list = applicationManager.getRelAppNames(clusterId);
			if (list != null && list.size() > 0) {
				return list.toArray(new String[list.size()]);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWebAppManager#initAppConfig(java.lang.String)
	 */
	public void initAppConfig(String appName) throws ConfigureException {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		try {
			configTemplateManager.initAppConfigFromTemplate(ZkConstants.APP_TYPE_DEFAULT, appName);
			ICluster[] clusters = clusterManager.getByApp(appName);
			if(clusters != null && clusters.length > 0) {
				for (ICluster cluster : clusters) {
					ServiceSourceModel model = new ServiceSourceModel(cluster.getType(), cluster.getId());
					ConfigItem item = new ConfigItem(cluster.getId());
					item.setValue(model);
					configManager.setAppConfigItem(appName, ZkConstants.CONFIG_MODULE_SERVICE_SOURCE, item);
				}
			}
		} catch (TimeoutException e) {
			throw new ConfigureException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWebAppManager#refreshAppService(java.lang.String)
	 */
	public void refreshAppService(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		configManager.removeAppModule(appName,
				ZkConstants.CONFIG_MODULE_SERVICE_SOURCE);
		ICluster[] clusters = clusterManager.getByApp(appName);
		if (clusters != null && clusters.length > 0) {
			for (ICluster cluster : clusters) {
				ServiceSourceModel model = new ServiceSourceModel(
						cluster.getType(), cluster.getId());
				ConfigItem item = new ConfigItem(cluster.getId());
				item.setValue(model);
				configManager.setAppConfigItem(appName,
						ZkConstants.CONFIG_MODULE_SERVICE_SOURCE, item);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWebAppManager#deleteAppConfig(java.lang.String)
	 */
	public void deleteAppConfig(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		configManager.removeAllAppModules(appName);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IWebAppManager#deleteAppConfigModule(java.lang.String, java.lang.String)
	 */
	public void deleteAppConfigModule(String appName, String module) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(module)) {
			return;
		}
		configManager.removeAppModule(appName, module);
	}

	/**
	 * 
	 * @return
	 */
	private List<AppEventListener> load() {
		List<AppEventListener> listeners = new ArrayList<AppEventListener>();
		ServiceExtensionLoader<AppEventListener> loader = ServiceExtensionLoader
				.load(AppEventListener.class);
		if (loader != null) {
			Iterator<AppEventListener> iterator = loader.iterator();
			while (iterator.hasNext()) {
				AppEventListener listener = iterator.next();
				if (listener != null) {
					listeners.add(listener);
				}
			}
		}
		return listeners;
	}

}
