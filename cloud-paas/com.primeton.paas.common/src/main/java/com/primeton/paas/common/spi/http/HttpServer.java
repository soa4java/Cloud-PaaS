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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;
import org.gocom.cloud.common.utility.api.ReflectUtil;
import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.common.impl.http.MethodDefinition;
import com.primeton.paas.common.impl.http.ObjectRegister;
import com.primeton.paas.common.impl.http.ResultDefinition;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class HttpServer {
	
	private static final ILogger log = LoggerFactory.getLogger(HttpServer.class);
	
	static {
		if (SystemProperties.getProperty("org.mortbay.io.nio.JVMBUG_THRESHHOLD") == null) { //$NON-NLS-1$
			System.setProperty("org.mortbay.io.nio.JVMBUG_THRESHHOLD", String.valueOf(Integer.MAX_VALUE)); //$NON-NLS-1$
		}
		if (SystemProperties.getProperty("org.mortbay.io.nio.BUSY_PAUSE") == null) { //$NON-NLS-1$
			System.setProperty("org.mortbay.io.nio.BUSY_PAUSE", String.valueOf(5)); //$NON-NLS-1$
		}
	}
	
	private String host = null;
	
	private int port = 0;
	
	private Server jettyServer = null;
	
	private ServletContextHandler contextHandler = null;
	
	private ThreadPoolExecutor workThreads = null;
	
	private ObjectRegister objectRegister = new ObjectRegister();

	private boolean isStarted = false;

	private static Map<String, HttpServer> httpCache = new HashMap<String, HttpServer>();
	
	private static final int MAX_MESSAGE_LENGTH = SystemProperties.getProperty("maxMessageLength", int.class, 10 * 1024 * 1024); //$NON-NLS-1$
	
	private HttpServer() {
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static HttpServer getHttpServer(String host, int port) {
		if (port <= 0) {
			throw new IllegalArgumentException("port'" + port + "' is error!");
		}
		String hostPort = String.valueOf(host).trim() + port;
		HttpServer server = httpCache.get(hostPort);
		if (server == null) {
			synchronized (HttpServer.class) {
				if (server == null) {
					server = new HttpServer();
					server.host = host;
					server.port = port;
					httpCache.put(hostPort, server);
				}
			}
		}
		return server;
	}

	/**
	 * 
	 * @return
	 */
	public String getHost() {
		if (host == null || host.trim().length() == 0) {
			return "127.0.0.1"; //$NON-NLS-1$
		}
		return host;
	}

	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * 
	 */
	public void start() {
		if (isStarted) {
			return;
		}
		try {
			jettyServer = new Server();
			
			contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
			contextHandler.setContextPath("/"); //$NON-NLS-1$
			contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
			
			contextHandler.addServlet(new ServletHolder(new HttpServlet() {
				private static final long serialVersionUID = -8758069387756548354L;

				@SuppressWarnings("rawtypes")
				@Override
				public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
					if (!isStarted) {
						return;
					}
					try {						
						ResultDefinition resultDef = new ResultDefinition();
						boolean isNeedResponse = false;
						try {
							DataInputStream in = new DataInputStream(request.getInputStream());
							int length = in.readInt();
							if (length <= 0) {
								log.warn("Error message format from {0}!", new Object[]{request.getRemoteHost() + ":" + request.getRemotePort()});
								return;
							}
							if (length > MAX_MESSAGE_LENGTH) {
								log.warn("Message length[={0}] is too large from {1}!", new Object[]{length, request.getRemoteHost() + ":" + request.getRemotePort()});
								return;
							}
							byte[] requestBytes = new byte[length];
							in.readFully(requestBytes);
							ObjectInputStream requestIn = new ObjectInputStream(new ByteArrayInputStream(requestBytes));
							MethodDefinition methodDef = (MethodDefinition)requestIn.readObject();
							isNeedResponse = methodDef.isNeedResponse();
							if (methodDef.getMethodName() == null || methodDef.getMethodName().trim().length() == 0) {
								throw new IllegalArgumentException("methodName is null!");
							}
							Object target = null;
							if (methodDef.getObjectName() != null) {
								target = objectRegister.getObject(methodDef.getObjectName());
								if (target == null) {
									throw new IllegalArgumentException("Not existed ObjectName:" + methodDef.getObjectName());
								}
							}
							if (target == null) {
								target = methodDef.getTarget();
							}
							Class clazz = null;
							if (target != null) {
								clazz = target.getClass();
							}
							if (clazz == null) {
								if (methodDef.getClassName() != null && methodDef.getClassName().length() > 0) {
									clazz = ReflectUtil.loadClass(null, methodDef.getClassName());					
								}
							}
							if (clazz == null) {
								throw new IllegalArgumentException("ClassName is null!");
							}
							
							Class[] parameterTypes = new Class[0];
							if (methodDef.getParamClassNames() != null && methodDef.getParamClassNames().length > 0) {
								parameterTypes = new Class[methodDef.getParamClassNames().length];				
								for (int i = 0; i < methodDef.getParamClassNames().length; i++) {
									parameterTypes[i] = ReflectUtil.loadClass(clazz.getClassLoader(), methodDef.getParamClassNames()[i]);
								}
							}
							if (parameterTypes.length == 0) {
								if (methodDef.getArgs() != null && methodDef.getArgs().length > 0) {
									parameterTypes = null;
								}
							}
							resultDef.setResult(ReflectUtil.invokeMethod(clazz, methodDef.getMethodName(), parameterTypes, true, 
									target, methodDef.getArgs()));
						} catch (InvocationTargetException t) {
							resultDef.setException(t.getTargetException());
							isNeedResponse = true;
						} catch (Throwable t) {
							resultDef.setException(t);
							isNeedResponse = true;
						}

						if (isNeedResponse) {
							ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
							ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
							objOut.writeObject(resultDef);
							
							DataOutputStream out = new DataOutputStream(response.getOutputStream());
							out.writeInt(byteOut.size());
							out.write(byteOut.toByteArray());
							out.flush();
						}
					} catch (Throwable e) {
						log.error(e);
					}
				}
			}), "/");
			
			jettyServer.setHandler(contextHandler);
			
			jettyServer.addConnector(createConnector());

			workThreads = new ThreadPoolExecutor(SystemProperties.getProperty("MIN_THREAD_SIZE", int.class, 10), 
					SystemProperties.getProperty("MAX_THREAD_SIZE", int.class, 100), 
					60000, TimeUnit.MILLISECONDS, 
					new LinkedBlockingQueue<Runnable>(), 
					new NamedThreadFactory("Http-Server[" + toString() + "]"));
			ExecutorThreadPool threadPool = new ExecutorThreadPool(workThreads);
			jettyServer.setThreadPool(threadPool);
			jettyServer.setStopAtShutdown(true);
			jettyServer.start();
		} catch (Throwable e) {
			log.error(e);
		}
		isStarted = true;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Connector createConnector() {
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setName(toString());
		connector.setHost(getHost());
		connector.setPort(getPort());
		connector.setReuseAddress(true);
		connector.setSoLingerTime(0);
		return connector;
	}
	
	/**
	 * 
	 * @author ZhongWen.Li (mailto:lizw@primeton.com)
	 *
	 */
	static class NamedThreadFactory implements ThreadFactory {
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		NamedThreadFactory(String name) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup()
					: Thread.currentThread().getThreadGroup();
			this.namePrefix = name + "-thread-"; //$NON-NLS-1$
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		if (isStarted == false) {
			return;
		}
		try {
			if (jettyServer != null) {
				jettyServer.stop();
				jettyServer.destroy();
				jettyServer = null;
			}				
		} catch (Throwable e) {
			log.error(e);
		}
		try {
			if (workThreads != null) {
				workThreads.shutdownNow();
				workThreads = null;
			}				
		} catch (Throwable e) {
			log.error(e);
		}
		objectRegister.clear();
		isStarted = false;
	}
	
	/**
	 * 
	 * @param remoteObjectName
	 * @param remoteObject
	 */
	public void publish(String remoteObjectName, Object remoteObject) {
		if (remoteObjectName == null || remoteObjectName.trim().length() == 0) {
			throw new IllegalArgumentException("remoteObjectName is null!");
		}
		if (remoteObject == null) {
			throw new IllegalArgumentException("remoteObject is null!");
		}
		objectRegister.register(remoteObjectName, remoteObject);
	}
	
	/**
	 * 
	 * @param remoteObjectName
	 */
	public void unpublish(String remoteObjectName) {
		if (remoteObjectName == null || remoteObjectName.trim().length() == 0) {
			throw new IllegalArgumentException("remoteObjectName is null!");
		}
		objectRegister.unregister(remoteObjectName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(host).append(":").append(port); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return buf.toString();
	}
	
}
