/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.List;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.api.EPSException;
import com.primeton.paas.cep.api.EPSInstanceManager;
import com.primeton.paas.cep.api.EPSInstanceManagerFactory;
import com.primeton.paas.cep.model.EPS;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.model.PreparedEPS;
import com.primeton.paas.manage.api.app.IServiceWarnStrategyManager;
import com.primeton.paas.manage.api.app.ServiceWarnStrategy;
import com.primeton.paas.manage.api.app.ServiceWarnStrategyItem;
import com.primeton.paas.manage.api.factory.ClusterManagerFactory;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultServiceEPSRelDao;
import com.primeton.paas.manage.api.impl.dao.DefaultServiceWarnStrategyDao;
import com.primeton.paas.manage.api.impl.dao.ServiceEPSRel;
import com.primeton.paas.manage.api.impl.dao.ServiceEPSRelDao;
import com.primeton.paas.manage.api.impl.monitor.Constants;
import com.primeton.paas.manage.api.manager.IClusterManager;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceWarnStrategyManager implements IServiceWarnStrategyManager {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceWarnStrategyManager.class);
	
	private static DefaultServiceWarnStrategyDao serviceWarnDao = DefaultServiceWarnStrategyDao.getInstance();
	private ServiceEPSRelDao serviceEPSRelDao = new DefaultServiceEPSRelDao();
	
	private EPSInstanceManager epsInstanceManager = EPSInstanceManagerFactory.getManager();
	
	private static final String upperListenerName = "com.primeton.paas.cep.listener.UpperServiceWarnEventListener";
	
	private static final String STATEMENT = "select clusterId, avg(cpu_us), avg(cpu_sy), avg(cpu_ni), avg(cpu_id), avg(cpu_wa), avg(cpu_hi), avg(cpu_si), avg(cpu_st), avg(cpu_oneload), avg(cpu_fiveload), avg(cpu_fifteenload), avg(mem_total), avg(mem_used), avg(mem_free), avg(mem_buffers), avg(mem_us), avg(io_si), avg(io_so), avg(io_bi), avg(io_bo) from serviceMonitorEvent(100-cpu_us>=0, 100-cpu_sy>=0, 100-cpu_ni>=0, 100-cpu_id>=0, 100-cpu_wa>=0, 100-cpu_hi>=0, 100-cpu_si>=0, 100-cpu_st>=0, 100-mem_us>=0).win:time(? min) where clusterId = ? having 1=2 ";
	
	private static IClusterManager clusterManager = ClusterManagerFactory.getManager(IClusterManager.DEFAULT_TYPE);
	private IServiceQuery serviceQuery = ServiceManagerFactory.getServiceQuery();
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#save(com.primeton.paas.manage.api.app.ServiceWarnStrategy)
	 */
	public void save(ServiceWarnStrategy strategy){
		if (strategy == null || StringUtil.isEmpty(strategy.getClusterId())) {
			logger.warn("Save strategy cancelled, strategy is null or clusterId is null.");
			return;
		}
		String clusterId = strategy.getClusterId();
		boolean isGlobalStrategy = false;
		boolean isSuccess = false;
		
		if (ServiceWarnStrategy.GLOBAL_STRATEGY_ID.equals(strategy.getClusterId())) {
			isGlobalStrategy = true;
		} 
		
		List<String> serviceList = null;
		try{
			logger.info("save service strategy.{clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "}.");
			serviceWarnDao.saveStrategy(strategy);
			if (isGlobalStrategy) {
				logger.info("update global stretch strategy : update relation application's cep rules");
				serviceList = serviceWarnDao.getServiceByStrategyId(ServiceWarnStrategy.GLOBAL_STRATEGY_ID);//clusterIds
			}
			isSuccess = true;
		} catch (DaoException e) {
			logger.error(e);
		}
		if (!isSuccess) {
			logger.info("Save service strategy.{clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "} failed.");
			return;
		}
		
		if (isGlobalStrategy) {
			if (serviceList != null && serviceList.size() > 0) {
				for (String cluster_Id: serviceList) {
					if(cluster_Id.equals(ServiceWarnStrategy.GLOBAL_STRATEGY_ID)){
						continue;
					}
					logger.info("Save service strategy : {clusterId=["+cluster_Id+"], strategtyId="+ strategy.getStrategyId() + "} 's cep rule");
					saveServiceCepRule(cluster_Id, strategy);
				}
			}
		} else {
			logger.info("Save service strategy : {clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "} 's cep rule");
			saveServiceCepRule(clusterId,strategy);
		}
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param strategy
	 * @return
	 */
	private boolean saveServiceCepRule(String clusterId,
			ServiceWarnStrategy strategy) {
		int cpu = 0;
		int mem = 0;
		float lb = 0;
	
		String strategyId = strategy.getStrategyId();
		boolean isEnable = strategy.isEnable();
		
		if (!isEnable) {
			if (logger.isInfoEnabled()) {
				logger.info("Get service-eps-rel.{clusterId:" +clusterId+"}.");
			}
			ServiceEPSRel rel = serviceEPSRelDao.get(clusterId);
			if (rel != null) {
				String instId = null;
				instId = rel.getIncInstId();
				if (StringUtil.isNotEmpty(instId) && Integer.parseInt(instId) > 0) {
					try {
						logger.info("Disable service cep rule. {strategyId:"+strategyId + ",cepInstId:"+instId + "}.");
						epsInstanceManager.disable(instId);
					} catch (EPSException e) {
						logger.error(e);
						throw new RuntimeException("");
					}
				}
			}
			return true;
		}
		logger.info("Begin save service cep rule. {clusterId:"+clusterId + "}.");
		List<ServiceWarnStrategyItem> itemList = strategy == null ? null :strategy.getServiceWarnStrategyItems(); 
		
		if (strategy == null || itemList == null || itemList.isEmpty()) {
			logger.info("Save service cep rules cancelled. strategy is null.{clusterId= "+ clusterId +"}.");
			return true;
		}
		
		for (ServiceWarnStrategyItem item : itemList) {
			String type = item.getItemType();
			String threshold = StringUtil.isEmpty(item.getThreshold()) ? "0" : item.getThreshold();
			if (ServiceWarnStrategyItem.TYPE_CPU.equals(type)) {
				cpu = Integer.parseInt(threshold);
			} else if (ServiceWarnStrategyItem.TYPE_MEMORY.equals(type)) {
				mem = Integer.parseInt(threshold);
			} else if (ServiceWarnStrategyItem.TYPE_LB.equals(type)) {
				lb = Float.parseFloat(threshold);
			}  
		}
		
		StringBuffer sbf = new StringBuffer();
		sbf.append(STATEMENT);
		
		long time = strategy.getContinuedTime();
		String statement = null;
			
		if (cpu > 0) {
			sbf.append(" or avg(cpu_us) >= " + cpu);
		} 
			
		if (mem > 0) {
			sbf.append(" or avg(mem_us) >= " + mem);
		}
			
		if (lb > 0) {
			sbf.append(" or avg(cpu_oneload) - " + lb + " >= 0 ");
		}
		
		ServiceEPSRel rel = serviceEPSRelDao.get(clusterId);
		boolean exists = true;
		if (rel == null) {
			exists = false;
			rel = new ServiceEPSRel();
			rel.setClusterId(clusterId);
			serviceEPSRelDao.add(rel);
		}
		
		String epsId = null;
		String listener = null;
		
		epsId = rel.getIncInstId();
		listener = upperListenerName;
		statement =  sbf.toString();
		if (exists && StringUtil.isNotEmpty(epsId) && !"0".equals(epsId)) { // UPDATE
			EPS eps = new PreparedEPS(statement);
			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, clusterId);
			
			EPSInstance instance = epsInstanceManager.get(epsId);
			if (instance == null) {
				instance = new EPSInstance();
				instance.setCreateTime(System.currentTimeMillis());
				instance.setEventName(Constants.EVENT_SERVICEMONITOR);
				instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
				try {
					epsInstanceManager.register(instance);
				} catch (EPSException e) {
					logger.error(e);
				}
				rel.setIncInstId(instance.getId());
				serviceEPSRelDao.update(rel);
				
			}
			instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
			instance.setEps(eps.exportStatement());
			
			try {
				epsInstanceManager.update(instance);
			} catch (EPSException e) {
				logger.error(e);
			}
			
		} else { // ADD
			EPS eps = new PreparedEPS(statement);
			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, clusterId);
		
			EPSInstance instance = new EPSInstance(eps);
			instance.setCreateTime(System.currentTimeMillis());
			instance.setEventName(Constants.EVENT_SERVICEMONITOR);
			instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
			instance.addListener(listener);
			EPSInstance result = null;
			
			try {
				result = epsInstanceManager.register(instance);
			} catch (EPSException e) {
				logger.error(e);
			}
			if (result != null) {
				rel.setIncInstId(result.getId());
			}
			serviceEPSRelDao.update(rel);
		}
		return true;
	}
	
	/**
	 * 
	 * @param strategy
	 */
	public void save_old(ServiceWarnStrategy strategy) {
		if(strategy == null || StringUtil.isEmpty(strategy.getClusterId())) {
			logger.warn("Save strategy cancelled, strategy is null or clusterId is null.");
			return;
		}
		String clusterId = strategy.getClusterId();
		boolean isSuccess = false;
		try{
			logger.info("save service strategy.{clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "}.");
			serviceWarnDao.saveStrategy(strategy);
			isSuccess = true;
		} catch (DaoException e) {
			logger.error(e);
		}
		if (!isSuccess) {
			logger.info("Save service strategy.{clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "} failed.");
			return;
		}
		logger.info("Save service strategy : {clusterId=["+clusterId+"], strategtyId="+ strategy.getStrategyId() + "} 's cep rule");
		saveServiceCepRule_old(clusterId, strategy);
	}
	
	/**
	 * 
	 * @param clusterId
	 * @param strategy
	 * @return
	 */
	private boolean saveServiceCepRule_old(String clusterId,
			ServiceWarnStrategy strategy) {
		int cpu = 0;
		int mem = 0;
		float lb = 0;
	
		String strategyId = strategy.getStrategyId();
		boolean isEnable = strategy.isEnable();
		
		if (!isEnable) {
			logger.info("Get service-eps-rel.{clusterId:" +clusterId+"}.");
			ServiceEPSRel rel = serviceEPSRelDao.get(clusterId);
			
			if (rel != null) {
				String instId = null;
				instId = rel.getIncInstId();
				if (StringUtil.isNotEmpty(instId) && Integer.parseInt(instId) > 0) {
					try {
						logger.info("Disable service cep rule. {strategyId:"+strategyId + ",cepInstId:"+instId + "}.");
						epsInstanceManager.disable(instId);
					} catch (EPSException e) {
						logger.error(e);
						throw new RuntimeException("");
					}
				}
			}
			return true;
		}
		
		logger.info("Begin save service cep rule. {strategyId:"+strategyId + "}.");
		List<ServiceWarnStrategyItem> itemList = strategy == null ? null :strategy.getServiceWarnStrategyItems(); 
		if (strategy == null || itemList == null || itemList.isEmpty()) {
			logger.info("Save service cep rules cancelled. strategy is null.{clusterId= "+ clusterId +"}.");
			return true;
		}
		
		for (ServiceWarnStrategyItem item : itemList) {
			String type = item.getItemType();
			String threshold = StringUtil.isEmpty(item.getThreshold()) ? "0" : item.getThreshold(); //$NON-NLS-1$
			if (ServiceWarnStrategyItem.TYPE_CPU.equals(type)) {
				cpu = Integer.parseInt(threshold);
			} else if (ServiceWarnStrategyItem.TYPE_MEMORY.equals(type)) {
				mem = Integer.parseInt(threshold);
			} else if (ServiceWarnStrategyItem.TYPE_LB.equals(type)) {
				lb = Float.parseFloat(threshold);
			}  
		}
		
		StringBuffer sbf = new StringBuffer();
		sbf.append(STATEMENT);
		
		long time = strategy.getContinuedTime();
		String statement = null;
			
		if (cpu > 0) {
			sbf.append(" or avg(cpu_us) >= " + cpu);
		} 
			
		if (mem > 0) {
			sbf.append(" or avg(mem_us) >= " + mem);
		}
			
		if (lb > 0) {
			sbf.append(" or avg(cpu_oneload) - " + lb + " >= 0 ");
		}
		
		ServiceEPSRel rel = serviceEPSRelDao.get(clusterId);
		boolean exists = true;
		if (rel == null) {
			exists = false;
			rel = new ServiceEPSRel();
			rel.setClusterId(clusterId);
			serviceEPSRelDao.add(rel);
		}
		
		String epsId = null;
		String listener = null;
		
		epsId = rel.getIncInstId();
		listener = upperListenerName;
		statement =  sbf.toString();
		if (exists && StringUtil.isNotEmpty(epsId) && !"0".equals(epsId)) { // UPDATE //$NON-NLS-1$
			EPS eps = new PreparedEPS(statement);
			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, clusterId);
			
			EPSInstance instance = epsInstanceManager.get(epsId);
			if (instance == null) {
				instance = new EPSInstance();
				instance.setCreateTime(System.currentTimeMillis());
				instance.setEventName(Constants.EVENT_SERVICEMONITOR);
				instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
				try {
					epsInstanceManager.register(instance);
				} catch (EPSException e) {
					logger.error(e);
				}
				rel.setIncInstId(instance.getId());
				serviceEPSRelDao.update(rel);
				
			}
			instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
			instance.setEps(eps.exportStatement());
			
			try {
				epsInstanceManager.update(instance);
			} catch (EPSException e) {
				logger.error(e);
			}
		} else { // ADD
			EPS eps = new PreparedEPS(statement);
			time = time > 0 ? time : 10L;
			eps.setLong(1, time);
			eps.setString(2, clusterId);
		
			EPSInstance instance = new EPSInstance(eps);
			instance.setCreateTime(System.currentTimeMillis());
			instance.setEventName(Constants.EVENT_SERVICEMONITOR);
			instance.setEnable(strategy.isEnable() ? EPSInstance.ENABLE : EPSInstance.DISABLE);
			instance.addListener(listener);
			EPSInstance result = null;
			
			try {
				result = epsInstanceManager.register(instance);
			} catch (EPSException e) {
				logger.error(e);
			}
			if (result != null) {
				rel.setIncInstId(result.getId());
			}
			serviceEPSRelDao.update(rel);
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#get(java.lang.String)
	 */
	public ServiceWarnStrategy get(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return null;
		}
		return serviceWarnDao.getWarnStrategy(clusterId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#remove(java.lang.String)
	 */
	public void remove(String clusterId) {
		if (StringUtil.isEmpty(clusterId)) {
			return;
		}
		ServiceEPSRel rel = serviceEPSRelDao.get(clusterId);
		if (rel != null) {
			if (StringUtil.isNotEmpty(rel.getIncInstId())) {
				try {
					epsInstanceManager.unregister(rel.getIncInstId());
				} catch (EPSException e) {
					logger.error(e);
				}
			}
			rel.setDecInstId("");
			rel.setIncInstId("");
			serviceEPSRelDao.update(rel);
		}
		
		try{
			ServiceWarnStrategy incStretch = get(clusterId);
			if (incStretch == null) {
				return;
			}
			serviceWarnDao.removeServiceWarnStrategyByClusterId(clusterId);
			serviceEPSRelDao.delete(clusterId);
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#getGlobalStrategy()
	 */
	public ServiceWarnStrategy getGlobalStrategy() {
		return serviceWarnDao.getWarnStrategyByStrategyId(ServiceWarnStrategy.GLOBAL_STRATEGY_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#queryAppServiceMonitor(java.lang.String)
	 */
	public boolean queryAppServiceMonitor(String appName) {
		if (appName == null) {
			return false;
		}
		ICluster[] clusters = clusterManager.getByApp(appName);
		for (ICluster cluster : clusters) {
			String clusterId = cluster.getId();
			IService[] instances = serviceQuery.getByCluster(clusterId);
			if (instances == null || instances.length <= 0
					|| IService.MODE_LOGIC.equals(instances[0].getMode())) {
				continue;
			}
			String strategyId = serviceWarnDao.getServiceStrategyIdByClusterId(clusterId);
			return null != strategyId;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.app.IServiceWarnStrategyManager#queryServiceMonitor(java.lang.String)
	 */
	public boolean queryServiceMonitor(String clusterId) {
		if (clusterId == null) {
			return false;
		}
		IService[] instances = serviceQuery.getByCluster(clusterId);
		if (instances == null || instances.length <= 0
				|| IService.MODE_LOGIC.equals(instances[0].getMode())) {
			return false;
		}
		String strategyId = serviceWarnDao
				.getServiceStrategyIdByClusterId(clusterId);
		if (strategyId == null) {
			return false;
		}
		return true;
	}
	
}
