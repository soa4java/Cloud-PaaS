/**
 * 
 */
package com.primeton.paas.app.api.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.gocom.cloud.common.utility.api.SystemProperties;

import com.primeton.paas.app.file.FileUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class OuterFileUtil {
	
	public final static String ENV_FILE_ROOT_DIR = "paas.fileRootDir";
	
	private static String getRootPath() {
		String rootPath = SystemProperties.getProperty(ENV_FILE_ROOT_DIR, "");
		if (rootPath == null || rootPath.trim().length() == 0) {
			throw new IllegalArgumentException("System environment ${paas.fileRootDir} is not set.");
		}
		return rootPath;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static IFile getFile(String path) {
		return FileUtil.getFile(getRootPath(), path);
	}
	
	/**
	 * 
	 * @param parent
	 * @param childPath
	 * @return
	 */
	public static IFile getFile(IFile parent, String childPath) {
		return FileUtil.getFile(getRootPath(), parent, childPath);
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static InputStream getInputStream(IFile file)
			throws FileNotFoundException, IOException {
		return FileUtil.getInputStream(getRootPath(), file);
	}
	
	/**
	 * 
	 * @param file
	 * @param isAppend
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static OutputStream getOutputStream(IFile file, boolean isAppend)
			throws FileNotFoundException, IOException {
		return FileUtil.getOutputStream(getRootPath(), file, isAppend);
	}
	
}
