/**
 * 
 */
package com.primeton.paas.console.platform.service.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.platform.service.srvmgr.ClusterMangerUtil;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.RuntimeExec;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 日志下载工具类. <br>
 * 
 * <li>Console-Platform-Console</li>
 * <li>Console-Platform-Cesium</li>
 * <li>Console-Platform-Manage</li>
 * <li>Console-Application-Console</li>
 * <li>Console-Application-Cesium</li>
 * <li>Console-Application-Manage</li>
 * <li>Application User</li>
 * <li>Application System</li>
 * <li>Application Other</li>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LogDownLoadUtil {

	private static ILogger logger = LoggerFactory.getLogger(LogDownLoadUtil.class);

	private static IWebAppManager appManager = WebAppManagerFactory.getManager();

	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);

	private static String WEB_APP_URL = "appUrl";

	/**
	 * query platform applications <br/>
	 * 
	 * @param criteria
	 *            应用名称appName, owner
	 * @param page
	 *            分页设置
	 * @return 已开通的应用
	 */
	public static WebApp[] queryInnerApps(WebApp criteria, PageCond page) {
		WebApp[] apps = appManager.getAll();

		List<WebApp> tempList = new ArrayList<WebApp>();
		List<WebApp> returnList = new ArrayList<WebApp>();

		for (WebApp app : apps) {
			if (StringUtil.contain(app.getName(), criteria.getName())
					&& StringUtil.contain(app.getDisplayName(),
							criteria.getDisplayName())
					&& StringUtil.contain(app.getOwner(), criteria.getOwner())) {
				tempList.add(app);
			}
		}

		List<WebApp> clusters = new ArrayList<WebApp>();
		if (tempList != null && !tempList.isEmpty()) {
			for (WebApp tempApp : tempList) {
				String app_Name = tempApp.getName();
				String url = app_Name + SystemVariables.getDomainPostfix();
				ICluster jettyCluster = clusterManager.getByApp(app_Name,
						JettyCluster.TYPE);
				if (jettyCluster == null) {
					ICluster tomcatCluster = clusterManager.getByApp(app_Name,
							TomcatCluster.TYPE);
					if (tomcatCluster == null) {
						tempApp.getAttributes().put("appStatus", //$NON-NLS-1$
								IService.STATE_NOT_RUNNING + "");
					} else {
						int upjasStatus = ClusterMangerUtil
								.getClusterState(tomcatCluster.getId());
						tempApp.getAttributes().put("appStatus", //$NON-NLS-1$
								upjasStatus + "");
					}
				} else {
					int jettyStatus = ClusterMangerUtil
							.getClusterState(jettyCluster.getId());
					tempApp.getAttributes().put("appStatus", jettyStatus + ""); //$NON-NLS-1$
				}
				tempApp.getAttributes().put(WEB_APP_URL, url);
				if (criteria.getAttributes().get("appStatus") != null) { //$NON-NLS-1$
					if (tempApp.getAttributes().get("appStatus") //$NON-NLS-1$
							.equals(criteria.getAttributes().get("appStatus"))) { //$NON-NLS-1$
						clusters.add(tempApp);
					}
				} else {
					clusters.add(tempApp);
				}
			}
		}
		page.setCount(clusters.size());
		int end = page.getBegin() + page.getLength() - 1;
		if (page.getBegin() + page.getLength() > page.getCount()) {
			end = page.getCount() - 1;
		}

		for (int i = page.getBegin(); i <= end; i++) {
			returnList.add(clusters.get(i));
		}

		return returnList.toArray(new WebApp[returnList.size()]);
	}

	/**
	 * 获取要下载的平台日志文件路径
	 * 
	 * @param consoleType
	 *            console-app | console-platform
	 * @param logType
	 *            console | manage | cesium
	 * @return zip文件路径
	 */
	public static String getPlatformLogURL(String consoleType, String logType) {
		String consoleLogsHomeDir = null;
		if ("console-app".equalsIgnoreCase(consoleType)) {  //$NON-NLS-1$
			consoleLogsHomeDir = getConsoleAppLogsHomeDir();
		} else {
			consoleLogsHomeDir = getConsolePlatformLogsHomeDir();
		}
		// 执行zip脚本，获取平台日志文件路径
		String errorFilePath = consoleLogsHomeDir + File.separator
				+ "downloaderror.log";// 如无法获取，直接返回console.log日志

		File errorFile = new File(errorFilePath);
		if (!errorFile.exists() || !errorFile.isFile()) { // 创建下载错误文件
			RuntimeExec exec = new RuntimeExec();
			String createErrorFile = "echo 'Download Log error' > "	+ errorFilePath;
			try {
				exec.execute(createErrorFile, 1000L * 60);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		if (StringUtil.isEmpty(consoleType) || StringUtil.isEmpty(logType)) {
			return errorFilePath;
		}
		String zip = consoleLogsHomeDir + File.separator + "tmp"
				+ File.separator + consoleType + "_" + logType + ".zip";

		String cmd = SystemVariables.getBinHome() + "/Console/bin/zip-consoleLog.sh " //$NON-NLS-1$
				+ " " + consoleLogsHomeDir + " " + consoleType + " " + logType;

		RuntimeExec exec = new RuntimeExec();
		try {
			exec.execute(cmd, 1000L * 60);
			File logZip = new File(zip);
			if (logZip.exists() && logZip.isFile()) {
				return zip;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return errorFilePath;
	}

	/**
	 * 获取要下载应用的日志文件路径
	 * 
	 * @param appName
	 *            应用名称 logType:user|system
	 * @return zip文件路径
	 */
	public static String getAppLogURL(String appName, String logType) {
		// 执行zip脚本，获取应用日志文件路径
		String logRootPath = SystemVariables.getLogRootPath();
		// ${logRootPath}/error.txt /primeton/paas/workspace/application/error.txt
		String errorFilePath = logRootPath + File.separator + "error.txt";
		File errorFile = new File(errorFilePath);
		if (!errorFile.exists() || !errorFile.isFile()) {
			// 创建error.txt echo "Null Log" > ${logRootPath}+ File.separator
			// + error.txt
			RuntimeExec exec = new RuntimeExec();
			String createErrorFile = "echo 'Null Log' > " + errorFilePath;
			try {
				exec.execute(createErrorFile, 1000L * 60);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		if (StringUtil.isEmpty(appName) || StringUtil.isEmpty(logRootPath)
				|| StringUtil.isEmpty(logType) || "null".equals(logType)) {
			logType = "userall";
		}

		if (StringUtil.isEmpty(logRootPath)) {
			return errorFilePath;
		}
		// ${logRootPath}/sample/tmp/sample_user.zip ||
		// ${logRootPath}/sample/tmp/sample_system.zip
		String zip = logRootPath + File.separator + appName + File.separator
				+ "tmp" + File.separator + appName + "_" + logType + ".zip"; //$NON-NLS-1$
		String cmd = SystemVariables.getBinHome() + "/Console/bin/zip-appLog.sh " //$NON-NLS-1$
				+ appName + " " + logType + " " + logRootPath;
		RuntimeExec exec = new RuntimeExec();
		try {
			exec.execute(cmd, 1000L * 60);
			File logZip = new File(zip);
			if (logZip.exists() && logZip.isFile()) {
				return zip;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return errorFilePath;
	}

	/**
	 * console-app log root dir
	 * 
	 * @return
	 */
	public static String getConsoleAppLogsHomeDir() {
		return SystemVariables.getProgramsHome() + "/Console/console-app/logs"; //$NON-NLS-1$
	}

	/**
	 * console-platform log root dir
	 * 
	 * @return
	 */
	public static String getConsolePlatformLogsHomeDir() {
		return SystemVariables.getProgramsHome() + "/Console/console-platform/logs"; //$NON-NLS-1$
	}

}
