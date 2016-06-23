/**
 * Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
 */

package com.primeton.paas.console.common;

import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;

import com.primeton.paas.manage.api.factory.HostManagerFactory;
import com.primeton.paas.manage.api.factory.HostTemplateManagerFactory;
import com.primeton.paas.manage.api.manager.IHostManager;
import com.primeton.paas.manage.api.manager.IHostTemplateManager;
import com.primeton.paas.manage.api.model.Host;
import com.primeton.paas.manage.api.model.HostTemplate;

/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class LicenseManager {
	
	private static ILogger logger = LoggerFactory.getLogger(LicenseManager.class);
	
	private static LicenseManager manager = new LicenseManager();
	
	private long cpuLimits = Long.MAX_VALUE;
	private boolean isLegal = true;

	/**
	 * Default. <br>
	 */
	private LicenseManager() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public static LicenseManager getManager() {
		return manager;
	}
	
	/**
	 * 
	 * @param licenseFile
	 */
	public void validate(String licenseFile) {
		logger.info("TODO");
		/*
		Map<String, Imprimatur> map = ImprimaturParserUtil.parseImprimaturFile(licenseFile);
		if (null == map || map.isEmpty()) {
			throw new IllegalStateException("The license file '" + licenseFile + "' is illegal. ");
		}
		Imprimatur imprimatur = null;
		for (Imprimatur e : map.values()) {
			String product = null == e ? null : e.getProductFamily();
			if (StringUtil.isNotEmpty(product) && product.toLowerCase().contains("paas")) { //$NON-NLS-1$
				imprimatur = e;
				break;
			}
		}
		if (null == imprimatur) {
			throw new RuntimeException("Illegal license file '" + licenseFile + "'.");
		}
		try {
			imprimatur.validate();
			if (imprimatur.isValidated()) {
				isLegal = true;
				logger.info("Validate license file '" + licenseFile + "' success.");
			} else {
				throw new RuntimeException(imprimatur.getErrorMessage());
			}
		} catch (Throwable e) {
			throw new RuntimeException("Validate license file '" + licenseFile + "' error.", e);
		}
		String cpu = imprimatur.getProperty("cpus").getValue(); //$NON-NLS-1$
		if ("unlimited".equalsIgnoreCase(cpu)) { //$NON-NLS-1$
			cpuLimits = Long.MAX_VALUE;
		} else {
			try {
				cpuLimits = Long.parseLong(cpu);
			} catch (NumberFormatException ignore) {
			}
		}
		*/
	}

	/**
	 * 
	 * @return
	 */
	public long getCpuLimits() {
		return cpuLimits;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLegal() {
		return isLegal;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTotalCpu() {
		IHostManager hostManager = HostManagerFactory.getHostManager();
		IHostTemplateManager templateManager = HostTemplateManagerFactory.getManager();
		List<HostTemplate> templates = templateManager.getTemplates(null);
		if (null == templates || templates.isEmpty()) {
			return 0;
		}
		long total = 0;
		for (HostTemplate template : templates) {
			Host[] hosts = hostManager.getByPackage(template.getTemplateId());
			total += ((null == hosts ? 0 : hosts.length) * template.getCpu());
		}
		return total;
	}

}
