/**
 * 
 */
package com.primeton.paas.openapi.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class BizInvokeResultCache {

	private static BizInvokeResultCache instance = new BizInvokeResultCache();

	private Map<String, Map<String, BizInvokeResult>> resultMap = new ConcurrentHashMap<String, Map<String, BizInvokeResult>>();

	private BizInvokeResultCache() {
	}

	public static BizInvokeResultCache getInstance() {
		if (instance == null) {
			instance = new BizInvokeResultCache();
		}
		return instance;
	}

	public static void clear() {
		instance.resultMap.clear();
		instance = null;
	}

	public int getSize() {
		return this.resultMap.size();
	}

	public void putBizInvokeResult(String transactionId, BizInvokeResult result) {
		Map<String, BizInvokeResult> map = this.resultMap.get(transactionId);
		if (map == null) {
			map = new ConcurrentHashMap<String, BizInvokeResult>();
			this.resultMap.put(transactionId, map);
		}
		map.put(result.getRequestId(), result);
	}

	public BizInvokeResult getAndRemoveBizInvokeResult(String transactionId, String requestId) {
		Map<String, BizInvokeResult> map = this.resultMap.get(transactionId);
		if (map == null)
			return null;

		BizInvokeResult result = map.remove(requestId);
		if (map.size() == 0) {
			this.resultMap.remove(transactionId);
		}
		return result;
	}
	
}
