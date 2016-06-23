package com.primeton.paas.app.api.open;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.gocom.cloud.cesium.manage.runtime.api.client.InstanceManagerFactory;
import org.gocom.cloud.cesium.manage.runtime.api.model.RuntimeInstance;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.app.AppConstants;
import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.spi.log.SystemLoggerFactory;
import com.primeton.paas.app.util.AppUtil;

/**
 * <pre>
 * String bizCode = "BankCardQuery";
 * 
 * Map<String, String> bizParams = new HashMap<String, String>();
 * bizParams.put("cardId", "1663535918075");
 * String responseBody = OpenAPIUtil.invokeBiz("BankCardQuery", bizParams);	
 * </pre>
 * 
 * <pre>
 * String bizCode = "SmsSend";
 * 
 * Map<String, String> bizParams = new HashMap<String, String>();
 * bizParams.put("number", "18652064830");
 * bizParams.put("content", "Hello For SMS Test");
 * bizParams.put("timeout", 0);
 * 
 * String responseBody = OpenAPIUtil.invokeBiz(bizCode, bizParams);
 * </pre>
 * 
 * @author Hao
 *
 */
public class OpenAPIUtil {
	
	private static ILogger log = SystemLoggerFactory.getLogger(OpenAPIUtil.class);
	
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
			instances = InstanceManagerFactory.createInstanceManager().getInstances("OpenAPI"); //$NON-NLS-1$
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
		httpost = new HttpPost("http://" + host + ":" + port +"/async"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return httpost;
	}
	
	
	/**
	 * 
	 * @param bizCode
	 * @param bizParams
	 * @return
	 */
	public static String invokeBiz(String bizCode, Map<String, String> bizParams) {
		if (AppConstants.RUN_MODE_SIMULATOR == ServerContext.getInstance().getRunMode()) {
			if (log.isErrorEnabled()) {
				log.error("Runtime Mode is Developer Mode, Can not invoke OpenAPI in Developer Mode.");
			}
			return "{\"Exception\":\"Runtime Mode is Developer Mode, Can not invoke OpenAPI in Developer Mode.\"}";
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
		
		String custId = ServerContext.getInstance().getAppName();
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
			String responseBody = AppUtil.convertStreamToString(is);

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
	/*
	public static void main(String[] args) {
		ServerContext.getInstance().setAppName("app1");
		Map<String, String> bizParams = new HashMap<String, String>();
		bizParams.put("cardId", "1663535918075");
		String responseBody = OpenAPIUtil.invokeBiz("BankCardQuery", bizParams);
		System.out.println(responseBody);
	}
	*/
}
