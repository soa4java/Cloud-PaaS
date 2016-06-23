/**
 * 
 */
package com.primeton.paas.manage.api.impl.util;

import java.io.File;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.app.IStretchStrategyManager;
import com.primeton.paas.manage.api.cluster.CEPEngineCluster;
import com.primeton.paas.manage.api.cluster.CardBinCluster;
import com.primeton.paas.manage.api.cluster.CollectorCluster;
import com.primeton.paas.manage.api.cluster.EsbCluster;
import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.cluster.JettyCluster;
import com.primeton.paas.manage.api.cluster.JobCtrlCluster;
import com.primeton.paas.manage.api.cluster.KeepalivedCluster;
import com.primeton.paas.manage.api.cluster.MailCluster;
import com.primeton.paas.manage.api.cluster.MemcachedCluster;
import com.primeton.paas.manage.api.cluster.MsgQueueCluster;
import com.primeton.paas.manage.api.cluster.MySQLCluster;
import com.primeton.paas.manage.api.cluster.NginxCluster;
import com.primeton.paas.manage.api.cluster.OpenAPICluster;
import com.primeton.paas.manage.api.cluster.RedisCluster;
import com.primeton.paas.manage.api.cluster.SVNCluster;
import com.primeton.paas.manage.api.cluster.SVNRepositoryCluster;
import com.primeton.paas.manage.api.cluster.SmsCluster;
import com.primeton.paas.manage.api.cluster.TomcatCluster;
import com.primeton.paas.manage.api.cluster.WarCluster;
import com.primeton.paas.manage.api.exception.ClusterException;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.StretchStrategyManagerFactory;
import com.primeton.paas.manage.api.factory.WebAppManagerFactory;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IWebAppManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * FIXME 不建议这么写（需要重构）. <br>
 * Comment by ZhongWen.Li (mailto:lizw@primeton.com)
 *
 * @author liyanping(liyp@primeton.com)
 * 
 */
