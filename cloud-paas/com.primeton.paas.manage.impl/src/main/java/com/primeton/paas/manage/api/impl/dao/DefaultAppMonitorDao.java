/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.jdbc.ConnectionProviderFactory;
import com.primeton.paas.manage.api.monitor.AppMetaData;
import com.primeton.paas.manage.api.monitor.MetaData;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultAppMonitorDao implements AppMonitorDao {

	private static final String NAMESPACE = "appMonitorSqlMap";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultAppMonitorDao.class);

	private static final String SQL = "INSERT INTO pas_app_monitor (id, appName, occur_time, cpu_us, cpu_sy, cpu_ni, cpu_id, cpu_wa, cpu_hi, cpu_si, cpu_st, cpu_oneload, cpu_fiveload, cpu_fifteenload, mem_total, mem_used, mem_free, mem_buffers, mem_us, io_si, io_so, io_bi, io_bo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppMonitorDao#add(java.util.Map)
	 */
	public boolean add(Map<String, Object> metaData) {
		if (metaData == null || metaData.isEmpty()) {
			return false;
		}
		String id = UUID.randomUUID().toString();
		metaData.put(MetaData.ID, id);
		
		try {
			baseDao.insert(getStatementName("insert"), metaData); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppMonitorDao#add(com.primeton.paas.manage.api.monitor.AppMetaData)
	 */
	public boolean add(AppMetaData metaData) {
		if (metaData != null) {
			return add(metaData.getMetaData());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppMonitorDao#get(java.lang.String, long, long)
	 */
	public List<AppMetaData> get(String appName, long begin, long end) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> args = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(appName)) {
			args.put("appName", appName); //$NON-NLS-1$
		}
		if (begin < end && begin > 0) {
			args.put("begin", begin); //$NON-NLS-1$
			args.put("end", end); //$NON-NLS-1$
		}
		
		try {
			list = baseDao.queryForList(getStatementName("select"), args); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		List<AppMetaData> metaDatas = new ArrayList<AppMetaData>();
		if (list != null) {
			for (Map<String, Object> obj : list) {
				AppMetaData data = new AppMetaData();
				data.getMetaData().putAll(obj);
				metaDatas.add(data);
			}
		}
		return metaDatas;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppMonitorDao#delete(long, long)
	 */
	public void delete(long begin, long end) {
		Map<String, Object> condition = new HashMap<String, Object>();
		if (begin > 0) {
			condition.put("begin", begin); //$NON-NLS-1$
		}
		if (end > 0) {
			condition.put("end", end); //$NON-NLS-1$
		}
		try {
			baseDao.delete(getStatementName("delete"), condition); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
	}

	Connection conn = null;
	
	PreparedStatement stmt = null;
	
	public DefaultAppMonitorDao(){
		init();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean init(){
		boolean rtn = true;
		try {
			if (conn == null || conn.isClosed()) {
				try {
					conn = ConnectionProviderFactory.getConnection();
					conn.setAutoCommit(false);
				} catch (SQLException e) {
					logger.error(e);
					rtn = false;
				}
			}
			if( conn != null && stmt == null){
				try {
					stmt = conn.prepareStatement(SQL);
				} catch (SQLException e) {
					logger.error(e);
					rtn = false;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
			rtn = false;
		}
		return rtn;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.AppMonitorDao#addBatch(java.util.Collection)
	 */
	public <T extends Collection<Map<String, Object>>> boolean addBatch(T datas) {
		if (datas == null || datas.isEmpty()) {
			return false;
		}
		
		if (!init()) {
			return false;
		}
		
		try {
			for (Map<String, Object> e : datas) {
				stmt.setString(1, UUID.randomUUID().toString());
				stmt.setObject(2, e.get(AppMetaData.APP_NAME));
				stmt.setObject(3, e.get(MetaData.OCCUR_TIME));
				stmt.setObject(4, e.get(MetaData.CPU_US));
				stmt.setObject(5, e.get(MetaData.CPU_SY));
				stmt.setObject(6, e.get(MetaData.CPU_NI));
				stmt.setObject(7, e.get(MetaData.CPU_ID));
				stmt.setObject(8, e.get(MetaData.CPU_WA));
				stmt.setObject(9, e.get(MetaData.CPU_HI));
				stmt.setObject(10, e.get(MetaData.CPU_SI));
				stmt.setObject(11, e.get(MetaData.CPU_ST));
				stmt.setObject(12, e.get(MetaData.CPU_ONELOAD));
				stmt.setObject(13, e.get(MetaData.CPU_FIVELOAD));
				stmt.setObject(14, e.get(MetaData.CPU_FIFTEENLOAD));
				stmt.setObject(15, e.get(MetaData.MEM_TOTAL));
				stmt.setObject(16, e.get(MetaData.MEM_USED));
				stmt.setObject(17, e.get(MetaData.MEM_FREE));
				stmt.setObject(18, e.get(MetaData.MEM_BUFFERS));
				stmt.setObject(19, e.get(MetaData.MEM_US));
				stmt.setObject(20, e.get(MetaData.IO_SI));
				stmt.setObject(21, e.get(MetaData.IO_SO));
				stmt.setObject(22, e.get(MetaData.IO_BI));
				stmt.setObject(23, e.get(MetaData.IO_BO));
				
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			
			conn.commit();
			
			stmt.clearBatch();
			
			return true;
			
		} catch (SQLException e) {
			logger.error(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1);
			}
		} finally {
		}
		return false;
	}

}
