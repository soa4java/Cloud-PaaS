/**
 *
 */
package com.primeton.paas.manage.api.impl.spring;

import org.gocom.cloud.common.logger.api.ILogger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.primeton.paas.manage.api.factory.ManageLoggerFactory;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SpringApplicationContext implements ApplicationContextAware {
	
	private static ILogger logger = ManageLoggerFactory.getLogger(SpringApplicationContext.class);
	
	private ApplicationContext applicationContext;
	
	private static SpringApplicationContext context;
	
	/**
	 * Default. <br>
	 */
	public SpringApplicationContext() {
		super();
		context = (null == context) ? this : context;
	}
	
	/**
	 * 
	 * @return
	 */
	public static final SpringApplicationContext getInstance() {
		return context;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		logger.info("\n####\n#### springframework init " + this + " success. #####\n####\n");
	}

	/**
	 * 
	 * @return
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
