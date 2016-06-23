/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.console.common.SystemVariable;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.util.RuntimeExec;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/appCert")
public class AppCertResource {
	
	private static IWebAppManager appManager = WebAppManagerFactory
			.getManager();
	private static ILogger logger = LoggerFactory.getLogger(AppCertResource.class);
	private static IClusterManager clusterManager = ClusterManagerFactory
			.getManager(IClusterManager.DEFAULT_TYPE);
	private static IClusterManager nginxManager = ClusterManagerFactory
			.getManager(NginxService.TYPE);

	/**
	 * 查询应用配置
	 * 
	 * @param appName
	 *            应用名称
	 * @return 配置信息
	 */
	@SuppressWarnings("rawtypes")
	public static Map getAppConfig(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		Map<String, Object> appInfo = new HashMap<String, Object>();
		WebApp app = appManager.get(appName);
		HaProxyCluster haCluster = (HaProxyCluster) clusterManager.getByApp(
				appName, HaProxyService.TYPE);
		if (haCluster != null) {
			appInfo.put("appName", app.getName()); //$NON-NLS-1$
			appInfo.put("protocolType", haCluster.getProtocolType()); //$NON-NLS-1$
			appInfo.put("sslLevel", haCluster.getSslLevel()); //$NON-NLS-1$
			appInfo.put("sslTag", 0); //$NON-NLS-1$

			String sslCertificatePath = SystemVariable.getSslCertificatePath();
			String sslCrt = sslCertificatePath + "/" + haCluster.getDomain()
					+ "/server.crt"; //$NON-NLS-1$
			String sslCrtKey = sslCertificatePath + "/" + haCluster.getDomain()
					+ "/server.key"; //$NON-NLS-1$
			String caSSl = sslCertificatePath + "/" + haCluster.getDomain()
					+ "/ca.crt"; //$NON-NLS-1$

			File _tmpFile1 = new File(sslCrt);
			File _tmpFile2 = new File(sslCrtKey);
			File _tmpFile3 = new File(caSSl);

			if (_tmpFile1.exists() && _tmpFile2.exists()) {
				appInfo.put("sslTag", 2); //$NON-NLS-1$
			}
			if (_tmpFile1.exists() && _tmpFile2.exists() && _tmpFile3.exists()) {
				appInfo.put("sslTag", 3); //$NON-NLS-1$
			}
		}
		return appInfo;
	}

