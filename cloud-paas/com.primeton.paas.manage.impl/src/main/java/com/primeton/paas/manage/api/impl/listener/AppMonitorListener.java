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
import com.primeton.paas.manage.api.impl.dao.AppEPSRel;
import com.primeton.paas.manage.api.impl.dao.AppEPSRelDao;
import com.primeton.paas.manage.api.impl.dao.DefaultAppEPSRelDao;
import com.primeton.paas.manage.api.impl.monitor.Constants;
import com.primeton.paas.manage.api.impl.util.SystemVariables;
import com.primeton.paas.manage.api.listener.AppEventListener;
import com.primeton.paas.manage.api.model.WebApp;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author LiZhongwen
 * @mail lizw@primeton.com
 * @dept RD
 * @date 2013-11-15
 *
 */
public class AppMonitorListener implements AppEventListener {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(AppMonitorListener.class);
	
	private EPSInstanceManager manager;
	private AppEPSRelDao dao;
	
	private static final String listenerName = "com.primeton.paas.cep.listener.AVGEventListener";
	
	/**
	 * 
	 */
	public AppMonitorListener() {
		super();
		manager = EPSInstanceManagerFactory.getManager();
		dao = new DefaultAppEPSRelDao();
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.AppEventListener#doCreate(com.primeton.paas.manage.api.model.WebApp)
	 */
	public void doCreate(WebApp webApp) {
		if (webApp == null || StringUtil.isEmpty(webApp.getName())) {
			return;
		}
		String appName = webApp.getName();
		if (dao.get(appName) != null) {
			dao.delete(appName);
		}
		long time = SystemVariables.getAppMonitorInterval();
		time = time > 0 ? time : 10L; // seconds
		
		// like SQL
		final String statement = "select appName, avg(cpu_us), avg(cpu_sy), avg(cpu_ni), avg(cpu_id), avg(cpu_wa), avg(cpu_hi), avg(cpu_si), avg(cpu_st), avg(cpu_oneload), avg(cpu_fiveload), avg(cpu_fifteenload), avg(mem_total), avg(mem_used), avg(mem_free), avg(mem_buffers), avg(mem_us), avg(io_si), avg(io_so), avg(io_bi), avg(io_bo) from appMonitorEvent(100-cpu_us>=0, 100-cpu_sy>=0, 100-cpu_ni>=0, 100-cpu_id>=0, 100-cpu_wa>=0, 100-cpu_hi>=0, 100-cpu_si>=0, 100-cpu_st>=0, 100-mem_us>=0).win:time_batch(? sec) where appName = ? group by appName";
		EPS eps = new PreparedEPS(statement);
		eps.setLong(1, time);
		eps.setString(2, appName);
		EPSInstance instance = new EPSInstance(eps);
		instance.setCreateTime(System.currentTimeMillis());
		instance.setEnable(EPSInstance.ENABLE);
		instance.setEventName(Constants.EVENT_APPMONITOR);
		instance.addListener(listenerName);
		
		EPSInstance result = null;
		try {
			result = manager.register(instance);
		} catch (EPSException e) {
			logger.error(e);
		}
		if (result != null) {
			AppEPSRel rel = new AppEPSRel();
			rel.setAppName(appName);
			rel.setAvgInstId(result.getId());
			dao.add(rel);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.AppEventListener#doDestroy(com.primeton.paas.manage.api.model.WebApp)
	 */
	public void doDestroy(WebApp webApp) {
		if (webApp == null || StringUtil.isEmpty(webApp.getName())) {
			return;
		}
		String appName = webApp.getName();
		AppEPSRel rel = dao.get(appName);
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
		dao.delete(appName);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.listener.AppEventListener#doModify(com.primeton.paas.manage.api.model.WebApp, com.primeton.paas.manage.api.model.WebApp)
	 */
	public void doModify(WebApp privious, WebApp now) {
		// Nothing to do
	}

}
