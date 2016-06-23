/**
 * 
 */
package com.primeton.paas.manage.api.impl.listener;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.cep.api.EPSException;
import com.primeton.paas.cep.api.EPSInstanceManager;
import com.primeton.paas.cep.api.EPSInstanceManagerFactory;
import com.primeton.paas.cep.model.EPS;
import com.primeton.paas.cep.model.EPSInstance;
import com.primeton.paas.cep.model.PreparedEPS;
import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.factory.ServiceManagerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultServiceEPSRelDao;
import com.primeton.paas.manage.api.impl.dao.ServiceEPSRel;
import com.primeton.paas.manage.api.impl.dao.ServiceEPSRelDao;
import com.primeton.paas.manage.api.impl.monitor.Constants;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.listener.ServiceEventListener;
import com.primeton.paas.manage.api.manager.IServiceQuery;
import com.primeton.paas.manage.api.model.ICluster;
import com.primeton.paas.manage.api.model.IService;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author liuyi(mailto:liu-yi@primeton.com)
 *
 */
public class ServiceMonitorListener implements ServiceEventListener {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(ServiceMonitorListener.class);
	
	private EPSInstanceManager manager;
	private ServiceEPSRelDao dao;
	private IServiceQuery query;
	
	private static final String listenerName = "com.primeton.paas.cep.listener.AVGServiceEventListener";
	
	/**
	 * 
	 */
	public ServiceMonitorListener() {
		super();
		manager = EPSInstanceManagerFactory.getManager();
		dao = new DefaultServiceEPSRelDao();
		query = ServiceManagerFactory.getServiceQuery();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.ServiceEventListener#doCreate(com.primeton.paas.manage.api.model.ICluster)
	 */
	public void doCreate(ICluster cluster) {
		if (cluster == null || StringUtil.isEmpty(cluster.getId())) {
			return;
		}
		String clusterId = cluster.getId();
		IService[] instances = query.getByCluster(clusterId);
		if (instances == null || instances.length <= 0
				|| IService.MODE_LOGIC.equals(instances[0].getMode())) {
			return;
		}
		
		if (dao.get(clusterId) != null) {
			dao.delete(clusterId);
		}
		
		long time = SystemVariables.getServiceMonitorInterval(); 
		time = time > 0 ? time : 10L; // seconds
		
		// like SQL
		final String statement = "select clusterId, avg(cpu_us), avg(cpu_sy), avg(cpu_ni), avg(cpu_id), avg(cpu_wa), avg(cpu_hi), avg(cpu_si), avg(cpu_st), avg(cpu_oneload), avg(cpu_fiveload), avg(cpu_fifteenload), avg(mem_total), avg(mem_used), avg(mem_free), avg(mem_buffers), avg(mem_us), avg(io_si), avg(io_so), avg(io_bi), avg(io_bo) from serviceMonitorEvent(100-cpu_us>=0, 100-cpu_sy>=0, 100-cpu_ni>=0, 100-cpu_id>=0, 100-cpu_wa>=0, 100-cpu_hi>=0, 100-cpu_si>=0, 100-cpu_st>=0, 100-mem_us>=0).win:time_batch(? sec) where clusterId = ? group by clusterId"; //$NON-NLS-1$
		EPS eps = new PreparedEPS(statement);
		eps.setLong(1, time);
		eps.setString(2, clusterId);
		EPSInstance instance = new EPSInstance(eps);
		instance.setCreateTime(System.currentTimeMillis());
		instance.setEnable(EPSInstance.ENABLE);
		instance.setEventName(Constants.EVENT_SERVICEMONITOR);
		instance.addListener(listenerName);
		
		EPSInstance result = null;
		try {
			result = manager.register(instance);
		} catch (EPSException e) {
			logger.error(e);
		}
		if (result != null) {
			ServiceEPSRel rel = new ServiceEPSRel();
			rel.setClusterId(clusterId);
			rel.setAvgInstId(result.getId());
			dao.add(rel);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.ServiceEventListener#doDestroy(com.primeton.paas.manage.api.model.ICluster)
	 */
	public void doDestroy(ICluster cluster) {
		if (cluster == null || StringUtil.isEmpty(cluster.getId())) {
			return;
		}
		String clusterId = cluster.getId();
		IService[] instances = query.getByCluster(clusterId);
		if (instances == null || instances.length <= 0
				|| IService.MODE_LOGIC.equals(instances[0].getMode())) {
			return;
		}
		ServiceEPSRel rel = dao.get(clusterId);
		if (rel == null) {
			return;
		}
		try {
			manager.unregister(rel.getDecInstId());
		} catch (EPSException e) {
			logger.error(e);
		}
		try {
			manager.unregister(rel.getIncInstId());
		} catch (EPSException e) {
			logger.error(e);
		}
		try {
			manager.unregister(rel.getAvgInstId());
		} catch (EPSException e) {
			logger.error(e);
		}
		dao.delete(clusterId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.ServiceEventListener#doModify(com.primeton.paas.manage.api.model.ICluster, com.primeton.paas.manage.api.model.ICluster)
	 */
	public void doModify(ICluster privious, ICluster now) {
	}

}
