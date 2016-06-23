/**
 * 
 */
package com.primeton.paas.sms.api;

import java.util.List;

import com.primeton.paas.sms.model.SmsResult;
import com.primeton.paas.sms.spi.SmsRuntimeException;


/**
 *
 * @author liyanping(liyp@primeton.com)
 */
public interface ISmsService {

	
	/**
	 * Send Message <br>
	 * 
	 * @param number   phone number,Separated by semicolons (;)
	 * @param content  sms content 
	 * @param timeout  the time waiting for response(ms)
	 * @return
	 * @throws SmsRuntimeException
	 */
	public List<SmsResult> send(String number, String content, long timeout)
			throws SmsRuntimeException;
	
}
