/**
 * 
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.gocom.cloud.cesium.common.spi.dao.DefaultBaseDao;
import org.gocom.cloud.cesium.common.spi.exception.DaoException;
import org.gocom.cloud.common.logger.api.ILogger;
import org.springframework.context.ApplicationContext;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.spring.SpringApplicationContext;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BaseDao {

	private static ILogger log = ManageLoggerFactory.getLogger(BaseDao.class);
	
	private static BaseDao instance = new BaseDao();
	
	private BaseDao() {
	}

	public static BaseDao getInstance() {
		return instance;
	}
	
	private static void logRunTime(String statementName, long runTime) {
		if (log.isDebugEnabled()) {
			log.debug("Sql [" + statementName + "] executed, spent " + runTime + " ms.");
		}
	}

	/**
	 * 
	 * @return
	 */
	public SqlSession getSqlSession() {
		ApplicationContext context = SpringApplicationContext.getInstance().getApplicationContext();
		return null == context ? null : (context.containsBean("sqlSession"))  //$NON-NLS-1$
				? context.getBean("sqlSession", SqlSession.class) //$NON-NLS-1$
				: context.getBean(SqlSession.class);
	}
	
	/**
	 * Give a table generate new unique id (auto increase). <br>
	 * 
	 * @param codeName
	 * @return
	 * @throws DaoException
	 */
	public String getNewId(String tableName) throws DaoException {
		if (StringUtil.isEmpty(tableName)) {
			return null;
		}
		// Please init cld_id_table
		try {
			return DefaultBaseDao.getInstance().getNewId(tableName) + ""; //$NON-NLS-1$
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param obj
	 * @return
	 * @throws DaoException
	 */
	public int insert(String statementName, Object obj) throws DaoException {
		if (StringUtil.isEmpty(statementName) || null == obj) {
			return -1;
		}
		try {
			long startTime = System.currentTimeMillis();
			int result = 0;
			result = getSqlSession().insert(statementName, obj);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return result;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param obj
	 * @return
	 * @throws DaoException
	 */
	public int delete(String statementName, Object obj) throws DaoException {
		if (StringUtil.isEmpty(statementName) || null == obj) {
			return -1;
		}
		try {
			long startTime = System.currentTimeMillis();
			int result = 0;
			result = getSqlSession().delete(statementName, obj);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return result;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @return
	 * @throws DaoException
	 */
	public int delete(String statementName) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return -1;
		}
		try {
			long startTime = System.currentTimeMillis();
			int result = getSqlSession().delete(statementName, new HashMap<String, String>());
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return result;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param obj
	 * @return
	 * @throws DaoException
	 */
	public int update(String statementName, Object obj) throws DaoException {
		if (StringUtil.isEmpty(statementName) || null == obj) {
			return -1;
		}
		try {
			long startTime = System.currentTimeMillis();
			int result = getSqlSession().update(statementName, obj);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return result;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @return
	 * @throws DaoException
	 */
	public int update(String statementName) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return -1;
		}
		try {
			long startTime = System.currentTimeMillis();
			int result = getSqlSession().update(statementName, new HashMap<String, String>());
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return result;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param obj
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String statementName, Object obj) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return null;
		}
		try {
			long startTime = System.currentTimeMillis();
			T returnObject = (T)getSqlSession().selectOne(statementName, obj);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return returnObject;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String statementName) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return null;
		}
		try {
			long startTime = System.currentTimeMillis();
			T returnObject = (T) getSqlSession().selectOne(statementName, new HashMap<String, String>());
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return returnObject;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param paramObj
	 * @return
	 * @throws DaoException
	 */
	public <T> List<T> queryForList(String statementName, Object paramObj) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return new ArrayList<T>();
		}
		try {
			long startTime = System.currentTimeMillis();
			List<T> list =  getSqlSession().selectList(statementName, paramObj);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return list;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @return
	 * @throws DaoException
	 */
	public <T> List<T> queryForList(String statementName) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return new ArrayList<T>();
		}
		try {
			long startTime = System.currentTimeMillis();
			List<T> list = getSqlSession().selectList(statementName, new HashMap<String, String>());
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return list;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param paramObj
	 * @param pageCond
	 * @return
	 * @throws DaoException
	 */
	public <T> List<T> queryForList(String statementName, Object paramObj, IPageCond pageCond) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return new ArrayList<T>();
		}
		pageCond = null == pageCond ? new PageCond() : pageCond;
		try {
			long startTime = System.currentTimeMillis();
			int skipResults = null == pageCond ? 0 : (int)pageCond.getBegin();
			int maxResults = null == pageCond ? 10 : (int)pageCond.getLength();
			RowBounds rowBounds = new RowBounds(skipResults,maxResults);
			List<T> list = getSqlSession().selectList(statementName, paramObj, rowBounds);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return list;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

	/**
	 * 
	 * @param statementName
	 * @param pageCond
	 * @return
	 * @throws DaoException
	 */
	public <T> List<T> queryForList(String statementName, IPageCond pageCond) throws DaoException {
		if (StringUtil.isEmpty(statementName)) {
			return new ArrayList<T>();
		}
		pageCond = null == pageCond ? new PageCond() : pageCond;
		try {
			long startTime = System.currentTimeMillis();
			int skipResults = (int)pageCond.getBegin();
			int maxResults = (int)pageCond.getLength();
			RowBounds rowBounds = new RowBounds(skipResults,maxResults);
			List<T> list = getSqlSession().selectList(statementName, new HashMap<String, String>(), rowBounds);
			long endTime = System.currentTimeMillis();
			logRunTime(statementName, endTime - startTime);
			return list;
		} catch (Throwable t) {
			throw new DaoException(t);
		}
	}

}
