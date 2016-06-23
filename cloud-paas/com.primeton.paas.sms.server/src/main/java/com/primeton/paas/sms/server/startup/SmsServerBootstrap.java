/**
 * 
 */
package com.primeton.paas.sms.server.startup;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.sms.spi.SmsConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class SmsServerBootstrap {
	
	private static String serverClassName = "com.primeton.paas.sms.server.startup.SmsServer";
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void main(String[] args) throws Exception {
		if ((args == null) || (args.length == 0)) {
			System.out.println("Error : [" + SmsServerBootstrap.class + " ][args length == 0]");
			return;
		}
		String home = System.getProperty(SmsConstants.SERVER_HOME_KEY);
		if ((home == null) || (home.trim().length() == 0)) {
			System.out.println("Error : " + SmsConstants.SERVER_HOME_KEY + " enviroment parameter is null or empty.");
			return;
		}
		File serverHome = new File(home);
		if (!serverHome.exists()) {
			System.out.println("Error : " + SmsConstants.SERVER_HOME_KEY + " is not existed : " + serverHome.getAbsolutePath());
			return;
		}
		if (serverHome.isFile()) {
			System.out.println("Error : " + SmsConstants.SERVER_HOME_KEY + " is file : " + serverHome.getAbsolutePath());
			return;
		}
		
		ClassLoader loader = getURLClassLoader(serverHome);
		Thread.currentThread().setContextClassLoader(loader);
		String arg = args[0];
		if ("start".equalsIgnoreCase(arg)) { //$NON-NLS-1$
			Class serverClass = loader.loadClass(serverClassName);
			Method methodStart = serverClass.getMethod("start", new Class[] { File.class }); //$NON-NLS-1$
			methodStart.invoke(serverClass.newInstance(), new Object[] {serverHome});
		} else if ("stop".equalsIgnoreCase(arg)) { //$NON-NLS-1$
			Class serverClass = loader.loadClass(serverClassName);
			Method methodStop = serverClass.getMethod("stopRemote", new Class[] { File.class }); //$NON-NLS-1$
			methodStop.invoke(serverClass.newInstance(), new Object[] {serverHome});
		} else {
			System.out.println("Error : [" + SmsServerBootstrap.class + " ][args[0] is not 'start' || 'stop'][" + arg + "]");
		}
	}

	private static ClassLoader getURLClassLoader(File serverHomeDir) throws Exception {
		List<URL> urlList = new ArrayList<URL>();
		listJarFile(new File(serverHomeDir, "lib"), urlList); //$NON-NLS-1$
		return new URLClassLoader(urlList.toArray(new URL[0]), SmsServerBootstrap.class.getClassLoader().getParent());
	}

	@SuppressWarnings("deprecation")
	private static void listJarFile(File libpath, List<URL> jarUrlList) throws Exception {
		if (libpath.isFile()) {
			if (libpath.getName().toLowerCase().endsWith(".jar")) { //$NON-NLS-1$
				jarUrlList.add(libpath.toURL());
			}
		} else {
			for (File jarFile : libpath.listFiles()) {
				listJarFile(jarFile, jarUrlList);
			}
		}
	}
	
}
