/**
 * 
 */
package com.primeton.paas.app.api.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManager;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;
import org.gocom.cloud.cesium.zkclient.api.ZkChildListener;
import org.gocom.cloud.cesium.zkclient.api.ZkClient;
import org.gocom.cloud.cesium.zkclient.api.ZkClientFactory;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.cache.MemCachedServiceImpl;
import com.primeton.paas.app.cache.SimpleCachedServiceImpl;
import com.primeton.paas.app.config.ConfigModelManager;
import com.primeton.paas.app.config.model.IServiceConstants;
import com.primeton.paas.app.config.model.ServiceSourceModel;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;

/**
 * <pre>
 * ICacheService cacheService = CacheServiceFactory.getCacheService(); 
 * cacheService.put("key1", "value1");
 * String value = cacheService.get("key1");
 * </pre>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class CacheServiceFactory {
	
	private static ILogger logger = SystemLoggerFactory.getLogger(CacheServiceFactory.class);
	
	private static final String BASE_PATH = "/Cloud/Cesium/Services/" + IServiceConstants.SERVICE_MEMCACHED; //$NON-NLS-1$
	
	private static ICacheService cacheService = null;
	private static MemcachedClient memcachedClient = null;
	private static List<String> servers = new ArrayList<String>();
	
	private static boolean isInitialize = false;
	
	/**
	 * 
	 */
	private CacheServiceFactory() {
		super();
	}

	/**
	 * 
	 * @return
	 * @throws CacheServiceException
	 */
	public static ICacheService getCacheService() throws CacheServiceException {
		synchronized (CacheServiceFactory.class) {
			if (cacheService == null || cacheService.isShutdown()) {
				if (ServerContext.getInstance().getRunMode() == AppConstants.RUN_MODE_CLOUD) {
					init();
					if (servers == null || servers.isEmpty()) {
						isInitialize = false;
						throw new CacheServiceException("Memcached server instances not found.");
					}
					StringBuffer address = new StringBuffer();
					for (String server : servers) {
						address.append(server).append(' ');
					}
					address.deleteCharAt(address.length() - 1);
					MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(address.toString()));
					try {
						memcachedClient = builder.build();
						cacheService = new MemCachedServiceImpl(memcachedClient);
					} catch (IOException e) {
						logger.error(e);
					}
				} else {
					cacheService = null == cacheService ? new SimpleCachedServiceImpl() : cacheService; 
				}
			}
		}
		return cacheService;
	}
	
	
	/**
	 * 
	 */
	private static void init() {
		if (isInitialize) {
			return;
		}
		ServiceSourceModel[] models = ConfigModelManager.getServiceSourceModels(IServiceConstants.SERVICE_MEMCACHED);
		if(models == null || models.length == 0) {
			logger.error("Can not find memcached instances on zookeeper.");
			return;
		}
		InstanceManager instanceManager = null;
		//		ZkConfig zkConfig = ConfigModelManager.getZkConfig();
		ZkClient zkClient = ZkClientFactory.getZkClient();
		
		try {
			instanceManager = InstanceManagerFactory.createInstanceManager();
			for (ServiceSourceModel model : models) {
				RuntimeInstance[] instances = instanceManager.getInstances(IServiceConstants.SERVICE_MEMCACHED, model.getClusterId());
				if(instances == null || instances.length == 0) {
					continue;
				}
				for (RuntimeInstance instance : instances) {
					String server = instance.getIp() + ":" + instance.getPort();
					if (!servers.contains(server)) {
						servers.add(server);
					}
				}
				String path = BASE_PATH + "/" + model.getClusterId();
				logger.info("Subscribe zookeeper path [" + path + "] child changes.");
				zkClient.subscribeChildChanges(path, new ZkChildListener() {
					
					public void handleChildChange(String parentPath, List<String> currentChilds)
							throws Exception {
						logger.info("Zookeeper path [" + parentPath + "] child changed to " + currentChilds + ".");
						if (currentChilds == null || currentChilds.isEmpty()) {
							cacheService.shutdown();
							servers.clear();
							isInitialize = false;
							return;
						}
						ZkClient zkClient = ZkClientFactory.getZkClient();
						List<String> addressList = new ArrayList<String>();
						
						for (String id : currentChilds) {
							RuntimeInstance instance = zkClient.readData(parentPath + "/" + id);
							if (instance != null) {
								String address = instance.getIp() + ":" + instance.getPort();
								if (!addressList.contains(address)) {
									addressList.add(address);
								}
							}
						}
						
						if (!addressList.isEmpty()) {
							StringBuffer loseAddress = new StringBuffer();
							StringBuffer newAddress = new StringBuffer();
							for (String server : servers) {
								if (!addressList.contains(server)) {
									loseAddress.append(server).append(' ');
								}
							}
							for (String server : addressList) {
								if (!servers.contains(server)) {
									newAddress.append(server).append(' ');
								}
							}
							if (loseAddress.length() > 1) {
								loseAddress.deleteCharAt(loseAddress.length() - 1);
								String hostList = loseAddress.toString();
								memcachedClient.removeServer(hostList);
								logger.info("Memcached server instances list changed, remove servers [" + hostList + "].");
							}
							if (newAddress.length() > 1) {
								newAddress.deleteCharAt(newAddress.length() - 1);
								String hostList = newAddress.toString();
								memcachedClient.addServer(hostList);
								logger.info("Memcached server instances list changed, add servers [" + hostList + "].");
							}
							servers.clear();
							servers = addressList;
							logger.info("Memcached server instances " + servers + ".");
						}
					}
				});
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		isInitialize = servers.isEmpty() ? false : true;
	}
	
}