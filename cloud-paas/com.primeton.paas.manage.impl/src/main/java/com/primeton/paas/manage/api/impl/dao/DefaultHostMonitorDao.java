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
import com.primeton.paas.manage.api.monitor.MetaData;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class DefaultHostMonitorDao implements HostMonitorDao {
	
	private static final String NAMESPACE = "hostMonitorSqlMap";
	
	private static final String SQL = "INSERT INTO pas_host_monitor (id, ip, occur_time, cpu_us, cpu_sy, cpu_ni, cpu_id, cpu_wa, cpu_hi, cpu_si, cpu_st, cpu_oneload, cpu_fiveload, cpu_fifteenload, mem_total, mem_used, mem_free, mem_buffers, mem_us, io_si, io_so, io_bi, io_bo,sto_filesystem,sto_size,sto_used,sto_avail,sto_use,sto_mounted ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultHostMonitorDao.class);
	
	private static String getStatementName(String id) {
		return NAMESPACE + "." + id;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#add(java.util.Map)
	 */
	public boolean add(Map<String, Object> data) {
		if (data == null || data.isEmpty()) {
			return false;
		}
		String id = UUID.randomUUID().toString();
		data.put(MetaData.ID, id);
		try {
			baseDao.insert(getStatementName("insert"), data); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#add(com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata)
	 */
	public boolean add(MetaData metadata) {
		if (metadata != null) {
			return add(metadata.getMetaData());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#get(java.lang.String, long, long)
	 */
	public List<MetaData> get(String ip, long begin, long end) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> args = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(ip)) {
			args.put("ip", ip);
		}
		if (begin < end && begin > 0) {
			args.put("begin", begin);
			args.put("end", end);
		}
		try {
			list = baseDao.queryForList(getStatementName("select"), args); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		List<MetaData> metaDatas = new ArrayList<MetaData>();
		if (list != null) {
			for (Map<String, Object> obj : list) {
				MetaData data = new MetaData();
				data.getMetaData().putAll(obj);
				metaDatas.add(data);
			}
		}
		return metaDatas;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#getLatestData(java.lang.String)
	 */
	public MetaData getLatestData(String ip) {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = baseDao.queryForObject(getStatementName("selectLatestData"), ip); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		MetaData data = new MetaData();
		if (map != null) {
			data.getMetaData().putAll(map);
		}
		return data;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#delete(long, long)
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
	
	public DefaultHostMonitorDao(){
		init();
	}
	
	public boolean init() {
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
			if (conn != null && stmt == null) {
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
	 * @see com.primeton.paas.manage.api.impl.dao.HostMonitorDao#addBatch(java.util.Collection)
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
				stmt.setObject(2, e.get(MetaData.IP));
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
				stmt.setObject(24, e.get(MetaData.STO_FILESYSTEM));
				stmt.setObject(25, e.get(MetaData.STO_SIZE));
				stmt.setObject(26, e.get(MetaData.STO_USED));
				stmt.setObject(27, e.get(MetaData.STO_AVAIL));
				stmt.setObject(28, e.get(MetaData.STO_USE));
				stmt.setObject(29, e.get(MetaData.STO_MOUNTED));
				
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
