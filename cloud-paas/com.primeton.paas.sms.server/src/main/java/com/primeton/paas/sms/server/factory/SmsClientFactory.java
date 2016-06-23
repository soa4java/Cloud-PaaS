/*package com.primeton.paas.sms.server.factory;

import com.primeton.paas.sms.server.impl.SmsClient;

public class SmsClientFactory {
	
	private static SmsClient smsClient = null;
	
	private SmsClientFactory() {}
	
	@SuppressWarnings("unchecked")
	public static SmsClient getSmsClient() {
		if (smsClient != null) {
			return smsClient;
		}
		synchronized(SmsClientFactory.class) {
			if(smsClient == null) {
				smsClient = new SmsClient();
			}
		}
		return smsClient;
	}
}
*/