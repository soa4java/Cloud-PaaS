/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用包部署辅助类
 * 
 * @author liming (mailto:li-ming@primeton.com)
 */
public class DeployHelper {

	private static ILogger logger = LoggerFactory.getLogger(DeployHelper.class);

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory
			.getServiceQuery();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	/**
	 * 比较两个版本的大小。<BR>
	 * 
	 * @param version1
	 * @param version2
	 * @return 1 0 -1
	 */
	public static int compareVersion(String version1, String version2) {
		if (null == version1 || null == version2) {
			return 0;
		}
		StringTokenizer st1 = new StringTokenizer(version1, ".");
		StringTokenizer st2 = new StringTokenizer(version2, ".");

		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();

		while (st1.hasMoreTokens()) {
			al1.add(st1.nextToken());
		}
		while (st2.hasMoreTokens()) {
			al2.add(st2.nextToken());
		}

		int size1 = al1.size();
		int size2 = al2.size();

		for (int i = 0; (i < size1) && (i < size2); i++) {
			int v1 = Integer.parseInt((String) al1.get(i));
			int v2 = Integer.parseInt((String) al2.get(i));

			if (v1 > v2) {
				return 1;
			}
			if (v1 < v2) {
				return -1;
			}
		}

		if (size1 > size2) {
			return 1;
		}
		if (size1 < size2) {
			return -1;
		}
		return 0;
	}

	/**
	 * 获取最新版本应用包 <br>
	 * 
	 * @param appName 应用名称
	 * @return WarService
	 * @throws ServiceException
	 */
	public static WarService getLastestWar(String appName) {
		ICluster webAppDef = clusterManager.getByApp(appName, WarService.TYPE);
		String warclusterId = webAppDef.getId();
		List<WarService> warServices = srvQueryMgr.getByCluster(warclusterId,WarService.class);

		if (warServices == null || warServices.size()<1) {
			return null;
		}
		
		WarService lastestWar = warServices.get(0);
		String latestVersion = null;
		
		for (WarService war : warServices) {
			String warVersion = war.getWarVersion();
			if (warVersion == null) {
				continue;
			} else if (latestVersion == null || compareVersion(warVersion, latestVersion) == 1) {
				latestVersion = warVersion;
				lastestWar = war;
			}
		}
		return lastestWar;
	}

	/**
	 * 获取指定版本应用包 <br>
	 * 
	 * @param appName
	 *            应用名称
	 * @param versionSign
	 *            版本号
	 * @return WarService
	 * @throws ServiceException
	 */
	public static WarService getSpecificWar(String appName, String versionSign)	throws PaasRuntimeException {
		ICluster warCluster = clusterManager.getByApp(appName, WarService.TYPE);
		
		String warclusterId = warCluster.getId();
		List<WarService> warDefs = srvQueryMgr.getByCluster(warclusterId,WarService.class);

		WarService specificWar = null;
		for (WarService warDef : warDefs) {
			if (versionSign.equals(warDef.getWarVersion())) {
				specificWar = warDef;
				break;
			}
		}

		return specificWar;
	}

	/**
	 * 获取已部署的应用包 <br>
	 * 
	 * @param appName
	 *            应用名称
	 * @return WarService
	 * @throws ServiceException
	 */
	public static WarService getDeployedWar(String appName)
			throws ServiceException {
		ICluster webAppDef = clusterManager.getByApp(appName, WarService.TYPE);
		String warclusterId = webAppDef.getId();
		List<WarService> warDefs = srvQueryMgr.getByCluster(warclusterId,
				WarService.class);

		WarService deployedWar = null;
		for (WarService warDef : warDefs) {
			if (warDef.isDeployVersion()) {
				deployedWar = warDef;
			}
		}

		return deployedWar;
	}

	/**
	 * 获取新版本编号 <br>
	 * 
	 * @param appName 应用名称
	 * @return 版本号
	 * @throws ServiceException
	 */
	public static String getNewVersion(String appName) {
		WarService lastestWar = getLastestWar(appName);
		if (lastestWar == null)
			return VersionUtil.getInitVersion();
		String lastestVersion = lastestWar.getWarVersion();

		if (StringUtil.isEmpty(lastestVersion)) {
			return VersionUtil.getInitVersion();
		} else {
			return VersionUtil.addFromOldVersion(lastestVersion);
		}
	}

	/**
	 * 检查应用部署类型 <br>
	 * 
	 * @param appName
	 *            应用名称
	 * @param versionSign
	 *            版本号
	 * @param deployWay
	 *            部署方式
	 * @return
	 * @throws ServiceException
	 */
	public static String checkWarDeployState(String appName, String versionSign, String deployWay) throws ServiceException {
		WarService lastestWar = getLastestWar(appName);
		if (lastestWar == null)
			return AppConstants.DEPLOY_STATUS_CREATE_NEW_VERSION;
		String lastestVersion = lastestWar.getWarVersion();
		// 版本号为空
		if (StringUtil.isEmpty(versionSign)) {
			// 如果使用默认提交方式，并且流程已经存在则覆盖最新版本
			if (AppConstants.DEPLOY_WAY_DEFAULT.equals(deployWay)) {
				if (StringUtil.isEmpty(lastestVersion)) {
					return AppConstants.DEPLOY_STATUS_CREATE_NEW_VERSION;
				} else {
					return AppConstants.DEPLOY_STATUS_OVERLAP_LASTEST_VERSION;
				}
			} else {
				return AppConstants.DEPLOY_STATUS_CREATE_NEW_VERSION;
			}
		}

		int compRet = compareVersion(versionSign, lastestVersion);
		if (compRet == 1) {
			return AppConstants.DEPLOY_STATUS_CREATE_NEW_VERSION;
		} else {
			return AppConstants.DEPLOY_STATUS_OVERLAP_OLD_VERSION;
		}
	}
	
	/**
	 * 获取应用关联的war集群<br>
	 * 
	 * @param appName 应用名称
	 * @return
	 */
	public static ICluster getWarCluster(String appName){
		ICluster warCluster = clusterManager.getByApp(appName, WarCluster.TYPE);
		if (warCluster != null) {
			return warCluster;
		}
		WarCluster cluster = new WarCluster();
		cluster.setMaxSize(1);
		cluster.setMinSize(1);
		cluster.setName("war-" + appName); //$NON-NLS-1$
		IClusterManager warClusterManager = ClusterManagerFactory.getManager(WarService.TYPE);
		try {
			warCluster = warClusterManager.create(cluster);
		} catch (ClusterException e) {
			logger.error(e);
			return null;
		}
		return warCluster;
	}
	
}
