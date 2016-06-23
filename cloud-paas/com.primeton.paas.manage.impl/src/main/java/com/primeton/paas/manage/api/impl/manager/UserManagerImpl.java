/**
 * 
 */
package com.primeton.paas.manage.api.impl.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.impl.dao.DefaultUserDao;
import com.primeton.paas.manage.api.impl.util.MD5Util;
import com.primeton.paas.manage.api.manager.IUserManager;
import com.primeton.paas.manage.api.model.IPageCond;
import com.primeton.paas.manage.api.model.User;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 *
 * @author liyanping(mailto:liyp@primeton.com)
 */
public class UserManagerImpl implements IUserManager {

	private static ILogger logger = ManageLoggerFactory.getLogger(UserManagerImpl.class);
	
	private static DefaultUserDao userDao = new DefaultUserDao();
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#add(com.primeton.paas.manage.api.model.User)
	 */
	public void add(User user) {
		if (null == user || StringUtil.isEmpty(user.getUserId())) {
			return;
		}
		try {
			if (user.getPassword() != null) {
				user.setPassword(MD5Util.md5(user.getPassword()));
			} else {
				user.setPassword(MD5Util.md5(user.getPassword()));
			}
			userDao.addUser(user);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#remove(java.lang.String)
	 */
	public void remove(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return;
		}
		try{
			userDao.delUser(userId);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#update(com.primeton.paas.manage.api.model.User)
	 */
	public void update(User user) {
		if (null == user || StringUtil.isEmpty(user.getUserId())) {
			return;
		}
		try{
			userDao.updateUser(user);
		} catch (Throwable t) {
			logger.error(t);
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manager.IUserManager#validatePasswd(java.lang.String, java.lang.String)
	 */
	public boolean validatePasswd(String userId, String oldPasswd) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		User user = get(userId);
		if (user != null) {
			String password = user.getPassword();
			String encrptPwd = MD5Util.md5(oldPasswd == null ? "" : oldPasswd);
			if (password != null && password.equals(encrptPwd)) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#resetPasswd(java.lang.String, java.lang.String)
	 */
	public void resetPasswd(String userId, String password) {
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(password)) {
			return;
		}
		String pwdEncrpt = MD5Util.md5(password);
		User user = new User();
		user.setUserId(userId);
		user.setPassword(pwdEncrpt);
		try{
			userDao.updatePasswd(user);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#updateStatus(java.lang.String, int, java.lang.String)
	 */
	public void updateStatus(String userId, int status, String handler) {
		if (StringUtil.isEmpty(userId)) {
			return;
		}
		User user = new User();
		user.setUserId(userId);
		user.setStatus(status);
		user.setHandler(handler ==null?"":handler);
		
		try{
			userDao.updateStatus(user);
		} catch (Throwable t) {
			logger.error(t);
		}
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#get(java.lang.String)
	 */
	public User get(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return null;
		}
		try {
			return userDao.getUser(userId);
		} catch (Throwable t) {
			logger.error(t);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#get(com.primeton.paas.manage.api.model.IPageCond)
	 */
	public User[] get(IPageCond page) {
		return get(null,page);
	}

	/* (non-Javadoc)
	 * @see com.primeton.paas.manage.api.manage.IUserManager#get(com.primeton.paas.manage.api.model.User, com.primeton.paas.manage.api.model.IPageCond)
	 */
	public User[] get(User criteria, IPageCond page) {
		List<User> userList = new ArrayList<User>();
		try {
			if (page == null) {
				userList = userDao.getUsers(criteria);
			} else {
				if (page.getCount() <= 0) {
					page.setCount(userDao.getUserCount(criteria));
				}
				userList = userDao.getUsers(criteria,page);
			}
		} catch (Throwable t) {
			logger.error(t);
		}
		
		if(userList != null && userList.size()>0){
			return userList.toArray(new User[userList.size()]);
		}
		return null;
	}

	public User get(String userId, String phone) {
		if (logger.isDebugEnabled()) {
			logger.debug("Begin Get User by userId '" + userId + "' and phone '" + phone + "'.");
		}
		if(userId==null||phone==null){
			return null;
		}
		List<User> userList = new ArrayList<User>();
		User criteria = new User();
		criteria.setUserId(userId);
		criteria.setPhone(phone);
		
		try {
			userList = userDao.getUserByUserIdAndPhone(criteria);
		} catch (Throwable t) {
			logger.error(t);
		}
		
		if(userList != null && userList.size()==1){
			return userList.get(0);
		}
		return null;
	}
	
	public String resetForgottenPassword(String userId, String phone) {
		User user = get(userId, phone);
		if (user == null || user.getUserId() == null || user.getPhone() == null) {
			return null;
		}
		String password = genRandomPwd(6);
		password = "000000"; // FIXME
		resetPasswd(userId, password);
		return password;
	}
	
	/**
	 * 
	 * @param length
	 * @return
	 */
	private static String genRandomPwd(int length) {
		final int maxNum = 36;
		int i;
		int count = 0;
		// char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		// 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		// 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < length) {
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
}
