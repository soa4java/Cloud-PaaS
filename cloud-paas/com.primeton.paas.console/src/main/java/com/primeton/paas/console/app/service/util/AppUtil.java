/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.TomcatService;

/**
 * 应用监控的 公共查询类
 * 
 * @author liming(li-ming@primeton.com)
 */
public class AppUtil {

	private static ILogger logger = LoggerFactory.getLogger(AppUtil.class);

	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 
	 * @param owner
	 * @return
	 */
	public static WebApp[] getWebapps(String owner) {
		try {
			WebApp[] apps = appManager.getByOwner(owner, null);
			return apps;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	public static List<WebApp> queryOpendApps(String userId, PageCond page) {
		List<WebApp> applist = new ArrayList<WebApp>();
		WebApp[] apps = appManager.getByOwner(userId, page);
		if (apps == null || apps.length <= 0) {
			return applist;
		}
		for (WebApp webApp : apps) {
			if (webApp.getState() == WebApp.STATE_OPEND) {
				applist.add(webApp);
			}
		}
		if (null != page) {
			page.setCount(applist.size());
		}
		return applist;
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static String queryServerType(String appName) {
		ICluster[] clusters = clusterManager.getByApp(appName);
		for (ICluster c : clusters) {
			if (JettyService.TYPE.equals(c.getType())
					|| TomcatService.TYPE.equals(c.getType())) {
				return c.getType();
			}
		}
		return null;
	}

}
