/**
 * 
 */
package com.primeton.paas.cardbin.server.startup;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.primeton.paas.cardbin.spi.CardBinConstants;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FinanceCardBinServerBootstrap {

	private static String serverClassName = "com.primeton.paas.cardbin.server.startup.FinanceCardBinServer";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception {
		if ((args == null) || (args.length == 0)) {
			System.out.println("Error : [" + FinanceCardBinServerBootstrap.class + " ][args length == 0]");
			return;
		}
		String home = System.getProperty(CardBinConstants.SERVER_HOME_KEY);
		if ((home == null) || (home.trim().length() == 0)) {
			System.out.println("Error : " + CardBinConstants.SERVER_HOME_KEY + " enviroment parameter is null or empty.");
			return;
		}
		File serverHome = new File(home);
		if (!serverHome.exists()) {
			System.out.println("Error : " + CardBinConstants.SERVER_HOME_KEY + " is not existed : " + serverHome.getAbsolutePath());
			return;
		}
		if (serverHome.isFile()) {
			System.out.println("Error : " + CardBinConstants.SERVER_HOME_KEY + " is file : " + serverHome.getAbsolutePath());
			return;
		}
		
		ClassLoader loader = getURLClassLoader(serverHome);
		Thread.currentThread().setContextClassLoader(loader);
		String arg = args[0];
		if ("start".equalsIgnoreCase(arg)) {
			Class serverClass = loader.loadClass(serverClassName);
			Method methodStart = serverClass.getMethod("start", new Class[] { File.class });
			methodStart.invoke(serverClass.newInstance(), new Object[] {serverHome});
		} else if ("stop".equalsIgnoreCase(arg)) {
			Class serverClass = loader.loadClass(serverClassName);
			Method methodStop = serverClass.getMethod("stopRemote", new Class[] { File.class });
			methodStop.invoke(serverClass.newInstance(), new Object[] {serverHome});
		} else if ("initdb".equalsIgnoreCase(arg)) {
			Class serverClass = loader.loadClass(serverClassName);
			Method methodStop = serverClass.getMethod("initdb", new Class[] { File.class, String.class });
			methodStop.invoke(serverClass.newInstance(), new Object[] {serverHome, args[1]});
		} else {
			System.out.println("Error : [" + FinanceCardBinServerBootstrap.class + " ][args[0] is not 'start' || 'stop'][" + arg + "]");
		}
	}

	private static ClassLoader getURLClassLoader(File serverHomeDir) throws Exception {
		List<URL> urlList = new ArrayList<URL>();
		listJarFile(new File(serverHomeDir, "lib"), urlList);
		return new URLClassLoader(urlList.toArray(new URL[0]), FinanceCardBinServerBootstrap.class.getClassLoader().getParent());
	}

	@SuppressWarnings("deprecation")
	private static void listJarFile(File libpath, List<URL> jarUrlList) throws Exception {
		if (libpath.isFile()) {
			if (libpath.getName().toLowerCase().endsWith(".jar")) {
				jarUrlList.add(libpath.toURL());
			}
		} else {
			for (File jarFile : libpath.listFiles()) {
				listJarFile(jarFile, jarUrlList);
			}
		}
	}
	
}
