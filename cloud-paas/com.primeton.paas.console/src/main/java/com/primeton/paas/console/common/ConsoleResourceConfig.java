/**
 *
 */
package com.primeton.paas.console.common;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * web.xml, org.glassfish.jersey.servlet.ServletContainer. <br>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class ConsoleResourceConfig extends ResourceConfig {
	
	private static Set<String> packs = new HashSet<String>();
	
	static {
		packs.add("com.primeton.paas.console.app.controller"); //$NON-NLS-1$
		packs.add("com.primeton.paas.console.platform.controller"); //$NON-NLS-1$
		packs.add("com.primeton.paas.console.coframe.controller"); //$NON-NLS-1$
		packs.add("com.primeton.paas.console.rest.controller"); //$NON-NLS-1$
	}

	/**
	 * Default. <br>
	 */
	public ConsoleResourceConfig() {
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		packages(getPacks().toArray(new String[packs.size()]));
	}

	/**
	 * 
	 * @return
	 */
	public static Set<String> getPacks() {
		return packs;
	}

}
