/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.app.service.util.AppConstants;
import com.primeton.paas.console.app.service.util.AppUtil;
import com.primeton.paas.console.app.service.util.DeployHelper;
import com.primeton.paas.console.app.service.util.FileUtil;
import com.primeton.paas.console.app.service.util.VersionUtil;
import com.primeton.paas.console.common.DataContextManager;
import com.primeton.paas.console.common.IUserObject;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.exception.api.PaasRuntimeException;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.SVNRepositoryCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.DeployException;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IJettyServiceManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.manager.ITomcatServiceManager;
import com.primeton.paas.manage.api.manager.IWarServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.JettyService;
import com.primeton.paas.manage.api.service.SVNRepositoryService;
import com.primeton.paas.manage.api.service.SVNService;
import com.primeton.paas.manage.api.service.TomcatService;
import com.primeton.paas.manage.api.service.WarService;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.rr.api.IRrClient;
import com.primeton.paas.rr.api.IRrEntry;
import com.primeton.paas.rr.api.RrClientFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appDeploy")
public class AppDeployResource {
	
	private static ILogger logger = LoggerFactory.getLogger(AppDeployResource.class);
	
	// private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	private static IServiceQuery srvQueryMgr = ServiceManagerFactory.getServiceQuery();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager();

	private static IWarServiceManager warServiceManager = ServiceManagerFactory
			.getManager(WarService.TYPE);

	private static IJettyServiceManager jettyServiceMgr = ServiceManagerFactory
			.getManager(JettyService.TYPE);
	
	private static ITomcatServiceManager tomcatServiceMgr = ServiceManagerFactory
			.getManager(TomcatService.TYPE);

	private static IWarServiceManager warServiceMgr = ServiceManagerFactory
			.getManager(WarService.TYPE);

	private static IClusterManager jettyClusterMgr = ClusterManagerFactory
			.getManager(JettyService.TYPE);

	private static IClusterManager tomcatClusterMgr = ClusterManagerFactory
			.getManager(TomcatService.TYPE);

	/**
	 * 查询应用版本 <br>
	 * 
	 * @param appName
	 *            应用名称
	 * @return 应用部署版本列表
	 */
	public static List<WarService> queryAppVersion(String appName) {
		List<WarService> versions = new ArrayList<WarService>();
		if (StringUtil.isEmpty(appName))
			return versions;

		ICluster warCluster = clusterManager.getByApp(appName, WarCluster.TYPE);

		if (warCluster == null) {
			return versions;
		}

		versions = srvQueryMgr.getByCluster(warCluster.getId(),
				WarService.class);
		return versions;
	}

