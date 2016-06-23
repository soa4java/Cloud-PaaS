/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class UrlUtil {

	public static final String FILE_URL_PREFIX = "file:";

	private UrlUtil() {
		super();
	}

	public static URL getURL(String resourceLocation) throws RuntimeException {
		return getURL(resourceLocation, null);
	}

	public static URL getURL(String resourceLocation, ClassLoader loader)
			throws RuntimeException {
		if (resourceLocation.startsWith(FILE_URL_PREFIX)) {
			resourceLocation = resourceLocation.substring(FILE_URL_PREFIX
					.length());
		}
		File file = new File(resourceLocation);
		if (file.exists()) {
			try {
				return new URL(resourceLocation);
			} catch (MalformedURLException ex) {
				try {
					return new URL(FILE_URL_PREFIX + resourceLocation);
				} catch (MalformedURLException ex2) {
					throw new RuntimeException(
							"The [resourceLocation="
									+ resourceLocation
									+ "] is neither a URL not a well-formed file path.",
							ex2);
				}
			}
		}
		URL url = null;
		if (loader != null) {
			url = loader.getResource(resourceLocation);
		}
		if (url == null) {
			url = Thread.currentThread().getContextClassLoader()
					.getResource(resourceLocation);
			if (url == null) {
				url = UrlUtil.class.getClassLoader().getResource(
						resourceLocation);
				if (url == null) {
					throw new RuntimeException(
							"Class path resource ["
									+ resourceLocation
									+ "] cannot be resolved to URL because it does not exist.");
				}
			}
		}
		return url;
	}

}