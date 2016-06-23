/**
 *
 */
package com.primeton.paas.console.app.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.primeton.paas.console.app.service.util.FileUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
@Path("/myDownload")
public class MyDownloadResource {

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Path("sdk")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] download(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		String path = request.getRealPath("/") + File.separator + "download" //$NON-NLS-1$ //$NON-NLS-2$
				+ File.separator + "sdk.zip"; //$NON-NLS-1$
		byte[] fb = FileUtil.readFile(path);
		response.setHeader("Content-Disposition", "attachment;filename=sdk.zip"); //$NON-NLS-1$ //$NON-NLS-2$
		response.addHeader("content-type", "application/zip"); //$NON-NLS-1$ //$NON-NLS-2$
		return fb;
	}

}
