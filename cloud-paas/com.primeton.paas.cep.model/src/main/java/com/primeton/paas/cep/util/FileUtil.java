/**
 * 
 */
package com.primeton.paas.cep.util;

import java.io.File;

/**
 * @author lizhongwen(mailto:lizw@primeton.com)
 *
 */
public class FileUtil {

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean exist(String path) {
		if (StringUtil.isEmpty(path)) {
			return false;
		}
		return new File(path).exists();
	}
	
	/**
	 * 
	 * @param path
	 * @param isFile
	 * @return
	 */
	public static boolean exist(String path, boolean isFile) {
		if (StringUtil.isEmpty(path)) {
			return false;
		}
		
		File target = new File(path);
		if (target.exists()) {
			if (isFile && target.isFile()) {
				return true;
			} else if (!isFile && target.isDirectory()) {
				return true;
			}
		}
		return false;
	}
	
}