	/**
	 * 更新应用配置
	 * 
	 * @param appName
	 *            应用名
	 * @param protocolName
	 *            访问方式
	 * @param sslPath
	 *            证书路径
	 * @param sslLevel
	 *            证书级别
	 * @return 成功、失败
	 */
	public static boolean updateAppConfig(String appName, String protocolName,
			String sslPath, String sslLevel) {
		if (appName != null && !appName.equals(appName)) {
			return false;
		}
		HaProxyCluster haCluster = (HaProxyCluster) clusterManager.getByApp(
				appName, HaProxyService.TYPE);
		haCluster.setProtocolType(protocolName);

		String sslCertificatePath = SystemVariable.getSslCertificatePath();
		String sslCrt = sslCertificatePath + "/" + haCluster.getDomain() //$NON-NLS-1$
				+ "/server.crt"; //$NON-NLS-1$
		String sslCrtKey = sslCertificatePath + "/" + haCluster.getDomain() //$NON-NLS-1$
				+ "/server.key"; //$NON-NLS-1$
		String caSSl = sslCertificatePath + "/" + haCluster.getDomain() //$NON-NLS-1$
				+ "/ca.crt"; //$NON-NLS-1$
		haCluster.setSslCertificate(sslCrt);
		haCluster.setSslCertificateKey(sslCrtKey);
		haCluster.setCaSslCertificate(caSSl);
		haCluster.setSslLevel(sslLevel);

		try {
			clusterManager.update(haCluster);
		} catch (ClusterException e) {
			logger.error("Update HaProxyCluster error.", e);
			return false;
		}

		String sslDir = sslCertificatePath + "/" + haCluster.getDomain(); //$NON-NLS-1$

		// 若有安全证书，则需要解压到指定的目录（以域名命名的子目录）
		if (sslPath != null && StringUtil.isNotEmpty(sslPath)) {

			File _sslDir = new File(sslDir);
			if (!_sslDir.exists() || !_sslDir.isDirectory()) {
				_sslDir.mkdir();
			}

			// 解压
			String cmd = "unzip -o " + sslPath + " -d " + _sslDir; //$NON-NLS-1$ //$NON-NLS-2$
			RuntimeExec exec = new RuntimeExec();
			try {
				exec.execute(cmd, 1000L * 20); // 20s
			} catch (Exception e) {
				logger.error(e);
				return false;
			}

			File[] files = _sslDir.listFiles();
			if (files == null || files.length < 1) {
				logger.error("Can not find ssl certificate files in {0}.", 
						new Object[] { sslPath});
				return false;
			}

			// 删除临时文件
			File sslTmpFile = new File(sslPath);
			if (sslTmpFile.exists()) {
				delFile(sslTmpFile);
			}
		}
		String nginxClusterId = getNginxClusterId();
		if (null != nginxClusterId && !"".equals(nginxClusterId)) {
			if (restartNginxCluster(nginxClusterId)) {
				logger.info("Restart nginx {0} success.", new Object[] { nginxClusterId });
			} else {
				logger.error("Restart nginx {0} error.", new Object[] { nginxClusterId });
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param fileName
	 */
	private static void delFile(File fileName) {
		if (!fileName.exists()) {
			return;
		}
		File[] files = fileName.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				} else {
					delFile(file);
				}
			}
		} else {
			fileName.delete();
		}
		fileName.delete();
	}

	/**
	 * 
	 * @return
	 */
	private static String getNginxClusterId() {
		ICluster[] clusters = clusterManager.getByType(NginxCluster.TYPE);
		if (clusters != null && clusters.length > 0 && clusters[0] != null) {
			String defaultName = NginxCluster.DEFAULT_CLUSTER_NAME;
			for (ICluster clt : clusters) {
				String nginxClusterId = clt.getId();
				String name = clt.getName();
				if (defaultName.equals(name)) {
					return nginxClusterId;
				}
			}
		}
		return null;
	}

	/**
	 * @param clusterId
	 */
	private static boolean restartNginxCluster(String clusterId) {
		try {
			nginxManager.restart(clusterId);
		} catch (Exception e) {
			logger.info("Restart nginx {0} error.", new Object[] { clusterId }, e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean restartNginxCluster() {
		String nginxClusterId = getNginxClusterId();
		if (null != nginxClusterId && !"".equals(nginxClusterId)) {
			if (restartNginxCluster(nginxClusterId)) {
				logger.info("Restart nginx {0} success.", new Object[] { nginxClusterId });
			} else {
				logger.error("Restart nginx {0} error.", new Object[] { nginxClusterId });
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	@Path("queryAppConfig/{appName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response queryAppConfig(@PathParam("appName") String appName) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", getAppConfig(appName)); //$NON-NLS-1$
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

	/**
	 * 
	 * @param keyData
	 * @return
	 */
	@Path("updateAppConfig")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAppConfig(HashMap<String, String> keyData) {
		// {appName=hdemo, sslPath=/primeton/paas/workspace/ssl\1405997626912.zip,
		// sslLevel=1, protocolName=HTTP}
		boolean result = false;
		if (null != keyData) {
			String appName = keyData.get("appName"); //$NON-NLS-1$
			String protocolName = keyData.get("protocolName"); //$NON-NLS-1$
			String sslPath = keyData.get("sslPath"); //$NON-NLS-1$
			String sslLevel = keyData.get("sslLevel"); //$NON-NLS-1$
			result = updateAppConfig(appName, protocolName,
					sslPath, sslLevel);

		}
		ResponseBuilder builder = Response.ok(result);
		return builder.build();
	}

}
