package com.primeton.paas.app.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @see org.apache.commons.io#FilenameUtils
 *
 *
 * @author <a href="mailto:wanglei@primeton.com">Wang Lei</a>
 */
public final class FilenameUtil {

	public static boolean onNetWare = OsUtil.isFamily("netware"); //$NON-NLS-1$
	public static boolean onDos = OsUtil.isFamily("dos"); //$NON-NLS-1$

	/**
	 *
	 * Only one instance is needed,so the default constructor is private<BR>
	 * Please refer to singleton design pattern.
	 */
	private FilenameUtil() {
		super();
	}

	/**
	 *
	 * @param folderName
	 * @param resourceName
	 * @return
	 */
	public static String getRelativePath(String folderName, String resourceName) {
		if (null == folderName || null == resourceName) {
			return null;
		}

		String t_FolderName = normalizeInUnixStyle(folderName);
		String t_ResourceName = normalizeInUnixStyle(resourceName);

		return StringUtil.remove(t_ResourceName, t_FolderName);
	}

	/**
	 * 
	 * @param folder
	 * @param resource
	 * @return
	 */
	public static String getRelativePath(File folder, File resource) {
		if (null == folder || null == resource) {
			return null;
		}
		return getRelativePath(folder.getAbsolutePath(), resource.getAbsolutePath());
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String normalizeInUnixStyle(String path) {
		path = FilenameUtils.normalize(path);
		return FilenameUtils.separatorsToUnix(path);
	}

	/**
	 * Convert a package name to a path.<BR>
	 *
	 * Example:
	 * org.test.name -> org/test/name
	 *
	 * @param packageName
	 * @return
	 */
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

	/**
	 *
	 * Convert a path to a package name.<BR>
	 *
	 * Example:
	 * org/test/name.txt -> org.test.name.txt
	 *  org\\test/name -> org.test.name
	 *
	 * @param path
	 * @return
	 */
	public static String toPackage(String path) {
		String t_Path = normalizeInUnixStyle(path);
		t_Path = StringUtil.replace(t_Path, "/", ".");
		if (null != t_Path && t_Path.startsWith(".")) {
			t_Path = StringUtil.removeStart(t_Path, ".");
		}
		if (null != t_Path && t_Path.endsWith(".")) {
			t_Path = StringUtil.removeEnd(t_Path, ".");
		}
		return t_Path;
	}

	/**
	 *
	 * Convert a path to a package name without file extension.<BR>
	 *
	 * Example:
	 * org/test/name.txt -> org.test.name
	 *  org\\test/name -> org.test.name
	 *
	 * @param r_Path
	 * @return
	 */
	public static String toPackageWithoutExtension(String r_Path) {
		String t_Path = FilenameUtils.removeExtension(r_Path);
		t_Path = normalizeInUnixStyle(t_Path);
		t_Path = StringUtil.replace(t_Path, "/", ".");
		if (null != t_Path && t_Path.startsWith(".")) {
			t_Path = StringUtil.removeStart(t_Path, ".");
		}
		return t_Path;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getPackagePath(Class<?> clazz) {
		return (clazz == null) ? null : (clazz.getPackage().getName().replace(".", "/") + "/");
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public static String getFileName(String className) {
		return "/" + StringUtil.replace(className, ".", File.separator);
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getFileName(Class<?> clazz) {
		return (clazz == null) ? null : ("/" + StringUtil.replace(clazz.getName(), ".", File.separator));
	}

	/**
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 */
	public static String getAbsoluteFilePath(Class<?> clazz, String fileName) {
		String path = getPackagePath(clazz);
		if (path == null) {
			return null;
		}
		return clazz.getClassLoader().getResource(path + fileName).getFile();
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getAbsoluteClassPath(Class<?> clazz) {
		if (clazz == null) {
			clazz = FilenameUtil.class;
		}
		String qnamePath = clazz.getName().replace('.', '/'); //$NON-NLS-1$ //$NON-NLS-2$
		String currentPath = UrlUtil.getURL(qnamePath.concat(".class")).getFile(); //$NON-NLS-1$
		int index = currentPath.lastIndexOf(qnamePath);
		return currentPath.substring(0, index);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String[] getAllFileNames(File file) {
		List<String> fileList = new ArrayList<String>();
		getAllFileNames(file, fileList);
		return fileList.toArray(new String[0]);
	}

	/**
	 * 
	 * @param file
	 * @param fileNameList
	 */
	public static void getAllFileNames(File file, List<String> fileNameList) {
		if (file.isFile()) {
			fileNameList.add(file.getAbsolutePath());
			return;
		}
		else {
			for (File f : file.listFiles()) {
				getAllFileNames(f, fileNameList);
			}
		}
	}

	/**
	 * 
	 * @param folder
	 * @param file
	 * @return
	 */
	public static boolean isPrefixOf(File folder, File file) {
		if (null == folder || null == file) {
			return false;
		}
		return isPrefixOf(folder.getAbsolutePath(), file.getAbsolutePath());
	}

	/**
	 * 
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public static boolean isPrefixOf(String folderName, String fileName) {
		if (null == folderName || null == fileName) {
			return false;
		}

		String t_FolderName = normalizeInUnixStyle(folderName);
		String t_ResourceName = normalizeInUnixStyle(fileName);

		return t_ResourceName.startsWith(t_FolderName);
	}

	/**
	 * Verifies that the specified filename represents an absolute path.
	 * Differs from new java.io.File("filename").isAbsolute() in that a path
	 * beginning with a double file separator--signifying a Windows UNC--must
	 * at minimum match "\\a\b" to be considered an absolute path.
	 * @param filename the filename to be checked.
	 * @return true if the filename represents an absolute path.
	 * @throws java.lang.NullPointerException if filename is null.
	 * @since Ant 1.6.3
	 */
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
