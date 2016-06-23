/**
 * Copyright © 2009 - 2015 Primeton. All Rights Reserved.
 */
package com.primeton.paas.manage.api.impl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;
import com.primeton.paas.manage.api.util.StringUtil;

/**
 * Velocity tool, generate content by template. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class VelocityUtil {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(VelocityUtil.class);

	/**
	 * Default. <br>
	 */
	private VelocityUtil() {
		super();
	}

	/**
	 * 
	 * @param template
	 * @param context
	 * @param logTag
	 * @return
	 */
	public static String parse(InputStream template, Map<String, Object> context, String logTag) {
		if (null == template) {
			return null;
		}
		logTag = null == logTag ? "" : logTag;
		Velocity.init();
		VelocityContext vcontext = new VelocityContext();
		if (null != context && !context.isEmpty()) {
			for (String key : context.keySet()) {
				vcontext.put(key, context.get(key));
			}
		}
		StringWriter writer = null;
		InputStreamReader reader = null;
		try {
			writer = new StringWriter();
			reader = new InputStreamReader(template);
			Velocity.evaluate(vcontext, writer, logTag, reader);
			writer.flush();
			String content = writer.toString();
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * if parse error return template(source). <br>
	 * 
	 * @param template
	 * @param context
	 * @param logTag
	 * @return
	 */
	public static String parse(String template, Map<String, Object> context, String logTag) {
		if (StringUtil.isEmpty(template)) {
			return template;
		}
		logTag = null == logTag ? "" : logTag;
		Velocity.init();
		VelocityContext vcontext = new VelocityContext();
		if (null != context && !context.isEmpty()) {
			for (String key : context.keySet()) {
				vcontext.put(key, context.get(key));
			}
		}
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			Velocity.evaluate(vcontext, writer, logTag, template);
			writer.flush();
			String content = writer.toString();
			return content;
		} catch (Throwable t) {
			logger.error(t);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return template;
	}
	
}
