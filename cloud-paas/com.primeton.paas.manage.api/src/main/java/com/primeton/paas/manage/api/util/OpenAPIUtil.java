package com.primeton.paas.manage.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.gocom.cloud.cesium.common.api.exception.TimeoutException;
import org.gocom.cloud.cesium.common.api.util.JsonSerializerUtil;
import org.gocom.cloud.cesium.common.api.util.StringUtil;
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.utility.spi.json.JSONObject;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;

/**
 * 
 * <code>
 * String bizCode = "BankCardQuery";<br>
 * 
 * Map<String, String> bizParams = new HashMap<String, String>();<br>
 * bizParams.put("cardId", "1663535918075");<br>
 * String responseBody = OpenAPIUtil.invokeBiz("BankCardQuery", bizParams);<br>	
 * </code>
 * 
 */
@Deprecated
public class OpenAPIUtil {
	
	private static ILogger log = ManageLoggerFactory.getLogger(OpenAPIUtil.class);
	
	public static String CUST_ID = "custId";
	public static String BIZ_CODE = "bizCode";
	public static String BIZ_PARAMS = "bizParams";

	/**
	 * 
	 * @return
	 */
	public static HttpPost getOpenAPIHost() {
		HttpPost httpost = null;
		
		String host = null;
		int port = 0;
		RuntimeInstance[] instances;
		try {
			instances = InstanceManagerFactory.createInstanceManager().getInstances("OpenAPI");
			if (instances != null && instances.length > 0) {
				RuntimeInstance instance = instances[0];
				host = instance.getIp();
				port = instance.getPort();
			} else {
				throw new RuntimeException("Can't find OpenAPI Service Instance on Zookeeper.");
			}
		} catch (TimeoutException e) {
			throw new RuntimeException(e);
		}
		
		//HttpPost httpost = new HttpPost("http://192.168.100.224:7900/async");
		httpost = new HttpPost("http://" + host + ":" + port +"/async");
		return httpost;
	}
	
	
	/**
	 * 
	 * @param bizCode
	 * @param bizParams
	 * @return
	 */
	public static String invokeBiz(String bizCode, Map<String, String> bizParams) {
		if (null == bizCode || bizCode.trim().length() == 0) {
			return null;
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = null;
		try {
			httpost = getOpenAPIHost();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Can not find OpenAPI Host : " + e.getMessage());
			}
		}
		if (httpost == null ) {
			return "Can not find OpenApi Host.";
		}
		
		String custId = "manage";
		String bizParamsJson = "";
		if (bizParams != null && bizParams.size() > 0) {
			try {
				bizParamsJson = JsonSerializerUtil.serialize(bizParams);
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("OpenApi Biz [ custId="+custId+", bizCode="+bizCode+"  ] bizParams Json Serializer Error : " + e.getMessage());
				}
			}
		}
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(CUST_ID, custId));
		nvps.add(new BasicNameValuePair(BIZ_CODE, bizCode));
		nvps.add(new BasicNameValuePair(BIZ_PARAMS, bizParamsJson));
		
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		
		try {
			HttpResponse response = httpclient.execute(httpost);
			if (log.isInfoEnabled()) {
				log.info("OpenApi Biz [ custId="+custId+", bizCode="+bizCode+", bizParams="+bizParamsJson+"  ] Invoke StatusLine : " + response.getStatusLine());
			}
			
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String responseBody = convertStreamToString(is);

			if (log.isInfoEnabled()) {
				log.info("OpenApi Biz [ custId="+custId+", bizCode="+bizCode+", bizParams="+bizParamsJson+"  ] Invoke Response Body : " + responseBody);
			}
			
			EntityUtils.consume(entity);
			return responseBody;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("OpenApi Biz [ custId="+custId+", bizCode="+bizCode+", bizParams="+bizParamsJson+"  ] Invoke Error : " + e.getMessage());
			}
			return e.getMessage();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param number
	 * @param content
	 * @param timeout
	 * @return
	 */
	public static String sendSMS(String number,String content,long timeout){
		if (StringUtil.isEmpty(number)) {
			System.out.println("Send message cancelled, number is null." );
		} 
		if (StringUtil.isEmpty(content)) {
			System.out.println("Send message :content is null.");
			content = "";
		}
		System.out.println("Send message {" + content+"} to {" + number+" }." );
		String bizCode = "SmsSend";
		Map<String, String> bizParams = new HashMap<String, String>();
		bizParams.put("number", number);
		
		bizParams.put("content", content);
		bizParams.put("timeout", String.valueOf(timeout));
		
		String responseBody = OpenAPIUtil.invokeBiz(bizCode, bizParams);
		System.out.println(responseBody);
		if (StringUtils.contains(responseBody, "\"exception\":")) {
			JSONObject json = new JSONObject(responseBody);
			System.err.println("Query exception:" + json.get("exception"));
			return "Send Message exception:" + json.get("exception");
		}
		responseBody = StringUtils.substringBeforeLast(responseBody, "<br>");
		responseBody = StringUtils.replace(responseBody, "com.primeton.paas.sms.model.SMSResult", "model.SmsResult");
		return null;
	}
	
}
