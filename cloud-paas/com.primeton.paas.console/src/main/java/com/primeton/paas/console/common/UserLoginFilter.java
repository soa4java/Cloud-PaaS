/**
 * 
 */
package com.primeton.paas.console.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.gocom.cloud.common.logger.api.ILogger;

/**
 * 用户登录拦截器. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class UserLoginFilter implements Filter {
	
	private static ILogger logger = LoggerFactory.getLogger(UserLoginFilter.class);
	
	public static final String SESSION_USER_OBJECT = "userObject";
	
	private String excludedPages;  
	
	private String[] excludedPageArray;
	
	/**
	 * @see src/main/webapp/WEB-INF/web.xml
	 */
	private final String SRVID = "/srv/"; 
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();
        String path = servletRequest.getRequestURI();
        
        Object userObject = session.getAttribute(SESSION_USER_OBJECT);
        IUserObject visitor = (null != userObject && userObject instanceof IUserObject) ? (IUserObject)userObject : null;

		if (isExcludedPage(path)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		if (null == visitor) {
			String username = (String) servletRequest.getParameter("username"); //$NON-NLS-1$
		    String password = (String) servletRequest.getParameter("password"); //$NON-NLS-1$
			if ((StringUtils.isEmpty(username) && StringUtils.isEmpty(password))) {
				if ((path.indexOf(SRVID)) > -1) {
					response.setContentType("application/json"); //$NON-NLS-1$
					response.getWriter().write("{result:false}"); //$NON-NLS-1$
				} else {
					String url = servletRequest.getContextPath() + "/login.jsp"; //$NON-NLS-1$
					servletResponse.sendRedirect(url);
					logger.info("Redirect to {0}.", new Object[] {url});
				}
				return;
			} else {
				if ((SystemVariable.CONSOLE_TYPE_APPLICATION.equalsIgnoreCase(SystemVariable.getConsoleType())
						&& UserLoginUtil.appLogin(username, password).isSuccess()) ||
						SystemVariable.CONSOLE_TYPE_PLATFORM.equalsIgnoreCase(SystemVariable.getConsoleType())
						&& UserLoginUtil.platformLogin(username, password).isSuccess()) {
					IUserObject user = OnlineUserManager.getUserObject();
					session.setAttribute(SESSION_USER_OBJECT, user); //$NON-NLS-1$
					redirectHomePage(servletRequest, servletResponse);
					return;
				}
				String url = servletRequest.getContextPath() + "/login.jsp?username=" + username + "&ret=false"; //$NON-NLS-1$ //$NON-NLS-2$
				servletResponse.sendRedirect(url);
				logger.info("Redirect to {0}.", new Object[] {url});
			}
		} else {
			// Redirect to index if has been login.
			if (path.contains("login.action") || path.contains("login.jsp")) { //$NON-NLS-1$ //$NON-NLS-2$
				redirectHomePage(servletRequest, servletResponse);
			}
			OnlineUserManager.login(visitor);
			chain.doFilter(servletRequest, servletResponse);
		}
    }
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	protected void redirectHomePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (null == request || null == response) {
			return;
		}
		String page = SystemVariable.CONSOLE_TYPE_PLATFORM
				.equalsIgnoreCase(SystemVariable.getConsoleType()) 
				? "platform.jsp" //$NON-NLS-1$
				: "app.jsp"; //$NON-NLS-1$
		response.sendRedirect(request.getContextPath()
				+ "/frame/" + page); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
	public void init(FilterConfig filterConfig) throws ServletException {
		excludedPages = filterConfig.getInitParameter("excludedPages"); //$NON-NLS-1$
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.split(",");
			if (null != excludedPageArray && excludedPageArray.length > 0) {
				List<String> urls = new ArrayList<String>();
				for (String url : excludedPageArray) {
					if (null == url) {
						continue;
					}
					String page = url.trim();
					if (page.length() > 0) {
						urls.add(page);
					}
				}
				excludedPageArray = urls.toArray(new String[urls.size()]);
			}
		}
		return;
	}
	
	/**
	 * 是否是例外的请求路径. <br>
	 */
	private boolean isExcludedPage(String path) {
		if (null == path || path.trim().length() == 0) {
			return false;
		}
		if (null != excludedPageArray && excludedPageArray.length > 0) {
			for (String page : excludedPageArray) {
				if (path.contains(page)) {
					return true;
				}
				if (page.endsWith("*")
						&& path.contains(page.substring(0, page.length() - 1))) {
					return true;
				}
				if (page.startsWith("*") && path.contains(page.substring(1))) {
					return true;
				}
				if (page.endsWith("*") && page.startsWith("*")
						&& page.length() > 2
						&& path.contains(page.substring(1, page.length() - 1))) {
					return true;
				}
			}
		}
		return false;
	}
	
}
