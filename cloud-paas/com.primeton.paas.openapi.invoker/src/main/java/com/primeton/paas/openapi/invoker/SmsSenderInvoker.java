/**
 * 
 */
package com.primeton.paas.openapi.invoker;

import java.util.List;
import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.base.uitl.JsonSerializerUtil;
import com.primeton.paas.openapi.engine.AbstractBizInvoker;
import com.primeton.paas.openapi.engine.BizInvokeResult;
import com.primeton.paas.openapi.engine.BizRequest;
import com.primeton.paas.sms.api.ISmsService;
import com.primeton.paas.sms.api.SmsServiceFactory;
import com.primeton.paas.sms.model.SmsResult;

/**
 * Marked @Deprecated by ZhongWen.Li
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Deprecated
public class SmsSenderInvoker extends AbstractBizInvoker {

	
	private static final ILogger log = LoggerFactory.getLogger(SmsSenderInvoker.class);
	
	/* (none Javadoc)
	 * @see com.primeton.paas.openapi.engine.IBizInvoker#getBizCode()
	 */
	public String getBizCode() {
		return "SmsSend";
	}

	/* (none Javadoc)
	 * @see com.primeton.paas.openapi.engine.IBizInvoker#invokeBiz(com.primeton.paas.openapi.engine.BizRequest)
	 */
	public BizInvokeResult invokeBiz(BizRequest req) {
		if (log.isDebugEnabled()){
			log.debug(this.getClass().getName() + ".invokeBiz()");
		}
		BizInvokeResult result = null;

		String transactionId = req.getTransactionId();
		String requestId = req.getTransactionId() + ".1";
		String custId = req.getCustId();
		String bizCode = req.getBizCode();
		String bizParams = req.getBizParams();
	
		if (log.isInfoEnabled()) {
			log.info(this.getClass().getName() + " invoke SMS Service custId [ "+custId+" ] bizCode [ "+bizCode+" ] bizParams [" + bizParams +"].");
		}
		
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>)JsonSerializerUtil.deserialize(bizParams);
			String number = params.get("number");
			String content = params.get("content");
			long timeout = params.get("timeout") == null || params.get("timeout").isEmpty() ? 0l : Long.parseLong(params.get("timeout"));
			
			ISmsService instance = SmsServiceFactory.getInstance().getSmsService(); 
			
			log.info("Begin invoke ISmsService.send method.");
			List<SmsResult> retList = instance.send(number, content, timeout);
			
			log.info("End invoke ISmsService.send method.");
			
			String resultBodyStr = JsonSerializerUtil.serialize("");
			if (retList != null && retList.size() > 0 && retList.get(0) != null) {
				resultBodyStr = JsonSerializerUtil.serialize(retList.get(0));
				//resultBodyStr = JsonSerializerUtil.serialize(retList);
				log.info("Return from invoke ISmsService. resultBodyStr:"+resultBodyStr);
			} else {
				log.info("Return from invoke ISmsService. result is empty.");
			}
			
			result = new BizInvokeResult();
			result.setTransactionId(transactionId);
			result.setRequestId(requestId);
			result.setCompletedStatus(BizInvokeResult.COMPLETED_STATUS_SUCCESS);
			result.setResultBodyStr(resultBodyStr);
			return result;

		} catch (Exception e) {
			String errorMsg = this.getClass().getName() + " invoke SMS Service custId [ "+custId+" ] bizCode [ "+bizCode+" ] exception: " + e.getMessage();
			log.error(errorMsg);
			result = new BizInvokeResult();
			result.setException(e);
			result.setRequestId(requestId);
			return result;
		}
	}

}
