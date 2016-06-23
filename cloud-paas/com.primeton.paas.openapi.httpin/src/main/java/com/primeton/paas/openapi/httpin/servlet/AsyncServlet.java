/**
 * 
 */
package com.primeton.paas.openapi.httpin.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.openapi.admin.AppConstants;
import com.primeton.paas.openapi.engine.BizRequest;
import com.primeton.paas.openapi.httpin.config.HttpInConfigModelManager;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@WebServlet(name = "AsyncServlet", urlPatterns = { "/async/*" }, asyncSupported = true)
public class AsyncServlet extends HttpServlet {
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2913442017576663702L;

	private static final ILogger log = LoggerFactory.getLogger(AsyncServlet.class);
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		if (req.getContentType() != null) {
			resp.setContentType(req.getContentType());
		} else {
			resp.setContentType(AppConstants.CONTENT_TYPE);
		}
		if (req.getCharacterEncoding() != null) {
			resp.setCharacterEncoding(req.getCharacterEncoding());
		} else {
			resp.setCharacterEncoding(AppConstants.SYS_ENCODING);
		}
		long timeout = HttpInConfigModelManager.getModel().getTimeout() * 1000;
		log.info("go into servlet reqID=" + req.hashCode());

		String custId = req.getParameter(AppConstants.CUST_ID);
		String bizCode = req.getParameter(AppConstants.BIZ_CODE);
		String bizParams = req.getParameter(AppConstants.BIZ_PARAMS);

		AsyncContext actx = req.startAsync(req, resp);
		actx.getRequest().setAttribute("startTime", startTime);
		actx.addListener(new AsyncServletListener(startTime));

		if (timeout > 0) {
			actx.setTimeout(timeout);
		}
		BizRequest bizReq = new BizRequest();
		bizReq.setCustId(custId);
		bizReq.setBizCode(bizCode);
		bizReq.setBizParams(bizParams);
		bizReq.setActx(actx);

		actx.start(new TaskExecutor(bizReq));
		if (log.isDebugEnabled()) {
			log.debug("go out servlet reqID=" + req.hashCode());
		}
	}

}
