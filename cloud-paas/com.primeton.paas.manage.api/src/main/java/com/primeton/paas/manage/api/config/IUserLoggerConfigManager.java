/**
 * 
 */
package com.primeton.paas.manage.api.config;

import com.primeton.paas.app.config.model.UserLogModel;
import com.primeton.paas.manage.api.exception.ConfigureException;

/**
 * 
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public interface IUserLoggerConfigManager {

	/**
	 * 
	 * @param appName
	 * @return
	 */
	UserLogModel getUserLogModel(String appName);
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @param level
	 * @throws ConfigureException
	 */
	void addUserLogModel(String appName,String type,String level) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param model
	 * @throws ConfigureException
	 */
	void addUserLogModel(String appName,UserLogModel model) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @throws ConfigureException
	 */
	void deleteUserLogModel(String appName,String type) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param model
	 * @throws ConfigureException
	 */
	void deleteUserLogModel(String appName,UserLogModel model) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param model
	 * @throws ConfigureException
	 */
	void updateUserLogModel(String appName, UserLogModel model) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param level
	 * @throws ConfigureException
	 */
	void updateUserLogModel(String appName, String level) throws ConfigureException;
	
	/**
	 * 
	 * @param appName
	 * @param type
	 * @param level
	 * @throws ConfigureException
	 */
	void updateUserLogsModel(String appName,String type,String level) throws ConfigureException;
	
}
