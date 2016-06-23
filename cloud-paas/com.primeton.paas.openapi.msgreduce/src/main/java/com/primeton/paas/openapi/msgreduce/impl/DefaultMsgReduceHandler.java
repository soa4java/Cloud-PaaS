/**
 * 
 */
package com.primeton.paas.openapi.msgreduce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.base.uitl.ArrayUtil;
import com.primeton.paas.openapi.msgreduce.IMsgReduceHandler;
import com.primeton.paas.openapi.msgreduce.config.MsgReducePolicyManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class DefaultMsgReduceHandler implements IMsgReduceHandler {
	
	private static final ILogger log = LoggerFactory.getLogger(DefaultMsgReduceHandler.class);

	/*
	 * (non-Javadoc)
	 * @see com.primeton.openplatform.openapi.msgreduce.IMsgReduceHandler#reduceJsonMsg(java.lang.String,
	 *      java.lang.String)
	 */
	public String reduceJsonMsg(String jsonMsgStr, String operationCode) {
		String[] reducedFields = MsgReducePolicyManager.getInstance().getReducedFields(operationCode);
		if (ArrayUtil.isEmpty(reducedFields)) {
			return jsonMsgStr;
		}
		try {
			JSONObject jobj = new JSONObject(jsonMsgStr);
			/**
			 * samples: jobj.remove("schoolName"); Object
			 * obj=jobj.get("students"); if(obj instanceof JSONArray) {
			 * JSONArray array=(JSONArray) obj; for(int i=0;i<array.length();i++) {
			 * JSONObject subObj=array.getJSONObject(i);
			 * subObj.remove("remark"); subObj.remove("id"); } }
			 */

			for (String fieldName : reducedFields) {
				List<String> subFieldNames = new ArrayList<String>(5);
				for (StringTokenizer tokens = new StringTokenizer(fieldName, "/"); tokens.hasMoreTokens();) {
					String subFieldName = tokens.nextToken();
					subFieldNames.add(subFieldName);
				}

				msgReduceRecusive(jobj, subFieldNames);
			}

			log.debug("\n" + jobj.toString(2));
			return jobj.toString();
		} catch (JSONException e) {
			log.error("Failed to reduce message " + jsonMsgStr + " with operationCode " + operationCode, e);
			return "{\"exception\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 
	 * @param jobj
	 * @param fieldNames
	 * @throws JSONException
	 */
	private void msgReduceRecusive(JSONObject jobj, List<String> fieldNames) throws JSONException {
		if (fieldNames.size() == 1) {
			jobj.remove(fieldNames.get(0));
		} else {
			String fieldName = fieldNames.get(0);
			fieldNames.remove(0);

			Object obj = jobj.get(fieldName);
			if (obj instanceof JSONArray) {
				JSONArray array = (JSONArray) obj;
				for (int i = 0; i < array.length(); i++) {
					JSONObject subObj = array.getJSONObject(i);
					msgReduceRecusive(subObj, fieldNames);
				}
			} else if (obj instanceof JSONObject) {
				JSONObject subJobj = (JSONObject) obj;
				msgReduceRecusive(subJobj, fieldNames);
			}
		}
	}

}
