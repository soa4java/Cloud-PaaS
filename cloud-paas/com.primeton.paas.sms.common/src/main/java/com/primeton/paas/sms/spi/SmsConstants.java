/**
 * 
 */
package com.primeton.paas.sms.spi;

/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public interface SmsConstants {

	String TYPE = "SMS";

	String SERVER_HOME_KEY = "SMS_HOME";

	String ZK_CONFIG_XML = "zkConfig.xml";

	String PROTOCOL_MMPP_2_0 = "mmpp2.0";

	String PROTOCOL_CMPP_2_0 = "cmpp2.0";

	String SYS_ENCODING = "UTF-8";

	int ASCII = 0;

	int BINARY = 4;

	int UTF = 8;
	
	String UTF_CODE = "UTF-16BE";

	int GB = 15;
	
	String GB_CODE = "GB18030";

	String SMS_SERVICE_REMOTE_OBJECT_NAME = "smsServiceObject";

	String SMS_SERVER_REMOTE_OBJECT_NAME = "smsServerObject";

	String HOST_KEY = "SMS_HOST";
	
	String PORT_KEY = "SMS_PORT";

	String DEFAULT_HOST_VALUE = "127.0.0.1";
	
	int DEFAULT_PORT_VALUE = 8010;

	int DEFAULT_VERSION = 0x20;

}
