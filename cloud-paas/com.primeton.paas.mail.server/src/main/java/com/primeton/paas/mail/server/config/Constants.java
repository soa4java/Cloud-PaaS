/**
 * 
 */
package com.primeton.paas.mail.server.config;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public interface Constants {

	public static final String SERVER_HOME_KEY = "MAIL_HOME";

	public static final String ATTACHMENT_HOME = "ATTACHMENT_HOME";

	public static final String MAX_MAILWORKER_NUM = "MAX_MAILWORKER_NUM";

	public static final String SYSTEM_JDBC_DRIVER_KEY = "JDBC_DRIVER";
	public static final String SYSTEM_JDBC_URL_KEY = "JDBC_URL";
	public static final String SYSTEM_JDBC_USER_KEY = "JDBC_USER";
	public static final String SYSTEM_JDBC_PASSWORD_KEY = "JDBC_PASSWORD";
	public static final String SYSTEM_JDBC_MIN_POOL_SIZE_KEY = "JDBC_MIN_POOL_SIZE";
	public static final String SYSTEM_JDBC_MAX_POOL_SIZE_KEY = "JDBC_MAX_POOL_SIZE";

	public static final String LOG4J = "log4j.properties";

	public static final String ZK_CONFIG_XML = "zkConfig.xml";

	public static final String SENDR_ESULT_SUCCESSED = "0";
	public static final String SENDR_ESULT_ERROR = "1";
	public static final String SENDR_ESULT_NOTSEND = "2";
	public static final String SENDR_ESULT_SENDING = "3";

	public static final String CODE_ADDRESS_EXCEPTION = "10";
	public static final String CODE_AUTHENTICATION_ERROR = "11";
	public static final String CODE_MESSAGEING_EXCEPTION = "12";

}
