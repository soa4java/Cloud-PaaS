/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class FilenameUtil {

	public static boolean onNetWare = OsUtil.isFamily("netware");
	
	public static boolean onDos = OsUtil.isFamily("dos");

	private FilenameUtil() {
		super();
	}

	public static String getRelativePath(String folderName, String resourceName) {
		if (null == folderName || null == resourceName) {
			return null;
		}
		String fName = normalizeInUnixStyle(folderName);
		String rName = normalizeInUnixStyle(resourceName);
		return StringUtil.remove(rName, fName);
	}

	public static String getRelativePath(File folder, File resource) {
		if (null == folder || null == resource) {
			return null;
		}
		return getRelativePath(folder.getAbsolutePath(), resource.getAbsolutePath());
	}

	public static String normalizeInUnixStyle(String path) {
		path = FilenameUtils.normalize(path);
		return FilenameUtils.separatorsToUnix(path);
	}

	public static String toPathInUnixStyle(String packageName) {
		String path = StringUtil.replace(packageName, ".", "/");
		path = normalizeInUnixStyle(path);
		if (null != path && path.startsWith("/")) {
			path = StringUtil.removeStart(path, "/");
		}
		if (null != path && path.endsWith("/")) {
			path = StringUtil.removeEnd(path, "/");
		}
		return path;
	}

	public static String toPackage(String path) {
		String unixPath = normalizeInUnixStyle(path);
		unixPath = StringUtil.replace(unixPath, "/", ".");
		if (null != unixPath && unixPath.startsWith(".")) {
			unixPath = StringUtil.removeStart(unixPath, ".");
		}
		if (null != unixPath && unixPath.endsWith(".")) {
			unixPath = StringUtil.removeEnd(unixPath, ".");
		}
		return unixPath;
	}

	public static String toPackageWithoutExtension(String path) {
		String _path = FilenameUtils.removeExtension(path);
		_path = normalizeInUnixStyle(_path);
		_path = StringUtil.replace(_path, "/", ".");
		if (null != _path && _path.startsWith(".")) {
			_path = StringUtil.removeStart(_path, ".");
		}
		return _path;
	}

	public static String getPackagePath(Class<?> clazz) {
		return (clazz == null) ? null : (clazz.getPackage().getName()
				.replace(".", "/") + "/");
	}

	public static String getFileName(String className) {
		return "/" + StringUtil.replace(className, ".", File.separator);
	}

	public static String getFileName(Class<?> clazz) {
		return (clazz == null) ? null : ("/" + StringUtil.replace(clazz.getName(), ".", File.separator));
	}

	public static String getAbsoluteFilePath(Class<?> clazz, String fileName) {
		String path = getPackagePath(clazz);
		if (path == null) {
			return null;
		}
		return clazz.getClassLoader().getResource(path + fileName).getFile();
	}

	public static String getAbsoluteClassPath(Class<?> clazz) {
		if (clazz == null) {
			clazz = FilenameUtil.class;
		}
		String qnamePath = clazz.getName().replace('.', '/');
		String currentPath = UrlUtil.getURL(qnamePath.concat(".class")).getFile();
		int index = currentPath.lastIndexOf(qnamePath);
		return currentPath.substring(0, index);
	}

	public static String[] getAllFileNames(File file) {
		List<String> fileList = new ArrayList<String>();
		getAllFileNames(file, fileList);
		return fileList.toArray(new String[0]);
	}

	public static void getAllFileNames(File file, List<String> fileNameList) {
		if (file.isFile()) {
			fileNameList.add(file.getAbsolutePath());
			return;
		} else {
			for (File f : file.listFiles()) {
				getAllFileNames(f, fileNameList);
			}
		}
	}

	public static boolean isPrefixOf(File folder, File file) {
		if (null == folder || null == file) {
			return false;
		}
		return isPrefixOf(folder.getAbsolutePath(), file.getAbsolutePath());
	}

	public static boolean isPrefixOf(String folderName, String fileName) {
		if (null == folderName || null == fileName) {
			return false;
		}
		String t_FolderName = normalizeInUnixStyle(folderName);
		String t_ResourceName = normalizeInUnixStyle(fileName);

		return t_ResourceName.startsWith(t_FolderName);
	}

	public static boolean isAbsolutePath(String filename) {
	    int len = filename.length();
	    if (len == 0) {
	        return false;
	    }
	    char sep = File.separatorChar;
	    filename = filename.replace('/', sep).replace('\\', sep);
	    char c = filename.charAt(0);
	    if (!(onDos || onNetWare)) {
	        return (c == sep);
	    }
	    if (c == sep) {
	        if (!(onDos && len > 4 && filename.charAt(1) == sep)) {
	            return false;
	        }
	        int nextsep = filename.indexOf(sep, 2);
	        return nextsep > 2 && nextsep + 1 < len;
	    }
	    int colon = filename.indexOf(':');
	    return (Character.isLetter(c) && colon == 1
	        && filename.length() > 2 && filename.charAt(2) == sep)
	        || (onNetWare && colon > 0);
	}

}