@SuppressWarnings("deprecation")
public class ServiceRemoveUtil {

	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceRemoveUtil.class);
	
	private static IWebAppManager appManager = WebAppManagerFactory.getManager(); 
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	private static IClusterManager jettyClusterManager = ClusterManagerFactory.getManager(JettyCluster.TYPE);
	private static IClusterManager mysqlClusterManager = ClusterManagerFactory.getManager(MySQLCluster.TYPE);
	private static IClusterManager svnRepoClusterManager = ClusterManagerFactory.getManager(SVNRepositoryCluster.TYPE);
	private static IClusterManager warClusterManager = ClusterManagerFactory.getManager(WarCluster.TYPE);
	private static IClusterManager memcachedClusterManager = ClusterManagerFactory.getManager(MemcachedCluster.TYPE);
	private static IClusterManager haproxyClusterManager =  ClusterManagerFactory.getManager(HaProxyCluster.TYPE);
	private static IClusterManager openapiClusterManager = ClusterManagerFactory.getManager(OpenAPICluster.TYPE);
	private static IClusterManager keepalivedClusterManager = ClusterManagerFactory.getManager(KeepalivedCluster.TYPE);
	private static IClusterManager nginxClusterManager = ClusterManagerFactory.getManager(NginxCluster.TYPE);
	private static IClusterManager svnClusterManager = ClusterManagerFactory.getManager(SVNCluster.TYPE);
	@Deprecated
	private static IClusterManager smsClusterManager = ClusterManagerFactory.getManager(SmsCluster.TYPE);
	private static IClusterManager cepEngineClusterManager = ClusterManagerFactory.getManager(CEPEngineCluster.TYPE);
	@Deprecated
	private static IClusterManager cardbinClusterManager = ClusterManagerFactory.getManager(CardBinCluster.TYPE);
	private static IClusterManager collectorClusterManager = ClusterManagerFactory.getManager(CollectorCluster.TYPE);
	private static IClusterManager mailClusterManager = ClusterManagerFactory.getManager(MailCluster.TYPE);
	private static IClusterManager tomcatClusterManager = ClusterManagerFactory.getManager(TomcatCluster.TYPE);
	private static IClusterManager redisClusterManager = ClusterManagerFactory.getManager(RedisCluster.TYPE);
	private static IClusterManager jobCtrlClusterManager = ClusterManagerFactory.getManager(JobCtrlCluster.TYPE);
	private static IClusterManager esbClusterManager = ClusterManagerFactory.getManager(EsbCluster.TYPE);
	private static IClusterManager msgQueueClusterManager = ClusterManagerFactory.getManager(MsgQueueCluster.TYPE);
	
	private static IStretchStrategyManager stretchMgr = StretchStrategyManagerFactory.getManager();
	
	/**
	 * Default. <br>
	 */
	private ServiceRemoveUtil() {
		super();
	}

	/**
	 * 
	 * @param appName
	 */
	public static void removeInnerApplication(String appName) {
		// remove app stretch strategy
		logger.info("Remove applciation '" + appName + "' stretch strategy.");
		stretchMgr.remove(appName);
		// remove app config
		logger.info("Remove application config {appName:" + appName + "}.");
		appManager.deleteAppConfig(appName);
		// remove app
		logger.info("Remove application '" + appName + "'.");
		appManager.delete(appName);
	}
	
	/**
	 * 
	 * @param appName
	 */
	public static void removeOuterApplication(String appName) {
		// remove app config
		logger.info("Remove outer application config. {appName:" + appName
				+ "}.");
		appManager.deleteAppConfig(appName);
		// remove app
		logger.info("Remove application.{appName:" + appName + "}.");
		appManager.delete(appName);
	}
	
	/**
	 * remove application's log director & application's war director <br>
	 * 
	 * workspace/logs/ + appName
	 * workspace/wars/ + appName
	 * 
	 * ssl certificate files
	 * 
	 * @param appName
	 */
	public static void removeAppResource(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return;
		}
		// remove app log director
		String appLogPath = SystemVariables.getLogRootPath() + File.separator
				+ "" + appName;
		File logFile = new File(appLogPath);
		if (logFile.exists()) {
			logger.info("Remove application log directory {appName:" + appName
					+ ",logRootPath:" + appLogPath + "}.");
			delFile(logFile);
		}
		// remove app war director
		String appWarPath = SystemVariables.getWarRootPath() + File.separator
				+ "" + appName;
		File warFile = new File(appWarPath);
		if (warFile.exists()) {
			logger.info("Remove application war directory {appName:" + appName
					+ ",warRootPath:" + appWarPath + "}.");
			delFile(warFile);
		}
		// remove app ssl director
		String appDomain = appName + SystemVariables.getDomainPostfix();
		String appSSLPath = SystemVariables.getSslCertificatePath()
				+ File.separator + appDomain;
		File sslFile = new File(appSSLPath);
		if (sslFile.exists()) {
			logger.info("Remove application ssl certificate directory {appName:"
					+ appName + ",sslRootPath:" + appSSLPath + "}.");
			delFile(sslFile);
		}
		return;
	}
	
	/**
	 * 
	 * @param f
	 */
	public static void delFile(File f) {
		if (!f.exists()) {
			return;
		}
		File[] files = f.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				} else {
					delFile(file);
				}
			}
		} else {
			f.delete();
		}
		f.delete();
	}
	
	/**
	 * remove jetty vm & cluster <br>
	 * 
	 * @param clusterId
	 * @param appName
	 */
	public static void removeJettyCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//unbind cluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Begin Unbind Jetty cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}
		//remove relation cluster
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				ICluster clt = clusterManager.get(id);
				if (clt == null) {
					//unbind relation
					logger.info("Unbind relation between cluster '" + id + "' and jetty cluster '" + clusterId + "'.");
					unbindClusterRel(clusterId, id);
					continue;
				}
				String type = clt.getType();
				if (MemcachedCluster.TYPE.equals(type)) {
					//unbind relation between session-cluster and jetty-cluster 
					logger.info("Unbind session cluster '" + clusterId + "' with jetty cluster '" + id + "'.");
					unbindClusterRel(clusterId, id);
					
					logger.info("Destroy session cluster '" + id + "'.");
					removeMemcachedCluster(id, appName);
				} else {
					//unbind relation
					logger.info("Unbind relation between "+type+"-cluster '" + id + "' and jetty cluster '" + clusterId + "'.");
					unbindClusterRel(clusterId,id);
				}
			}
		}
		//remove cluster
		logger.info("Begin Destroy Jetty cluster '" + clusterId + "'.");
		try {
			jettyClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy Jetty cluster failed." +e.getMessage());
		}
		
	}
	
	/**
	 * remove mysql vm & clsuter <br>
	 * 
	 * @param clusterId
	 * @param appName
	 */
	public static void removeMysqlCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		if(!StringUtil.isEmpty(appName)){
			//unbind cluster with app
			logger.info("Begin Unbind MySQL cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}
		//remove cluster
		logger.info("Begin Destroy MySQL cluster '" + clusterId + "'.");
		try {
			mysqlClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy MySQL cluster failed." + e.getMessage());
		}
	}

	
	/**
	 * remove svnRepository-cluster
	 * 
	 * @param clusterId
	 * @param appName
	 * @throws ClusterException
	 */
	public static void removeSvnRepoCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//remove relation
		if(StringUtil.isNotEmpty(appName)){
			//unbind cluster with app
			logger.info("Begin Unbind SvnRepository cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}
		//remove cluster
		logger.info("Begin Destroy SvnRepository cluster '" + clusterId + "'.");
		try {
			svnRepoClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy SvnRepository cluster failed." + e.getMessage());
		}
	}

	/**
	 * remove war-cluster
	 * 
	 * @param clusterId
	 * @param appName
	 * @throws ClusterException
	 */
	public static void removeWarCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//unbind cluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Begin Unbind War cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}
		//remove cluster
		logger.info("Begin Destroy War cluster '" + clusterId + "'.");
		try {
			warClusterManager.destroy(clusterId);
		} catch (Exception e) {
			logger.error("Destroy War cluster failed." + e.getMessage());
		}
	}
	
	/**
	 * remove memcached-cluster
	 * 
	 * @param clusterId
	 * @param appName
	 * @throws ClusterException
	 */
	public static void removeMemcachedCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//unbind cluster with app
		if(!StringUtil.isEmpty(appName)){
			logger.info("Begin Unbind Memcached cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}
		//remove cluster
		logger.info("Begin Destroy Memcached cluster '" + clusterId + "'.");
		try {
			memcachedClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy Memcached cluster failed." + e.getMessage());
		}
	}

	
	/**
	 * remove haproxy-cluster & restart nginx
	 * 
	 * @param clusterId
	 * @param appName
	 */
	public static void removeHaproxyCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		if(!StringUtil.isEmpty(appName)){
			//unbind cluster with app
			logger.info("Begin Unbind Haproxy cluster '" + clusterId + "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] appNames = appManager.getRelationApp(clusterId);
			if (appNames != null && appNames.length > 0) {
				for (String name : appNames) {
					//unbind cluster with app
					logger.info("Begin Unbind Haproxy cluster '" + clusterId + "' with app '" + appName + "'.");
					appManager.unbind(name, clusterId);
				}
			}
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				ICluster clt = clusterManager.get(id);
				if (clt == null) {
					//unbind relation
					logger.info("Unbind relation between cluster '" + id + "' and  haproxy cluster '" + clusterId + "'.");
					unbindClusterRel(clusterId, id);
					continue;
				}
				String type = clt.getType();
				if (KeepalivedCluster.TYPE.equals(type)) {
					//unbind relation between haproxy-cluster and nginx-cluster 
					logger.info("Unbind haproxy cluster '" + clusterId + "' with keepalived cluster '" + id + "'.");
					unbindClusterRel(clusterId, id);
					logger.info("Destroy keeepalived cluster '" + id + ".");
					removeKeepalivedCluster(id);
				} else {
					//unbind relation
					logger.info("Unbind relation between cluster '" + id + "' and  haproxy cluster '" + clusterId + "'.");
					unbindClusterRel(clusterId,id);
				}
			}
		}
		try {
			haproxyClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy Haproxy cluster error.\n" + e);
		}
	}
	
	/**
	 * remove keepalived-cluster 
	 * 
	 * @param keepalivedClusterId
	 */
	public static void removeKeepalivedCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy keepalived cluster '" + clusterId + "'.");
			keepalivedClusterManager.destroy(clusterId);
			logger.info("End destroy keepalived cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy keepalived cluster error.\n" + e);
		}
	}
	
	/**
	 * remove openapi-cluster
	 * 
	 * @param clusterId
	 */
	public static void removeOpenAPICluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy openapi cluster '" + clusterId + "'.");
			openapiClusterManager.destroy(clusterId);
			logger.info("End destroy openapi cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy OpenAPI cluster error.\n" + e);
		}
	}
	
	/**
	 * remove nginx cluster 
	 * 
	 * @param clusterId
	 */
	public static void removeNginx(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds) {
				//unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  nginx cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
				ICluster clt = clusterManager.get(id);
				if (clt != null) {
					String type = clt.getType();
					if (KeepalivedCluster.TYPE.equals(type)) {
						//unbind relation between nginx-cluster and nginx-cluster 
						logger.info("Unbind nginx cluster '" + clusterId + "' with keepalived cluster '" + id + "'.");
						unbindClusterRel(clusterId, id);
						logger.info("Destroy keeepalived cluster '" + id + ".");
						removeKeepalivedCluster(id);
					}
				}
			}
		}
		
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy nginx cluster '" + clusterId + "'.");
			nginxClusterManager.destroy(clusterId);
			logger.info("End destroy nginx cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy nginx cluster error.\n" + e);
		}
	}
	
	/**
	 * remove svn cluster 
	 * 
	 * @param clusterId
	 */
	public static void removeSVN(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				//unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  svn cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
			}
		}
		
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy svn cluster '" + clusterId + "'.");
			svnClusterManager.destroy(clusterId);
			logger.info("End destroy svn cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy svn cluster error.\n" + e);
		}
	}
	
	
	/**
	 * remove cep analysis cluster
	 * 
	 * @param clusterId
	 */
	public static void removeCEPEngineCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy CEPAnalysis cluster '" + clusterId + "'.");
			cepEngineClusterManager.destroy(clusterId);
			logger.info("End destroy CEPAnalysis cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy CEPAnalysis cluster error.\n" + e);
		}
	}
	
	/**
	 * remove sms cluster
	 * 
	 * @param clusterId
	 */
	public static void removeSmsCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				//unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  sms cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
			}
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy sms cluster '" + clusterId + "'.");
			smsClusterManager.destroy(clusterId);
			logger.info("End destroy sms cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy sms cluster error.\n" + e);
		}
	}
	
	/**
	 * remove collector cluster
	 * 
	 * @param clusterId
	 */
	public static void removeCollectorCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				// unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  collector cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
			}
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy collector cluster '" + clusterId + "'.");
			collectorClusterManager.destroy(clusterId);
			logger.info("End destroy collector cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy collector cluster error.\n" + e.getMessage());
		}
	}
	
	/**
	 * remove cardbin cluster
	 * 
	 * @param clusterId
	 */
	public static void removeCardBinCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds){
				//unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  cardbin cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
			}
		}
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy cardbin cluster '" + clusterId + "'.");
			cardbinClusterManager.destroy(clusterId);
			logger.info("End destroy cardbin cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy cardbin cluster error.\n" + e);
		}
	}
	
	/**
	 * remove Mail cluster
	 * 
	 * @param clusterId
	 */
	public static void removeMailCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length>0 ) {
			for (String id : relClusterIds) {
				//unbind relation
				logger.info("Unbind relation between cluster '" + id + "' and  mail cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId,id);
			}
		}
		// remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String _name : apps) {
				appManager.unbind(_name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy mail cluster '" + clusterId + "'.");
			mailClusterManager.destroy(clusterId);
			logger.info("End destroy mail cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy mail cluster error.\n" + e);
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param appName
	 */
	public static void removeTomcatCluster(String clusterId, String appName) {
		if (StringUtil.isEmpty(clusterId)) {
			return;
		}
		// unbind cluster with app
		if (!StringUtil.isEmpty(appName)) {
			logger.info("Begin unbind tomcat cluster '" + clusterId
					+ "' with app '" + appName + "'.");
			appManager.unbind(appName, clusterId);
		} else {
			String[] apps = appManager.getRelationApp(clusterId);
			if (apps != null && apps.length > 0) {
				for (String _name : apps) {
					appManager.unbind(_name, clusterId);
				}
			}
		}

		// remove relation cluster
		String[] relClusterIds = clusterManager
				.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length > 0) {
			for (String id : relClusterIds) {
				// unbind relation
				logger.info("Unbind relation between cluster '" + id
						+ "' and  tomcat cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId, id);
			}
		}
		// remove cluster
		logger.info("Begin Destroy tomcat cluster '" + clusterId + "'.");
		try {
			tomcatClusterManager.destroy(clusterId);
		} catch (ClusterException e) {
			logger.error("Destroy UPJAS cluster failed." + e.getMessage());
		}
	}
	
	/**
	 * unbind relationship between clusters
	 * 
	 * @param clusterId1
	 * @param clusterId2
	 */
	public static void unbindClusterRel(String clusterId1, String clusterId2) {
		if (StringUtil.isEmpty(clusterId1) || StringUtil.isEmpty(clusterId2)) {
			return;
		}
		try {
			clusterManager.unbindCluster(clusterId1, clusterId2);
		} catch (ClusterException e) {
			logger.error("Unbind Cluster Relation Failed. (id=" + clusterId1
					+ ",relId=" + clusterId2 + ").");
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public static void removeRedisCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length > 0) {
			for (String id : relClusterIds) {
				// unbind relation
				logger.info("Unbind relation between cluster '" + id
						+ "' and  redis cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId, id);
			}
		}
		
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String name : apps) {
				appManager.unbind(name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy redis cluster '" + clusterId + "'.");
			redisClusterManager.destroy(clusterId);
			logger.info("End destroy redis cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy redis cluster error.\n" + e);
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public static void removeJobCtrlCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length > 0) {
			for (String id : relClusterIds) {
				// unbind relation
				logger.info("Unbind relation between cluster '" + id
						+ "' and JobCtrl cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId, id);
			}
		}
		
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String name : apps) {
				appManager.unbind(name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy JobCtrl cluster '" + clusterId + "'.");
			jobCtrlClusterManager.destroy(clusterId);
			logger.info("End destroy JobCtrl cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy JobCtrl cluster {0} error.", new Object[] { clusterId }, e);
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public static void removeEsbCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length > 0) {
			for (String id : relClusterIds) {
				// unbind relation
				logger.info("Unbind relation between cluster {0} and ESB cluster {1}.", 
						new Object[] { id, clusterId });
				unbindClusterRel(clusterId, id);
			}
		}
		
		//remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String name : apps) {
				appManager.unbind(name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy ESB cluster {0}.", new Object[] { clusterId });
			esbClusterManager.destroy(clusterId);
			logger.info("End destroy ESB cluster {0}.", new Object[] { clusterId });
		} catch (ClusterException e) {
			logger.error("Destroy ESB cluster {0} error.", new Object[] { clusterId }, e);
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 */
	public static void removeMsgQueueCluster(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return ;
		}
		String[] relClusterIds = clusterManager.getRelationClustersId(clusterId);
		if (relClusterIds != null && relClusterIds.length > 0) {
			for (String id : relClusterIds) {
				// unbind relation
				logger.info("Unbind relation between cluster '" + id
						+ "' and  MsgQueue cluster '" + clusterId + "'.");
				unbindClusterRel(clusterId, id);
			}
		}
		
		// remove relations with application
		String[] apps = appManager.getRelationApp(clusterId);
		if (apps != null && apps.length > 0) {
			for (String name : apps) {
				appManager.unbind(name, clusterId);
			}
		}
		try {
			logger.info("Begin destroy MsgQueue cluster '" + clusterId + "'.");
			msgQueueClusterManager.destroy(clusterId);
			logger.info("End destroy MsgQueue cluster '" + clusterId + "'.");
		} catch (ClusterException e) {
			logger.error("Destroy MsgQueue cluster {0} error.", new Object[] { clusterId }, e);
		}
	}
	
}