/**
 *
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.Task;

/**
 * 
 * @author liming(mailto:li-ming@primeton.com)
 */
public class DefaultTaskDao {

	private final static String USER_SQL_MAP = "taskSqlMap";

	private static DefaultTaskDao instance = new DefaultTaskDao();

	private static BaseDao baseDao = BaseDao.getInstance();;

	private DefaultTaskDao() {
	}

	public static DefaultTaskDao getInstance() {
		return instance;
	}

	private String getSqlMap(String sqlId) {
		return USER_SQL_MAP + "." + sqlId;
	}

	public void addTask(Task task) throws DaoException {
		baseDao.insert(getSqlMap("addTask"), task);
	}

	public int getOrderCount(Map<String, Object> criteria) throws DaoException {
		if (criteria == null)
			criteria = new HashMap<String, Object>();
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("getTaskCount"), criteria); //$NON-NLS-1$
		return null == num ? -1 : num;
	}

	public Task getTask(String id)
			throws DaoException {
		return baseDao.queryForObject(getSqlMap("getTaskById"), id); //$NON-NLS-1$
	}
	
	public void delTask(String id) throws DaoException {
		 baseDao.delete(getSqlMap("delTaskById"), id); //$NON-NLS-1$
	}

	public List<Task> getTasks(Map<String, Object> criteria)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getTasks"), criteria); //$NON-NLS-1$
	}

	public List<Task> getTasks(Map<String, Object> criteria, IPageCond pageCond)
			throws DaoException {
		return baseDao.queryForList(getSqlMap("getTasks"), criteria, pageCond); //$NON-NLS-1$
	}
	
	public void updateTask(Task task) throws DaoException{
		baseDao.update(getSqlMap("updateTask"), task); //$NON-NLS-1$
	}

}
