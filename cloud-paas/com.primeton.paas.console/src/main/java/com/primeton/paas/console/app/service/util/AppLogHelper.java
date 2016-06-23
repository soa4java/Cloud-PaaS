/**
 * 
 */
package com.primeton.paas.console.app.service.util;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.console.common.LoggerFactory;
import com.primeton.paas.manage.api.config.AppConfigManagerFactory;
import com.primeton.paas.manage.api.config.IUserLoggerConfigManager;
import com.primeton.paas.manage.api.exception.ConfigureException;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * 应用日志配置管理工具类
 *
 * @author liming(li-ming@primeton.com)
 */
public class AppLogHelper {

	private static ILogger logger = LoggerFactory.getLogger(AppLogHelper.class);

	private static IUserLoggerConfigManager userlogConfMgr = AppConfigManagerFactory
			.getUserLoggerConfigManager();

	/**
	 * 查询应用日志配置信息 <br>
	 * @param appName  应用名称
	 * @return  应用日志配置信息
	 */
	public static UserLogModel queryAppLog(String appName) {
		if (StringUtil.isEmpty(appName)) {
			return null;
		}
		try {
			UserLogModel userLog = userlogConfMgr.getUserLogModel(appName);
			return userLog;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
	
	/**
	 * 保存用户日志配置
	 * @param appName
	 * @param userlog
	 * @return
	 */
	public static boolean appLogSave(String appName, UserLogModel userlog) {
		if (StringUtil.isEmpty(appName) || userlog == null) {
			return false;
		}
		try {
			userlogConfMgr.updateUserLogModel(appName, userlog);
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除用户日志类型
	 * @param appName 应用名
	 * @param ulsm 用户日志信息
	 * @return
	 */
	public static boolean addUserLog(String appName,LogModel ulsm){
		if (StringUtil.isEmpty(appName) || ulsm == null) {
			return false;
		}
		try {
			userlogConfMgr.addUserLogModel(appName, ulsm.getType(), ulsm.getLevel());//.updateUserLogsModel(appName, ulsm.getType(),ulsm.getLevel());
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @return
	 */
	public static boolean deleteUserLog(String appName, String type) {
		if (StringUtil.isEmpty(appName) || type == null) {
			return false;
		}
		if (UserLogModel.DEFAULT_USERLOG_TYPE.equalsIgnoreCase(type)) {
			return false;
		}
		try {
			userlogConfMgr.deleteUserLogModel(appName, type);
		} catch (ConfigureException e) {
			logger.error(e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 用户日志级别设置<br>
	 * 
	 * @param appName 应用名称
	 * @param userlog 用户日志配置
	 * @return 保存成功则返回true, 否则返回false
	 */
	public static boolean appLogSetting(String appName, UserLogModel userlog) {
		// TODO
		return true;
	}
	
}