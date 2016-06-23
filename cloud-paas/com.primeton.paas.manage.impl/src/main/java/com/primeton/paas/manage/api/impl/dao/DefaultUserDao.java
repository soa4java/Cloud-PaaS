/**
 *
 */
package com.primeton.paas.manage.api.impl.dao;

import java.util.List;

import org.gocom.cloud.cesium.common.spi.exception.DaoException;

import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.User;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class DefaultUserDao {
	
	private final static String USER_SQL_MAP = "userSqlMap";
	
	private final static String PAAS_USER_TABLE_NAME = "PAS_USER";

	private static BaseDao baseDao = BaseDao.getInstance();
	
	private String getSqlMap(String sqlId){
		return USER_SQL_MAP + "." + sqlId;
	}
	
	public void addUser(User user) throws DaoException{
		if (null == user) {
			return;
		}
		String id = baseDao.getNewId(PAAS_USER_TABLE_NAME);
		user.setId(Long.valueOf(id));
		baseDao.insert(getSqlMap("addUser"), user); //$NON-NLS-1$
	}
	
	public void delUser(String userId) throws DaoException {
		if (StringUtil.isEmpty(userId)) {
			return;
		}
		baseDao.delete(getSqlMap("delUser"),userId); //$NON-NLS-1$
	}
	
	/**
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public User getUser(String userId) throws DaoException {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		return baseDao.queryForObject(getSqlMap("getUserById"), userId); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void updateUser(User user) throws DaoException {
		if (null == user) {
			return;
		}
		baseDao.update(getSqlMap("updateUser"), user); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void updatePasswd(User user) throws DaoException {
		if (null == user) {
			return;
		}
		baseDao.update(getSqlMap("updatePasswd"), user); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void updateStatus(User user) throws DaoException {
		if (null == user) {
			return;
		}
		baseDao.update(getSqlMap("updateStatus"), user); //$NON-NLS-1$
	}
	
	
	/**
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<User> getUsers(User criteria) throws DaoException {
		if (criteria == null)
			criteria = new User();
		return baseDao.queryForList(getSqlMap("getUsers"), criteria); //$NON-NLS-1$
	}
	
	/**
	 * @param user
	 * @param pc
	 * @return
	 * @throws DaoException
	 */
	public int getUserCount(User criteria) throws DaoException {
		if (criteria == null)
			criteria = new User();
		Integer num = (Integer) baseDao.queryForObject(
				getSqlMap("getUserCount"), criteria); //$NON-NLS-1$
		return null == num ? -1 : num;
	}

	
	/**
	 * @param user
	 * @param pc
	 * @return
	 * @throws DaoException
	 */
	public List<User> getUsers(User criteria, IPageCond page)
			throws DaoException {
		if (criteria == null) {
			criteria = new User();
		}
		if (null == page) {
			page = new PageCond();
		}
		return baseDao.queryForList(getSqlMap("getUsers"), criteria, page); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<User> getUserByUserIdAndPhone(User criteria)
			throws DaoException {
		if (criteria == null)
			criteria = new User();
		return baseDao.queryForList(
				getSqlMap("getUsersByUserIdAndPhone"), criteria); //$NON-NLS-1$
	}
	
}