	/**
	 * 卸载应用版本 <br>
	 * 
	 * @param appName 应用名称
	 * @param versionSign 版本号
	 */
	public static boolean uninstallAppVersion(String appName, String versionSign) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(versionSign))
			return false;

		logger.info("Begin Uninstall App {0}, versionSign {1}.", new Object[] { appName, versionSign });

		ICluster jettyCluster = clusterManager.getByApp(appName,
				JettyCluster.TYPE);
		if (jettyCluster == null) {
			return uninstallTomcatAppVersion(appName, versionSign);
		}

		String jettyClusterId = jettyCluster.getId();
		List<JettyService> services = srvQueryMgr.getByCluster(jettyClusterId,
				JettyService.class);
		if (services == null || services.isEmpty()) {
			return false;
		}

		try {
			jettyClusterMgr.stop(jettyClusterId);
			jettyServiceMgr.undeploy(services.get(0).getId());
			ICluster warCluster = clusterManager.getByApp(appName,
					WarCluster.TYPE);
			if (warCluster == null) {
				return true;
			}
			// Update warService(isdeployFlag)
			String warClusterId = warCluster.getId();
			List<WarService> versions = srvQueryMgr.getByCluster(
					warCluster.getId(), WarService.class);
			if (versions != null && !versions.isEmpty()) {
				for (WarService war : versions) {
					// 更改warService信息
					String instId = war.getId();
					warServiceMgr.setWarDeployFlag(warClusterId, instId, false);
				}
			}
			// Start Jetty Cluster
			jettyClusterMgr.start(jettyClusterId);
		} catch (Throwable e) {
			logger.error("Undeploy app {0} version {1} error.", new Object[] { appName, versionSign }, e);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param appName
	 * @param versionSign
	 * @return
	 */
	public static boolean uninstallTomcatAppVersion(String appName,
			String versionSign) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(versionSign)) {
			return false;
		}
		ICluster tomcatCluster = clusterManager.getByApp(appName,
				TomcatCluster.TYPE);
		if (tomcatCluster == null) {
			return false;
		}
		String tomcatClusterId = tomcatCluster.getId();
		List<TomcatService> services = srvQueryMgr.getByCluster(tomcatClusterId,
				TomcatService.class);
		if (services == null || services.isEmpty()) {
			return false;
		}
		try {
			tomcatClusterMgr.stop(tomcatClusterId);
			for (TomcatService service : services) {
				try {
					tomcatServiceMgr.undeploy(service.getId());
				} catch (Throwable t) {
					logger.error("Undeploy app {0} version {1} error", new Object[] { appName, versionSign }, t);
				}
			}
			ICluster warCluster = clusterManager.getByApp(appName,
					WarCluster.TYPE);
			if (warCluster == null) {
				return true;
			}
			String warClusterId = warCluster.getId();
			List<WarService> versions = srvQueryMgr.getByCluster(
					warCluster.getId(), WarService.class);
			if (versions != null && !versions.isEmpty()) {
				for (WarService war : versions) {
					// 更改warService信息
					String instId = war.getId();
					warServiceMgr.setWarDeployFlag(warClusterId, instId, false);
				}
			}
			tomcatClusterMgr.start(tomcatClusterId);
		} catch (Throwable e) {
			logger.error("Undeploy app {0} version {1} error", new Object[] { appName, versionSign }, e);
			return false;
		}
		return true;
	}

	/**
	 * 删除应用版本
	 * 
	 * @param appName 应用名称
	 * @param versionSign 版本号
	 */
	public static boolean removeAppVersion(String appName, String versionSign) {
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(versionSign))
			return false;
		try {
			List<WarService> warSrvs = queryAppVersion(appName);
			WarService targetWar = null;

			if (warSrvs != null && !warSrvs.isEmpty()) {
				for (WarService war : warSrvs) {
					String curVersion = war.getWarVersion();
					if (versionSign.equals(curVersion)) {
						targetWar = war;
						if (war.isDeployVersion()) {
							uninstallAppVersion(appName, versionSign);
						}
						break;
					}
				}
			}
			if (targetWar != null) {
				String targetWarId = targetWar.getId();
				warServiceMgr.destroy(targetWarId);
			}
			return true;
		} catch (Throwable e) {
			logger.error("Undeploy app {0} version {1} error.", new Object[] { appName, versionSign }, e);
			return false;
		}
	}

	/**
	 * 部署应用（指定版本的应用）
	 * 
	 * @param appName 应用名称
	 * @param warVersion 版本号
	 */
	public static DeployResult deployAppVersion(String appName,
			String warVersion) {
		DeployResult result = new DeployResult();
		result.setAppName(appName);
		result.setWarVersion(warVersion);
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(warVersion)) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Parameter (appName or warVersion) is null");
			return result;
		}
		ICluster jettyCluster = clusterManager.getByApp(appName,
				JettyCluster.TYPE);
		ICluster svnRepoCluster = clusterManager.getByApp(appName,
				SVNRepositoryCluster.TYPE);
		if (jettyCluster == null) {
			return deployTomcatAppVersion(appName, warVersion);
		}
		if (svnRepoCluster == null) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s JettyCluster or SVNRepoCluster. Deploy cancelled.");
			return result;
		}
		// Get JettyService
		String jettyClusterId = jettyCluster.getId();
		List<JettyService> services = srvQueryMgr.getByCluster(jettyClusterId,
				JettyService.class);
		if (services == null || services.isEmpty()) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s JettyService. Deploy cancelled.");
			return result;
		}

		// Get SVNRepositoryService
		String svnRepoClusterId = svnRepoCluster.getId();
		List<SVNRepositoryService> svnRepos = srvQueryMgr.getByCluster(
				svnRepoClusterId, SVNRepositoryService.class);
		if (svnRepos == null || svnRepos.isEmpty()) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s SVNRepositoryService. Deploy cancelled.");
			return result;
		}
		SVNRepositoryService svnRepoService = svnRepos.get(0);
		String svnRepoId = svnRepoService.getId();

		// Get WarService
		ICluster warCluster = clusterManager.getByApp(appName, WarService.TYPE);
		String warclusterId = warCluster == null ? null : warCluster.getId();
		List<WarService> warDefs = srvQueryMgr.getByCluster(warclusterId,
				WarService.class);
		WarService warService = null;

		try {
			for (WarService warDef : warDefs) {
				warServiceMgr.setWarDeployFlag(warclusterId, warDef.getId(),
						false);
			}
		} catch (Throwable e) {
			logger.error(e);
		}

		for (WarService warDef : warDefs) {
			if (warVersion.equals(warDef.getWarVersion())) {
				warService = warDef;
				break;
			}
		}
		if (warService == null) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s WarService. Deploy cancelled.");
			return result;
		}

		String warId = warService.getId();

		try {
			// Stop Jetty Cluster
			jettyClusterMgr.stop(jettyClusterId);
			// Deploy application
			String id = services.get(0).getId();
			jettyServiceMgr.deploy(id, warId, svnRepoId);
			// Update warService(isdeployFlag)
			// logger.log(ObjectNameFactory.MANAGEMENT_TYPE,
			// "Update warService '"+ warId + " isdeployFlag to false.");
			warServiceMgr.setWarDeployFlag(warclusterId, warId, true);

			// Start Jetty Cluster
			jettyClusterMgr.start(jettyClusterId);

			// logger.log(ObjectNameFactory.MANAGEMENT_TYPE,
			// "Update warService '"+ warId + " isdeployFlag to true.");
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_SUCCESS);
			result.setDeployException("");

		} catch (DeployException e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("deploy error:" + e.getMessage());

		} catch (ServiceException e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("setWarDeployFlag error:"
					+ e.getMessage());
		} catch (Throwable e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Stop JettyService error:"
					+ e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * @param appName
	 * @param warVersion
	 * @return
	 */
	public static DeployResult deployTomcatAppVersion(String appName,
			String warVersion) {
		DeployResult result = new DeployResult();
		result.setAppName(appName);
		result.setWarVersion(warVersion);
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(warVersion)) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Parameter (appName or warVersion) is null");
			return result;
		}

		ICluster tomcatCluster = clusterManager.getByApp(appName,
				TomcatCluster.TYPE);
		ICluster svnRepoCluster = clusterManager.getByApp(appName,
				SVNRepositoryCluster.TYPE);
		if (tomcatCluster == null || svnRepoCluster == null) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s TomcatCluster or SVNRepoCluster. Deploy cancelled.");
			return result;
		}
		// Get TomcatService
		List<TomcatService> services = srvQueryMgr.getByCluster(tomcatCluster.getId(),
				TomcatService.class);
		if (services == null || services.isEmpty()) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s TomcatService. Deploy cancelled.");
			return result;
		}

		// Get SVNRepositoryService
		String svnRepoClusterId = svnRepoCluster.getId();
		List<SVNRepositoryService> svnRepos = srvQueryMgr.getByCluster(
				svnRepoClusterId, SVNRepositoryService.class);
		if (svnRepos == null || svnRepos.isEmpty()) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s SVNRepositoryService. Deploy cancelled.");
			return result;
		}
		SVNRepositoryService svnRepoService = svnRepos.get(0);
		String svnRepoId = svnRepoService.getId();

		// Get WarService
		ICluster warCluster = clusterManager.getByApp(appName, WarService.TYPE);
		String warclusterId = warCluster == null ? null : warCluster.getId();
		List<WarService> warDefs = srvQueryMgr.getByCluster(warclusterId,
				WarService.class);
		WarService warService = null;
		try {
			for (WarService warDef : warDefs) {
				warServiceMgr.setWarDeployFlag(warclusterId, warDef.getId(),
						false);
			}
		} catch (ServiceException e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
		}
		for (WarService warDef : warDefs) {
			if (warVersion.equals(warDef.getWarVersion())) {
				warService = warDef;
				break;
			}
		}
		if (warService == null) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Can't find application '" + appName
					+ "'s WarService. Deploy cancelled.");
			return result;
		}
		String warId = warService.getId();
		try {
			tomcatClusterMgr.stop(tomcatCluster.getId());
			// Deploy application
			for (TomcatService service : services) {
				try {
					tomcatServiceMgr.deploy(service.getId(), warId, svnRepoId);
				} catch (Throwable t) {
					logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, t);
				}
			}
			// Update warService(isdeployFlag)
			warServiceMgr.setWarDeployFlag(warclusterId, warId, true);
			// Start Tomcat Cluster
			tomcatClusterMgr.start(tomcatCluster.getId());
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_SUCCESS);
			result.setDeployException("");
		} catch (ServiceException e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("setWarDeployFlag error:"
					+ e.getMessage());
		} catch (Throwable e) {
			logger.error("Deploy app {0} version {1} error.", new Object[] { appName, warVersion}, e);
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setDeployException("Stop service error:"
					+ e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询应用 <br>
	 * 
	 * @param owner  云Paas平台owner
	 * @return 应用列表
	 */
	public static List<WebApp> queryApps(String owner) {
		List<WebApp> appList = new ArrayList<WebApp>();
		if (StringUtil.isEmpty(owner)) {
			return appList;
		}
		WebApp[] apps = AppUtil.getWebapps(owner);
		if (apps != null && apps.length >0) {
			for (WebApp app : apps) {
				int appState = app.getState();
				//只查询 “已开通” 的应用
				if (appState != WebApp.STATE_OPEND) {
					continue;
				}
				//查询应用部署的版本信息
				ICluster warCluster = clusterManager.getByApp(app.getName(), WarCluster.TYPE);
				if (warCluster != null) {
					List<WarService> warServices = srvQueryMgr.getByCluster(warCluster.getId(), WarService.class);
					int num = warServices==null ? 0 : warServices.size();// 获取应用的版本数 
					app.getAttributes().put("versionNum", String.valueOf(num));
					
					for (WarService war : warServices){
						if (war.isDeployVersion()) {
							app.getAttributes().put("deployVersion", war.getWarVersion());
						}
					}
					WarService latest_warService = DeployHelper.getLastestWar(app.getName());
					String latestVersion = latest_warService == null ? "": latest_warService.getWarVersion();
					app.getAttributes().put("latestVersion",  latestVersion); //$NON-NLS-1$
				} else {
					app.getAttributes().put("versionNum", "0"); //$NON-NLS-1$
					app.getAttributes().put("latestVersion", VersionUtil.getInitVersion()); //$NON-NLS-1$
					app.getAttributes().put("deployVersion", "0"); //$NON-NLS-1$
				}
				
				appList.add(app);
			}
		}
		return appList;
	}

	/**
	 * 查询已开通的应用<br>
	 * 
	 * @param userID
	 *            云Paas平台租户ID
	 * @return 应用列表
	 */
	public static List<WebApp> queryTargetApps(String owner) {
		return queryApps(owner);
	}

	/**
	 * 提交应用war包 <br>
	 * 
	 * @param war 部署应用参数配置
	 * @param user 当前登录用户
	 * @return 部署结果
	 */
	public static DeployResult submitWar(AppWar war, String user) {
		if (war == null || StringUtil.isEmpty(user)) {
			DeployResult result = new DeployResult();
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);
			result.setSubmitStatus(AppConstants.SUBMIT_RESULT_FAIL);
			result.setSubmitException("war or user is empty.");
			return result;
		}

		String appName = war.getAppName();
		String displayName = war.getDisplayName();
		String deployWay = war.getDeployWay(); // 部署方式（1.默认 D; 2.覆盖 O; // 3.创建新版本 N.）
		if (deployWay == null || 
						(AppWar.DEPLOY_WAY_DEFAULT.equals(deployWay) == false
						&& AppWar.DEPLOY_WAY_OVERRIDE.equals(deployWay) == false 
						&& AppWar.DEPLOY_WAY_NEW.equals(deployWay) == false)) {
			deployWay = AppWar.DEPLOY_WAY_DEFAULT;
		}
		String desc = war.getDesc()==null ? "": war.getDesc().trim(); // 部署描述
		String specificVersion = war.getOverrideVersion(); // 覆盖指定版本
		String clientFileName = war.getClientFileName(); // 客户端文件名
		// String fileName = deployAppVar.getFileName(); // 服务端文件名
		
		String filePath = war.getFilePath(); // 服务端文件路径
		File tempFile = new File(filePath); // 上传的WAR文件
		
		String versionSign = StringUtil.isEmpty(specificVersion) ? "" : specificVersion; // 应用包版本
		String absolutePath = tempFile.getAbsolutePath();
		String suffixName = absolutePath.substring(absolutePath.lastIndexOf("."), absolutePath.length()); //.war or .ear
		String warFileName = appName + suffixName;//".war"; //sample.war  or ear
		String repoFile = warFileName; // 资源名
		long revision = 0L; // 资源版本

		DeployResult result = new DeployResult(); // 部署结果
		result.setAppName(appName);
		result.setDisplayName(displayName);
		
		// WebApp app = appManager.get(appName);
		File appRepository = null;
		
		// Step1: 提交应用包
		try {
			
			// 复制应用包到本地应用仓库
			//	String appLocalPath = ApplicationContext.getInstance().getWarRealPath() + File.separator + ServiceConstants.REPO_LOCAL_PATH + File.separator + appName;
			String appLocalPath = SystemVariables.getWarRootPath() + File.separator + appName; // /primeton/paas/workspace/wars/sample
			appRepository = new File(appLocalPath); //

			String warFilePath = appLocalPath + File.separator + warFileName;//.../sample/sample.war 本地应用仓库路径
			File warFile = new File(warFilePath);
			
			File appLocalSvn = new File(appLocalPath, ".svn");
			boolean isSvnExists = appLocalSvn.exists();
			
			// 调用资源库客户端提交应用包
			SvnConfig svnConf = getSvnConfig(appName);
			if (svnConf == null) {
				result.setSubmitException("Can't find application '" + appName + "' SvnService Definition and Svn Definition.");
				result.setSubmitStatus(AppConstants.SUBMIT_RESULT_FAIL);	// 提交失败
				
				if(appRepository != null && appRepository.exists()) {
					appRepository.delete();  // 删除repository目录下的应用资源文件
				}
				return result;
			}
			
			// http://192.168.100.218:18880/repos/
			String rrServeUrl = AppConstants.REPO_HTTP + svnConf.getSvnIp() + ":" +  svnConf.getHttpPort() + "/" + svnConf.getRepoRoot() + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			String repoName = svnConf.getRepoName();
			String repoUser = svnConf.getUser();
			String repoPwd = svnConf.getPassword();
			IRrClient rrClient = RrClientFactory.getInstance().getClient(rrServeUrl, repoName, repoUser, repoPwd);
			
			if (isSvnExists) {
				// 更新仓库 
				logger.info("Update SVN local files {0}.", new Object[] { appLocalPath });
				rrClient.update(appLocalPath, AppConstants.THE_LASTEST_REVISION);
			} else {
				// 检出仓库
				logger.info("Check SVN files to local path {0}.", new Object[] { appLocalPath });
				rrClient.checkout(AppConstants.REPO_REMOTE_PATH, appLocalPath, AppConstants.THE_LASTEST_REVISION);
			}
			
			String[] commitPathes = new String[] { warFilePath };
			boolean isWarExists = warFile.exists();
			logger.info("Copy war file {0} to {1}", new Object[] { filePath, warFilePath });
			
			File destFile = new File(warFilePath);
			 try {
				FileUtils.copyFile(tempFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!isWarExists) {
				commitPathes = rrClient.add(commitPathes , "Add the application [" + appName + "] resource [" + warFileName + "] to RR repository.");
			}
			// 提交 
			long begin = System.currentTimeMillis();
			logger.info("Begin Commit application [" + appName + "] war file [" + warFileName + "] to repository.");
			
			revision = rrClient.commit(commitPathes, "Commit the application [" + appName + "] resource [" + clientFileName + "] to RR repository. (commitPathes =" + commitPathes +").");
			
			long end = System.currentTimeMillis();
			logger.info("End Commit application [" + appName + "] war file [" + warFileName + "] to repository. Time Spent : " + (end - begin) / 1000 + " second.");
			
			if (revision == AppConstants.THE_LASTEST_REVISION) {
				IRrEntry[] entries = rrClient.listEntries(AppConstants.REPO_REMOTE_PATH, -1);
				for (IRrEntry entry : entries) {
					if (warFileName.equals(entry.getName())) {
						revision = entry.getRevision();
						break;
					}
				}
			}
			result.setSubmitStatus(AppConstants.SUBMIT_RESULT_SUCCESS);	// 提交成功
			logger.info("Submit application [" + appName + "] war file [" + warFileName + "] to repository successed.");
		} catch (PaasRuntimeException e) {
			result.setSubmitException(e.getMessage());
			result.setSubmitStatus(AppConstants.SUBMIT_RESULT_FAIL);	// 提交失败
			if (appRepository != null && appRepository.exists()) {
				appRepository.delete(); // 删除repository目录下的应用资源文件
			}
			logger.error("Submit app {0} war file {1} to repository error.", new Object[] { appName, warFileName }, e);
			return result; 
		}  finally {
			if (tempFile != null && tempFile.exists()) { // remove upload war
				tempFile.delete();
			}
		}
		// Step2: 注册war服务信息
		try {
			String checkRet = DeployHelper.checkWarDeployState(appName, versionSign, deployWay);
			ICluster warCluster = DeployHelper.getWarCluster(appName);
			String warClusterId = warCluster==null ? "" : warCluster.getId();
			
			if (AppConstants.DEPLOY_STATUS_OVERLAP_OLD_VERSION.equals(checkRet)) {
			
				// 覆盖指定版本
				WarService specificWar = DeployHelper.getSpecificWar(appName, specificVersion);
				
				if (specificWar == null) {
					logger.error("Can't find application [" + appName + "] specific version [" + specificVersion + "] war definition.");
				} else {
					specificWar.setRevision(revision);
					specificWar.setRepoFile(repoFile);
					//specificWar.setRepoPath(repoPath);
					specificWar.setRevision(revision);
					specificWar.setDesc(desc);
					specificWar.setSubmitTime(new Date());
					
					//更新指定版本warService
					warServiceManager.update(warClusterId, specificWar);
				}
			} else if (AppConstants.DEPLOY_STATUS_OVERLAP_LASTEST_VERSION.equals(checkRet)) {
				
				// 覆盖最新版本
				WarService lastestWar = DeployHelper.getLastestWar(appName);
				
				if (lastestWar == null) {
					logger.error("Can't find application [" + appName + "] lastest version war definition.");
				} else {
					versionSign = lastestWar.getWarVersion();
					lastestWar.setDesc(desc);
					//lastestWar.setModifiedBy(owner);
					//lastestWar.setRepoPath(repoPath);
					lastestWar.setRepoFile(repoFile);
					lastestWar.setRevision(revision);
					lastestWar.setSubmitTime(new Date());
					//更新最新版本warService
					warServiceManager.update(warClusterId, lastestWar);
				}
			} else {
				//	生成新版本
				versionSign = DeployHelper.getNewVersion(appName);
				
				// 创建新版本
				logger.info("Create application [" + appName + "] a new version war definition.");
				
				//新增 warService
				String warVersion = DeployHelper.getNewVersion(appName);
				WarService warService = new WarService();
				warService.setDesc(desc);
				warService.setName(appName);// 应用名称
				warService.setOwner(user);
				warService.setState(IService.STATE_RUNNING);
				warService.setRevision(revision);
				warService.setWarVersion(warVersion);
				warService.setRepoFile(repoFile);
				warService.setDeployVersion(false);
				warService.setSubmitTime(new Date());
				warService.setCreatedDate(new Date());
				logger.info("Begin Create app '" + appName + " 's War Service '" + displayName + "'.");
				warServiceManager.add(warService, warClusterId);
			}
			result.setDeployStatus(AppConstants.SUBMIT_RESULT_SUCCESS);	//提交成功
			logger.info("Submit application [" + appName + "] war successed.");
		} catch (ServiceException e) {
			result.setSubmitException(e.getMessage());
			result.setSubmitStatus(AppConstants.SUBMIT_RESULT_FAIL);	// 提交失败
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL); // 部署失败
			if (appRepository != null && appRepository.exists()) {
				appRepository.delete();  // 删除repository目录下的应用资源文件
			}
			logger.error("Submit app {0} war file {1} to repository error", 
					new Object[] { appName, warFileName},  e);
			return result; 
		} catch (Exception e) {
			result.setDeployStatus(AppConstants.DEPLOY_RESULT_FAIL);	// 部署失败
			result.setDeployException(e.getMessage());
			logger.error("Submit app {0} war file {1} to repository error", 
					new Object[] { appName, warFileName},  e);
		}
		
		result.setWarVersion(versionSign);		// 设置部署应用包版本
		result.setDeployStatus("U");  // 未部署 //$NON-NLS-1$
		return result;
	}
	
	/**
	 * 取消部署 <br>
	 * 
	 * @param filePath 上传WAR的路径
	 */
	public static void cancelDeploy(String filePath) {
		if (filePath == null || filePath.trim().length() == 0) {
			return;
		}
		File tempFile = new File(filePath);
		if (tempFile != null && tempFile.exists() && tempFile.isFile()) {
			tempFile.delete(); // 删除Console临时文件夹中上传的WAR包
		}
	}
	
	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static SvnConfig getSvnConfig(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		SvnConfig conf = new SvnConfig();
		ICluster svnRepoCluster = clusterManager.getByApp(appName,
				SVNRepositoryCluster.TYPE);
		if (svnRepoCluster == null
				|| StringUtil.isEmpty(svnRepoCluster.getId())) {
			return null;
		}
		String svnRepoClusterId = svnRepoCluster.getId();
		List<SVNRepositoryService> svnRepos = srvQueryMgr.getByCluster(
				svnRepoClusterId, SVNRepositoryService.class);
		if (svnRepos == null || svnRepos.isEmpty()) {
			return null;
		}
		SVNRepositoryService svnRepoService = svnRepos.get(0);
		String svnServiceId = svnRepoService.getParentId();
		SVNService svnService = srvQueryMgr.get(svnServiceId, SVNService.class);
		if (svnService == null || StringUtil.isEmpty(svnService.getId())) {
			return null;
		}
		String repoRoot = svnService.getRepoRoot();// repos/
		int httpPort = svnService.getPort();// 18880
		String repoName = svnRepoService.getRepoName();// appName: 'sample'
		String user = svnRepoService.getUser();
		String password = svnRepoService.getPassword();
		String svnIp = svnService.getIp();
		conf.setRepoName(repoName);
		conf.setRepoRoot(repoRoot);
		conf.setHttpPort(httpPort);
		conf.setUser(user);
		conf.setPassword(password);
		conf.setSvnIp(svnIp);
		return conf;
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class SvnConfig implements Serializable {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -1214692990670765530L;

		private String svnIp;
		private String basePath;
		private String dataPath;
		private String configPath;
		private String repoRoot;// repos/
		private int httpPort;
		private int httpsPort;
		private String repoName;// appName: 'sample'
		private String user;
		private String password;

		public String getSvnIp() {
			return svnIp;
		}

		public void setSvnIp(String svnIp) {
			this.svnIp = svnIp;
		}

		public String getBasePath() {
			return basePath;
		}

		public void setBasePath(String basePath) {
			this.basePath = basePath;
		}

		public String getConfigPath() {
			return configPath;
		}

		public void setConfigPath(String configPath) {
			this.configPath = configPath;
		}

		public String getDataPath() {
			return dataPath;
		}

		public void setDataPath(String dataPath) {
			this.dataPath = dataPath;
		}

		public int getHttpPort() {
			return httpPort;
		}

		public void setHttpPort(int httpPort) {
			this.httpPort = httpPort;
		}

		public int getHttpsPort() {
			return httpsPort;
		}

		public void setHttpsPort(int httpsPort) {
			this.httpsPort = httpsPort;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRepoName() {
			return repoName;
		}

		public void setRepoName(String repoName) {
			this.repoName = repoName;
		}

		public String getRepoRoot() {
			return repoRoot;
		}

		public void setRepoRoot(String repoRoot) {
			this.repoRoot = repoRoot;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

	}

	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param keyData
	 * @return
	 */
	@Path("/listApp")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listApp(@FormParam("pageIndex") int pageIndex,
			@FormParam("pageSize") int pageSize,
			@FormParam("keyData") String keyData) {
		PageCond pageCond = new PageCond();
		pageCond.setBegin(pageIndex);
		pageCond.setLength(pageSize);

		IUserObject user = DataContextManager.current().getMUODataContext()
				.getUserObject();
		String currentUser = user.getUserId();

		List<WebApp> appList = queryApps(currentUser);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", appList); //$NON-NLS-1$
		result.put("total", appList.size()); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param uploadFileStream
	 * @param fileInfo
	 * @param appName
	 * @param radWay
	 * @param vsName
	 * @param deployTag
	 * @param des
	 * @return
	 */
	@POST
	@Path("/deploy")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response Deploy(
			@FormDataParam("uploadFile") InputStream uploadFileStream,
			@FormDataParam("uploadFile") FormDataContentDisposition fileInfo,
			@FormDataParam("selAppList") String appName,
			@FormDataParam("radWay") String radWay,
			@FormDataParam("vsName") String vsName,
			@FormDataParam("deployTag") String deployTag,
			@FormDataParam("des") String des) {
		dataMsg.put(deployTag, "W"); //$NON-NLS-1$
		if (appName != null) {
			String path = SystemVariable.getWarTempRootPath() + File.separator
					+ appName;
			File dir = new File(path);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdirs();
			}
			String filePath = path + File.separator + fileInfo.getFileName();
			try {
				FileUtil.saveFile(uploadFileStream, filePath);
			} catch (IOException ex) {
				dataMsg.put(deployTag, AppConstants.DEPLOY_RESULT_FAIL);
				return null;
			}
			IUserObject user = DataContextManager.current().getMUODataContext()
					.getUserObject();
			String currentUser = user.getUserId();

			AppWar war = new AppWar();
			war.setAppName(appName);
			war.setClientFileName(fileInfo.getFileName());
			war.setDeployWay(radWay);
			war.setDesc(des);
			war.setOverrideVersion(vsName);
			war.setFilePath(filePath);
			DeployResult deployResult = submitWar(war,
					currentUser);
			String warVersion = deployResult.getWarVersion();
			DeployResult dr = new DeployResult();
			if (warVersion != null) {
				dr = deployAppVersion(appName, warVersion);
			}
			System.out.println(dr.getDeployStatus());
			dataMsg.put(deployTag,
					dr.getDeployStatus() != null ? dr.getDeployStatus()
							: AppConstants.DEPLOY_RESULT_FAIL);
			/* ResponseBuilder builder = */Response.ok(dr);
		} else {
			dataMsg.put(deployTag, AppConstants.DEPLOY_RESULT_FAIL);
		}
		return null;
	}

	private static Map<String, String> dataMsg = new HashMap<String, String>();

	/**
	 * 
	 * @param deployTag
	 * @return
	 */
	@Path("getDeployMsg/{deployTag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeployMsg(@PathParam("deployTag") String deployTag) {
		boolean rtn = false;
		if (deployTag != null && deployTag != null) {
			String v = dataMsg.get(deployTag);
			if (v == null) {
				int i = 0;
				while (i < 20) {
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
					}
					v = dataMsg.get(deployTag);
					if (v != null) {
						break;
					}
					i++;
				}
			}
			if (v != null) {
				int i = 0;
				while (i < 50) {
					v = dataMsg.get(deployTag);
					if (v != null && !v.equals("W")) { //$NON-NLS-1$
						if (v.equals("S")) {
							rtn = true;
						}
						break;
					}
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
					}
					i++;
				}
				dataMsg.remove(deployTag);
			}
		}
		ResponseBuilder builder = Response.ok(rtn);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("listAppVersion/{appName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listAppVersion(@PathParam("appName") String appName) {
		List<WarService> warList = queryAppVersion(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", warList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("listAppVersion/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response listAppVersionByGet(@PathParam("appName") String appName) {
		List<WarService> warList = queryAppVersion(appName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", warList); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param warVersion
	 * @return
	 */
	@Path("deploy/{appName}-{warVersion}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deploy(@PathParam("appName") String appName,
			@PathParam("warVersion") String warVersion) {
		DeployResult dr = new DeployResult();
		if (appName != null && warVersion != null) {
			dr = deployAppVersion(appName, warVersion);
		}
		ResponseBuilder builder = Response.ok(dr);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param warVersion
	 * @return
	 */
	@Path("uninstall/{appName}-{warVersion}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstall(@PathParam("appName") String appName,
			@PathParam("warVersion") String warVersion) {
		boolean rtn = false;
		if (appName != null && warVersion != null) {
			rtn = uninstallAppVersion(appName, warVersion);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn);
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param appName
	 * @param warVersion
	 * @return
	 */
	@Path("delete/{appName}-{warVersion}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("appName") String appName,
			@PathParam("warVersion") String warVersion) {
		boolean rtn = false;
		if (appName != null && warVersion != null) {
			rtn = removeAppVersion(appName, warVersion);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtn); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}
	
	public static class AppWar {
		
		public static final String DEPLOY_WAY_DEFAULT = "D";
		public static final String DEPLOY_WAY_OVERRIDE = "O";
		public static final String DEPLOY_WAY_NEW = "N";
		
		public static final String DEPLOY_ACTION_NOW = "Y";
		public static final String DEPLOY_ACTION_FALSE = "N";

		private String appName;  // 应用名称
		private String displayName;  // 应用显示名称
		private String deployWay = DEPLOY_WAY_DEFAULT;  //部署方式（1.默认 D; 2.覆盖 O; 3.创建新版本 N.）
		private String overrideVersion;  // 覆盖版本
		//private String deployNow = DEPLOY_ACTION_FALSE;  // 立即部署 (Y|N)
		private String desc;  // 部署描述
		private String clientFileName; // 客户端文件名
		private String fileName; // 服务端文件名
		private String filePath; // 服务端文件路径
		
		public AppWar() {
			super();
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String name) {
			this.appName = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getDeployWay() {
			return deployWay;
		}

		public void setDeployWay(String deployWay) {
			this.deployWay = deployWay;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getOverrideVersion() {
			return overrideVersion;
		}

		public void setOverrideVersion(String overrideVersion) {
			this.overrideVersion = overrideVersion;
		}

		public String getClientFileName() {
			return clientFileName;
		}

		public void setClientFileName(String clientFileName) {
			this.clientFileName = clientFileName;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String toString() {
			return "AppWar [appName=" + appName + ", displayName="
					+ displayName + ", deployWay=" + deployWay
					+ ", overrideVersion=" + overrideVersion + ", desc=" + desc
					+ ", clientFileName=" + clientFileName + ", fileName="
					+ fileName + ", filePath=" + filePath + "]";
		}
		
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	public static class DeployResult {

		private String appName;  // 应用名称
		private String displayName;  // 应用显示名称
		private String deployStatus;  // 部署状态
		private String submitStatus;  // 提交状态
		private String warVersion;  // 版本号
		private String submitException;  // 提交异常
		private String deployException;  // 部署异常
		
		public DeployResult() {
			super();
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getDeployException() {
			return deployException;
		}

		public void setDeployException(String deployException) {
			this.deployException = deployException;
		}

		public String getDeployStatus() {
			return deployStatus;
		}

		public void setDeployStatus(String deployStatus) {
			this.deployStatus = deployStatus;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String name) {
			this.appName = name;
		}

		public String getSubmitException() {
			return submitException;
		}

		public void setSubmitException(String submitException) {
			this.submitException = submitException;
		}

		public String getSubmitStatus() {
			return submitStatus;
		}

		public void setSubmitStatus(String submitStatus) {
			this.submitStatus = submitStatus;
		}

		public String getWarVersion() {
			return warVersion;
		}

		public void setWarVersion(String versionNO) {
			this.warVersion = versionNO;
		}
		
	}
	
}
