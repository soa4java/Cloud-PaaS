/**
 * 
 */
package com.primeton.paas.app.api.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.primeton.paas.app.ServerContext;
import com.primeton.paas.app.file.FileUtil;

/**
 * <pre>
 * IFile file = InnerFileUtil.getFile("sample/sample.txt");
 * if (!file.exists()) {
 *     IFile parent = file.getParent();
 *     if(!parent.exists()) {
 *         parent.mkdirs();
 *     }
 *     file.createNewFile();
 * }
 * </pre>
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class InnerFileUtil {
	
	private static String getRootPath() {
		String rootPath = ServerContext.getInstance().getWarRealPath() + File.separator + "files"; //$NON-NLS-1$
		if (rootPath == null || rootPath.trim().length() == 0) {
			throw new IllegalArgumentException("Inner File RootPath is not setter!");
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
	public static InputStream getInputStream(IFile file) throws FileNotFoundException, IOException {
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
	public static OutputStream getOutputStream(IFile file, boolean isAppend) throws FileNotFoundException, IOException {
		return FileUtil.getOutputStream(getRootPath(), file, isAppend);
	}
	
}
