/**
 * 
 */
package com.primeton.paas.app.file;

import java.io.File;
import java.io.IOException;

import org.gocom.cloud.common.utility.api.IOUtil;

import com.primeton.paas.app.api.file.IFile;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FileImpl implements IFile {
	
	private static final long serialVersionUID = -5469132904553888938L;
	
	private File fileWrapper = null;
	
	private String rootPath = null;
	
	private String path = null;
	
	private String parent = null;
	
	private String name = null;
	
	/**
	 * 
	 * @param rootPath
	 * @param path
	 */
	public FileImpl(String rootPath, String path) {
		if (rootPath == null) {
			throw new IllegalArgumentException("rootPath is null!");
		}
		if (path == null) {
			throw new IllegalArgumentException("path is null!");
		}
		this.rootPath = IOUtil.normalizeInUnixStyle(rootPath);
		this.path = IOUtil.normalizeInUnixStyle(path);
		
		if (this.path.equals("/")) {
			this.parent = null;
			this.name = "/";
			this.fileWrapper = new File(this.rootPath);
		} else {
			int index = this.path.lastIndexOf('/');
			if (index != -1) {
				this.parent = this.path.substring(0, index);
				this.name = this.path.substring(index + 1);
			}
			if (this.parent == null || this.parent.length() == 0) {
				this.parent = "/";
			}
			if (this.name == null || this.name.length() == 0) {
				this.name = "/";
			}
			this.fileWrapper = new File(this.rootPath, this.path);
		}
	}
	
	public int compareTo(IFile o) {
		return fileWrapper.compareTo(((FileImpl)o).fileWrapper);
	}

	public String getName() {
		return name;
	}
	
	public String getRootPath() {
		return rootPath;
	}

	public String getPath() {
		return path;
	}

	public IFile getParent() {
		if (parent == null) {
			return null;
		}
		return new FileImpl(this.rootPath, parent);
	}
	
	public File getFile() {
		return fileWrapper;
	}

	public boolean createNewFile() throws IOException {
		if (!fileWrapper.getParentFile().exists()) {
			fileWrapper.getParentFile().mkdirs();
		}
		return fileWrapper.createNewFile();
	}

	public long length() {
		return fileWrapper.length();
	}

	public boolean exists() {
		return fileWrapper.exists();
	}

	public boolean isDirectory() {
		return fileWrapper.isDirectory();
	}

	public boolean isFile() {
		return fileWrapper.isFile();
	}

	public long lastModified() {
		return fileWrapper.lastModified();
	}

	public boolean setLastModified(long time) {
		return fileWrapper.setLastModified(time);
	}

	public boolean delete() {
		return fileWrapper.delete();
	}

	public IFile[] listFiles() {
		File[] childs = fileWrapper.listFiles();
		if (childs == null) {
			return new IFile[0];
		}
		IFile[] result = new IFile[childs.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new FileImpl(this.rootPath, this.path + "/" + childs[i].getName());
 		}
		return result;
	}

	public boolean mkdirs() {
		return fileWrapper.mkdirs();
	}

	public boolean renameTo(IFile dest) {
		FileImpl destImpl = (FileImpl)dest;
		this.rootPath = destImpl.rootPath;
		this.parent = destImpl.parent;
		this.path = destImpl.path;
		this.name = destImpl.name;
		return fileWrapper.renameTo(destImpl.fileWrapper);
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof IFile) {
			return fileWrapper.equals(((FileImpl)obj).fileWrapper);
		} else {
			return fileWrapper.equals(obj);
		}
	}

	public int hashCode() {
		return fileWrapper.hashCode();
	}

	public String toString() {
		return getPath();
	}
	
}
