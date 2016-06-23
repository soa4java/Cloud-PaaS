/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.console.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.cloud.common.logger.api.ILogger;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LicenseFilter implements Filter {
	
	private static ILogger logger = LoggerFactory.getLogger(LicenseFilter.class);
	
	/**
	 * Default. <br>
	 */
	public LicenseFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// String url = ((HttpServletRequest)request).getRequestURI();
		// 校验当前环境下使用的CPU的总核心数
		long limit = LicenseManager.getManager().getCpuLimits();
		long total = LicenseManager.getManager().getTotalCpu();
		
		if (total > limit) {
			logger.warn("PAAS product license limit CPU core number is {0}, but now is {1}, beyond the limits.", 
					new Object[] { limit, total });
			// 跳转到License警告提示页面
			String url = ((HttpServletRequest)request).getContextPath() + "/license.jsp?limit=" + limit + "&total=" + total; //$NON-NLS-1$ //$NON-NLS-2$
			((HttpServletResponse)response).sendRedirect(url);
			logger.info("Redirect to {0}.", new Object[] { url });
			return;
		}
		
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// Auto-generated method stub
		logger.info("Init {0} success.", new Object[] { this });
	}

}
