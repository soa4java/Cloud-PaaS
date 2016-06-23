/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.cluster.HaProxyCluster;
import com.primeton.paas.manage.api.exception.ServiceException;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.util.SendMessageUtil;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.manager.INginxServiceManager;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.service.HaProxyService;
import com.primeton.paas.manage.api.service.NginxService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class NginxClusterManager extends DefaultClusterManager {
	
	public static final String TYPE = NginxService.TYPE;
	
	private ILogger logger = ManageLoggerFactory.getLogger(HaProxyServiceManager.class);
	
	private static INginxServiceManager nginxServiceManager = ServiceManagerFactory.getManager(NginxService.TYPE);
	
	// Bash shell script for generate nginx configuration file.
	private static final String CONFIG_SCRIPT = "config.sh";

	/**
	 * Default. <br>
	 */
	public NginxClusterManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#start(java.lang.String)
	 */
	public void start(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			updateConf(id);
			// instanceManager.startCluster(id);
			// nginx keepalived
			IService[] instances = getServiceQuery().getByCluster(id);
			if (instances != null && instances.length > 0) {
				for (IService instance : instances) {
					getNginxServiceManager().start(instance.getId());
				}
			}
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.manager.DefaultClusterManager#stop(java.lang.String)
	 */
	public void stop(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			// nginx keepalived 
			IService[] instances = getServiceQuery().getByCluster(id);
			if (instances != null && instances.length > 0) {
				for (IService instance : instances) {
					getNginxServiceManager().stop(instance.getId());
				}
			}
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.impl.DefaultClusterManager#restart(java.lang.String)
	 */
	public void restart(String id) throws ServiceException {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		try {
			updateConf(id);
			// super.restart(id);
			getInstanceManager().restartCluster(id);
		} catch (Throwable t) {
			throw new ServiceException(t);
		}
	}

	/**
	 * TODO 后期需要重构，配置较复杂时，这种方式不能满足；建议远程修改文件/重新文件. <br>
	 * 
	 * @param id
	 * @throws Exception
	 */
	private void updateConf(String id) throws Exception {
		if (StringUtil.isEmpty(id)) {
			return;
		}
		
		// [HTTP]
		StringBuffer httpHosts = new StringBuffer();
		StringBuffer httpHaproxyUrls = new StringBuffer();
		StringBuffer httpServerNames = new StringBuffer();
		
		// [HTTPS]
		StringBuffer httpsHosts = new StringBuffer();
		StringBuffer httpsHaproxyUrls = new StringBuffer();
		StringBuffer httpsServerNames = new StringBuffer();
		StringBuffer httpsCertificates = new StringBuffer();
		StringBuffer httpsCertificateKeys = new StringBuffer();
		
		// [HTTP + HTTPS]
		StringBuffer hosts = new StringBuffer();			// upstream id
		StringBuffer haproxyUrls = new StringBuffer();		// upstream - server
		StringBuffer serverNames = new StringBuffer();		// server - server_name (domain)
		StringBuffer certificates = new StringBuffer();		// SSL certificates
		StringBuffer certificateKeys = new StringBuffer();	// SSL certificates key
		
		// [MUTUAL HTTPS]
		StringBuffer mHttpsHosts = new StringBuffer();
		StringBuffer mHttpsHaproxyUrls = new StringBuffer();
		StringBuffer mHttpsServerNames = new StringBuffer();
		StringBuffer mHttpsCertificates = new StringBuffer();		// SSL certificates
		StringBuffer mHttpsCertificateKeys = new StringBuffer();	// SSL certificates key
		StringBuffer mHttpsCaCertificates = new StringBuffer();		// SSL CA Certificates 
		StringBuffer mHttpsSslLevels = new StringBuffer();			// SSL CA Levels  
		
		// [MUTUAL HTTP + HTTPS] 
		StringBuffer mHosts = new StringBuffer();			// upstream id
		StringBuffer mHaproxyUrls = new StringBuffer();		// upstream - server
		StringBuffer mServerNames = new StringBuffer();		// server - server_name (domain)
		StringBuffer mCertificates = new StringBuffer();		// SSL certificates
		StringBuffer mCertificateKeys = new StringBuffer();	// SSL certificates key
		StringBuffer mCaCertificates = new StringBuffer();// SSL CA Certificates 
		StringBuffer mSslLevels = new StringBuffer();	// SSL CA Levels  
		
		int port = 80;
		int sslPort = 443;
		
		ICluster cluster = get(id);
		if (cluster == null) {
			throw new Exception("Cluster [id:" + id + ", type:Nginx] not found.");
		}
		List<NginxService> nginxServices = getServiceQuery().getByCluster(id, NginxService.class);
		if (nginxServices == null || nginxServices.size() == 0) {
			throw new Exception("Cluster [id:" + id + ", type:Nginx] instances not found.");
		}
		
		// HaProxy
		String[] haproxyClusters = getRelationClustersId(id);
		if (haproxyClusters != null && haproxyClusters.length > 0) {
			for (String cid : haproxyClusters) {
				ICluster e = get(cid); 
				if (e != null && e instanceof HaProxyCluster) {
					List<HaProxyService> services = getServiceQuery().getByCluster(cid, HaProxyService.class);
					HaProxyCluster hc = (HaProxyCluster) e;
					if(hc.getIsEnableDomain().equals("N")) continue;
					String domain = hc.getDomain();
					domain = StringUtil.isEmpty(domain) ? UUID.randomUUID().toString() : domain;
					
					String protocalType = hc.getProtocolType();
					if (HaProxyCluster.PROTOCOL_HTTP.equals(protocalType)) {
						httpServerNames.append(domain).append(','); // http [server_name xxx.paas.primeton.com]
						httpHosts.append(hc.getId()).append(','); // upstream xxx { server 192.168.100.11:7800; }
					} else if(HaProxyCluster.PROTOCOL_HTTPS.equals(protocalType)) {
						httpsServerNames.append(domain).append(','); // https [server_name xxx.paas.primeton.com]
						httpsHosts.append(hc.getId()).append(','); // upstream xxx { server 192.168.100.11:7800; }
						httpsCertificates.append(hc.getSslCertificate()).append(',');
						httpsCertificateKeys.append(hc.getSslCertificateKey()).append(',');
					} else if(HaProxyCluster.PROTOCOL_HTTP_HTTPS.equals(protocalType)) {
						serverNames.append(domain).append(','); // http + https [server_name xxx.paas.primeton.com]
						hosts.append(hc.getId()).append(','); // upstream xxx { server 192.168.100.11:7800; }
						certificates.append(hc.getSslCertificate()).append(',');
						certificateKeys.append(hc.getSslCertificateKey()).append(',');
					} else if(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(protocalType)){
						mHttpsServerNames.append(domain).append(',');// https [server_name xxx.paas.primeton.com]
						mHttpsHosts.append(hc.getId()).append(',');// upstream xxx { server 192.168.100.11:7800; }
						mHttpsCertificates.append(hc.getSslCertificate()).append(',');
						mHttpsCertificateKeys.append(hc.getSslCertificateKey()).append(',');
						mHttpsCaCertificates.append(hc.getCaSslCertificate()).append(',');
						mHttpsSslLevels.append(hc.getSslLevel()).append(',');
					} else if (HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(protocalType)) {
						mServerNames.append(domain).append(',');// http + https [server_name xxx.paas.primeton.com]
						mHosts.append(hc.getId()).append(',');// upstream xxx { server 192.168.100.11:7800; }
						mCertificates.append(hc.getSslCertificate()).append(',');
						mCertificateKeys.append(hc.getSslCertificateKey()).append(',');
						mCaCertificates.append(hc.getCaSslCertificate()).append(',');
						mSslLevels.append(hc.getSslLevel()).append(',');
					} else {
						httpServerNames.append(domain).append(','); // http [server_name xxx.paas.primeton.com]
						httpHosts.append(hc.getId()).append(','); // upstream xxx { server 192.168.100.11:7800; }
					}

					if (services != null && !services.isEmpty()) {
						for (int i=0; i<services.size(); i++) {
							HaProxyService instance = services.get(i);
							if(IService.HA_MODE_MASTER.equals(instance.getHaMode())) {
								HaProxyService first = services.get(0);
								services.set(0, instance);
								services.set(i, first);
								break;
							}
						}
						for (HaProxyService instance : services) {
							if (HaProxyCluster.PROTOCOL_HTTP.equals(protocalType)) {
								httpHaproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							} else if(HaProxyCluster.PROTOCOL_HTTPS.equals(protocalType)) {
								httpsHaproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							} else if(HaProxyCluster.PROTOCOL_HTTP_HTTPS.equals(protocalType)) {
								haproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							} else if(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(protocalType)){
								mHttpsHaproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							} else if (HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(protocalType)) {
								mHaproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							} else {
								httpHaproxyUrls.append(instance.getIp()).append(":").append(instance.getPort()).append(",");
							
							}
						}
						// ip:port,ip:port*ip:port*ip:port,ip:port...
						if (HaProxyCluster.PROTOCOL_HTTP.equals(protocalType)) {
							httpHaproxyUrls.deleteCharAt(httpHaproxyUrls.length() - 1);
						} else if(HaProxyCluster.PROTOCOL_HTTPS.equals(protocalType)) {
							httpsHaproxyUrls.deleteCharAt(httpsHaproxyUrls.length() - 1);
						} else if(HaProxyCluster.PROTOCOL_HTTP_HTTPS.equals(protocalType)) {
							haproxyUrls.deleteCharAt(haproxyUrls.length() - 1);
						} else if(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(protocalType)){
							mHttpsHaproxyUrls.deleteCharAt(mHttpsHaproxyUrls.length()-1);
						} else if (HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(protocalType)) {
							mHaproxyUrls.deleteCharAt(mHaproxyUrls.length()-1);
						} else {
							httpHaproxyUrls.deleteCharAt(httpHaproxyUrls.length() - 1);
						}
						
					} else {
						if (HaProxyCluster.PROTOCOL_HTTP.equals(protocalType)) {
							httpHaproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						} else if(HaProxyCluster.PROTOCOL_HTTPS.equals(protocalType)) {
							httpsHaproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						
						} else if(HaProxyCluster.PROTOCOL_HTTP_HTTPS.equals(protocalType)) {
							haproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						
						} else if(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(protocalType)){
							mHttpsHaproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						
						} else if (HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(protocalType)) {
							mHaproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						
						} else {
							httpHaproxyUrls.append(StringUtil.isNotEmpty(hc.getMembers()) ? hc.getMembers() : "127.0.0.1:80");
						}
					}

					if (HaProxyCluster.PROTOCOL_HTTP.equals(protocalType)) {
						httpHaproxyUrls.append("*");
					
					} else if(HaProxyCluster.PROTOCOL_HTTPS.equals(protocalType)) {
						httpsHaproxyUrls.append("*");
					
					} else if(HaProxyCluster.PROTOCOL_HTTP_HTTPS.equals(protocalType)) {
						haproxyUrls.append("*");
					
					} else if(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS.equals(protocalType)){
						mHttpsHaproxyUrls.append("*");
						
					} else if (HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS.equals(protocalType)) {
						mHaproxyUrls.append("*");
					
					} else {
						httpHaproxyUrls.append("*");
					}
				}
			}
		}
		
		if (hosts.length() > 0) hosts.deleteCharAt(hosts.length() - 1);
		if (httpHosts.length() > 0) httpHosts.deleteCharAt(httpHosts.length() - 1);
		if (httpsHosts.length() > 0) httpsHosts.deleteCharAt(httpsHosts.length() - 1);
		if (mHttpsHosts.length() > 0) mHttpsHosts.deleteCharAt(mHttpsHosts.length() - 1);//
		if (mHosts.length() > 0) mHosts.deleteCharAt(mHosts.length() - 1);//
		
		
		if (haproxyUrls.length() > 1) haproxyUrls.deleteCharAt(haproxyUrls.length() - 1);
		if (httpHaproxyUrls.length() > 1) httpHaproxyUrls.deleteCharAt(httpHaproxyUrls.length() - 1);
		if (httpsHaproxyUrls.length() > 1) httpsHaproxyUrls.deleteCharAt(httpsHaproxyUrls.length() - 1);
		if (mHttpsHaproxyUrls.length() > 0) mHttpsHaproxyUrls.deleteCharAt(mHttpsHaproxyUrls.length() - 1);//
		if (mHaproxyUrls.length() > 0) mHaproxyUrls.deleteCharAt(mHaproxyUrls.length() - 1);//
		
		if (serverNames.length() > 0) serverNames.deleteCharAt(serverNames.length() - 1);
		if (httpServerNames.length() > 0) httpServerNames.deleteCharAt(httpServerNames.length() - 1);
		if (httpsServerNames.length() > 0) httpsServerNames.deleteCharAt(httpsServerNames.length() - 1);
		if (mHttpsServerNames.length() > 0) mHttpsServerNames.deleteCharAt(mHttpsServerNames.length() - 1);//
		if (mServerNames.length() > 0) mServerNames.deleteCharAt(mServerNames.length() - 1);//
		
		if (certificates.length() > 0) certificates.deleteCharAt(certificates.length() - 1);
		if (httpsCertificates.length() > 0) httpsCertificates.deleteCharAt(httpsCertificates.length() - 1);
		if (mHttpsCertificates.length() > 0) mHttpsCertificates.deleteCharAt(mHttpsCertificates.length() - 1);//
		if (mCertificates.length() > 0) mCertificates.deleteCharAt(mCertificates.length() - 1);//
		
		if (certificateKeys.length() > 0) certificateKeys.deleteCharAt(certificateKeys.length() - 1);
		if (httpsCertificateKeys.length() > 0) httpsCertificateKeys.deleteCharAt(httpsCertificateKeys.length() - 1);//
		if (mHttpsCertificateKeys.length() > 0) mHttpsCertificateKeys.deleteCharAt(mHttpsCertificateKeys.length() - 1);//
		if (mCertificateKeys.length() > 0) mCertificateKeys.deleteCharAt(mCertificateKeys.length() - 1);//

		for (NginxService nginxService : nginxServices) {
			Map<String, String> arguments = new HashMap<String, String>();
			
			// Base
			arguments.put("nginxId", nginxService.getId()); //$NON-NLS-1$
			arguments.put("port", String.valueOf(port)); //$NON-NLS-1$
			arguments.put("sslPort", String.valueOf(sslPort)); //$NON-NLS-1$
			
			// upstream
			arguments.put("hosts", hosts.toString()); //$NON-NLS-1$
			arguments.put("httpHosts", httpHosts.toString()); //$NON-NLS-1$
			arguments.put("httpsHosts", httpsHosts.toString()); //$NON-NLS-1$
			arguments.put("mHosts", mHosts.toString()); //$NON-NLS-1$
			arguments.put("mHttpsHosts", mHttpsHosts.toString()); //$NON-NLS-1$
			
			
			// HaProxy URL
			arguments.put("haproxyUrls", haproxyUrls.toString()); //$NON-NLS-1$
			arguments.put("httpHaproxyUrls", httpHaproxyUrls.toString()); //$NON-NLS-1$
			arguments.put("httpsHaproxyUrls", httpsHaproxyUrls.toString()); //$NON-NLS-1$
			arguments.put("mHaproxyUrls", mHaproxyUrls.toString()); //$NON-NLS-1$
			arguments.put("mHttpsHaproxyUrls", mHttpsHaproxyUrls.toString()); //$NON-NLS-1$
			
			// Domain
			arguments.put("serverNames", serverNames.toString()); //$NON-NLS-1$
			arguments.put("httpServerNames", httpServerNames.toString()); //$NON-NLS-1$
			arguments.put("httpsServerNames", httpsServerNames.toString()); //$NON-NLS-1$
			arguments.put("mServerNames", mServerNames.toString()); //$NON-NLS-1$
			arguments.put("mHttpsServerNames", mHttpsServerNames.toString()); //$NON-NLS-1$
			
			// SSL Certificate
			arguments.put("certificates", certificates.toString()); //$NON-NLS-1$
			arguments.put("httpsCertificates", httpsCertificates.toString()); //$NON-NLS-1$
			arguments.put("mCertificates", mCertificates.toString()); //$NON-NLS-1$
			arguments.put("mHttpsCertificates", mHttpsCertificates.toString()); //$NON-NLS-1$
			
			// SSL Certificate Key
			arguments.put("certificateKeys", certificateKeys.toString()); //$NON-NLS-1$
			arguments.put("httpsCertificateKeys", httpsCertificateKeys.toString()); //$NON-NLS-1$
			arguments.put("mCertificateKeys", mCertificateKeys.toString()); //$NON-NLS-1$
			arguments.put("mHttpsCertificateKeys", mHttpsCertificateKeys.toString()); //$NON-NLS-1$
			
			//SSL Ca Certificate
			arguments.put("mCaCertificates", mCaCertificates.toString()); //$NON-NLS-1$
			arguments.put("mSslLevels", mSslLevels.toString()); //$NON-NLS-1$
			arguments.put("mHttpsCaCertificates", mHttpsCaCertificates.toString()); //$NON-NLS-1$
			arguments.put("mHttpsSslLevels", mHttpsSslLevels.toString()); //$NON-NLS-1$
			
			// ADD
			arguments.putAll(nginxService.getAttributes());

			String cmd = SystemVariables.getScriptPath(NginxService.TYPE, CONFIG_SCRIPT);
			SendMessageUtil.sendMessage(nginxService.getIp(), cmd, arguments, false);
		}
		
		logger.info("Generate nginx configuration file success.");
	}

	/**
	 * 
	 * @return
	 */
	protected static INginxServiceManager getNginxServiceManager() {
		return nginxServiceManager = (null == nginxServiceManager) 
				? (INginxServiceManager)ServiceManagerFactory.getManager(NginxService.TYPE) 
				: nginxServiceManager;
	}
	
}
