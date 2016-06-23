/**
 * 
 */
package com.primeton.paas.common.spi.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import com.primeton.paas.common.impl.http.MethodDefinition;
import com.primeton.paas.common.impl.http.ResultDefinition;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpClient {
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param remoteObjectName
	 * @param remoteObjectInterface
	 * @param isNeedResponse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteObject(String host, int port, final String remoteObjectName, Class<T> remoteObjectInterface, final boolean isNeedResponse) {
		if (host == null || host.trim().length() == 0) {
			throw new IllegalArgumentException("host is null!");
		}
		if (port <= 0) {
			throw new IllegalArgumentException("port'" + port + "' is error!");
		}
		if (remoteObjectName == null || remoteObjectName.trim().length() == 0) {
			throw new IllegalArgumentException("remoteObjectName is null!");
		}
		StringBuilder buf = new StringBuilder();
		buf.append("http://").append(host).append(":").append(port); //$NON-NLS-1$ //$NON-NLS-2$
		final String url = buf.toString();
		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{remoteObjectInterface}, 
				new InvocationHandler() {
					@SuppressWarnings("rawtypes")
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						MethodDefinition methodDef = new MethodDefinition();
						methodDef.setObjectName(remoteObjectName);
						methodDef.setMethodName(method.getName());
						Class[] paramClasses = method.getParameterTypes();
						String[] paramClassNames = new String[paramClasses.length];
						for (int i = 0; i < paramClassNames.length; i++) {
							paramClassNames[i] = paramClasses[i].getName();
						}
						methodDef.setParamClassNames(paramClassNames);
						methodDef.setArgs(args);
						methodDef.setNeedResponse(isNeedResponse);
						return request(url, methodDef, isNeedResponse);
					}			
		});
	}
	
	/**
	 * 
	 * @param url
	 * @param obj
	 * @param isNeedResponse
	 * @return
	 * @throws Throwable
	 */
	private static Object request(String url, Object obj, boolean isNeedResponse) throws Throwable {

		HttpURLConnection conn = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			conn = (HttpURLConnection)getURL(url).openConnection();
			conn.setConnectTimeout(30000);  
			conn.setReadTimeout(3 * 1000 * 60);  // 3m 
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Content-type", "application/x-java-serialized-object"); //$NON-NLS-1$ //$NON-NLS-2$
			conn.setRequestProperty("Cache-Control","no-cache");  //$NON-NLS-1$ //$NON-NLS-2$
			conn.setRequestMethod("POST"); //$NON-NLS-1$
			conn.connect();
			
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(obj);
			out = new DataOutputStream(conn.getOutputStream());
			out.writeInt(byteOut.size());
			out.write(byteOut.toByteArray());
			out.flush();

			in = new DataInputStream(conn.getInputStream());
			
			if (!isNeedResponse) {
				return null;
			}
			
			int length = in.readInt();
			byte[] resultBytes = new byte[length];
			in.readFully(resultBytes);
			
			ResultDefinition result = (ResultDefinition)new ObjectInputStream(new ByteArrayInputStream(resultBytes)).readObject();
			if (result.getException() == null) {
				return result.getResult();
			} else {
				throw result.getException();
			}
		} catch (ConnectException e) {
			System.out.println("Connect to " + url + " failured.");
			throw new IOException(url, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Throwable ignore) {
			}

			try {
				if (out != null) {
					out.close();
				}
			} catch (Throwable ignore) {
			}
			
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Throwable ignore) {
			}
		}
	}
	
	private static ConcurrentHashMap<String, URL> urlConnectionMap = new ConcurrentHashMap<String, URL>();
	private static Object lock = new Object();

	/**
	 * 
	 * @param url
	 * @return
	 * @throws Throwable
	 */
	private static URL getURL(String url) throws Throwable {
		if (urlConnectionMap.get(url) == null) {
			synchronized (lock) {
				if (urlConnectionMap.get(url) == null) {
					urlConnectionMap.put(url, new URL(url));
				}
			}
		}
		return (URL)urlConnectionMap.get(url);
	}
	
}

