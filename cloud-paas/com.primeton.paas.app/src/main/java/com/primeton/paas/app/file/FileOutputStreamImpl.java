/**
 * 
 */
package com.primeton.paas.app.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.primeton.paas.app.api.file.IFile;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class FileOutputStreamImpl extends FileOutputStream {
	
	/**
	 * 
	 * @param file
	 * @param fileLock
	 * @param isAppend
	 * @throws FileNotFoundException
	 */
	public FileOutputStreamImpl(IFile file, IFile fileLock, boolean isAppend) throws FileNotFoundException {
		super(((FileImpl)file).getFile(), isAppend);
	}
	
}
