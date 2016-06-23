/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.util.StringUtil;
import com.primeton.paas.manage.spi.model.VIPSegment;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultVIPDao {
	
	private final static String VIP_SQL_MAP = "vipSqlMap";
	
	private static BaseDao baseDao = BaseDao.getInstance();
	
	private static DefaultVIPDao instance = new DefaultVIPDao();
	
	private static ILogger logger = ManageLoggerFactory.getLogger(DefaultVIPDao.class);
	
	private DefaultVIPDao(){}
	
	/**
	 * 
	 * @return
	 */
	public static DefaultVIPDao getInstance() {
		return instance;
	}
	
	private String getSqlMap(String sqlId){
		return VIP_SQL_MAP + "." + sqlId;
	}

	/**
	 * 
	 * @param segment
	 * @return
	 */
	public boolean addVIPSegment(VIPSegment segment) {
		if (segment == null || StringUtil.isEmpty(segment.getId())) {
			return false;
		}
		VIPSegment obj = getVIPSegment(segment.getId());
		if(obj != null) {
			logger.warn(obj + " is already exists.");
			return false;
		}
		try {
			baseDao.insert(getSqlMap("addVIPSegment"), segment); //$NON-NLS-1$
			return true;
		}catch(DaoException e){
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param ip
	 * @param segmentId
	 * @return
	 */
	public boolean addVIP(String ip, String segmentId) {
		if (ip == null || segmentId == null) {
			return false;
		}
		List<String> usedIp = getUsedIPs(segmentId);
		if (usedIp != null && usedIp.contains(ip)) {
			logger.warn("IP: " + ip + " is already used.");
			return false;
		}
		try {
			Map<String, String> addVip = new HashMap<String, String>();
			addVip.put("vip", ip); //$NON-NLS-1$
			addVip.put("segmentId", segmentId); //$NON-NLS-1$
			baseDao.insert(getSqlMap("addVIP"), addVip); //$NON-NLS-1$
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	public boolean updateVIPSegment(VIPSegment segment) {
		if (segment == null || StringUtil.isEmpty(segment.getId())) {
			return false;
		}
		VIPSegment obj = getVIPSegment(segment.getId());
		if (obj == null) {
			logger.warn("VIPSegment " + segment.getId() + " is not exists.");
			return false;
		}
		try {
			Map<String, String> updateMap = new HashMap<String, String>();
			updateMap.put("id", segment.getId()); //$NON-NLS-1$
			updateMap.put("begin", segment.getBegin()); //$NON-NLS-1$
			updateMap.put("end", segment.getEnd()); //$NON-NLS-1$
			updateMap.put("netmask", segment.getNetmask()); //$NON-NLS-1$

			int result = baseDao.update(getSqlMap("updateVIPSegment"), //$NON-NLS-1$
					updateMap);
			return (result != 0);
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param segmentId
	 * @return
	 */
	public boolean deleteVIPSegment(String segmentId) {
		if (StringUtil.isEmpty(segmentId)) {
			logger.error("segmentId is null!");
			return false;
		}
		VIPSegment obj = getVIPSegment(segmentId);
		if (obj == null) {
			logger.warn("segmentId: " + segmentId + " is not exists.");
			return false;
		}
		try {
			int result = baseDao.delete(getSqlMap("delVIPSegment"), segmentId);
			if (result != 0) {
				deleteVIPBySegmentId(segmentId);
			}
			return true;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param segmentId
	 * @return
	 */
	private boolean deleteVIPBySegmentId(String segmentId) {
		if (StringUtil.isEmpty(segmentId)) {
			logger.error("segmentId is null!");
			return false;
		}
		try {
			baseDao.delete(getSqlMap("delVIPBySegmentId"), segmentId); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return true;
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	public boolean deleteVIP(String ip) {
		if (StringUtil.isEmpty(ip)) {
			logger.warn("IP: " + ip + " is null");
			return false;
		}
		List<String> vips = getAllUsedIPs();
		if (vips == null || !vips.contains(ip)) {
			logger.warn("IP: " + ip + " is not used.");
			return false;
		}
		try {
			int result = baseDao.delete(getSqlMap("delVIP"), ip);
			return result != 0;
		} catch (DaoException e) {
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param segmentId
	 * @return
	 */
	public VIPSegment getVIPSegment(String segmentId) {
		if (StringUtil.isEmpty(segmentId)) {
			return null;
		}
		try {
			return baseDao.queryForObject(getSqlMap("getVIPSegment"), segmentId); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param pageCond
	 * @return
	 */
	public List<VIPSegment> getAllVIPSegment(IPageCond pageCond) {
		try {
			Integer count = baseDao.queryForObject(getSqlMap("countAllVIPSegment")); //$NON-NLS-1$
			if (count == null || count == 0) {
				return new ArrayList<VIPSegment>();
			}
			pageCond.setCount(count);
			return baseDao.queryForList(getSqlMap("getAllVIPSegment"), pageCond); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<VIPSegment>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<VIPSegment> getAllVIPSegment() {
		try {
			return baseDao.queryForList(getSqlMap("getAllVIPSegment")); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<VIPSegment>();
	}

	/**
	 * 
	 * @param segmentId
	 * @return
	 */
	public List<String> getUsedIPs(String segmentId) {
		if (StringUtil.isEmpty(segmentId)) {
			return null;
		}
		try {
			return baseDao.queryForList(getSqlMap("getUsedIPs"), segmentId); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<String>();
	}

	/**
	 * 
	 * @param segmentId
	 * @param pageCond
	 * @return
	 */
	public List<String> getUsedIPs(String segmentId, IPageCond pageCond) {
		if (StringUtil.isEmpty(segmentId)) {
			return null;
		}
		try {
			Integer count = baseDao.queryForObject(
					getSqlMap("countUsedIPs"), segmentId); //$NON-NLS-1$
			if (count == null || count == 0) {
				return new ArrayList<String>();
			}
			pageCond.setCount(count);
			return baseDao.queryForList(
					getSqlMap("getUsedIPs"), segmentId, pageCond); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getAllUsedIPs() {
		try {
			return baseDao.queryForList(getSqlMap("getAllUsedIPs")); //$NON-NLS-1$
		} catch (DaoException e) {
			logger.error(e);
		}
		return null;
	}
	
}
