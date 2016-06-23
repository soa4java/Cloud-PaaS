/**
 * 
 */
package com.primeton.paas.console.platform.service.audit;

import java.util.Arrays;
import java.util.Date;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.factory.UserManagerFactory;
import com.primeton.paas.manage.api.manager.IUserManager;
import com.primeton.paas.manage.api.model.PageCond;
import com.primeton.paas.manage.api.model.User;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 自助服务门户注册用户审批
 *
 * @author liuyi(liu-yi@primeton.com)
 *
 */
public class UserAuditMgrUtil {

	private static ILogger logger = LoggerFactory.getLogger(UserAuditMgrUtil.class);
	
	private static IUserManager manager = UserManagerFactory.getManager();

	/**
	 * 批量删除用户
	 * 
	 * @param users
	 * @return
	 */
	public static boolean removeUsers(String[] userIds) {
		if (userIds == null || userIds.length < 1) {
			return true;
		}
		try {
			for (String userId : userIds) {
				manager.remove(userId);
			}
		} catch (Exception e) {
			logger.error("Remove users {0} error.", new Object[] {Arrays.toString(userIds)}, e);
			throw new RuntimeException("remove users error:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 批量激活
	 * 
	 * @param users
	 * @param handler
	 * @return
	 */
	public static boolean agreeUsersReg(String[] userIds, String handler) {
		if (userIds == null || userIds.length < 1) {
			return true;
		}

		try {
			for (String userId : userIds) {
				agreeUserReg(userId, handler);
			}
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 批量拒绝
	 * 
	 * @param users
	 * @param notes
	 * @param handler
	 * @return
	 */
	public static boolean rejectUsersReg(String[] userIds, String notes,
			String handler) {
		if (userIds == null || userIds.length < 1) {
			return true;
		}
		try {
			for (String userId : userIds) {
				rejectUserReg(userId, notes, handler);
			}
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 查询用户详细信息
	 * 
	 * @param userId
	 * @return
	 */
	public static User getUser(String userId) {
		try {
			return manager.get(userId);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 密码重置
	 * 
	 * @param userId
	 * @param newPwd
	 */
	public static void resetPasswd(String userId, String newPwd) {
		try {
			manager.resetPasswd(userId, newPwd == null ? "" : newPwd);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 根据条件查询用户信息（分页）
	 * 
	 * @param critetia
	 * @param page
	 * @return
	 */
	public static User[] getUsers(User critetia, PageCond page) {
		try {
			return manager.get(critetia, page);
		} catch (Exception e) {
			logger.error(e);
		}
		return new User[0];
	}

	/**
	 * 同意用户注册申请
	 * 
	 * @param userId
	 * @param handler
	 * @return
	 */
	public static boolean agreeUserReg(String userId, String handler) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		try {
			/** 更改账户状态 */
			manager.updateStatus(userId, User.USER_STATUS_ACTIVED, handler);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 拒绝用户注册申请
	 * 
	 * @param userId
	 *            需审批的用户标识
	 * @param notes
	 *            拒绝理由
	 * @param handler
	 *            当前处理人
	 * @return
	 */
	public static boolean rejectUserReg(String userId, String notes,
			String handler) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		User user = getUser(userId);
		if (user == null) {
			return false;
		}
		String oldNotes = user.getNotes() == null ? "" : user.getNotes();
		user.setNotes(oldNotes + "([Reject] " + notes + ".)");

		user.setStatus(User.USER_STATUS_REJECTED);
		user.setHandler(handler);
		try {
			/** 更新用户信息 */
			manager.update(user);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 删除单个用户
	 * 
	 * @param userID
	 * @return
	 */
	public static boolean removeUser(String userID) {
		if (StringUtil.isEmpty(userID)) {
			return true;
		}
		try {
			manager.remove(userID);
		} catch (Exception e) {
			logger.error(e);
		}
		return true;
	}

	/**
	 * 自助服务门户用户注册
	 * 
	 * @param user
	 * @return
	 */
	public static boolean register(User user) {
		if (user == null) {
			return false;
		}
		boolean isExist = isExistUser(user.getUserId());
		if (isExist) {
			return false;
		}
		user.setStatus(User.USER_STATUS_UNAUDIT); // 初始状态：待审批状态
		user.setCreatetime(new Date());
		manager.add(user);
		return true;
	}

	/**
	 * 检验手机号码是否重复
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isExistPhone(String phone) {
		if (StringUtil.isEmpty(phone)) {
			return false;
		}
		try {
			User user = new User();
			user.setPhone(phone.trim());
			User[] users = manager.get(user, null);
			return users == null || users.length < 1 ? false : true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	/**
	 * 检验邮箱是否重复
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isExistEmail(String email) {
		if (StringUtil.isEmpty(email)) {
			return false;
		}
		try {
			User user = new User();
			user.setEmail(email.trim());
			User[] users = manager.get(user, null);

			return users == null || users.length < 1 ? false : true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	/**
	 * 检验用户是否存在
	 * 
	 * @param userID
	 * @return
	 */
	public static boolean isExistUser(String userId) {
		try {
			User obj = manager.get(userId);
			return obj == null ? false : true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

}
