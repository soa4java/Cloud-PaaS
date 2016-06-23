/**
 * 
 */
package com.primeton.paas.sms.server.conf;

import org.gocom.cloud.cesium.common.api.model.Variable;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.manage.runtime.api.naming.NamingUtil;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.sms.model.SmsConfig;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SmsConfUtil {

	public static final String PREFIX = "PAAS.SMS.";
	
	public static final String MSG_CLIENT_CONF = PREFIX + "MsgClientConf";
	
	private static ILogger logger = LoggerFactory.getLogger(SmsConfUtil.class);

	private static SmsConfig smsConf = null;
	
	public static synchronized SmsConfig getSmsConf(){
		if (smsConf == null) {
			initSmsConf();
		}
		return smsConf;
	}
	
	public static void initSmsConf(){
		logger.info("load sms config from zookeeper.");
		
		Variable var = NamingUtil.lookupVariable(MSG_CLIENT_CONF);
		if(var != null) {
			String val = var.getValue();
			
			logger.info("load sms config . value = "+ val);
			if(val != null && val.trim().length() > 0) {
				try {
					smsConf = (SmsConfig)JsonSerializerUtil.deserialize(val);
				} catch (Exception e) {
					logger.error("Json deserialize error:"+ e.getMessage());
					throw new RuntimeException("Json deserialize error:"+ e.getMessage());
 				}
			}
		}
		
		if (smsConf == null) {
			throw new RuntimeException("load sms config failed. smsConf is null");
		}
		
		logger.info("load sms config from zk succeed: "+ smsConf.toString());
	}
	
	/*
	public static void main(String[] args) {
		SmsConfig conf = new SmsConfig();
		conf.setUsername("1234");
		conf.setPassword("password");
		conf.setProtocol(SmsConstants.PROTOCOL_MMPP_2_0);
		conf.setRemoteAddr("192.168.100.1");
		conf.setRemotePort(8899);
		conf.setRegisteredDelivery(0);
		conf.setPause(3000);
		conf.setExtendCode("");
		conf.setServiceCode("MB95516");
		conf.setWaitTime(30000);
		conf.setVersion(0x20);
		String val = "";
		try {
			val = JsonSerializerUtil.serialize(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(val);
	}
	*/
	
}
