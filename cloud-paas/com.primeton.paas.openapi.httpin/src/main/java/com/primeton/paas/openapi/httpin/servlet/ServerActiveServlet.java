/**
 * 
 */
package com.primeton.paas.openapi.httpin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeton.paas.openapi.admin.ServerContext;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ServerActiveServlet extends HttpServlet {

	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -188452527146132826L;

	protected void doHead(HttpServletRequest req, HttpServletResponse resp) {
		if (ServerContext.getInstance().isStarted())
			resp.setStatus(HttpServletResponse.SC_OK);
		else
			resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doHead(req, resp);
	}
	
}
