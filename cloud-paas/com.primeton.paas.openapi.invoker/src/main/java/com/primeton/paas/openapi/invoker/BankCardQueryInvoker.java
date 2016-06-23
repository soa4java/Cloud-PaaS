/**
 * 
 */
package com.primeton.paas.openapi.invoker;

import java.util.Map;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.cardbin.api.FinanceServiceFactory;
import com.primeton.paas.cardbin.api.ICardBinService;
import com.primeton.paas.cardbin.model.BankCard;
import com.primeton.paas.openapi.base.uitl.JsonSerializerUtil;
import com.primeton.paas.openapi.engine.AbstractBizInvoker;
import com.primeton.paas.openapi.engine.BizInvokeResult;
import com.primeton.paas.openapi.engine.BizRequest;

/**
 * Marked @Deprecated by ZhongWen.Li
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Deprecated
public class BankCardQueryInvoker extends AbstractBizInvoker {

	private static final ILogger log = LoggerFactory.getLogger(BankCardQueryInvoker.class);

	public String getBizCode() {
		return "BankCardQuery";
	}

	public BizInvokeResult invokeBiz(BizRequest req) {
		if (log.isDebugEnabled())
			log.debug(this.getClass().getName() + ".invokeBiz()");


		BizInvokeResult result = null;

		String transactionId = req.getTransactionId();
		String requestId = req.getTransactionId() + ".1";
		String custId = req.getCustId();
		String bizCode = req.getBizCode();
		String bizParams = req.getBizParams();

		try {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>)JsonSerializerUtil.deserialize(bizParams);
			String pan = params.get("cardId");

			ICardBinService service = FinanceServiceFactory.getInstance().getCardBinService();
			BankCard card = service.getCardByPan(pan);
			String resultBodyStr = JsonSerializerUtil.serialize(card);
			
			result = new BizInvokeResult();
			result.setTransactionId(transactionId);
			result.setRequestId(requestId);
			result.setCompletedStatus(BizInvokeResult.COMPLETED_STATUS_SUCCESS);
			result.setResultBodyStr(resultBodyStr);
			
			return result;

		} catch (Exception e) {
			String errorMsg = this.getClass().getName() + " invoke CardBin Service custId [ "+custId+" ] bizCode [ "+bizCode+" ] exception: " + e.getMessage();
			log.error(errorMsg);
			result = new BizInvokeResult();
			result.setException(e);
			result.setRequestId(requestId);
			return result;
		}
	}

}
