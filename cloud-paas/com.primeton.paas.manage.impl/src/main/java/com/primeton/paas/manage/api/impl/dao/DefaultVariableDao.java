/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.List;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.Variable;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class DefaultVariableDao {

	private final static String VARIABLE_SQL_MAP = "varSqlMap";

	private static BaseDao baseDao = BaseDao.getInstance();

	private static DefaultVariableDao instance = new DefaultVariableDao();

	private String getSqlMap(String sqlId) {
		return VARIABLE_SQL_MAP + "." + sqlId;
	}

	public static DefaultVariableDao getInstance() {
		return instance;
	}

	public void addVar(Variable var) throws DaoException {
		baseDao.insert(getSqlMap("addVariable"), var); //$NON-NLS-1$
	}

	public void updateVar(Variable var) throws DaoException {
		baseDao.update(getSqlMap("updateVariable"), var); //$NON-NLS-1$
	}

	public void delVar(String key) throws DaoException {
		baseDao.delete(getSqlMap("delVariable"), key); //$NON-NLS-1$
	}

	public void delAll() throws DaoException {
		baseDao.delete(getSqlMap("delAll")); //$NON-NLS-1$
	}

	public Variable get(String varKey) throws DaoException {
		return baseDao.queryForObject(getSqlMap("getVariable"), varKey); //$NON-NLS-1$
	}

	public List<Variable> getAll() throws DaoException {
		return baseDao.queryForList(getSqlMap("getAll")); //$NON-NLS-1$
	}

	public int getVarCount(Variable criteria) throws DaoException {
		if (criteria == null) {
			criteria = new Variable();
		}
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("getVarCount"), criteria); //$NON-NLS-1$
		return null == num ? -1 : num;
	}

	public List<Variable> getVars(Variable criteria) throws DaoException {
		if (criteria == null) {
			criteria = new Variable();
		}
		return baseDao.queryForList(getSqlMap("getVars"), criteria); //$NON-NLS-1$
	}

	public List<Variable> getVars(Variable criteria, IPageCond page)
			throws DaoException {
		if (criteria == null) {
			criteria = new Variable();
		}
		if (null == page) {
			page = new PageCond();
		}
		return baseDao.queryForList(getSqlMap("getVars"), criteria, page); //$NON-NLS-1$
	}
	
}
