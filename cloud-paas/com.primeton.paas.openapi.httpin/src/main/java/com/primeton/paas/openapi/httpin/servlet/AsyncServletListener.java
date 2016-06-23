/**
 * 
 */
package com.primeton.paas.openapi.httpin.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AsyncServletListener implements AsyncListener {
	
	private static final ILogger log = LoggerFactory.getLogger(AsyncServletListener.class);

	private long startTime;

	public AsyncServletListener(long startTime) {
		this.startTime = startTime;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.AsyncListener#onComplete(javax.servlet.AsyncEvent)
	 */
	public void onComplete(AsyncEvent event) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("AsyncServletListener: async calling completed. request=" + event.getAsyncContext().getRequest().hashCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.AsyncListener#onTimeout(javax.servlet.AsyncEvent)
	 */
	public void onTimeout(AsyncEvent event) throws IOException {
		long endTime = System.currentTimeMillis();
		if (log.isErrorEnabled()) {
			log.error("AsyncServletListener: asyncCalling timeout(" + (endTime - startTime) + "ms,startTime:" + new SimpleDateFormat("HH:mm:ss").format(new Date(startTime))
					+ ").reqId=" + event.getAsyncContext().getRequest().hashCode(), event.getThrowable());
		}
		event.getAsyncContext().getResponse().getWriter().write("{\"Timeout\":\"true\"}"); //$NON-NLS-1$
		event.getAsyncContext().getResponse().getWriter().flush();

	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.AsyncListener#onError(javax.servlet.AsyncEvent)
	 */
	public void onError(AsyncEvent event) throws IOException {
		if (log.isErrorEnabled()) {
			log.error("AsyncServletListener: async calling failed.request=" + event.getAsyncContext().getRequest().hashCode(), event.getThrowable());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(event.getThrowable().getMessage());
		if (event.getThrowable().getCause() != null) {
			sb.append("\n");
			sb.append("Caused by: " + event.getThrowable().getCause().getMessage());
		}
		event.getAsyncContext().getResponse().getWriter().write("{\"Exception\":\"" + sb.toString() + "\"}");
		event.getAsyncContext().getResponse().getWriter().flush();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.AsyncListener#onStartAsync(javax.servlet.AsyncEvent)
	 */
	public void onStartAsync(AsyncEvent event) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("AsyncServletListener: async calling started.request=" + event.getAsyncContext().getRequest().hashCode());
		}
	}
	
}
