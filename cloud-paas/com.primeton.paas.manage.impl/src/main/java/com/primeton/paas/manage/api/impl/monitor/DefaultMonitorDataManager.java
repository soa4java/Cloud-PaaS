/**
 * 
 */
package com.primeton.paas.manage.api.impl.monitor;

import java.util.List;

import com.primeton.paas.manage.api.impl.dao.AppMonitorDao;
import com.primeton.paas.manage.api.impl.dao.DefaultAppMonitorDao;
import com.primeton.paas.manage.api.impl.dao.DefaultHostMonitorDao;
import com.primeton.paas.manage.api.impl.dao.DefaultServiceMonitorDao;
import com.primeton.paas.manage.api.impl.dao.HostMonitorDao;
import com.primeton.paas.manage.api.impl.dao.ServiceMonitorDao;
import com.primeton.paas.manage.api.monitor.AppMetaData;
import com.primeton.paas.manage.api.monitor.IMonitorDataManager;
import com.primeton.paas.manage.api.monitor.MetaData;
import com.primeton.paas.manage.api.monitor.ServiceMetaData;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultMonitorDataManager implements IMonitorDataManager {
	
	private HostMonitorDao hostDao = new DefaultHostMonitorDao();
	private AppMonitorDao appDao = new DefaultAppMonitorDao();
	private ServiceMonitorDao serviceDao = new DefaultServiceMonitorDao();

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.monitor.MonitorDataManager#getByIp(java.lang.String, long, long)
	 */
	public List<MetaData> getByIp(String ip, long begin, long end) {
		return hostDao.get(ip, begin, end);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.monitor.MonitorDataManager#getByApp(java.lang.String, long, long)
	 */
	public List<AppMetaData> getByApp(String appName, long begin, long end) {
		return appDao.get(appName, begin, end);
	}

	public MetaData getLatestDataByIp(String ip) {
		return hostDao.getLatestData(ip);
	}
	
	public ServiceMetaData getByClusterId(String clusterId) {
		return serviceDao.get(clusterId);
	}
	
}
